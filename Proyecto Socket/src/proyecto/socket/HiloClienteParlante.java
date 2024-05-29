/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto.socket;

import java.util.logging.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

/**
 *
 * @author yahir
 */
public class HiloClienteParlante extends Thread {

    protected Socket sk;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    private int id;
    Scanner scanner = new Scanner(System.in);
    static int num[] = {0, 0}; // Se hace static para compartir entre hilos
    static boolean[] numIngresado = {false, false}; // Indica si los números han sido ingresados
    static Object lock = new Object(); // Objeto para sincronización

    public HiloClienteParlante(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            
            sk = new Socket("25.38.135.113", 1005);//192.168.1.143/25.38.135.113
            dos = new DataOutputStream(sk.getOutputStream());
            dis = new DataInputStream(sk.getInputStream());
            System. out.println ("cliente: "+id + " entro a calculadora");
            dos.writeUTF("hola");
            String respuesta="";
            respuesta = dis.readUTF();

            synchronized (lock) {
                System.out.println("cliente: " + id + " ingrese un numero:");
                num[id] = scanner.nextInt();
                System.out.println("cliente: " + id + " ha ingresado: " + num[id]);
                numIngresado[id] = true;

                // Notificar a los otros hilos que un número ha sido ingresado
                lock.notifyAll();

                // Esperar hasta que ambos números hayan sido ingresados
                while (!numIngresado[0] || !numIngresado[1]) {
                    lock.wait();
                }

                menu();
                System. out.println ("cliente: "+id + " Servidor cierra calculadora: " + respuesta);
            }

            dis.close();
            dos.close();
            sk.close();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(HiloClienteParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void menu() {
        System.out.println("cliente" + id + " Elige la operacion que quieres");
        System.out.println("1.-Suma");
        System.out.println("2.-Resta");
        System.out.println("3.-Multiplicacion");
        System.out.println("4.-Division");
        int opc = scanner.nextInt();

        switch (opc) {
            case 1:
                System.out.println("cliente: " + id + " Resultado de la suma es: " + suma(num[0], num[1]));
                break;
            case 2:
                System.out.println("cliente: " + id + " Resultado de la Resta es: " + resta(num[0], num[1]));
                break;
            case 3:
                System.out.println("cliente: " + id + " Resultado de la multiplicacion es: " + multiplicacion(num[0], num[1]));
                break;
            case 4:
                try {
                    System.out.println("cliente: " + id + " Resultado de la division es: " + division(num[0], num[1]));
                } catch (ArithmeticException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Opcion no valida");
                break;
        }
    }

    public int suma(int num1, int num2) {
        return (num1 + num2);
    }

    public int resta(int num1, int num2) {
        return (num1 - num2);
    }

    public int multiplicacion(int num1, int num2) {
        return (num1 * num2);
    }

    public double division(int num1, int num2) {
        if (num2 == 0) {
            throw new ArithmeticException("No se puede dividir por cero");
        }
        return (double) num1 / num2;
    }

    public static void main(String[] args) {
        Thread cliente1 = new HiloClienteParlante(0);
        Thread cliente2 = new HiloClienteParlante(1);

        cliente1.start();
        cliente2.start();
    }
}
