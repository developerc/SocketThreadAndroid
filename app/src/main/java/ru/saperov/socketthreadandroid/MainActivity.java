package ru.saperov.socketthreadandroid;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Runnable r;
    Thread t=null;
    boolean tIsAlive = false;
    HTTGATaxometr httgataxometr; //обьявили класс для запуска потока сокетов
    TextView tvOut;
    private Thread tWrite = null;
   static boolean isResumed = false;
    static Handler h;
    static EditText etIPAdr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = (TextView) findViewById(R.id.tvOut);
        etIPAdr = (EditText) findViewById(R.id.etIPAdr);
        etIPAdr.setText("mx.tih.kuban.ru");

        h = new Handler(){
          public void handleMessage(android.os.Message msg) {
              Bundle bundle = msg.getData();
              String text = bundle.getString("key");

              tvOut.setText(text);
          }
        };

        /*
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // CheckOrder();
                if(isResumed) {
                    Message message = h.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("key", MyVariables.inStr);
                    message.setData(bundle);
                    h.sendMessage(message);
                 //   h.sendMessage();
                }
                   // tvOut.setText("proba");
            }
        }, 0, 1000);
        */



        //tvOut.setText(wifiIpAddress(this));
        //new HTTGATaxometr().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
    }

    class HTTGATaxometr extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            try {
            /*
            InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
            System.out.println("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?");
            Socket socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            System.out.println("Yes! I just got hold of the program.");*/

                //  Runnable r = new WatchingThread(socket);
                r = new WatchingThread();
                while (true) {
                    if(!tIsAlive) {
                        System.out.println("Пытаюсь создать новый поток t");
                        //r = new WatchingThread();
                        t = new Thread(r);
                        t.start();
                        System.out.println(" до таймаута Состояние потока t" + t.getState());
                        Thread.sleep(5*1000);
                        if(t.isAlive()) {
                            tIsAlive = true;
                            System.out.println("Создан новый поток t");

                        }


                    }
                    if(!t.isAlive()) {
                        tIsAlive = false;
                        System.out.println(" после таймаута Состояние потока t" + t.getState());

                        //t.interrupt();
                    }
                    //System.out.println(" после таймаута Состояние потока t" + t.getState());
                    // Thread.sleep(5*1000);
                }
            }
            catch (Exception x) {
                System.out.println("Exception:" + x);
            }

            return null;
        } //получаем первоначальные настройки таксометра

    }

    //функция для получения своего IP-адреса при подключении к WiFi
    protected String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }

    public void onMyButtonClick(View view)
    {
        // выводим сообщение


        new HTTGATaxometr().execute();
        Toast.makeText(this, "Начат обмен через сокет!", Toast.LENGTH_SHORT).show();
    }
}
