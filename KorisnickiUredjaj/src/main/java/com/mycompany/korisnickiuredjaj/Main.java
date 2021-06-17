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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
                System.out.println("Unesite vreme u formatu dd-MM-yyyy HH:mm.");
                vreme = reader.readLine();
                
                if(!vreme.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}")) {
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
    
    private static String readString() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            
            String obaveza = reader.readLine();
            
            return obaveza;
            
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
                        + "5) Navijte periodicni alarm sa odredjenom periodom i zeljenim zvukom. \n"
                        + "6) Obrisi alarm. \n"
                        + "7) Navij alarm za neko od ponudjenih vremena. \n"
                        + "8) Dodajte novu obavezu. \n"
                        + "9) Izlistajte sve Vase obaveze. \n"
                        + "10) Obrisite neku od Vasih obaveza. \n"
                        + "11) Izmenite naziv Vase obaveze. \n"
                        + "12) Izmenite vreme Vase obaveze. \n"
                        + "13) Izmenite lokaciju Vase obaveze. \n"
                        + "14) Izmenite trajanje Vase obaveze. \n"
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
                        vreme = vreme.replaceAll(" ", "");
                        if(vreme.length() == 0) {
                            doThis = false;
                            break;
                        }
                        System.out.println("Unesite ID zeljene pesme.");
                        int idPesme = sc.nextInt();
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/alarm/navij/"+vreme+"/"+idPesme)).GET().build();
                        break;
                    }

                    case 5: {
                        String vreme = checkInsertedTime();
                        vreme = vreme.replaceAll(" ", "");
                        if(vreme.length() == 0) {
                            doThis = false;
                            break;
                        }
                        System.out.println("Unesite ID zeljene pesme.");
                        int idPesme = sc.nextInt();
                        System.out.println("Unesite oznaku za mernu jedinicu periode. 1 - minut. 2 - sat. 3 - dan.");
                        int mera = sc.nextInt();
                        System.out.println("Unesite periodu.");
                        int perioda = sc.nextInt();
                        
                        int brojMinuta = 0;
                        
                        switch(mera) {
                            case 1: {
                                brojMinuta = perioda;
                                break;
                            }
                            case 2: {
                                brojMinuta = perioda * 60;
                                break;
                            }
                            case 3: {
                                brojMinuta = perioda * 60 * 24;
                                break;
                            }
                        }
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/alarm/periodicni/"+vreme+"/"+idPesme+"/"+brojMinuta)).GET().build();
                        break;
                    }
                    
                    case 6: {
                        System.out.println("S obzirom da ovo nije u zahtevima, pogledaj u bazi ID alarma koji zelis da obrises i unesi ga.");
                        int idAlarm = sc.nextInt();
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/alarm/obrisi/"+idAlarm)).GET().build();
                        break;
                    }
                    
                    case 7: {
                        System.out.println("Odaberite zeljeno vreme.");
                        int ponudjenBroj = 0;
                        int i = 0;
                        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        String currentDate = "" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        
                        List<String> listOfPotentialTimes = new ArrayList<>();
                        
                        for(i = currentHour + 1; i < 24; i++) {
                            System.out.print(ponudjenBroj++ + ") ");
                            String s = currentDate + i + ":00";
                            listOfPotentialTimes.add(s);
                            System.out.println(s);
                        }
                        
                        currentDate = "" + LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        for(i = 0; i <= currentHour; i++) {
                            System.out.print(ponudjenBroj++ + ")");
                            String s = currentDate + " " + i + ":00";
                            listOfPotentialTimes.add(s);
                            System.out.println(s);
                        }
                        
                        System.out.println("Odaberite vreme kada zelite da navijete alarm.");
                        int chosen = sc.nextInt();
                        while(chosen > 23 || chosen < 0) {
                            System.out.println("Nedozvoljena vrednost!");
                            chosen = sc.nextInt();
                        }
                        
                        String vreme = listOfPotentialTimes.get(chosen);
                        vreme = vreme.replaceAll(" ", "");
                        
                        System.out.println("Unesite ID zeljene pesme.");
                        int idPesme = sc.nextInt();
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/alarm/navij/"+vreme+"/"+idPesme)).GET().build();
                        break;
                    }
                    
                    case 8: {
                        System.out.println("Unesite naziv Vase nove obaveze.");
                        String obaveza = readString();
                        obaveza = obaveza.replaceAll(" ", "_");
                        
                        String vreme = checkInsertedTime();
                        vreme = vreme.replaceAll(" ", "");

                        System.out.println("Unesite oznaku za mernu jedinicu trajanja obaveze. 1 - minut. 2 - sat. 3 - dan.");
                        int mera = sc.nextInt();
                        System.out.println("Unesite trajanje.");
                        int perioda = sc.nextInt();
                        
                        int brojMinuta = 0;
                        
                        switch(mera) {
                            case 1: {
                                brojMinuta = perioda;
                                break;
                            }
                            case 2: {
                                brojMinuta = perioda * 60;
                                break;
                            }
                            case 3: {
                                brojMinuta = perioda * 60 * 24;
                                break;
                            }
                        }
                        
                        boolean imaLokaciju = false;
                        System.out.println("Ukoliko Vasa obaveza nije kod Vas kuci, unesite lokaciju obaveze.");
                        String lokacija = readString();
                        lokacija = lokacija.replaceAll(" ", "_");
                        if(lokacija.length() != 0) {
                            imaLokaciju = true;
                        }
                        
                        if(imaLokaciju) {
                            request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/planer/dodajSaLokacijom/"+username+"/"+obaveza+"/"+vreme+"/"+lokacija+"/"+brojMinuta)).GET().build();
                        }
                        else {
                            request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/planer/dodajBezLokacije/"+username+"/"+obaveza+"/"+vreme+"/"+brojMinuta)).GET().build();
                        }
                        
                        break;
                    }
                    
                    case 9: {
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/planer/izlistaj/"+username)).GET().build();
                        break;
                    }
                    
                    case 10: {
                        System.out.println("Unesite ID obaveze koju zelite da obrisete.");
                        int idObaveza = sc.nextInt();
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/planer/obrisi/"+idObaveza)).GET().build();
                        break;
                    }
                    
                    case 11: { // naziv
                        System.out.println("Unesite ID obaveze kojoj zelite da izmenite naziv.");
                        int idObaveza = sc.nextInt();
                        
                        System.out.println("Unesite novi naziv za obavezu.");
                        String noviNaziv = readString();
                        noviNaziv = noviNaziv.replaceAll(" ", "_");
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/planer/izmeniNaziv/"+idObaveza+"/"+noviNaziv)).GET().build();
                        break;
                    }
                    
                    case 12: { // vreme
                        System.out.println("Unesite ID obaveze kojoj zelite da izmenite naziv.");
                        int idObaveza = sc.nextInt();
                        
                        String novoVreme = checkInsertedTime();
                        novoVreme = novoVreme.replaceAll(" ", "");
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/planer/izmeniVreme/"+idObaveza+"/"+novoVreme)).GET().build();
                        break;
                    }
                    
                    case 13: { // lokaciju
                        System.out.println("Unesite ID obaveze kojoj zelite da izmenite naziv.");
                        int idObaveza = sc.nextInt();
                        
                        System.out.println("Unesite novu lokaciju za obavezu.");
                        String novaLok = readString();
                        novaLok = novaLok.replaceAll(" ", "_");
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/planer/izmeniLokaciju/"+idObaveza+"/"+novaLok)).GET().build();
                        break;
                    }
                    
                    case 14: { // trajanje
                        System.out.println("Unesite ID obaveze kojoj zelite da izmenite naziv.");
                        int idObaveza = sc.nextInt();
                     
                        System.out.println("Unesite oznaku za mernu jedinicu trajanja obaveze. 1 - minut. 2 - sat. 3 - dan.");
                        int mera = sc.nextInt();
                        System.out.println("Unesite trajanje.");
                        int perioda = sc.nextInt();
                        
                        int brojMinuta = 0;
                        
                        switch(mera) {
                            case 1: {
                                brojMinuta = perioda;
                                break;
                            }
                            case 2: {
                                brojMinuta = perioda * 60;
                                break;
                            }
                            case 3: {
                                brojMinuta = perioda * 60 * 24;
                                break;
                            }
                        }
                        
                        
                        request = HttpRequest.newBuilder(new URI("http://localhost:8080/KorisnickiServis/smart/planer/izmeniTrajanje/"+idObaveza+"/"+brojMinuta)).GET().build();
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
