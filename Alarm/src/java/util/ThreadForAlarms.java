/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entities.Alarm;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 *
 * @author 38164
 */
public class ThreadForAlarms implements Runnable {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("AlarmPU");
    private static final EntityManager em = emf.createEntityManager();
    
    private long DELAY;
    private final String pesmaURL;
    private boolean isRepeatable;
    private int perioda;
    private Date date;
    
    public ThreadForAlarms(long delay, String pesmaURL, boolean isRepeatable, int perioda, Date date) {
        this.DELAY = delay;
        this.pesmaURL = pesmaURL;
        this.isRepeatable = isRepeatable;
        this.perioda = perioda;
        this.date = date;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Pokrenuta nit za -> " + DELAY);
            Thread.sleep(DELAY);
            
            try {
                Alarm josUBaziAlarm = em.createNamedQuery("Alarm.findByVreme", Alarm.class).setParameter("vreme", date).getSingleResult();
                // ako nije periodican, obrisi ga
                if(josUBaziAlarm.getPeriodican() == 0) {
                    em.getTransaction().begin();
                    em.remove(josUBaziAlarm);
                    em.getTransaction().commit();
                }
                
                Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome " + pesmaURL});
            }
            catch(NoResultException e) {
                isRepeatable = false;
            }
            
            // ponovi ga, ako je repeatable i nije obrisan
            while(isRepeatable) {
                System.out.println("Pokrenuta opet nit za -> " + DELAY);
                DELAY = perioda * 60 * 1000;
                Thread.sleep(DELAY);
                
                try {
                    // probaj da pronadjes alarm u bazi
                    Alarm josUBaziAlarm = em.createNamedQuery("Alarm.findByVreme", Alarm.class).setParameter("vreme", date).getSingleResult();
                
                    // ako ne postoji, ides u izuzetak
                    // ako postoji, odradi sta treba
                    Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome " + pesmaURL});
                }
                catch(NoResultException e) {
                    isRepeatable = false;
                }
            }
            
        } catch (InterruptedException ex) {
            System.out.println("interrupted");
        } catch (IOException ex) {
            Logger.getLogger(ThreadForAlarms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
