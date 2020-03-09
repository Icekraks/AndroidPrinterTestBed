
package com.example.test;

import android.content.Context;
import com.brother.ptouch.sdk.PrinterInfo.ErrorCode;

public class PdfPrint extends BasePrint {

    private int startIndex;
    private int endIndex;
    private String mPdfFile;

    public PdfPrint(Context context,String PaperType) {
        super(context,PaperType);
    }


    /**
     * Sets the starting and ending indexes of the pdf file.
     */
    public void setPrintPage(int start, int end) {

        startIndex = start;
        endIndex = end;
    }

    /**
     * set print data
     */
    public void setFiles(String file) {
        mPdfFile = file;

    }

    /**
     * Implements the abstracted doPrint method in BasePrint.java for pdf specific printing.
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
