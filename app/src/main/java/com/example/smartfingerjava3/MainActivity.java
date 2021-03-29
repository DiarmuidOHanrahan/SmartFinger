package com.example.smartfingerjava3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Button btnOn;
    Button btnOff;
    EditText txtAddress;
    TextView status;
    Socket myAppSocket = null;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOn = (Button) findViewById(R.id.btnOn);
        btnOff = (Button) findViewById(R.id.btnOff);
        status = (TextView)findViewById(R.id.status);

        txtAddress = (EditText) findViewById(R.id.IPAddress);

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "1";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
                String status_on = "Status: ON";
                status.setText(status_on);
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIPandPort();
                CMD = "0";
                Soket_AsyncTask cmd_increase_servo = new Soket_AsyncTask();
                cmd_increase_servo.execute();
                String status_off = "Status OFF";
                status.setText(status_off);
            }
        });
    }
    public void getIPandPort()
    {
        String iPandPort=txtAddress.getText().toString();
        Log.d("MYTEST","IP String: "+ iPandPort);
        String temp[]= iPandPort.split(":");
        wifiModuleIp=temp[0];
        wifiModulePort= Integer.valueOf(temp[1]);
        Log.d("MY TEST","IP:" +wifiModuleIp);
        Log.d("MY TEST", "PORT:"+wifiModulePort);
    }

    public class Soket_AsyncTask extends AsyncTask<Void,Void,Void>{

        Socket socket;

        @Override
        protected Void doInBackground(Void... params) {
            try{
                InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIp);
                socket = new java.net.Socket(inetAddress,MainActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            }catch (UnknownHostException e) {e.printStackTrace();}catch (IOException e) {e.printStackTrace();}
            return null;
        }
    }
} 