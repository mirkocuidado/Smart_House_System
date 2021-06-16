/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author 38164
 */
public class PorukaZaPlaner implements Serializable{
    private int idPoruka;
    private String naziv;
    private String kada;
    private String doKada;
    private String lokacija;
    private String username;
    
    /* 
    0 - listaj 
    1 - dodaj sa lokacijom
    2 - dodaj bez lokacije
    3 - obrisi
    4 - menjaj naziv
    5 - menjaj vreme
    6 - menjaj lokaciju
    7 - menjaj trajanje
    */

    public PorukaZaPlaner(int idPoruka, String naziv, String kada, String lokacija, String username, String doKada) {
        this.idPoruka = idPoruka;
        this.naziv = naziv;
        this.kada = kada;
        this.lokacija = lokacija;
        this.username = username;
        this.doKada = doKada;
    }

    public int getIdPoruka() {
        return idPoruka;
    }

    public void setIdPoruka(int idPoruka) {
        this.idPoruka = idPoruka;
    }

    public String getUsername() {
        return username;
    }

    public String getDoKada() {
        return doKada;
    }

    public void setDoKada(String doKada) {
        this.doKada = doKada;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKada() {
        return kada;
    }

    public void setKada(String kada) {
        this.kada = kada;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
    
    

    
    
    
}
