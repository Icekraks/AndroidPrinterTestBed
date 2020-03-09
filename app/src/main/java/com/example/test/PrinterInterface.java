package com.example.test;

import java.util.ArrayList;

public class PrinterInterface {
    BasePrint myPrint;

    PrinterInterface(){
        myPrint = new printPDF();
    }


    public void setPrinterDetails(ArrayList<String> printerDetails){
        if(printerDetails!=null && printerDetails.size()==4){
            myPrint.setPrinterInfo(printerDetails.get(0),printerDetails.get(1),printerDetails.get(2));
            printPDF temp = (printPDF)myPrint;
            temp.setFile(printerDetails.get(3));
        }
    }
    public void print(){
        myPrint.print();
    }

}
