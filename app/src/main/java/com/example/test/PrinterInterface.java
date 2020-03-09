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
            ((printPDF)myPrint).setFile(printerDetails.get(3));
            ((printPDF)myPrint).setPrintPages(1,2);
        }
    }
    public void print(){
        myPrint.print();
    }

}
