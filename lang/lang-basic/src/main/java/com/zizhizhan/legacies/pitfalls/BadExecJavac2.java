package com.zizhizhan.legacies.pitfalls;

import java.io.IOException;

public class BadExecJavac2 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        Process p;
        try {
            p = rt.exec("javac");
            int ret = p.waitFor();
            System.out.println("Process exitValue: " + ret);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}

}
