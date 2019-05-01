package com.example.tremote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class KeylogActivity extends AppCompatActivity {

    /* Variable declarations */
    private DatagramSocket clientSocket;
    private byte[] data;
    private InetAddress serverAddress;
    private String password;
    private byte backed = 0;

    /* Private constants */
    private final String KEY_RIGHT_CODE = "0002";
    private final String KEY_LEFT_CODE = "0003";
    private final String EXIT_CODE = "0004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keylog);

        /* Initialize variables with data from the other activity */
        data = new byte[MainActivity.BUFFER_SIZE];
        try {
            clientSocket = new DatagramSocket();
            serverAddress = InetAddress.getByName(getIntent().getStringExtra(MainActivity.EXTRA_IPADDR));
            password = getIntent().getStringExtra(MainActivity.EXTRA_PASSWD);
        } catch (Exception e){ }

    }

    @Override
    /* Press volume down for next slide, press volume up for last slide
     * The format is PIN#KEYCODE
     * The server will understand this code and act accordingly
     * */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            String message;
            Thread sendThread;
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    message = password + "#" + KEY_RIGHT_CODE;
                    data = message.getBytes();
                    final DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, MainActivity.DEFAULT_PORT);
                    sendThread = new Thread(new Runnable() {
                        @Override
                        public void run(){
                            try{ clientSocket.send(sendPacket); Log.d("TEST", "Sent" + sendPacket.getData().toString());}
                            catch (Exception e){ Log.d("TEST", "EXCEPTION SENDING"); }
                        }
                    });
                    sendThread.start();
                    return true;
                case KeyEvent.KEYCODE_VOLUME_UP:
                    message = password + "#" + KEY_LEFT_CODE;
                    data = message.getBytes();
                    final DatagramPacket sendLPacket = new DatagramPacket(data, data.length, serverAddress, MainActivity.DEFAULT_PORT);
                    sendThread = new Thread(new Runnable() {
                        @Override
                        public void run(){
                            try{ clientSocket.send(sendLPacket); Log.d("TEST", "Sent" + sendLPacket.getData().toString());}
                            catch (Exception e){ Log.d("TEST", "EXCEPTION SENDING"); }
                        }
                    });
                    sendThread.start();
                    return true;
                default:
                    return false;
            }
        } catch (Exception e){ Log.d("EXCEPTION", "Exception: " + e); return false; }
    }

    @Override
    /* Press back twice for exit
     * On exit, notify the server and close the port
     * */
    public void onBackPressed(){
        try {
            if (backed > 0) {
                String message = password + "#" + EXIT_CODE;
                data = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, MainActivity.DEFAULT_PORT);
                clientSocket.send(sendPacket);
                clientSocket.close();
                finish();
            }
            else{
                backed++;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) { }
    }
}
