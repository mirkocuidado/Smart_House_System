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

/**
 *
 * @author 38164
 */
@Path("alarm")
public class AlarmEndpoint {
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
            
            queueServisAlarm = (Queue) ic.lookup("odSSKaAA");
            queueAlarmServis = (Queue) ic.lookup("odAAKaSS");
            connectionFactory = (ConnectionFactory) ic.lookup("jms/__defaultConnectionFactory");
            
            context = connectionFactory.createContext();
            consumer = context.createConsumer(queueAlarmServis);
            producer = context.createProducer();
            
        } catch (NamingException ex) {
            System.err.println(ex);
        }
    }
    
    @GET
    @Path("navij/{vreme}/{idZvono}")
    public Response navijAlarm(@PathParam("vreme") String vreme, @PathParam("idZvono") String idZvono) throws IOException {
        int idPesma = Integer.parseInt(idZvono);
        String uspeh = "";
        
        try {
            PorukaZaAlarm pza = new PorukaZaAlarm(1, vreme, -1, idPesma);
            ObjectMessage objMsg = context.createObjectMessage(pza);
            producer.send(queueServisAlarm, objMsg);
            
            ObjectMessage objMsg2 = (ObjectMessage)consumer.receive();
            PorukaZaAlarm pza2 = (PorukaZaAlarm) objMsg2.getObject();
            
            uspeh = pza2.getZeljenoVreme();
            
        }
        catch (JMSException ex) {
            Logger.getLogger(PesmeEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(200).entity(uspeh).build();
    }
    
}
