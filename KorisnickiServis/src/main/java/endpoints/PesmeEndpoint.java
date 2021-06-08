/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Pesma;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import util.ElemForListOfSongs;
import util.PorukaZaPesmu;

/**
 *
 * @author 38164
 */

// http://localhost:8080/KorisnickiServis/smartHouse/pesme/pesme/1

@Path("pesme")
public class PesmeEndpoint {
    
    private static JMSContext context;
    private static JMSConsumer consumer;
    private static JMSProducer producer;
    
    private static ConnectionFactory connectionFactory;
    private static Queue queueServisPesma;
    private static Queue queuePesmaServis;
    
    static {
        final Properties initialContextProperties = new Properties();
        final InitialContext ic;
        
        try {
            ic = new InitialContext(initialContextProperties);
            
            queueServisPesma = (Queue) ic.lookup("odSSKaPP");
            queuePesmaServis = (Queue) ic.lookup("odPPKaSS");
            connectionFactory = (ConnectionFactory) ic.lookup("jms/__defaultConnectionFactory");
            
            context = connectionFactory.createContext();
            consumer = context.createConsumer(queuePesmaServis);
            producer = context.createProducer();
            
        } catch (NamingException ex) {
            System.err.println(ex);
        }
    }
    
    @GET
    @Path("pesme/{username}/{idPesma}")
    public Response getSongWithId(@PathParam("username") String username, @PathParam("idPesma") String idPesmaURL) throws IOException {
        
        String pesmaURL = "";
        
        int idPesma = Integer.parseInt(idPesmaURL);
        
        try {
            PorukaZaPesmu pzp = new PorukaZaPesmu(idPesma, username, 1);
            ObjectMessage objMsg = context.createObjectMessage(pzp);
            producer.send(queueServisPesma, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaPesmu pzp2 = (PorukaZaPesmu) objMsg2.getObject();
            
            pesmaURL = pzp2.getUrlPesme();
            
            if(pesmaURL.equals("Not authorized")) {
                String returnString = "Not authorized!";
                return Response.status(200).entity(returnString).build();
            }
            
            //Desktop.getDesktop().browse(URI.create(pesmaURL));
            
            Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome " + pesmaURL});
            
        } catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String returnString = "Pustena pesma!";
        return Response.status(200).entity(returnString).build();
    }
    
    
    @GET
    @Path("istorija/{username}")
    public Response getHistory(@PathParam("username") String username) throws JMSException {
        PorukaZaPesmu pzp = new PorukaZaPesmu(-1, username, 2);
        ObjectMessage objMsg = context.createObjectMessage(pzp);
        producer.send(queueServisPesma, objMsg);
        
        ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
        PorukaZaPesmu pzp2 = (PorukaZaPesmu) objMsg2.getObject();
        
        return Response.status(200).entity(pzp2.getListOfSongs()).build();
    }
    
    @GET
    @Path("listaj/{username}")
    public Response getAllSongs(@PathParam("username") String username) throws JMSException {
        PorukaZaPesmu pzp = new PorukaZaPesmu(-1, username, 3);
        ObjectMessage objMsg = context.createObjectMessage(pzp);
        producer.send(queueServisPesma, objMsg);
        
        ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
        PorukaZaPesmu pzp2 = (PorukaZaPesmu) objMsg2.getObject();
        
        return Response.status(200).entity(pzp2.getListOfSongs()).build();
    }
    
}
