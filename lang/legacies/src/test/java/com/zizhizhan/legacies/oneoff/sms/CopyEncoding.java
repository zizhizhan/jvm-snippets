package com.zizhizhan.legacies.oneoff.sms;

import com.google.common.io.Files;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 8/6/15
 *         Time: 11:35 PM
 */
public class CopyEncoding {

    public static void main(String[] args) {
        try {
            StringBuilder sb = new StringBuilder();
            Files.copy(new File("/Users/james/Downloads/2015-08-06-22-44-58-msg-58.csv"), Charset.forName("gbk"), sb);

            System.out.println(sb);
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("/Users/james/Downloads/smsgbk.csv"), Charset.forName("GBK"));
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
