package com.zizhizhan.legacies.oneoff.sms;

import com.google.common.base.Joiner;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 8/6/15
 *         Time: 11:49 PM
 */
public class CSVProcressing {
    public static void main(String[] args) throws Exception {
        CSVReader rd = new CSVReader(new InputStreamReader(
                new FileInputStream("/Users/james/Downloads/2015-08-06-22-44-58-msg-58.csv"), Charset.forName("GBK")));

        DateFormat inf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat ouf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        CSVWriter wr = new CSVWriter(new OutputStreamWriter(new FileOutputStream("/Users/james/Downloads/hahaha.csv"), Charset.forName("UTF-8")));
        wr.writeNext(new String[]{"PhoneNumber", "DateCreated", "Content", "Type", "State"}, false);
        int i = 0;
        Iterator<String[]> itor = rd.iterator();
        while (itor.hasNext()) {
            String[] fields = itor.next();
            if (i > 0) {
                String type = fields[4].equals("收件箱") ? "recv" : "send";
                String date = ouf.format(inf.parse(fields[3]));
                String contact = fields[2];
                if (!Pattern.matches("\\d+", fields[1])) {
                    contact += " (" + StringUtils.trim(fields[1]) + ")";
                }
                String content = fields[0].replaceAll("/\\r\\n/", " ");
                String[] target = new String[]{contact, date, content, type, "read"};
                System.out.println(Joiner.on(",").join(target));
                wr.writeNext(target, false);
            }
            i++;
        }
        wr.close();
    }
}
