/**
 * Activity of printing pdf files
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.example.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;
import java.util.Set;

public class Activity_PrintPdf extends BaseActivity {

    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 10001;
    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
    private static final int REQUEST_ENABLE_BT = 55;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // initialization for Activity

        printInterface = new PrinterInterface();
        ArrayList<String> temp = new ArrayList<>();

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
                if (deviceName.contains("QL-820NWB")) {
                    temp.add(0,deviceName);
                    temp.add(1,deviceHardwareAddress);
                    temp.add(2,"paperType");
                    temp.add(3,"/storage/emulated/0/Download/eeb.pdf");
                }

            }
        }
        for(String x: temp){
            System.out.println(x);
        }

        printInterface.setPrinterDetails(temp);

        Button mBtnPrint = (Button) findViewById(R.id.button);
        mBtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printInterface.print();
            }
        });



        // set the adapter when printing by way of Bluetooth


        // get data from other application by way of intent sending

        //HARD CODED THE PATH OF THE IMAGE,
    }

    private boolean isPermitWriteStorage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        if (!isPermitWriteStorage()) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }


}

