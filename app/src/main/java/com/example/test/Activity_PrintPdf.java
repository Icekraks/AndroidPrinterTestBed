/**
 * Activity of printing pdf files
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.example.test;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.common.Common;
import com.example.test.common.MsgDialog;
import com.example.test.common.MsgHandle;

import java.io.File;

public class Activity_PrintPdf extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialization for Activity



        // initialization for printing
        mDialog = new MsgDialog(this);
        mHandle = new MsgHandle(this, mDialog);
        myPrint = new PdfPrint(this, mHandle, mDialog);

        // set the adapter when printing by way of Bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        myPrint.setBluetoothAdapter(bluetoothAdapter);


        // get data from other application by way of intent sending

            //HARD CODED THE PATH OF THE IMAGE,
            File tempFile = new File("/storage/emulated/0/Download/Tom.pdf");
            System.out.println("File Exists? "+tempFile.exists());
            String file = "/storage/emulated/0/Download/frame.pdf";
            setPdfFile(file);
        Button mBtnPrint = (Button) findViewById(R.id.button);
        mBtnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printButtonOnClick();
            }
        });
    }

    @Override
    public void selectFileButtonOnClick() {
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
