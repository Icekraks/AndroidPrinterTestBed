package com.example.test;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;
import com.brother.ptouch.sdk.PrinterInfo.Model;
import com.brother.ptouch.sdk.PrinterStatus;

import java.util.Set;

@SuppressWarnings("ALL")
public abstract class BasePrint {
    static Printer mPrinter;
    static boolean mCancel;
    private final Context mContext;
    PrinterStatus mPrintResult;
    private boolean manualCustomPaperSettingsEnabled;
    private String customSetting;
    private PrinterInfo mPrinterInfo;
    private String PaperType;

    BasePrint(Context context,String PaperrType) {

        mContext = context;
        PaperType = PaperrType;
        mCancel = false;
        // initialization for print
        mPrinterInfo = new PrinterInfo();
        mPrinter = new Printer();
        mPrinterInfo = mPrinter.getPrinterInfo();
    }

    public static void cancel() {
        if (mPrinter != null)
            mPrinter.cancel();
        mCancel = true;
    }

    protected abstract void doPrint();

    static class BasePrintResult {
        final boolean success;
        final String errorMessage;

        static BasePrintResult success() {
            return new BasePrintResult(true, "");
        }

        static BasePrintResult fail(String message) {
            return new BasePrintResult(false, message);
        }

        private BasePrintResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
    }
    /**
     * set PrinterInfo
     *
     * @return setCustomPaper's result. can ignore other than raster print
     */
    public BasePrintResult setPrinterInfo() {
        setPreferences();
        mPrinter.setPrinterInfo(mPrinterInfo);
        return BasePrintResult.success();

    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        mPrinter.setBluetooth(bluetoothAdapter);
    }


    /**
     * Sets the printer settings from the SharedPreferences
     */
    private void setPreferences() {
        if (mPrinterInfo == null) {
            mPrinterInfo = new PrinterInfo();
            return;
        }
        mPrinterInfo.printerModel = Model.QL_820NWB;
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName.contains("QL-820NWB")) {
                    mPrinterInfo.setLocalName(deviceName);
                    mPrinterInfo.macAddress = deviceHardwareAddress;
                }
            }
        }
        switch(PaperType){
            case "62*100":
                mPrinterInfo.labelNameIndex = LabelInfo.QL700.W62H100.ordinal();
            case "29*90":
                mPrinterInfo.labelNameIndex = LabelInfo.QL700.W29H90.ordinal();
            case "62RedBlack":
                mPrinterInfo.labelNameIndex = LabelInfo.QL700.W62RB.ordinal();
        }
        mPrinterInfo.orientation = PrinterInfo.Orientation.LANDSCAPE;
        mPrinterInfo.printMode = PrinterInfo.PrintMode.FIT_TO_PAGE;
        mPrinterInfo.halftone = PrinterInfo.Halftone.ERRORDIFFUSION;
        mPrinterInfo.numberOfCopies = 1;
        mPrinterInfo.isAutoCut = true;
        mPrinterInfo.scaleValue = 1.0;
        mPrinterInfo.printQuality = PrinterInfo.PrintQuality.NORMAL;
    }

    /**
     * Launch the thread to print
     */
    public void print() {
        mCancel = false;
        PrinterThread printTread = new PrinterThread();
        printTread.start();
    }


    /**
     * get the end message of print
     */
    @SuppressWarnings("UnusedAssignment")
    public String showResult() {

        String result;
        if (mPrintResult.errorCode == ErrorCode.ERROR_NONE) {
            result = "Succeeded";
        } else {
            result = mPrintResult.errorCode.toString();
        }

        return result;
    }



    /**
     * Thread for printing
     */
    private class PrinterThread extends Thread {
        @Override
        public void run() {

            // set info. for printing
            BasePrintResult result = setPrinterInfo();
            if (result.success == false) {
                System.out.println(result.toString());
                return;
            }

            // start message
            System.out.println("Starting to print");


            mPrintResult = new PrinterStatus();

            mPrinter.startCommunication();
            if (!mCancel) {
                doPrint();
            } else {
                mPrintResult.errorCode = ErrorCode.ERROR_CANCEL;
            }
            mPrinter.endCommunication();

            // end message
            System.out.println(showResult());

        }
    }

    /**
     * Thread for getting the printer's status
     */
}
