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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.io.File;

public class Activity_PrintPdf extends BaseActivity {

    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialization for Activity

        Button mBtnPrint = (Button) findViewById(R.id.button);
        mBtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printButtonOnClick();
            }
        });


        // initialization for printing
        myPrint = new PdfPrint(this);

        // set the adapter when printing by way of Bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        myPrint.setBluetoothAdapter(bluetoothAdapter);


        // get data from other application by way of intent sending

            //HARD CODED THE PATH OF THE IMAGE,
            File tempFile = new File("/storage/emulated/0/Download/eeb.pdf");
            System.out.println("File Exists? "+tempFile.exists());
            setPdfFile(tempFile.getPath());

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


    /**
     * Called when [Print] button is tapped
     */
    @Override
    public void printButtonOnClick() {
        int startPage = 1;
        int endPage = 1;

        // All pages
        // errogtr if startPage > endPage

        // call function to print
        ((PdfPrint) myPrint).setPrintPage(startPage, endPage);
        myPrint.print();

    }

//    /**
//     * Called when an activity you launched exits, giving you the requestCode
//     * you started it with, the resultCode it returned, and any additional data
//     * from it.
//     */
//    @Override
//    protected void onActivityResult(final int requestCode,
//                                    final int resultCode, final Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // get pdf File and set the new data to display
//        if (resultCode == RESULT_OK && requestCode == Common.FILE_SELECT_PDF) {
//            final String strRtn = dataca.getStringExtra(Common.INTENT_FILE_NAME);
//            setPdfFile(strRtn);
//        }
//    }

    /**
     * set the pdf file for printing
     */
    private void setPdfFile(String file) {
        System.out.println("setPDFFile"+file);
        if (isPdfFile(file)) {
            ((PdfPrint) myPrint).setFiles(file);
        }

    }
    public static boolean isPdfFile(String path) {

        String extention = path.substring(
                path.lastIndexOf(".", path.length()) + 1, path.length());
        return extention.equalsIgnoreCase("pdf");
    }

}
