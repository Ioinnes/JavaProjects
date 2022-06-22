package com.ioinnes.ru.springboot.testtask.processor;

import com.ioinnes.ru.springboot.testtask.auxiliry.ValidChecker;
import com.ioinnes.ru.springboot.testtask.controller.UserController;
import com.ioinnes.ru.springboot.testtask.dao.UserDAO;
import com.ioinnes.ru.springboot.testtask.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

// I decided to move some logics out from UserController
@Component
public class UserProcessor {
    private final Logger logger = Logger.getLogger(UserController.class.getSimpleName() + "::" + UserProcessor.class.getSimpleName());
    @Value("${images.default.image}")
    private String defaultAvatar;
    public List<User> processGetAll(String status, String timestampString, UserDAO userDAO) {

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
            logger.log(Level.WARNING, "Incorrect timestamp");
            return Collections.emptyList();
        }

        if (status != null)
            return userDAO.findByStatusOrderByTimestampStatus(status);

        return userDAO.findAll();
    }

    public String processSave(User user, UserDAO userDAO) {

        // user contract
        if (!ValidChecker.isUsernameValid(user.getUserName())) {
            logger.log(Level.INFO, "try to add user with illegal username");
            return "Invalid username";
        }

        // email contract
        if (!ValidChecker.isEmailValid(user.getEmail())) {
            logger.log(Level.INFO, "invalid email");
            return "Invalid email";
        }

        // check username duplicates
        if (userDAO.findByUserName(user.getUserName()).isPresent()) {
            logger.log(Level.INFO, "there is user with the same username");
            return "user with the same username already exists";
        }

        // check email duplicates
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            logger.log(Level.INFO, "there is user with the same email");
            return "user with the same email already exists";
        }

        // set up default avatar if is necessary
        if (user.getImageURI() == null)
            user.setImageURI(URI.create(defaultAvatar).toASCIIString());

        // check image existing
        if (!Files.exists(Paths.get(user.getImageURI()))) {
            logger.log(Level.INFO, "invalid image");
            return "invalid image";
        }

        // set up timestamp status and status
        user.setTimestampStatus(Timestamp.from(Instant.now()));
        user.setStatus("online");

        //save user to database
        User addedUser = userDAO.save(user);

        // return required string
        return String.format("User with id = %d was added", addedUser.getId());
    }

    public String processChangeStatus(int id, String status, UserDAO userDAO) {

        // check status's valid
        if (!"online".equals(status) && !"offline".equals(status) && status != null) {
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
            user.setStatus(status);
            user.setTimestampStatus(Timestamp.from(Instant.now()));

            userDAO.save(user);
            return String.format("User with id = %d changed his status from %s to %s", id, currentStatus, status);
        }

        //inform user is not existing
        return String.format("There is no user with id = %d", id);
    }
}
