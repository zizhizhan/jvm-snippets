package com.zizhizhan.textify;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ReadPdfByPage {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/james/Downloads/GJB_192_1-2011.pdf");
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                System.out.printf("---------------%d--------------\n", i);
                System.out.println(stripper.getText(doc));
                System.out.printf("===============%d==============\n", i);
            }
        }
    }
}
