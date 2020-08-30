package com.zizhizhan.auth;

import com.sun.security.auth.module.Krb5LoginModule;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginWithKeytab {

    public static void main(String[] args) throws LoginException {
        System.setProperty("sun.security.krb5.debug", "true");
        System.setProperty("java.security.krb5.conf", "/opt/etc/krb5.conf");

        Subject subject = new Subject();

        Krb5LoginModule krb5 = new Krb5LoginModule();
        Map<String, String> map = new HashMap<>();

        map.put("debug", "true");
        map.put("useTicketCache", "false");
        map.put("useKeyTab", "true");
        map.put("keyTab", "/opt/etc/deploy.keytab");
        map.put("principal", "deploy@ZIZHIZHAN.COM");
        map.put("storeKey", "true");

        krb5.initialize(subject, null, new HashMap<>(), map);

        boolean loginOk = krb5.login();
        log.info("Krb5 Login {}.", loginOk);
        boolean commitOk = krb5.commit();
        log.info("Krb5 Commit {}.", commitOk);

        log.info("Subject is {}.", subject);
    }

}
