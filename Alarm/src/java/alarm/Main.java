/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

import entities.Alarm;
import entities.Pesma;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import util.PorukaZaAlarm;
import util.ThreadForAlarms;

/**
 *
 * @author 38164
 */
public class Main {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "odSSKaAA")
    static Queue queueServisAlarm;
    
    @Resource(lookup = "odAAKaSS")
    static Queue queueAlarmServis;
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("AlarmPU");
    private static final EntityManager em = emf.createEntityManager();
    
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queueServisAlarm);
        JMSProducer producer = context.createProducer();
        
        while(true) {
            System.out.println("Alarm krenuo!");
            Message message = consumer.receive();
            
            if(!(message instanceof ObjectMessage)) {
                break;
            }
            try {
                
                Object content = ((ObjectMessage) message).getObject();

                if(!(content instanceof PorukaZaAlarm)) {
                    System.out.println("Sadrzaj poruke koji nije predvidjen u redu -> odServisaKaAlarmu");
                    break;
                }

                PorukaZaAlarm pza = (PorukaZaAlarm) content;

                int tipPoruke = pza.getIdPoruka();

                switch(tipPoruke) {
                    case 1: {
                        String vremeZvonjenja = pza.getZeljenoVreme();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyyHH:mm");
                        Date date = formatter.parse(vremeZvonjenja);
                        
                        long difference_In_Time = date.getTime() - new Date().getTime();
                        
                        String zvono = em.find(Pesma.class, pza.getIdPesmeZaZvonjenje()).getUrl();
                        
                        Thread threadForAlarm = new Thread(new ThreadForAlarms(difference_In_Time, zvono));
                        threadForAlarm.start();
                        
                        Alarm noviAlarm = new Alarm();
                        noviAlarm.setVreme(date);
                        noviAlarm.setZvuk(pza.getIdPesmeZaZvonjenje());
                        noviAlarm.setPerioda(0);
                        noviAlarm.setPeriodican(0);
                        
                        em.getTransaction().begin();
                        em.persist(noviAlarm);
                        em.getTransaction().commit();
                        
                        PorukaZaAlarm pza2 = new PorukaZaAlarm(-1, "Uspesno navijen alarm!", 1, 1);
                        
                        ObjectMessage objMsg = context.createObjectMessage(pza2);

                        producer.send(queueAlarmServis, objMsg);
                                                
                        break;
                    }


                    default: {
                        System.out.println("UNREACHABLE CODE!");
                    }

                }

            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }
        
    }
    
}
