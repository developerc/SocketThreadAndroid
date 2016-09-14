package ru.saperov.socketthreadandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by saperov on 31.08.16.
 */
public class ThreadOutHandler extends Thread {
    private Socket outgouing;
    public Thread mytOut;
    int iter=0;

    public ThreadOutHandler(Socket o, Thread tOut){
        outgouing = o;
        mytOut = tOut;
    }

    public void interrupt() {
        super.interrupt();
        try {
            outgouing.close();
            //  Thread.currentThread().interrupt();
        } catch (IOException e) {} // quietly close
    }


    @Override
    public void run() {
        try {
            try {
                OutputStream outputStream = outgouing.getOutputStream();
                PrintWriter out = new PrintWriter(outgouing.getOutputStream(), true);

                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
                String line = null;
                boolean done = false;
              /*  while (!done && !WatchingThread.threadClosed) {
                    line = keyboard.readLine(); // ждем пока пользователь введет что-то и нажмет кнопку Enter.
                    System.out.println("Sending this line to the server...");
                    out.println(line);
                    if(line.equals("BYE")){
                        Thread.currentThread().interrupt();
                        done=true;
                    }
                }*/

                while (!done && !WatchingThread.threadClosed){
                    System.out.println("Sending this line to the server..." + iter);
                    out.println(iter);
                    iter++;
                    if(iter>9) iter=0;
                    Thread.sleep(5*1000);
                }
                //  System.out.println("состояние потока tOut после interrupt:" + mytOut.getState());
            }catch (SocketException e) {
                System.out.println("В исходящем потоке Появилось SocketException:" + e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally
            {
                outgouing.close();
                System.out.println("Исходящий поток клиента завершил работу");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
