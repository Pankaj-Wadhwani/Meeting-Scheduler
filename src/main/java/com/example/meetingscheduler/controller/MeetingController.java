package com.example.meetingscheduler.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.meetingscheduler.entity.Meeting;
import com.example.meetingscheduler.service.MeetingService;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

	private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private MeetingService meetingService;

    @PostMapping
    public ResponseEntity<?> scheduleMeeting(@RequestBody Meeting meeting) {
        logger.info("Request received to schedule a meeting: {}", meeting.getTitle());
        try {
            Meeting scheduledMeeting = meetingService.scheduleMeeting(meeting);
            logger.info("Meeting scheduled successfully with ID: {}", scheduledMeeting.getId());
            return ResponseEntity.ok(scheduledMeeting);
        } catch (RuntimeException e) {
            logger.error("Failed to schedule meeting: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable Long id) {
        logger.info("Request received to fetch meeting with ID: {}", id);
        Meeting meeting = meetingService.findById(id);
        if (ObjectUtils.isEmpty(meeting)) {
            logger.warn("Meeting with ID: {} not found", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Meeting found with ID: {}", id);
        return ResponseEntity.ok(meeting);
    }
}
