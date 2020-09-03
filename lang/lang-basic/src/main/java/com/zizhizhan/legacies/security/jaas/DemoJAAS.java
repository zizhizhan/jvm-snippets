package com.zizhizhan.legacies.security.jaas;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class DemoJAAS {

    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("java.home"));
        System.setProperty("user.home", new File(".").getCanonicalPath());

        DemoJAAS demo = new DemoJAAS();
        LoginContext lc = null;
        try {
            lc = new LoginContext("TestJAAS", demo.new DemoCallbackHandler());
        } catch (LoginException e) {
            System.err.println("Cloud not create login context.");
            e.printStackTrace();
        }
        int tries = 1;
        boolean succ = false;
        for (; tries <= 3; tries++) {
            try {
                lc.login();
                succ = true;
                break;
            } catch (LoginException e) {
                System.err.println("LoginException: authentication failed: " + e);
                e.printStackTrace();
            }
        }
        if (tries <= 3) {
            Set<Principal> principals = lc.getSubject().getPrincipals();
            Iterator<Principal> i = principals.iterator();
            System.out.println("Login successful.");
            while (i.hasNext()) {
                succ = true;
                System.out.println("\tPrincipal:" + i.next().getName());
            }
        } else {
            System.out.println("Login failed");
        }

        System.out.println("succ = " + succ);
    }

    public class DemoCallbackHandler implements CallbackHandler {
        @Override
        public void handle(Callback[] callbacks) throws IOException,
                UnsupportedCallbackException {
            boolean foundCallback = false;
            for (Callback cb : callbacks) {
                if (cb instanceof NameCallback) {
                    foundCallback = true;
                    NameCallback name = (NameCallback) cb;
                    System.out.print(name.getPrompt() + " ");
                    name.setName(new BufferedReader(new InputStreamReader(System.in)).readLine());
                }
                if (cb instanceof PasswordCallback) {
                    foundCallback = true;
                    PasswordCallback password = (PasswordCallback) cb;
                    String pwd = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    password.setPassword(pwd.toCharArray());
                }
            }

            if (!foundCallback) {
                throw new UnsupportedCallbackException(callbacks[0], "No valid callback found.");
            }

        }
    }

}
