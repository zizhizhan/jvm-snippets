package com.zizhizhan.test.service.login;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:55
 * To change this template use File | Settings | File Templates.
 */
public interface LoginService {

    boolean login(String username, String password);

    void sayHello(String message);

    boolean logout(String username);

    void exception();
}
