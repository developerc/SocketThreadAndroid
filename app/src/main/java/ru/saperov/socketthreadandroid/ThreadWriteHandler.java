package ru.saperov.socketthreadandroid;

import java.io.IOException;

/**
 * Created by saperov on 05.09.16.
 */
public class ThreadWriteHandler extends Thread {
    private String myInStr;

    public ThreadWriteHandler() {

    }

    /*
    @Override
    public void run() {
        try {


            while (true) {
                Thread.sleep(3*1000);
                myInStr = MyVariables.inStr;
                if(MainActivity.isResumed)
                MainActivity.tvOut.setText(myInStr);

                //Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
}
