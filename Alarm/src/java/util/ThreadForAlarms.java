/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 38164
 */
public class ThreadForAlarms implements Runnable {

    private final long DELAY;
    private final String pesmaURL;
    
    public ThreadForAlarms(long delay, String pesmaURL) {
        this.DELAY = delay;
        this.pesmaURL = pesmaURL;
    }
    
    @Override
    public void run() {
        try {
            System.out.println("Pokrenuta nit za -> " + DELAY);
            Thread.sleep(DELAY);
            Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome " + pesmaURL});
        } catch (InterruptedException ex) {
            System.out.println("interrupted");
        } catch (IOException ex) {
            Logger.getLogger(ThreadForAlarms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
