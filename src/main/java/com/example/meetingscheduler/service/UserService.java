package com.example.meetingscheduler.service;

import com.example.meetingscheduler.entity.User;
import com.example.meetingscheduler.repository.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        logger.info("Saving user: {}", user.getName());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }
}
