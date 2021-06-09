/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;

/**
 *
 * @author 38164
 */
public class PorukaZaAlarm implements Serializable{
    private int idPoruka;
    private String zeljenoVreme;
    private int periodaUMinutima;
    private int idPesmeZaZvonjenje;

    public PorukaZaAlarm(int idPoruka, String zeljenoVreme, int periodaUMinutima, int idPesmeZaZvonjenje) {
        this.idPoruka = idPoruka;
        this.zeljenoVreme = zeljenoVreme;
        this.periodaUMinutima = periodaUMinutima;
        this.idPesmeZaZvonjenje = idPesmeZaZvonjenje;
    }

    public int getIdPoruka() {
        return idPoruka;
    }

    public void setIdPoruka(int idPoruka) {
        this.idPoruka = idPoruka;
    }

    public String getZeljenoVreme() {
        return zeljenoVreme;
    }

    public void setZeljenoVreme(String zeljenoVreme) {
        this.zeljenoVreme = zeljenoVreme;
    }

    public int getPeriodaUMinutima() {
        return periodaUMinutima;
    }

    public void setPeriodaUMinutima(int periodaUMinutima) {
        this.periodaUMinutima = periodaUMinutima;
    }

    public int getIdPesmeZaZvonjenje() {
        return idPesmeZaZvonjenje;
    }

    public void setIdPesmeZaZvonjenje(int idPesmeZaZvonjenje) {
        this.idPesmeZaZvonjenje = idPesmeZaZvonjenje;
    }
    
    
}
