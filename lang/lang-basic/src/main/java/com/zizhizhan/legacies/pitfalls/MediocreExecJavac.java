package com.zizhizhan.legacies.pitfalls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MediocreExecJavac {

    /**
     * @param args
     */
    public static void main(String[] args) {

        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("javac");
            InputStream stderr = p.getErrorStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(stderr));

            System.out.println("<error>");
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("</error>");

            int ret = p.waitFor();
            System.out.println("Process exitValue: " + ret);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}

}
