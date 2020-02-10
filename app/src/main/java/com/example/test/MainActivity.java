package com.example.test;

import com.brother.ptouch.sdk.*;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import java.util.List;


public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    //TODO: FIX NULL POINTER BLUETOOT

    testObject lol;
    public MainActivity(){
        this.lol = new testObject(null,null,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               printerMethod("Hello World");
            }
        });



    }

    protected void getPrinter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Printer printer = new Printer();
                List<BLEPrinter> printerList = printer.getBLEPrinters(BluetoothAdapter.getDefaultAdapter(), 30);
                if(printerList.isEmpty()){
                    System.out.println("failed to find printers");
                } else {
                    for (BLEPrinter p : printerList) {
                        System.out.println("Local Name: " + p.localName);
                        Log.d("TAG", "Local Name: " + p.localName);
                    }
                }
            }
        }).start();

    }
    protected void printerMethod(String output){
        System.out.println("lol1");
        getPrinter();
        System.out.println("after get printrer");
    }

//    protected testObject getObject(){
//        return lol;
//    }
//
//    protected void setObjectValues(String name, Integer id, String qrCode){
//        this.lol.Name = name;
//        this.lol.Number = id;
//        this.lol.QRCode = qrCode;
//    }

}

