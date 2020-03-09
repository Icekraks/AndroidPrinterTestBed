/**
 * PdfPrint for printing
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */
package com.example.test;

import android.content.Context;
import android.os.Build;

import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;

public class PdfPrint extends BasePrint {

    private int startIndex;
    private int endIndex;
    private String mPdfFile;

    public PdfPrint(Context context) {
        super(context);
    }


    /**
     * set print pdf pages
     */
    public void setPrintPage(int start, int end) {

        startIndex = start;
        endIndex = end;
        System.out.println(startIndex);
        System.out.println(endIndex);
    }

    /**
     * set print data
     */
    public void setFiles(String file) {
        mPdfFile = file;

    }

    /**
     * do the particular print
     */
    @Override
    protected void doPrint() {


        for (int i = 0; i < 1; i++) {

            System.out.println("right b4 print");
            System.out.println(mPdfFile);
            mPrintResult = mPrinter.printPdfFile(mPdfFile, 1);


            if (mPrintResult.errorCode != ErrorCode.ERROR_NONE) {
                System.err.println("Failed to Print. Error: " + mPrintResult.errorCode);
                break;
            }
        }
    }

}
