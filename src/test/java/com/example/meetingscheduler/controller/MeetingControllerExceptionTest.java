package com.example.meetingscheduler.controller;

import com.example.meetingscheduler.entity.Meeting;
import com.example.meetingscheduler.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingController.class)
public class MeetingControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeetingService meetingService;

    @Test
    public void testScheduleMeeting_RuntimeException() throws Exception {

        Meeting meeting = new Meeting();
        meeting.setTitle("Test Meeting");

        Mockito.when(meetingService.scheduleMeeting(Mockito.any(Meeting.class)))
               .thenThrow(new RuntimeException("Simulated runtime exception"));

        mockMvc.perform(post("/meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Meeting\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Simulated runtime exception"));
    }
}
