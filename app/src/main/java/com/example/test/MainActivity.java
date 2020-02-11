package com.example.test;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import android.bluetooth.BluetoothAdapter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.print.PrintHelper;
import com.brother.ptouch.sdk.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 55;
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    Printer myPrinter;

    private static String PrinterName;
    private static String MacAddress;

    testObject lol;

    public MainActivity() {
        this.lol = new testObject(null, null, null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!bluetooth.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName.equals("QL-820NWB8014")) {
                    PrinterName = "QL-820NWB8014";
                    MacAddress = deviceHardwareAddress;
                }
                System.out.println(deviceName);
                System.out.println(deviceHardwareAddress);
            }
        }

        myPrinter = new Printer();
        PrinterInfo info = new PrinterInfo();
        info.macAddress = MacAddress;
        info.labelNameIndex = LabelInfo.QL700.W29H90.ordinal();
        info.printerModel = PrinterInfo.Model.QL_820NWB;
        info.printMode = PrinterInfo.PrintMode.FIT_TO_PAPER;
        info.halftone = PrinterInfo.Halftone.ERRORDIFFUSION;
        myPrinter.setPrinterInfo(info);
        myPrinter.setBluetooth(bluetooth);
        System.out.println(myPrinter.getPrinterStatus());
        System.out.println(myPrinter.getSerialNumber());


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printLabel();
            }
        });

        //COMMENTED CODE, MASS DISCOVery of bluetooth devices.
//        else {
//            bluetooth.startDiscovery();
//            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            BroadcastReceiver receiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    String action = intent.getAction();
//                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                        // Discovery has found a device. Get the BluetoothDevice
//                        // object and its info from the Intent.
//                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                        String deviceName = device.getName();
//                        String deviceHardwareAddress = device.getAddress(); // MAC address
//                        System.out.println(deviceName);
//                        System.out.println(deviceHardwareAddress);
//                    }
//
//                }
//            };
//            registerReceiver(receiver, filter);
//        }
    }

    protected void printLabel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (myPrinter.startCommunication()) {
//                    PrinterStatus result = myPrinter.printImage(bitmap);

                    PrinterStatus result = myPrinter.printPdfFile();
                    if (result.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
                        Log.d("TAG", "ERROR - " + result.errorCode);
                    }
                    myPrinter.endCommunication();
                }
            }
        }).start();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.

    }

}

