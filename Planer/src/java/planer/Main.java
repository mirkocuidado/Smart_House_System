/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import entities.Alarm;
import entities.Obaveza;
import entities.Pesma;
import entities.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import util.PorukaZaPlaner;

/**
 *
 * @author 38164
 */
public class Main {

    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "odSSSKaPPP")
    static Queue queueServisPlaner;
    
    @Resource(lookup = "odPPPKaSSS")
    static Queue queuePlanerServis;
    
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PlanerPU");
    private static final EntityManager em = emf.createEntityManager();
    
    static private Date addMinutesToJavaUtilDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
    
    public static void main(String[] args) {
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(queueServisPlaner);
        JMSProducer producer = context.createProducer();
        
        while(true) {
            System.out.println("Planer krenuo!");
            Message message = consumer.receive();
            
            if(!(message instanceof ObjectMessage)) {
                break;
            }
            
            try {
                
                Object content = ((ObjectMessage) message).getObject();

                if(!(content instanceof PorukaZaPlaner)) {
                    System.out.println("Sadrzaj poruke koji nije predvidjen u redu -> odServisaKaPlaneru");
                    break;
                }

                PorukaZaPlaner pzp = (PorukaZaPlaner) content;

                int tipPoruke = pzp.getIdPoruka();

                switch(tipPoruke) {
                    case 0: { // listaj
                        String username = pzp.getUsername();
                        TypedQuery<Obaveza> tqPs = em.createQuery("SELECT o FROM Obaveza o WHERE o.username=:username", Obaveza.class);
                        List<Obaveza> listOfObaveze = tqPs.setParameter("username", username).getResultList();
                        
                        PorukaZaPlaner pzp2 = new PorukaZaPlaner(100, "", "", "", "", "");
                        
                        String listOfSongs = "";
                        
                        for(Obaveza o : listOfObaveze) {
                            listOfSongs = listOfSongs + " " + o.getId() + ") " + o.getNaziv() + " - " + o.getLokacija() + ", od " + o.getKada() + " do " + o.getTrajanje() + "\n"; 
                        }
                        
                        pzp2.setNaziv(listOfSongs);

                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePlanerServis, objMsg);
                        break;
                    }
                    
                    case 1: { // dodaj sa lokacijom
                        String username = pzp.getUsername();
                        String obavezaNaziv = pzp.getNaziv();
                        
                        String kada = pzp.getKada();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyyHH:mm");
                        Date date = formatter.parse(kada);
                        
                        String lokacija = pzp.getLokacija();
                        
                        String doKada = pzp.getDoKada();
                        int doKadaBroj = Integer.parseInt(doKada);
                        Date date2 = addMinutesToJavaUtilDate(date, doKadaBroj);
                        
                        Obaveza obaveza = new Obaveza();
                        obaveza.setKada(date);
                        obaveza.setNaziv(obavezaNaziv);
                        obaveza.setLokacija(lokacija);
                        obaveza.setUsername(username);
                        obaveza.setTrajanje(date2);
                        
                        em.getTransaction().begin();
                        em.persist(obaveza);
                        em.getTransaction().commit();
                        
                        PorukaZaPlaner pzp2 = new PorukaZaPlaner(100, "Uspesno dodata obaveza!", "", "", "", "");
                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePlanerServis, objMsg);
                        
                        break;
                    }
                    
                    case 2: { // dodaj bez lokacije
                        String username = pzp.getUsername();
                        String obavezaNaziv = pzp.getNaziv();
                        
                        String kada = pzp.getKada();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyyHH:mm");
                        Date date = formatter.parse(kada);
                        
                        String lokacija = "";
                        User currentUser = em.find(User.class, username);
                        lokacija = currentUser.getAdresa();
                                
                        String doKada = pzp.getDoKada();
                        int doKadaBroj = Integer.parseInt(doKada);
                        Date date2 = addMinutesToJavaUtilDate(date, doKadaBroj);
                        
                        Obaveza obaveza = new Obaveza();
                        obaveza.setKada(date);
                        obaveza.setNaziv(obavezaNaziv);
                        obaveza.setLokacija(lokacija);
                        obaveza.setTrajanje(date2);
                        obaveza.setUsername(username);
                        
                        em.getTransaction().begin();
                        em.persist(obaveza);
                        em.getTransaction().commit();
                        
                        PorukaZaPlaner pzp2 = new PorukaZaPlaner(100, "Uspesno dodata obaveza!", "", "", "", "");
                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePlanerServis, objMsg);
                        
                        break;
                    }
                    
                    case 3: {
                        String idObaveze = pzp.getNaziv();
                        int idO = Integer.parseInt(idObaveze);
                        
                        try {
                            Obaveza obaveza = em.createNamedQuery("Obaveza.findById", Obaveza.class).setParameter("id", idO).getSingleResult();
                        
                            em.getTransaction().begin();
                            em.remove(obaveza);
                            em.getTransaction().commit();
                        }
                        catch(NoResultException e) {
                            System.out.println("Vec obrisan!");
                        }
                        
                        PorukaZaPlaner pzp2 = new PorukaZaPlaner(100, "Uspesno obrisana obaveza!", "", "", "", "");
                        
                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePlanerServis, objMsg);
                        break;
                    }
                    
                    case 4: {
                        String idObaveze = pzp.getKada();
                        int idO = Integer.parseInt(idObaveze);
                        
                        String noviNaziv = pzp.getNaziv();
                        
                        try {
                            Obaveza obaveza = em.createNamedQuery("Obaveza.findById", Obaveza.class).setParameter("id", idO).getSingleResult();
                        
                            em.getTransaction().begin();
                            obaveza.setNaziv(noviNaziv);
                            em.getTransaction().commit();
                        }
                        catch(NoResultException e) {
                            System.out.println("Neuspeh!");
                        }
                        
                        PorukaZaPlaner pzp2 = new PorukaZaPlaner(100, "Uspesno izmenjen naziv obaveze!", "", "", "", "");
                        
                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePlanerServis, objMsg);
                        break;
                    }
                    
                    case 5: {
                        String idObaveze = pzp.getKada();
                        int idO = Integer.parseInt(idObaveze);
                        
                        String novoVreme = pzp.getNaziv();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyyHH:mm");
                        Date date = formatter.parse(novoVreme);
                        
                        try {
                            Obaveza obaveza = em.createNamedQuery("Obaveza.findById", Obaveza.class).setParameter("id", idO).getSingleResult();
                        
                            Date kada = obaveza.getKada();
                            Date doKada = obaveza.getTrajanje();
                            
                            long minutiTrajanjaProsli = (doKada.getTime() - kada.getTime());
                            Date d2 = new Date(date.getTime() + minutiTrajanjaProsli);
                            
                            em.getTransaction().begin();
                            obaveza.setKada(date);
                            obaveza.setTrajanje(d2);
                            em.getTransaction().commit();
                        }
                        catch(NoResultException e) {
                            System.out.println("Neuspeh!");
                        }
                        
                        PorukaZaPlaner pzp2 = new PorukaZaPlaner(100, "Uspesno izmenjeno vreme obaveze!", "", "", "", "");
                        
                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePlanerServis, objMsg);
                        break;
                    }
                    
                    case 6: {
                        String idObaveze = pzp.getKada();
                        int idO = Integer.parseInt(idObaveze);
                        
                        String novaLokacija = pzp.getNaziv();
                        
                        try {
                            Obaveza obaveza = em.createNamedQuery("Obaveza.findById", Obaveza.class).setParameter("id", idO).getSingleResult();
                        
                            em.getTransaction().begin();
                            obaveza.setLokacija(novaLokacija);
                            em.getTransaction().commit();
                        }
                        catch(NoResultException e) {
                            System.out.println("Neuspeh!");
                        }
                        
                        PorukaZaPlaner pzp2 = new PorukaZaPlaner(100, "Uspesno izmenjena lokacija obaveze!", "", "", "", "");
                        
                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePlanerServis, objMsg);
                        break;
                    }
                    
                    case 7: {
                        String idObaveze = pzp.getKada();
                        int idO = Integer.parseInt(idObaveze);
                        
                        String novoTrajanje = pzp.getNaziv();
                        
                        try {
                            Obaveza obaveza = em.createNamedQuery("Obaveza.findById", Obaveza.class).setParameter("id", idO).getSingleResult();
                        
                            Date date = obaveza.getKada();
                            int doKadaBroj = Integer.parseInt(novoTrajanje);
                            Date date2 = addMinutesToJavaUtilDate(date, doKadaBroj);
                            
                            
                            em.getTransaction().begin();
                            obaveza.setTrajanje(date2);
                            em.getTransaction().commit();
                        }
                        catch(NoResultException e) {
                            System.out.println("Neuspeh!");
                        }
                        
                        PorukaZaPlaner pzp2 = new PorukaZaPlaner(100, "Uspesno izmenjeno trajanje obaveze!", "", "", "", "");
                        
                        ObjectMessage objMsg = context.createObjectMessage(pzp2);

                        producer.send(queuePlanerServis, objMsg);
                        break;
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
