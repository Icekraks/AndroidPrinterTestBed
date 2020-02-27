/**
 * Activity of printing pdf files
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.example.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.test.common.Common;
import com.example.test.common.MsgDialog;
import com.example.test.common.MsgHandle;

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
        mDialog = new MsgDialog(this);
        mHandle = new MsgHandle(this, mDialog);
        myPrint = new PdfPrint(this, mHandle, mDialog);

        // set the adapter when printing by way of Bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        myPrint.setBluetoothAdapter(bluetoothAdapter);


        // get data from other application by way of intent sending

            //HARD CODED THE PATH OF THE IMAGE,
            File tempFile = new File("/storage/emulated/0/Download/babyyoda.pdf");
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

    @Override
    public void selectFileButtonOnClick() {

        // call File Explorer Activity to select a pdf file
//        File [] files = new File[3];
//        files[0]=new File("/storage/emulated/0/Download/Tom.pdf");
//        files[1]=new File("/storage/emulated/0/Download/frame.pdf");
//        files[2]=new File("/storage/emulated/0/Download/IcekraksIG.pdf");

        return;
    }

    /**
     * Called when [Print] button is tapped
     */
    @Override
    public void printButtonOnClick() {
        int startPage = 1;
        int endPage = 1;

        // All pages
        // error if startPage > endPage

        // call function to print
        ((PdfPrint) myPrint).setPrintPage(startPage, endPage);
        myPrint.print();

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional data
     * from it.
     */
    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // get pdf File and set the new data to display
        if (resultCode == RESULT_OK && requestCode == Common.FILE_SELECT_PDF) {
            final String strRtn = data.getStringExtra(Common.INTENT_FILE_NAME);
            setPdfFile(strRtn);
        }
    }

    /**
     * set the pdf file for printing
     */
    private void setPdfFile(String file) {
        System.out.println("setPDFFile"+file);
        if (Common.isPdfFile(file)) {
            ((PdfPrint) myPrint).setFiles(file);
        }

    }

}
