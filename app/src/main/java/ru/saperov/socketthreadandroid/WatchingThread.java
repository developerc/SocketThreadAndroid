package ru.saperov.socketthreadandroid;

import android.text.Editable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by saperov on 31.08.16.
 */
public class WatchingThread implements Runnable  {
    static public boolean threadClosed = false;
    private Thread tIn=null;
    private Thread tOut = null;
    //private Thread tWrite = null;
    InetAddress ipAddress;
    Socket socket;

    int serverPort = 8195; // здесь обязательно нужно указать порт к которому привязывается сервер.
    //String address = "10.0.2.2"; // это IP-адрес компьютера, где исполняется наша серверная программа.
    String address = MainActivity.etIPAdr.getText().toString();

    /* public WatchingThread(Socket i){
         incoming = i;
     }*/
    public WatchingThread(){

    }


    @Override
    public void run() {

        try {


            ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
            System.out.println("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?");
            socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            // socket.setSoTimeout(60*1000);
            System.out.println("Yes! I just got hold of the program.");
            int sockNum = 0;
            System.out.println("создали сокет " + sockNum + " socket TimeOut:" + socket.getSoTimeout());


            //  Runnable rOut = new ThreadOutHandler(incoming);
            //  Thread tOut = new Thread(rOut);
            tOut = new ThreadOutHandler(socket, tOut);
            tOut.start();

            // Runnable rIn = new ThreadInHandler(incoming);
            // tIn = new Thread(rIn, tIn);
            tIn = new ThreadInHandler(socket, tIn);
            tIn.start();

            //tWrite = new ThreadWriteHandler();
           // tWrite.start();


            while (true) {
                if (!tIn.isAlive()) {
                    sockNum++;
                    System.out.println("WatchingThread сообщает входящий поток остановлен!");
                    System.out.println("состояние потока tIn:" + tIn.getState());
                    System.out.println("состояние потока tOut:" + tOut.getState());

                    socket = new Socket(ipAddress, serverPort); // снова создаем сокет используя IP-адрес и порт сервера.
                    //socket.setSoTimeout(60*1000);
                    System.out.println("создали сокет " + sockNum + " socket TimeOut:" + socket.getSoTimeout());

                    threadClosed = false;
                    Thread.sleep(1000);
                    tOut = new ThreadOutHandler(socket, tOut);
                    tOut.start();
                    tIn = new ThreadInHandler(socket, tIn);
                    tIn.start();
                    // break;
                }
            }

         /*   while (true){
                if (!tIn.isAlive()) {
                    System.out.println("2  WatchingThread сообщает входящий поток остановлен!");
                    System.out.println("2  состояние потока tIn:" + tIn.getState());
                    System.out.println("2  состояние потока tOut:" + tOut.getState());
                    break;
                }
            }*/

        } catch (SocketException e) {
            System.out.println("Появилось SocketException:" + e);
            tIn.interrupt();
            tOut.interrupt();
            threadClosed = true;
            Thread.currentThread().interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
