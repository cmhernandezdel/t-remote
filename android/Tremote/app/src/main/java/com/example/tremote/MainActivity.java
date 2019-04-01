package com.example.tremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.*;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    DatagramSocket clientSocket;
    byte[] data;
    byte[] receiveData;
    private final int BUFFER_SIZE = 256;
    private final String CONNECTION_ACCEPT_CODE = "0001";
    private final String KEY_RIGHT_CODE = "0002";
    private final String KEY_LEFT_CODE = "0003";
    private final String EXIT_CODE = "0004";
    private final String ACK_ACCEPTED = "ACK";
    private final int DEFAULT_PORT = 2050;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void establishConnection() throws Exception{
        /* Send PIN#0001#IP */
        data = new byte[BUFFER_SIZE];
        clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(((EditText)findViewById(R.id.ipInput)).getText().toString());
        String password = ((EditText) findViewById(R.id.passwordInput)).getText().toString();
        String message = password + "#" + CONNECTION_ACCEPT_CODE;
        data = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, DEFAULT_PORT);
        clientSocket.send(sendPacket);

        /* Now wait for server ACK */
        receiveData = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        String ack = new String(receivePacket.getData());

        if(ack.equals(ACK_ACCEPTED)){
            //TODO: Start new intent

        }

        else{
            Log.d("[Error]", "Server rejected the connection: " + ack);
        }
    }

}
