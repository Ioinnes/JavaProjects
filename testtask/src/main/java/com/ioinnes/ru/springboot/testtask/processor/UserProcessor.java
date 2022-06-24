package com.ioinnes.ru.springboot.testtask.processor;

import com.ioinnes.ru.springboot.testtask.auxiliry.Message;
import com.ioinnes.ru.springboot.testtask.auxiliry.ValidChecker;
import com.ioinnes.ru.springboot.testtask.dao.UserDAO;
import com.ioinnes.ru.springboot.testtask.entity.Status;
import com.ioinnes.ru.springboot.testtask.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// I decided to move some logics out from UserController
@Component
public class UserProcessor {

    @Value("${images.default.image}")
    private String defaultAvatar;

    public List<User> processGetAll(String status, Timestamp timestamp, UserDAO userDAO) {

        // check status's valid
        if (!Status.online.toString().equals(status)
                && !Status.offline.toString().equals(status)
                && status != null) {
            throw new IllegalArgumentException(Message.INVALID_STATUS);
        }

        //main logic
        if (timestamp != null && status != null)
            return userDAO.findByStatusAndTimestampStatusAfterOrderByTimestampStatus(status, timestamp);

        if (timestamp != null)
            return userDAO.findByTimestampStatusAfterOrderByStatusAscTimestampStatusAsc(timestamp);


        if (status != null)
            return userDAO.findByStatusOrderByTimestampStatus(status);

        return userDAO.findAll();
    }

    public String processSave(User user, UserDAO userDAO) {

        // username and email contract
        if (!ValidChecker.isUsernameValid(user.getUserName()) ||
                !ValidChecker.isEmailValid(user.getEmail())) {
            throw new IllegalArgumentException(Message.INVALID_DATA);
        }

        // check username duplicates
        if (userDAO.findByUserName(user.getUserName()).isPresent() ||
                userDAO.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Duplicate");
        }

        // check image existing
        if (user.getImageURI() != null &&
                !Files.exists(Paths.get(user.getImageURI()))) {
            throw new IllegalArgumentException(Message.INVALID_IMAGE);
        }

        // set up default avatar if is necessary
        // default avatar exists
        if (user.getImageURI() == null)
            user.setImageURI(URI.create(defaultAvatar).toASCIIString());

        // set up timestamp status and status
        user.setTimestampStatus(Timestamp.from(Instant.now()));
        user.setStatus(Status.online.toString());

        User addedUser;
        try {
            addedUser = userDAO.save(user);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        // return required string
        return String.format(Message.USER_WITH_ID_WAS_ADDED, addedUser.getId());
    }

    public String processChangeStatus(int id, String status, UserDAO userDAO) {

        // check status's valid
        if (!ValidChecker.isStatusValid(status)) {
            throw new IllegalArgumentException();
        }

        Optional<User> optional = userDAO.findById(id);

        // request to another server/api
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // make manipulations with user
        if (optional.isPresent()) {
            User user = optional.get();

            String currentStatus = user.getStatus();
            user.setStatus(status);
            user.setTimestampStatus(Timestamp.from(Instant.now()));

            userDAO.save(user);
            return String.format(Message.USER_WITH_ID_CHANGED_HIS_STATUS_FROM_S_TO_S, id, currentStatus, status);
        }

        //inform user is not existing
        throw new NoSuchElementException();

    }

    public String processDeletingById(int id, UserDAO userDAO) {

        try {
            userDAO.deleteById(id);
        } catch (Exception ignore) {
            throw new NoSuchElementException();
        }

        return String.format(Message.USER_WITH_ID_WAS_FOUND_AND_REMOVED, id);
    }


    public User processFindById(int id, UserDAO userDAO) {

            Optional <User> optional = userDAO.findById(id);

            if (optional.isPresent())
                return optional.get();

            throw new NoSuchElementException();

    }

    public List<User> processFindAll(UserDAO userDAO) {
        return userDAO.findAll();
    }
}
