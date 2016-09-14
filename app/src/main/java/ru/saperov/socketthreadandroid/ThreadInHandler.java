package ru.saperov.socketthreadandroid;

import android.os.Bundle;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.Socket;

/**
 * Created by saperov on 31.08.16.
 */
public class ThreadInHandler extends Thread  {
    private Socket incoming;
    String readInStr=null;
    public Thread mytIn;

    public ThreadInHandler(Socket i, Thread tIn){
        incoming = i;
        mytIn = tIn;
    }

    public void interrupt() {
        super.interrupt();
        try {
            incoming.close();
            // Thread.currentThread().interrupt();
        } catch (IOException e) {} // quietly close
    }


    @Override
    public void run() {
        try {
            try {
                // InputStream inputStream = incoming.getInputStream();

                // Scanner in = new Scanner(inputStream);
                BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(incoming.getInputStream()));

                // while (true) {
                while (!incoming.isClosed()){
                    if((readInStr=inStreamReader.readLine())!=null) {

                        System.out.println("server says:" + readInStr);
                        MyVariables.inStr = "server says:" + readInStr;

                        if(MainActivity.isResumed){
                            Message message = MainActivity.h.obtainMessage();
                            Bundle bundle = new Bundle();
                            bundle.putString("key", readInStr);
                            message.setData(bundle);
                            MainActivity.h.sendMessage(message);
                        }
                    }
                    else break;
                }

            }
            catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted via InterruptedIOException");
            } catch (IllegalThreadStateException itse){
                System.out.println("IllegalThreadStateException:"+itse);
            }
            finally {
                System.out.println("Входящий поток клиента завершил работу");
                incoming.close();
                //  interrupt();
                //   System.out.println("состояние потока tIn:" + mytIn.getState());

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
