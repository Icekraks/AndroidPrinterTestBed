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
import com.example.test.common.MsgDialog;
import com.example.test.common.MsgHandle;

public class PdfPrint extends BasePrint {

    private int startIndex;
    private int endIndex;
    private String mPdfFile;

    public PdfPrint(Context context, MsgHandle mHandle, MsgDialog mDialog) {
        super(context, mHandle, mDialog);
    }

    /**
     * get print pdf pages
     */
    public int getPdfPages(String file) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return mPrinter.getPDFPages(file);
        } else {
            return mPrinter.getPDFFilePages(file);

        }
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
        mPdfFile = "/storage/emulated/0/Download/Tom.pdf";
        System.out.println("TITLE: setFiles pdfPrint File" + file );
        System.out.println("TITLE: setFiles pdfPrint mPDF" + mPdfFile );



    }

    /**
     * do the particular print
     */
    @Override
    protected void doPrint() {


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mPrintResult = mPrinter.printPDF(mPdfFile, 1);
            } else {
                System.out.println("right b4 print");
                System.out.println(mPdfFile);
                mPrintResult = mPrinter.printPdfFile(mPdfFile, 1);

            }
            if (mPrintResult.errorCode != ErrorCode.ERROR_NONE) {
                System.err.println("Failed to Print. Error: "+mPrintResult.errorCode);
                return;
            }
    }

}
