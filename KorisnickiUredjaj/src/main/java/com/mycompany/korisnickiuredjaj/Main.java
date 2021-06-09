/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.korisnickiuredjaj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 38164
 */
public class Main {
    
    static String [] users = {"cuidado", "tasha", "stubic" };
    static String [] passwords = {"123", "123", "123" };
    
    private static String successfulLogIn() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            String username, password;
            String password_check = "";
            
            while(true) {
                System.out.println("Unesite Vas username:");
                username = reader.readLine();
                
                for(int i = 0; i < users.length; i++)
                    if(users[i].equals(username)) {
                        password_check = passwords[i];
                    }
                
                System.out.println("Unesite Vasu sifru:");
                password = reader.readLine();
                
                if(password_check.equals(password)) {
                    return username;
                }
                else {
                    System.out.println("Pogresan username ili password! Probajte ponovo!");
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    private static String checkInsertedTime() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            String vreme = "";
            
            while(true) {
                System.out.println("Unesite vreme u formatu dd-MM-yyyyHH:mm.");
                vreme = reader.readLine();
                
                if(!vreme.matches("\\d{2}-\\d{2}-\\d{4}\\d{2}:\\d{2}")) {
                    System.out.println("Neodgovarajuci format za vreme! Pazi da HH treba da bude spojeno sa yyyy!");
                    break;
                }
                
                return vreme;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public static void main(String [] args) {
        try {
            Scanner sc= new Scanner(System.in);
            
            boolean loop = true;
            boolean doThis = true;
            
            int choice;
            
            HttpRequest request = null;
            HttpResponse response = null;
            
            String username = successfulLogIn();
            
            while(loop) {
                doThis = true;
                System.out.println("Unesite broj za izvrsavanje komande: \n"
                        + "1) Izlistajte sve Vase pesme. \n"
                        + "2) Unesite ID pesme koju zelite da pustite. \n"
                        + "3) Izlistajte istoriju pustanja pesama. \n"
                        + "4) Navijte alarm u zeljeno vreme i za zeljeni zvuk. \n"
                );

                choice = sc.nextInt();

                switch(choice) {
                    case 1: {
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/pesme/listaj/"+username)).GET().build();
                        break;
                    }

                    case 2: {
                        System.out.println("Unesite ID zeljene pesme.");
                        int idPesme = sc.nextInt();
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/pesme/pesme/"+username+"/"+idPesme)).GET().build();
                        break;
                    }

                    case 3: {
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/pesme/istorija/"+username)).GET().build();
                        break;
                    }
                    
                    case 4: {
                        String vreme = checkInsertedTime();
                        if(vreme.length() == 0) {
                            doThis = false;
                            break;
                        }
                        System.out.println("Unesite ID zeljene pesme.");
                        int idPesme = sc.nextInt();
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/alarm/navij/"+vreme+"/"+idPesme)).GET().build();
                        break;
                    }

                    default: {
                        loop = false;
                        break;
                    }
                }
                
                if(doThis) {
                    response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                    if(response.statusCode() == 200) System.out.println(response.body());
                    else System.err.println(response.body());
                }
            }
        }
        catch(Exception e) {
            System.out.println("MAIN EXCEPTION -> Korisnicki Uredjaj");
            System.err.print(e);
        }
    }
}
