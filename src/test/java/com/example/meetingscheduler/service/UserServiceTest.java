package com.example.meetingscheduler.service;

import com.example.meetingscheduler.entity.User;
import com.example.meetingscheduler.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alice");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertTrue(userService.getUserById(1L).isPresent());
        assertEquals("Alice", userService.getUserById(1L).get().getName());
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Alice");

        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals("Alice", userService.saveUser(user).getName());
    }

   
}
