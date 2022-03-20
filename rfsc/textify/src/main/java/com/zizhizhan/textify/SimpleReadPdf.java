package com.zizhizhan.textify;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class SimpleReadPdf {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/james/Downloads/HB_7767-2005.pdf");
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            stripper.setEndPage(doc.getNumberOfPages());
            System.out.println(stripper.getText(doc));
        }
    }

}
