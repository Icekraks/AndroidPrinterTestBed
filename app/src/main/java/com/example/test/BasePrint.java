package com.example.test;

import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;

public abstract class BasePrint {
    static Printer mPrinter;
    protected PrinterInfo mPrinterInfo;
    PrinterStatus mPrintResult;
    static boolean mCancel;

    BasePrint(){
        mCancel = false;
        mPrinterInfo = new PrinterInfo();
        mPrinter = new Printer();
        mPrinterInfo = mPrinter.getPrinterInfo();
    }

    public static void cancel(){
        if(mPrinter!=null){
            mPrinter.cancel();
        }
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

    public PrinterInfo getPrinterInfo(){
        return mPrinterInfo;
    }

    public void setPrinterInfo(String name, String macAddress,String paperType){
        //TODO: change so its parsing values from the device's bluetooth module to the library.
        if(mPrinterInfo==null){
            mPrinterInfo = new PrinterInfo();
        }
        mPrinterInfo.printerModel = PrinterInfo.Model.QL_820NWB;
        mPrinterInfo.macAddress = macAddress;

        //TODO: STRING TO PAPER TYPE CONVERSION
        mPrinterInfo.labelNameIndex = LabelInfo.QL700.W62H100.ordinal();
        mPrinterInfo.orientation = PrinterInfo.Orientation.LANDSCAPE;
        mPrinterInfo.printMode = PrinterInfo.PrintMode.FIT_TO_PAPER;
        mPrinterInfo.halftone = PrinterInfo.Halftone.ERRORDIFFUSION;
        mPrinterInfo.numberOfCopies = 1;
        mPrinterInfo.isAutoCut = true;
        mPrinterInfo.scaleValue = 1.0;
        mPrinterInfo.printQuality = PrinterInfo.PrintQuality.NORMAL;




    }

    public void bindPrinterInfo(){
        mPrinter.setPrinterInfo(mPrinterInfo);
    }


    public void print(){
        mCancel = false;
        PrinterThread printerThread = new PrinterThread();
        printerThread.start();
    }
    public void getPrinterStatus() {
        mCancel = false;
        getStatusThread getTread = new getStatusThread();
        getTread.start();
    }

    public void sendFile() {


        SendFileThread getThread = new SendFileThread();
        getThread.start();
    }
    public String showResult() {

        String result;
        if (mPrintResult.errorCode == PrinterInfo.ErrorCode.ERROR_NONE) {
            result = "Succeeded";
        } else {
            result = mPrintResult.errorCode.toString();
        }

        return result;
    }

    private class PrinterThread extends Thread {
        @Override
        public void run(){
            bindPrinterInfo();
            mPrintResult = new PrinterStatus();
            mPrinter.startCommunication();
            if (!mCancel) {
                doPrint();
            } else {
                mPrintResult.errorCode = PrinterInfo.ErrorCode.ERROR_CANCEL;
            }
            mPrinter.endCommunication();
            System.out.println(showResult());
        }
    }

    private class getStatusThread extends Thread {
        @Override
        public void run() {

            bindPrinterInfo();




            mPrintResult = new PrinterStatus();
            if (!mCancel) {
                mPrintResult = mPrinter.getPrinterStatus();
            } else {
                mPrintResult.errorCode = PrinterInfo.ErrorCode.ERROR_CANCEL;
            }

            System.out.println(showResult());

        }
    }

    /**
     * Thread for getting the printer's status
     */
    private class SendFileThread extends Thread {
        @Override
        public void run() {

            // set info. for printing
            bindPrinterInfo();

            mPrintResult = new PrinterStatus();

            mPrinter.startCommunication();

            doPrint();

            mPrinter.endCommunication();
            System.out.println(showResult());

        }
    }

}
