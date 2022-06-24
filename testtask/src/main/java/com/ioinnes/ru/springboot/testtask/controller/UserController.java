package com.ioinnes.ru.springboot.testtask.controller;

import com.ioinnes.ru.springboot.testtask.dao.UserDAO;
import com.ioinnes.ru.springboot.testtask.entity.User;
import com.ioinnes.ru.springboot.testtask.processor.UserProcessor;
import com.ioinnes.ru.springboot.testtask.requests.ActivityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserProcessor userProcessor;
    @PostMapping("/users")
    @Order
    public CompletableFuture<String> save(@RequestBody User user) {
        return CompletableFuture.supplyAsync(() -> userProcessor.processSave(user, userDAO));
    }

    @PutMapping(value = "/users/{id}")
    @Order
    public CompletableFuture<String> changeStatus(@PathVariable("id") int id, @RequestParam String status) {
        return CompletableFuture.supplyAsync(() -> userProcessor.processChangeStatus(id, status, userDAO));
    }

    @GetMapping("/users")
    @Order
    public CompletableFuture<List<User>> findAll() {
        return CompletableFuture.supplyAsync(() -> userProcessor.processFindAll(userDAO));
    }

    @GetMapping("/users/{id}")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CompletableFuture<User> findById(@PathVariable int id) {
        return CompletableFuture.supplyAsync(() -> userProcessor.processFindById(id, userDAO));
    }


    @GetMapping(value = "/users/activity",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CompletableFuture<List<User>> findAllWithConditions(@RequestBody ActivityRequest request) {

        if (request.getId() == null)
            return CompletableFuture.supplyAsync(() -> userProcessor.processGetAll(request.getStatus(), null, userDAO));

        return userDAO.findById(request.getId())
                .map(user ->
                        CompletableFuture.supplyAsync(
                                () -> userProcessor.processGetAll(request.getStatus(),
                                        user.getTimestampStatus(), userDAO)))
                .orElse(null);

    }


    @DeleteMapping(value = "/users/{id}")
    @Order
    public CompletableFuture<String> deleteById(@PathVariable int id) {
        return CompletableFuture.supplyAsync(() -> userProcessor.processDeletingById(id, userDAO));
    }
 }
