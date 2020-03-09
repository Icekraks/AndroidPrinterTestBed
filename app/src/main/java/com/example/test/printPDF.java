package com.example.test;

import com.brother.ptouch.sdk.PrinterInfo;

public class printPDF extends BasePrint {

    private int startIndex;
    private int endIndex;
    private String mPdfFile; //TODO: SUBJECT TO CHANGE BASED ON FLUTTER OUTPUT. (MUST BE STRING FOR PRINT PDF FUNCTION.)

    public void setPrintPages(int start,int end){
        startIndex = start;
        endIndex = end;
    }

    public void setFile(String file){
        mPdfFile = file;
    }
    @Override
    protected void doPrint() {
        for(int i =startIndex;i<endIndex;i++){
            mPrintResult = mPrinter.printPdfFile(mPdfFile,i);

            if (mPrintResult.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
                System.err.println("Failed to Print. Error: " + mPrintResult.errorCode);
                break;
            }
        }

    }
}
