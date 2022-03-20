package com.zizhizhan.textify;

import io.github.jonathanlink.PDFLayoutTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ReadPDFWithTable {

    public static void main(String[] args) throws IOException {
        File file = new File(String.format("/Users/james/Downloads/%s.pdf", "HB_7767-2005"));
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFLayoutTextStripper();
            stripper.setStartPage(6);
            stripper.setEndPage(6);
            System.out.println(stripper.getText(doc));
        }
    }

}
