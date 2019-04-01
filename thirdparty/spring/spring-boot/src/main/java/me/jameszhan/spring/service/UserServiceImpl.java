package me.jameszhan.spring.service;

import me.jameszhan.spring.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: James Zhan
 * Email: zhiqiangzhan@gmail.com
 * Date: 2019-01-07 23:21
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final AtomicLong counter = new AtomicLong();
    private static List<User> users;

    static{
        users= populateDummyUsers();
    }

    public List<User> findAll() {
        return users;
    }

    public User findById(Long id) {
        for(User user : users){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }


    public void save(User user) {
        user.setId(counter.incrementAndGet());
        users.add(user);
    }

    public void update(User user) {
        int index = users.indexOf(user);
        users.set(index, user);
    }

    public void delete(Long id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    public boolean exist(User user) {
        return findByName(user.getName()) != null;
    }

    private User findByName(String name) {
        for(User user : users){
            if(user.getName().equalsIgnoreCase(name)){
                return user;
            }
        }
        return null;
    }


    private static List<User> populateDummyUsers(){
        List<User> users = new ArrayList<>();
        users.add(new User(counter.incrementAndGet(), "Sam",30));
        users.add(new User(counter.incrementAndGet(), "Tom",40));
        users.add(new User(counter.incrementAndGet(), "Jerome",45));
        users.add(new User(counter.incrementAndGet(), "Silvia",50));
        return users;
    }

}
