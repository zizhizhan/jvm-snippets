package com.zizhizhan.textify;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;

@Slf4j
public class LoadPdfFiles {

    public static void main(String[] args) {
        Tika tika = new Tika();
        try{
            File file = new File("/Users/james/Downloads/HB_7767-2005.pdf");
            String filetype = tika.detect(file);

            System.out.format("filetype: %s\n", filetype);
            System.out.println(tika.parseToString(file));
        } catch (IOException ex) {
            log.error("IOException: ", ex);
        } catch (TikaException ex) {
            log.error("TikaException: ", ex);
        }
    }

}
