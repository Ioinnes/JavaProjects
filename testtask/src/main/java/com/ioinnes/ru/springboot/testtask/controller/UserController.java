package com.ioinnes.ru.springboot.testtask.controller;

import com.ioinnes.ru.springboot.testtask.dao.UserDAO;
import com.ioinnes.ru.springboot.testtask.entity.User;
import com.ioinnes.ru.springboot.testtask.processor.UserProcessor;
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
    public CompletableFuture<String> save(@RequestBody User user) {
        return CompletableFuture.supplyAsync(() -> userProcessor.processSave(user, userDAO));
    }

    @PutMapping(value = "/users/{id}")
    public CompletableFuture<String> changeStatus(@PathVariable("id") int id, @RequestParam String status) {
        return CompletableFuture.supplyAsync(() -> userProcessor.processChangeStatus(id, status, userDAO));
    }

    @GetMapping("/users")
    public CompletableFuture<List<User>> findAll() {
        return CompletableFuture.supplyAsync(() -> userDAO.findAll());
    }

    @GetMapping("/users/{id}")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CompletableFuture<User> findById(@PathVariable int id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional <User> optional = userDAO.findById(id);
            return optional.orElse(null);
        });
    }


    @GetMapping(value = "/users/activity",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CompletableFuture<List<User>> findAllWithConditions(@RequestParam(value = "status", required = false) String status,
                                                @RequestParam(value = "timestampString", required = false) String timestampString) {
        return CompletableFuture.supplyAsync(() -> userProcessor.processGetAll(status, timestampString, userDAO));
    }


    @DeleteMapping(value = "/users/{id}")
    public CompletableFuture<String> deleteById(@PathVariable int id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                userDAO.deleteById(id);
            } catch (Exception e) {
                return "There is no user with id " + id;
            }
            return String.format("User with id = %d was removed", id);
        });
    }
 }
