/**
 * BasePrint for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.example.test;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;
import com.brother.ptouch.sdk.PrinterInfo.Model;
import com.brother.ptouch.sdk.PrinterStatus;
import com.example.test.common.Common;
import com.example.test.common.MsgDialog;
import com.example.test.common.MsgHandle;

import java.util.Set;

@SuppressWarnings("ALL")
public abstract class BasePrint {
    private static final long BLE_RESOLVE_TIMEOUT = 5000;
    static Printer mPrinter;
    static boolean mCancel;
    final MsgHandle mHandle;
    final MsgDialog mDialog;
    private final SharedPreferences sharedPreferences;
    private final Context mContext;
    PrinterStatus mPrintResult;
    private boolean manualCustomPaperSettingsEnabled;
    private String customSetting;
    private PrinterInfo mPrinterInfo;

    BasePrint(Context context, MsgHandle handle, MsgDialog dialog) {

        mContext = context;
        mDialog = dialog;
        mHandle = handle;
        mDialog.setHandle(mHandle);
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        mCancel = false;
        // initialization for print
        mPrinterInfo = new PrinterInfo();
        mPrinter = new Printer();
        mPrinterInfo = mPrinter.getPrinterInfo();
        mPrinter.setMessageHandle(mHandle, Common.MSG_SDK_EVENT);
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
        getPreferences();
        mPrinter.setPrinterInfo(mPrinterInfo);
        if (mPrinterInfo.port == PrinterInfo.Port.USB) {
            while (true) {
                if (Common.mUsbRequest != 0)
                    break;
            }
            if (Common.mUsbRequest != 1) {
            }
        }

            return BasePrintResult.success();

    }

    /**
     * get PrinterInfo
     */
    public PrinterInfo getPrinterInfo() {
        getPreferences();
        return mPrinterInfo;
    }

    /**
     * get Printer
     */
    public Printer getPrinter() {

        return mPrinter;
    }

    /**
     * get Printer
     */
    public PrinterStatus getPrintResult() {
        return mPrintResult;
    }

    /**
     * get Printer
     */
    public void setPrintResult(PrinterStatus printResult) {
        mPrintResult = printResult;
    }

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        mPrinter.setBluetooth(bluetoothAdapter);
        mPrinter.setBluetoothLowEnergy(mContext, bluetoothAdapter, BLE_RESOLVE_TIMEOUT);
    }


    /**
     * get the printer settings from the SharedPreferences
     */
    private void getPreferences() {
        if (mPrinterInfo == null) {
            mPrinterInfo = new PrinterInfo();
            return;
        }
        String input;
        mPrinterInfo.printerModel = Model.QL_820NWB;
        Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName.equals("QL-820NWB8014")) {
                    mPrinterInfo.setLocalName(deviceName);
                    mPrinterInfo.macAddress = deviceHardwareAddress;
                }
                System.out.println(deviceName);
                System.out.println(deviceHardwareAddress);

            }
        }
        mPrinterInfo.orientation = PrinterInfo.Orientation.PORTRAIT;
        mPrinterInfo.labelNameIndex = LabelInfo.QL700.W62H100.ordinal();
        mPrinterInfo.printMode = PrinterInfo.PrintMode.FIT_TO_PAGE;
        mPrinterInfo.halftone = PrinterInfo.Halftone.PATTERNDITHER;
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
     * Launch the thread to get the printer's status
     */
    public void getPrinterStatus() {
        mCancel = false;
        getStatusThread getTread = new getStatusThread();
        getTread.start();
    }

    /**
     * Launch the thread to print
     */
    public void sendFile() {


        SendFileThread getTread = new SendFileThread();
        getTread.start();
    }

    /**
     * set custom paper for RJ and TD
     */


    private float parseFloat(String s, float defaultValue) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
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
     * show information of battery
     */


    /**
     * Thread for printing
     */
    private class PrinterThread extends Thread {
        @Override
        public void run() {

            // set info. for printing
            BasePrintResult result = setPrinterInfo();
            if (result.success == false) {
                mHandle.setResult(result.errorMessage);
                mHandle.sendMessage(mHandle.obtainMessage(Common.MSG_PRINT_END));
                return;
            }

            // start message
            Message msg = mHandle.obtainMessage(Common.MSG_PRINT_START);
            mHandle.sendMessage(msg);

            mPrintResult = new PrinterStatus();

            mPrinter.startCommunication();
            if (!mCancel) {
                doPrint();
            } else {
                mPrintResult.errorCode = ErrorCode.ERROR_CANCEL;
            }
            mPrinter.endCommunication();

            // end message
            mHandle.setResult(showResult());
            msg = mHandle.obtainMessage(Common.MSG_PRINT_END);
            mHandle.sendMessage(msg);
        }
    }

    /**
     * Thread for getting the printer's status
     */
    private class getStatusThread extends Thread {
        @Override
        public void run() {

            // set info. for printing
            setPrinterInfo();

            // start message
            Message msg = mHandle.obtainMessage(Common.MSG_PRINT_START);
            mHandle.sendMessage(msg);

            mPrintResult = new PrinterStatus();
            if (!mCancel) {
                mPrintResult = mPrinter.getPrinterStatus();
            } else {
                mPrintResult.errorCode = ErrorCode.ERROR_CANCEL;
            }
            // end message
            mHandle.setResult(showResult());
            msg = mHandle.obtainMessage(Common.MSG_PRINT_END);
            mHandle.sendMessage(msg);

        }
    }

    /**
     * Thread for getting the printer's status
     */
    private class SendFileThread extends Thread {
        @Override
        public void run() {

            // set info. for printing
            setPrinterInfo();

            // start message
            Message msg = mHandle.obtainMessage(Common.MSG_PRINT_START);
            mHandle.sendMessage(msg);

            mPrintResult = new PrinterStatus();

            mPrinter.startCommunication();

            doPrint();

            mPrinter.endCommunication();
            // end message
            mHandle.setResult(showResult());
            msg = mHandle.obtainMessage(Common.MSG_PRINT_END);
            mHandle.sendMessage(msg);

        }
    }
}
