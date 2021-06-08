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
public class PorukaZaPesmu implements Serializable{
    private int idPesme;
    private String urlPesme;
    
    private int tipPoruke;
    
    /* 
    0 -
    1 - zahtev za pustanje pesme odredjenog ID-ja
    2 - zahtev za istoriju svih pesama
    3 - zahtev svih pesama koje neki korisnik poseduje
    */
    
    private String listOfSongs;

    public PorukaZaPesmu(int idPesme, String urlPesme, int tipPoruke) {
        this.idPesme = idPesme;
        this.urlPesme = urlPesme;
        this.tipPoruke = tipPoruke;
    }

    public String getListOfSongs() {
        return listOfSongs;
    }

    public void setListOfSongs(String listOfSongs) {
        this.listOfSongs = listOfSongs;
    }

    public int getIdPesme() {
        return idPesme;
    }

    public void setIdPesme(int idPesme) {
        this.idPesme = idPesme;
    }

    public String getUrlPesme() {
        return urlPesme;
    }

    public void setUrlPesme(String urlPesme) {
        this.urlPesme = urlPesme;
    }

    public int getTipPoruke() {
        return tipPoruke;
    }

    public void setTipPoruke(int tipPoruke) {
        this.tipPoruke = tipPoruke;
    }
    
    
}
