package com.example.test;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class PrinterInterface extends BaseActivity {
    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 10001;

    PrinterInterface(Context context, BluetoothAdapter bluetoothAdapter,String paperType){
        myPrint = new PdfPrint(context,paperType);
        myPrint.setBluetoothAdapter(bluetoothAdapter);
    }


    /**
     * Prompts the user with the permissions to access files on the device. This is a one time thing for a device.
     */
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
     * Calls the print method from PdfPrint.java while setting the start and end pages in the case of multiple
     * pages of printing.
     */
    @Override
    public void printButtonOnClick() {
        int startPage = 1;
        int endPage = 1;

        ((PdfPrint) myPrint).setPrintPage(startPage, endPage);
        myPrint.print();

    }

    /**
     * Set the pdf file for printing
     */
    public void setPdfFile(String file) {
        System.out.println("setPDFFile"+file);
        if (isPdfFile(file)) {
            ((PdfPrint) myPrint).setFiles(file);
        }

    }

    /**
     * Checks if the the file is a pdf, if it is, ignore the pdf extension of the file.
     */
    public static boolean isPdfFile(String path) {

        String extention = path.substring(
                path.lastIndexOf(".", path.length()) + 1, path.length());
        return extention.equalsIgnoreCase("pdf");
    }
}
