package com.zizhizhan.legacies.pitfalls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GoodWindowsExec {

    /**
     * @param args
     */
    public static void main(String[] args) {

        try {
            Runtime rt = Runtime.getRuntime();
            String[] cmd = new String[3];
            cmd[0] = "cmd";
            cmd[1] = "/c";
            if (args.length > 0) {
                cmd[2] = args[0];
            } else {
                cmd[2] = "";
            }

            Process p = rt.exec(cmd);
            new StreamGobbler(p.getErrorStream(), "ERROR").start();
            new StreamGobbler(p.getInputStream(), "INPUT").start();

            int ret = p.waitFor();
            System.out.println("Process exitValue: " + ret);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}

    private static class StreamGobbler extends Thread {
        InputStream is;
        String type;

        StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;

                while ((line = br.readLine()) != null) {
                    System.out.println(type + " >  " + line);
                    System.out.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
