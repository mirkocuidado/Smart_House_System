/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import util.PorukaZaAlarm;
import util.PorukaZaPlaner;

/**
 *
 * @author 38164
 */
@Path("planer")
public class PlanerEndpoint {
    
    private static JMSContext context;
    private static JMSConsumer consumer;
    private static JMSProducer producer;
    
    private static ConnectionFactory connectionFactory;
    private static Queue queueServisAlarm;
    private static Queue queueAlarmServis;
    
    static {
        final Properties initialContextProperties = new Properties();
        final InitialContext ic;
        
        try {
            ic = new InitialContext(initialContextProperties);
            
            queueServisAlarm = (Queue) ic.lookup("odSSSKaPPP");
            queueAlarmServis = (Queue) ic.lookup("odPPPKaSSS");
            connectionFactory = (ConnectionFactory) ic.lookup("jms/__defaultConnectionFactory");
            
            context = connectionFactory.createContext();
            consumer = context.createConsumer(queueAlarmServis);
            producer = context.createProducer();
            
        } catch (NamingException ex) {
            System.err.println(ex);
        }
    }
    
    @GET
    @Path("dodajSaLokacijom/{username}/{obaveza}/{kada}/{lokacija}/{trajanje}")
    public Response dodajSaLokacijom(@PathParam("username") String username, @PathParam("obaveza") String obaveza, @PathParam("kada") String kada, @PathParam("lokacija") String lokacija, @PathParam("trajanje") String trajanje) throws IOException {
        String uspeh = "";
        
        try {
            PorukaZaPlaner pzp = new PorukaZaPlaner(1, obaveza, kada, lokacija, username, trajanje);
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPlaner pzp2 = (PorukaZaPlaner) objMsg2.getObject();
            
            uspeh = pzp2.getNaziv();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }
    
    @GET
    @Path("dodajBezLokacije/{username}/{obaveza}/{kada}/{trajanje}")
    public Response dodajBezLokacije(@PathParam("username") String username, @PathParam("obaveza") String obaveza, @PathParam("kada") String kada, @PathParam("trajanje") String trajanje) throws IOException {
        String uspeh = "";
        
        try {
            PorukaZaPlaner pzp = new PorukaZaPlaner(2, obaveza, kada, "", username, trajanje);
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPlaner pzp2 = (PorukaZaPlaner) objMsg2.getObject();
            
            uspeh = pzp2.getNaziv();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }
    
    @GET
    @Path("izlistaj/{username}")
    public Response izlistajObaveze (@PathParam("username") String username) throws IOException {
        String uspeh = "";
        
        try {
            PorukaZaPlaner pzp = new PorukaZaPlaner(0, "", "", "", username, "");
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPlaner pzp2 = (PorukaZaPlaner) objMsg2.getObject();
            
            uspeh = pzp2.getNaziv();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }
    
    @GET
    @Path("obrisi/{idObaveza}")
    public Response obrisiObavezu (@PathParam("idObaveza") String idObaveza) throws IOException {
        String uspeh = "";
        
        try {
            PorukaZaPlaner pzp = new PorukaZaPlaner(3, idObaveza, "", "", "", "");
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPlaner pzp2 = (PorukaZaPlaner) objMsg2.getObject();
            
            uspeh = pzp2.getNaziv();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }
    
    @GET
    @Path("izmeniNaziv/{idObaveza}/{noviNaziv}")
    public Response izmeniNazivObaveze (@PathParam("idObaveza") String idObaveza, @PathParam("noviNaziv") String noviNaziv) throws IOException {
        String uspeh = "";
        
        try {
            PorukaZaPlaner pzp = new PorukaZaPlaner(4, noviNaziv, idObaveza, "", "", "");
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPlaner pzp2 = (PorukaZaPlaner) objMsg2.getObject();
            
            uspeh = pzp2.getNaziv();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }

    @GET
    @Path("izmeniVreme/{idObaveza}/{novoVreme}")
    public Response izmeniVremeObaveze (@PathParam("idObaveza") String idObaveza, @PathParam("novoVreme") String novoVreme) throws IOException {
        String uspeh = "";
        
        try {
            PorukaZaPlaner pzp = new PorukaZaPlaner(5, novoVreme, idObaveza, "", "", "");
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPlaner pzp2 = (PorukaZaPlaner) objMsg2.getObject();
            
            uspeh = pzp2.getNaziv();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }
    
    @GET
    @Path("izmeniLokaciju/{idObaveza}/{novaLokacija}")
    public Response izmeniLokacijuObaveze (@PathParam("idObaveza") String idObaveza, @PathParam("novaLokacija") String novaLokacija) throws IOException {
        String uspeh = "";
        
        try {
            PorukaZaPlaner pzp = new PorukaZaPlaner(6, novaLokacija, idObaveza, "", "", "");
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPlaner pzp2 = (PorukaZaPlaner) objMsg2.getObject();
            
            uspeh = pzp2.getNaziv();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }

    @GET
    @Path("izmeniTrajanje/{idObaveza}/{novoTrajanje}")
    public Response izmeniTrajanjeObaveze (@PathParam("idObaveza") String idObaveza, @PathParam("novoTrajanje") String novoTrajanje) throws IOException {
        String uspeh = "";
        
        try {
            PorukaZaPlaner pzp = new PorukaZaPlaner(7, novoTrajanje, idObaveza, "", "", "");
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPlaner pzp2 = (PorukaZaPlaner) objMsg2.getObject();
            
            uspeh = pzp2.getNaziv();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }
    
}
