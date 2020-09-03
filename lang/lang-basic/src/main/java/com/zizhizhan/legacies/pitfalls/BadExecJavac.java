package com.zizhizhan.legacies.pitfalls;

import java.io.IOException;

public class BadExecJavac {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process p = rt.exec("javac");
            int ret = p.exitValue();
            System.out.println("Process exitValue: " + ret);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
