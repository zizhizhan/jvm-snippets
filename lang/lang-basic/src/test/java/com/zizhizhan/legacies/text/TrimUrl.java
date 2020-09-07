package com.zizhizhan.legacies.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TrimUrl {

    public static void main(String[] args) throws IOException {
        List<String> list = new ArrayList<>();
        int i = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(
        		TrimUrl.class.getResourceAsStream("/urllist.txt")));

        String line;
        while (!(line = reader.readLine()).equalsIgnoreCase("")) {
            String tmp = line;
            line = line.replaceAll("http://", "");
            int pos = line.indexOf('/');
            if (pos == -1) {
                pos = line.length();
            }
            line = line.substring(0, pos).toLowerCase();
            System.out.println(tmp);
            System.out.println(line);

            if (!list.contains(line)) {
                list.add(line);
            }
            i++;
        }
        i = 0;

        for (String string : list) {
            System.out.println(++i + "\t" + string);
        }

        System.out.println(i);
        System.out.println(list.size());
    }
}
