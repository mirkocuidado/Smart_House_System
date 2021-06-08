/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zvuk;

import entities.Istorija;
import entities.Pesma;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.TypedQuery;
import util.ElemForListOfSongs;
import util.PorukaZaPesmu;

/**
 *
 * @author 38164
 */
public class Main {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "odSSKaPP")
    static Queue queueServisPesma;
    
    @Resource(lookup = "odPPKaSS")
    static Queue queuePesmaServis;
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZvukPU");
    private static final EntityManager em = emf.createEntityManager();
    
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queueServisPesma);
        JMSProducer producer = context.createProducer();

        while(true) {
            System.out.println("Zvuk krenuo!");
            Message message = consumer.receive();
            
            if(!(message instanceof ObjectMessage)) {
                break;
            }
            
            try {
                Object content = ((ObjectMessage) message).getObject();
                
                if(!(content instanceof PorukaZaPesmu)) {
                    System.out.println("Sadrzaj poruke koji nije predvidjen u redu -> odServisaKaPesmama");
                    break;
                }
                
                PorukaZaPesmu pzp = (PorukaZaPesmu)content;
                
                int tipPoruke = pzp.getTipPoruke();
                
                switch(tipPoruke) {
                    case 1: {
                        int idPesme = pzp.getIdPesme();
                        String username = pzp.getUrlPesme();
                        
                        Pesma pesmaWithThisID = em.find(Pesma.class, idPesme);

                        if(!pesmaWithThisID.getUsername().equals(username)) {
                            PorukaZaPesmu pzp2 = new PorukaZaPesmu(idPesme, "Not authorized", 2);

                            ObjectMessage objMsg = context.createObjectMessage(pzp2);

                            producer.send(queuePesmaServis, objMsg);
                            
                            break;
                        }
                        
                        Istorija i = new Istorija();
                        i.setAutor(pesmaWithThisID.getAutor());
                        i.setNaziv(pesmaWithThisID.getNaziv());
                        i.setUsername(username);
                        i.setDate(new Date());
                        em.persist(i);
                        
                        PorukaZaPesmu pzp2 = new PorukaZaPesmu(idPesme, pesmaWithThisID.getUrl(), 2);

                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePesmaServis, objMsg);
                        
                        break;
                    }
                    
                    case 2: {
                        String username = pzp.getUrlPesme();
                        TypedQuery<Istorija> tqPs = em.createQuery("SELECT i FROM Istorija i WHERE i.username = :username", Istorija.class);
                        List<Istorija> historyOfSongs = tqPs.setParameter("username", username).getResultList();
                        
                        PorukaZaPesmu pzp2 = new PorukaZaPesmu(0, "empty", 0);
                        
                        String listOfSongs = "";
                        for(Istorija p : historyOfSongs) {
                            listOfSongs = listOfSongs + " " + p.getDate() + " === " + p.getAutor() + " === " + p.getNaziv() + "\n"; 
                        }
                        
                        pzp2.setListOfSongs(listOfSongs);

                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePesmaServis, objMsg);
                        
                        break;
                    }
                    
                    case 3: {
                        String username = pzp.getUrlPesme();
                        TypedQuery<Pesma> tqPs = em.createQuery("SELECT p FROM Pesma p WHERE p.username=:username", Pesma.class);
                        List<Pesma> listOfPesme = tqPs.setParameter("username", username).getResultList();
                        
                        PorukaZaPesmu pzp2 = new PorukaZaPesmu(0, "empty", 0);
                        
                        String listOfSongs = "";
                        
                        for(Pesma p : listOfPesme) {
                            listOfSongs = listOfSongs + " " + p.getIdpesma() + " === " + p.getAutor() + " === " + p.getNaziv() + "\n"; 
                        }
                        
                        pzp2.setListOfSongs(listOfSongs);

                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePesmaServis, objMsg);
                        
                        break;
                    }
                    
                    default: {
                        System.out.println("UNREACHABLE CODE!");
                    }
                }
                
                
                
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
