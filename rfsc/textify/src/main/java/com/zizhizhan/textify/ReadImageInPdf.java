package com.zizhizhan.textify;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ReadImageInPdf {

    public static void main(String[] args) throws IOException {
        extractImages("HB_7767-2005");
        extractImages("HB_7756.1-2005");
        extractImages("GJB_192_1-2011");
        extractImages("GJB 191A-1997");
    }

    private static void extractImages(String filename) throws IOException {
        File file = new File(String.format("/Users/james/Downloads/%s.pdf", filename));
        try (PDDocument doc = PDDocument.load(file)) {
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                PDPage page = doc.getPage(i - 1);
                PDResources resources = page.getResources();
                Iterable<COSName> xObjectNames = resources.getXObjectNames();
                for (COSName name : xObjectNames) {
                    PDXObject xObject = resources.getXObject(name);
                    if (xObject instanceof PDImageXObject) {
                        String suffix = ((PDImageXObject) xObject).getSuffix();
                        String imageFile = String.format("/tmp/%s_%03d_%s.%s", filename, i, name.getName(), suffix);
                        ImageIO.write(((PDImageXObject) xObject).getImage(), suffix, new File(imageFile));
                    } else {
                        System.out.println("Unknown object " + filename + ": " + xObject);
                    }
                }
            }
        }
    }
}
