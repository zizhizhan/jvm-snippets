package me.jameszhan.spring.service;

import me.jameszhan.spring.model.User;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: James Zhan
 * Email: zhiqiangzhan@gmail.com
 * Date: 2019-01-07 23:21
 */
public interface UserService {

    List<User> findAll();

    User findById(Long id);

    boolean exist(User user);

    void save(User user);

    void update(User user);

    void delete(Long id);

}
