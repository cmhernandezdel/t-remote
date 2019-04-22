package com.example.tremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    /* Variables */
    private DatagramSocket clientSocket;
    private byte[] data;
    private byte[] receiveData;
    private String serverIP;

    /* Private constants */
    private final String ACK_ACCEPTED = "ACK";
    private final String CONNECTION_ACCEPT_CODE = "0001";

    /* Public constants */
    public static final int BUFFER_SIZE = 256;
    public static final int DEFAULT_PORT = 2050;
    public static final String EXTRA_IPADDR = "com.example.tremote.IPADDR";
    public static final String EXTRA_PASSWD = "com.example.tremote.PASSWD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* Establish connection when SEND button is pressed */
    // TODO: Check for errors
    public void establishConnection(View view) throws Exception{

        /* Variable assignments and initializations */
        data = new byte[BUFFER_SIZE];
        clientSocket = new DatagramSocket();
        serverIP = ((EditText)findViewById(R.id.ipInput)).getText().toString();
        InetAddress IPAddress = InetAddress.getByName(serverIP);
        String password = ((EditText) findViewById(R.id.passwordInput)).getText().toString();
        String message = password + "#" + CONNECTION_ACCEPT_CODE;
        data = message.getBytes();
        final DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, DEFAULT_PORT);

        /* Send the data to the server */
        /* Needs to be done in another thread, otherwise NetworkOnMainThreadException is thrown */
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run(){
                try{ clientSocket.send(sendPacket); }
                catch (Exception e){ Log.d("TEST", "EXCEPTION SENDING"); }
            }
        });
        sendThread.start();
        Log.d("TEST", "Sent" + data);

        /* Now wait for server ACK */
        receiveData = new byte[BUFFER_SIZE];
        final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        /* Receive data from the server */
        /* Needs to be done in another thread, otherwise NetworkOnMainThreadException is thrown */
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run(){
                try{ clientSocket.receive(receivePacket); }
                catch (Exception e) { }
            }
        });
        receiveThread.start();

        String ack = new String(receivePacket.getData());


        /* If receive ACK go to next activity */
        if(ack.equals(ACK_ACCEPTED)){
            Intent intent = new Intent(this, KeylogActivity.class);
            intent.putExtra(EXTRA_IPADDR, serverIP);
            intent.putExtra(EXTRA_PASSWD, password);
            startActivity(intent);
        }

        /* If we don't, log error */
        // TODO: do actual job here
        else{
            Log.d("[Error]", "Server rejected the connection: " + ack);
        }
    }

}
