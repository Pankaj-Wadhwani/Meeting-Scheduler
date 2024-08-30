package com.example.meetingscheduler.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.meetingscheduler.entity.Meeting;
import com.example.meetingscheduler.entity.Room;
import com.example.meetingscheduler.entity.User;
import com.example.meetingscheduler.repository.MeetingRepository;

public class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private MeetingService meetingService;

    private Room room;
    private User user;
    private Meeting existingMeeting;
    private Meeting newMeeting;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        room = new Room();
        room.setId(1L);
        room.setName("Conference Room");

        user = new User();
        user.setId(1L);
        user.setName("John Doe");

        existingMeeting = new Meeting();
        existingMeeting.setId(1L);
        existingMeeting.setRoom(room);
        existingMeeting.setStartTime(LocalDateTime.of(2024, 8, 28, 10, 0));
        existingMeeting.setEndTime(LocalDateTime.of(2024, 8, 28, 11, 0));
        existingMeeting.setParticipants(Collections.singletonList(user));

        newMeeting = new Meeting();
        newMeeting.setRoom(room);
        newMeeting.setStartTime(LocalDateTime.of(2024, 8, 28, 10, 30));
        newMeeting.setEndTime(LocalDateTime.of(2024, 8, 28, 11, 30));
        newMeeting.setParticipants(Collections.singletonList(user));
    }

    @Test(expected = RuntimeException.class)
    public void testRoomCollision() {
        when(meetingRepository.findByRoomAndStartTimeBetween(
                eq(room), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(existingMeeting));

       
                meetingService.scheduleMeeting(newMeeting);
       
    }

    @Test(expected = RuntimeException.class)
    public void testParticipantCollision() {
        when(meetingRepository.findByParticipantsAndStartTimeBetween(
                eq(user), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(existingMeeting));

        
                meetingService.scheduleMeeting(newMeeting);
        
    }

    @Test
    public void testNoCollision() {
        when(meetingRepository.findByRoomAndStartTimeBetween(
                eq(room), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<Meeting>());

        when(meetingRepository.findByParticipantsAndStartTimeBetween(
                eq(user), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<Meeting>());

        meetingService.scheduleMeeting(newMeeting);

        verify(meetingRepository, times(1)).save(newMeeting);
    }
}
