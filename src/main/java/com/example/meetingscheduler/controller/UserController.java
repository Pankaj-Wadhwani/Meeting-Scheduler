package com.example.meetingscheduler.controller;

import com.example.meetingscheduler.entity.User;
import com.example.meetingscheduler.service.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

	private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("GET /users - Fetching all users");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            logger.warn("GET /users - No users found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.info("GET /users - {} users found", users.size());
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        logger.info("GET /users/{} - Fetching user by ID", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            logger.info("GET /users/{} - User found: {}", id, user.get().getName());
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            logger.warn("GET /users/{} - User not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("POST /users - Creating new user: {}", user.getName());
        try {
            User savedUser = userService.saveUser(user);
            logger.info("POST /users - User created successfully with ID: {}", savedUser.getId());
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("POST /users - Error creating user: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        logger.info("PUT /users/{} - Updating user", id);
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            try {
                User updatedUser = userService.saveUser(user);
                logger.info("PUT /users/{} - User updated successfully", id);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } catch (Exception e) {
                logger.error("PUT /users/{} - Error updating user: {}", id, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.warn("PUT /users/{} - User not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        logger.info("DELETE /users/{} - Deleting user", id);
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            try {
                userService.deleteUser(id);
                logger.info("DELETE /users/{} - User deleted successfully", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                logger.error("DELETE /users/{} - Error deleting user: {}", id, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.warn("DELETE /users/{} - User not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
