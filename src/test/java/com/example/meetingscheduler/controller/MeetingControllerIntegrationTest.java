package com.example.meetingscheduler.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import com.example.meetingscheduler.entity.Meeting;
import com.example.meetingscheduler.entity.Room;
import com.example.meetingscheduler.entity.User;
import com.example.meetingscheduler.repository.MeetingRepository;
import com.example.meetingscheduler.repository.RoomRepository;
import com.example.meetingscheduler.repository.UserRepository; 

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeetingControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository; 

    private String getBaseUrl() {
        return "http://localhost:" + port + "/meetings";
    }

    @BeforeEach
    public void setUp() {
        
        meetingRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateMeeting() {
        
        Room room = new Room();
        room.setName("Conference Room 1");
        Room savedRoom = roomRepository.save(room);

        
        User user1 = new User();
        user1.setName("John Doe");
        User savedUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setName("Jane Smith");
        User savedUser2 = userRepository.save(user2);

        
        Meeting meeting = new Meeting();
        meeting.setTitle("Project Planning");
        meeting.setRoom(savedRoom);
        meeting.setStartTime(LocalDateTime.now().plusDays(1));
        meeting.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));
        List<User> participants = new ArrayList<>();
        participants.add(savedUser1);
        participants.add(savedUser2);
        meeting.setParticipants(participants);

        
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Meeting> request = new HttpEntity<>(meeting, headers);

        ResponseEntity<Meeting> response = restTemplate.exchange(
            getBaseUrl(),
            HttpMethod.POST,
            request,
            Meeting.class
        );

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Meeting createdMeeting = response.getBody();
        assertNotNull(createdMeeting);
        assertNotNull(createdMeeting.getId());
        assertEquals("Project Planning", createdMeeting.getTitle());
        assertEquals(savedRoom.getId(), createdMeeting.getRoom().getId());
        assertEquals(2, createdMeeting.getParticipants().size());
    }

    @Test
    public void testGetMeetingById() {
        
        Room room = new Room();
        room.setName("Conference Room 2");
        Room savedRoom = roomRepository.save(room);

        User user = new User();
        user.setName("Alice");
        User savedUser = userRepository.save(user);

        Meeting meeting = new Meeting();
        meeting.setTitle("Team Sync");
        meeting.setRoom(savedRoom);
        meeting.setStartTime(LocalDateTime.now().plusDays(2));
        meeting.setEndTime(LocalDateTime.now().plusDays(2).plusHours(1));
        List<User> participants = Arrays.asList(savedUser);
        meeting.setParticipants(participants);

        Meeting savedMeeting = meetingRepository.save(meeting);

        
        ResponseEntity<Meeting> response = restTemplate.getForEntity(
            getBaseUrl() + "/" + savedMeeting.getId(),
            Meeting.class
        );

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Meeting retrievedMeeting = response.getBody();
        assertNotNull(retrievedMeeting);
        assertEquals(savedMeeting.getId(), retrievedMeeting.getId());
        assertEquals("Team Sync", retrievedMeeting.getTitle());
        assertEquals(savedRoom.getId(), retrievedMeeting.getRoom().getId());
        assertEquals(1, retrievedMeeting.getParticipants().size());
    }
}
