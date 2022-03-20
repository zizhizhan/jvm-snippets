package com.zizhizhan.textify;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfboxReader {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/james/Downloads/HB_7767-2005.pdf");
        PDDocument doc = PDDocument.load(file);
        int totalPage = doc.getNumberOfPages();
        float version = doc.getVersion();
        PDDocumentCatalog catalog = doc.getDocumentCatalog();
        System.out.format("totalPage=%d, version=%f\n", totalPage, version);
        System.out.println(catalog);
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);
        System.out.println(text);

//        PDPageTree tree = doc.getDocumentCatalog().getPages();
//        int pageNo = 0;
//        for (PDPage page : tree) {
//            pageNo += 1;
//            PDResources resources = page.getResources();
//            COSDictionary cosDictionary = resources.getCOSObject();
//            System.out.println(page);
//        }
//
//        System.out.println(pageNo);
    }

}
