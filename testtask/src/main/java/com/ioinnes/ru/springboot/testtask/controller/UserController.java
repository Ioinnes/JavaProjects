package com.ioinnes.ru.springboot.testtask.controller;

import com.ioinnes.ru.springboot.testtask.dao.UserDAO;
import com.ioinnes.ru.springboot.testtask.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/")
public class UserController {
    private final Logger logger = Logger.getLogger(UserController.class.getSimpleName());
    @Autowired
    private UserDAO userDAO;
    @Value("${images.default.image}")
    private String defaultAvatar;

    @PostMapping("/users")
    public String save(@RequestBody User user) {
        // check if is null
        if (user == null) {
            logger.log(Level.INFO, "user can't be null");
            return "user can't be null";
        }
        // set up default avatar if is necessary
        if (user.getImageURI() == null)
            user.setImageURI(URI.create(defaultAvatar).toASCIIString());
        // set up timestamp status and status
        user.setTimestampStatus(Timestamp.from(Instant.now()));
        user.setStatus("online");
        //save user to database
        User addedUser = userDAO.save(user);
        // return required string
        return String.format("User with id = %d was added", addedUser.getId());
    }

    @GetMapping("/users")
    public List<User> findAll() {
        // idea helped me to simplify
        return userDAO.findAll();
    }

    @GetMapping("/users/{id}")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public User findById(@PathVariable int id) {

        Optional<User> optional = userDAO.findById(id);
        return optional.orElse(null);
    }


    @GetMapping(value = "/users/activity",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public List<User> getAll(@RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "timestampString", required = false) String timestampString) {
        // check status's valid
        if (!"online".equals(status) && !"offline".equals(status) && status != null) {
            logger.log(Level.INFO, "Illegal status");
            return null;
        }
        //main logic
        try {
            if (timestampString != null && status != null)
                return userDAO.findByStatusAndTimestampStatusAfterOrderByTimestampStatus(status, Timestamp.valueOf(timestampString));

            if (timestampString != null)
                return userDAO.findByTimestampStatusAfterOrderByStatusAscTimestampStatusAsc(Timestamp.valueOf(timestampString));

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Incorrect timeStampString");
            return Collections.emptyList();
        }

        if (status != null)
            return userDAO.findByStatusOrderByTimestampStatus(status);

        return userDAO.findAll();
    }


    @PutMapping("/users/{id}")
    public String changeStatus(@PathVariable int id, @RequestParam("status") String newStatus) {
        // check status's valid
        if (!"online".equals(newStatus) && !"offline".equals(newStatus)) {
            logger.log(Level.INFO, "Illegal status");
            return "Status is not valid";
        }
        Optional<User> optional = userDAO.findById(id);
        // request to another server/api
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Something wrong with another server/api");
        }
        // make manipulations with user
        if (optional.isPresent()) {
            User user = optional.get();
            String currentStatus = user.getStatus();
            user.setStatus(newStatus);
            user.setTimestampStatus(Timestamp.from(Instant.now()));
            userDAO.save(user);
            return String.format("User with id = %d changed his status from %s to %s", id, currentStatus, newStatus);
        }
        //inform user is not existing
        return String.format("There is no user with id = %d", id);
    }
 }
