package com.zizhizhan.legacies.security.jaas.research;

import java.security.PrivilegedAction;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class LoginTests {

    public static void main(String[] args) {
        try {
            LoginContext lc = new LoginContext("LoginTests");
            lc.login();

            Subject sub = lc.getSubject();
            Subject.doAs(sub, new PrivilegedAction<Boolean>() {
                @Override
                public Boolean run() {
                    return null;
                }
            });
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
