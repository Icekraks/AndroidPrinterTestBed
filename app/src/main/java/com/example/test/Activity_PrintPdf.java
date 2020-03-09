
package com.example.test;


import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


import java.io.File;

public class Activity_PrintPdf extends AppCompatActivity {

    PrinterInterface printInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialization for Activity

        Button mBtnPrint = (Button) findViewById(R.id.button);
        mBtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printInterface.printButtonOnClick();
            }
        });

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Parse the Context, Bluetooth adapater, and Paper Type
        printInterface = new PrinterInterface(this,bluetoothAdapter,"Paper");

        //Parse the file path
        File tempFile = new File("/storage/emulated/0/Download/babyyoda.pdf");
        printInterface.setPdfFile(tempFile.getPath());


    }






}
