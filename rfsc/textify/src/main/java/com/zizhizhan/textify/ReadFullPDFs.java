package com.zizhizhan.textify;

import lombok.Data;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReadFullPDFs {

    public static void main(String[] args) throws IOException {
        for (String filename : new String[]{"HB_7767-2005", "HB_7756.1-2005", "GJB_192_1-2011", "GJB_191A-1997"}) {
            List<PageData> data = extractPdf(filename);
            DumperOptions options = new DumperOptions();
            Representer representer = new Representer();
            representer.addClassTag(PageData.class, Tag.MAP);
//            representer.addClassTag(List.class, Tag.SEQ);

            Yaml yaml = new Yaml(representer, options);
            try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(String.format("/tmp/%s.yaml", filename)), StandardCharsets.UTF_8)) {
                yaml.dump(data, osw);
            }
        }

        Yaml yaml = new Yaml();
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(String.format("/tmp/%s.yaml", "HB_7767-2005")), StandardCharsets.UTF_8)) {
            List<Object> list = yaml.loadAs(isr, List.class);
            System.out.println(list);
        }
    }

    private static List<PageData> extractPdf(String filename) throws IOException {
        List<PageData> pds = new ArrayList<>();
        File file = new File(String.format("/Users/james/Downloads/%s.pdf", filename));
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                pds.add(extractPage(doc, stripper, filename, i));
            }
        }

        return pds;
    }

    private static PageData extractPage(PDDocument doc, PDFTextStripper stripper, String filename, int pageNo) throws IOException {
        PageData pd = new PageData();
        pd.setPageNo(pageNo);

        stripper.setStartPage(pageNo);
        stripper.setEndPage(pageNo);
        pd.setText(stripper.getText(doc));

        PDPage page = doc.getPage(pageNo - 1);
        PDResources resources = page.getResources();
        Iterable<COSName> xObjectNames = resources.getXObjectNames();
        for (COSName name : xObjectNames) {
            PDXObject xObject = resources.getXObject(name);
            if (xObject instanceof PDImageXObject) {
                String suffix = ((PDImageXObject) xObject).getSuffix();
                String imageFile = String.format("/tmp/%s_%03d_%s.%s", filename, pageNo, name.getName(), suffix);
                ImageIO.write(((PDImageXObject) xObject).getImage(), suffix, new File(imageFile));
                pd.getImages().add(imageFile);
            } else {
                System.out.println("Unknown object " + filename + ": " + xObject);
            }
        }
        return pd;
    }
}

@Data
class PageData implements Serializable {
    private Integer pageNo;
    private String text;
    private List<String> images;

    public PageData() {
        this.images = new ArrayList<>();
    }
}
