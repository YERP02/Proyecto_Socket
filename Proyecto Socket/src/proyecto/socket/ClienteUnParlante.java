/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author yahir
 */
public class ClienteUnParlante {
    
    static final int MAX_HILOS = 10;
    public static final String HOST = "25.38.135.113";//192.168.1.143//25.38.135.113

    public static void main(String[] args) throws IOException, InterruptedException{

        ArrayList<Thread> clients = new ArrayList<Thread>();
        for (int i = 0; i < 2; i++) {
            clients.add (new HiloClienteParlante(i));
        }
        for (Thread thread : clients) {
            thread.start ();
        }
    }
    
}
