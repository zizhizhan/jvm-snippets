package me.jameszhan.spring.web;

import lombok.extern.slf4j.Slf4j;
import me.jameszhan.spring.model.User;
import me.jameszhan.spring.service.UserService;
import me.jameszhan.spring.util.CustomErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: James Zhan
 * Email: zhiqiangzhan@gmail.com
 * Date: 2019-01-07 23:21
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> index() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> create(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        log.info("Creating User : {}", user);
        if (userService.exist(user)) {
            log.error("Unable to create. A User with name {} with id {} already exist", user.getName(), user.getId());
            return new ResponseEntity<>(new CustomErrorType("Unable to create. A User with name " +
                    user.getName() + " already exist."), HttpStatus.CONFLICT);
        }
        userService.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> show(@PathVariable("id") long id) {
        log.info("Fetching User with id {}", id);
        User user = userService.findById(id);
        if (user == null) {
            log.error("User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("User with id " + id + " not found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/users/{id}", method = { RequestMethod.PATCH, RequestMethod.PUT })
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody User user) {
        log.info("Updating User with id {}", id);
        User currentUser = userService.findById(id);
        if (currentUser == null) {
            log.error("Unable to update. User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Unable to update. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        userService.update(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> destroy(@PathVariable("id") long id) {
        log.info("Fetching & Deleting User with id {}", id);
        User user = userService.findById(id);
        if (user == null) {
            log.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity<>(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.delete(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

}
