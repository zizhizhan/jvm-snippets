package com.zizhizhan.test.service.login;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM9:56
 * To change this template use File | Settings | File Templates.
 */
public class LoginServiceImp implements LoginService {

    public boolean login(String username, String password) {
        System.out.format("username: %s, password: %s have login.\n", username, password);
        return true;
    }

    public void sayHello(String message) {
        System.out.println(message);
    }

    public boolean logout(String username) {
        System.out.format("username: %s has log out.\n", username);
        return true;
    }

    public void exception() {
        throw new IllegalStateException("My Exception.");
    }
}