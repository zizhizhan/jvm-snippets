package com.zizhizhan.lang;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunShellCommand {

    public static void main(String[] args) throws Exception {

        // Open Visual Studio Code
        Runtime.getRuntime().exec("code");
        Process process = Runtime.getRuntime().exec("ls -lah");
        String line;
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = (buffer.readLine())) != null) {
                System.out.println(line);
            }
        }
    }

}
