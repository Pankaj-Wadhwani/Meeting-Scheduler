package com.example.meetingscheduler.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.meetingscheduler.entity.Meeting;
import com.example.meetingscheduler.entity.Room;
import com.example.meetingscheduler.entity.User;
import com.example.meetingscheduler.repository.MeetingRepository;

@Service
public class MeetingService {

	private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private MeetingRepository meetingRepository;

    public Meeting scheduleMeeting(Meeting meeting) {
        logger.info("Scheduling meeting with title: {}", meeting.getTitle());

        boolean roomAvailable = isRoomAvailable(meeting.getRoom(), meeting.getStartTime(), meeting.getEndTime());
        if (!roomAvailable) {
            logger.warn("Room {} is not available during the selected time: {} - {}", 
                         meeting.getRoom().getName(), meeting.getStartTime(), meeting.getEndTime());
            throw new RuntimeException("Room is not available during the selected time.");
        }

        boolean participantsAvailable = areParticipantsAvailable(meeting.getParticipants(), meeting.getStartTime(), meeting.getEndTime());
        if (!participantsAvailable) {
            logger.warn("One or more participants are not available during the selected time: {} - {}",
                         meeting.getStartTime(), meeting.getEndTime());
            throw new RuntimeException("One or more participants are not available during the selected time.");
        }

        Meeting savedMeeting = meetingRepository.save(meeting);
        logger.info("Meeting scheduled successfully with ID: {}", savedMeeting.getId());
        return savedMeeting;
    }

    private boolean isRoomAvailable(Room room, LocalDateTime startTime, LocalDateTime endTime) {
        logger.debug("Checking availability for room {} between {} and {}", 
                      room.getName(), startTime, endTime);
        List<Meeting> roomMeetings = meetingRepository.findByRoomAndStartTimeBetween(room, startTime, endTime);
        boolean isAvailable = roomMeetings.isEmpty();
        logger.debug("Room availability: {}", isAvailable);
        return isAvailable; 
    }

    private boolean areParticipantsAvailable(List<User> participants, LocalDateTime startTime, LocalDateTime endTime) {
        logger.debug("Checking participants' availability between {} and {}", startTime, endTime);
        for (User participant : participants) {
            logger.debug("Checking availability for participant: {}", participant.getName());
            List<Meeting> participantMeetings = meetingRepository.findByParticipantsAndStartTimeBetween(participant, startTime, endTime);
            if (!participantMeetings.isEmpty()) {
                logger.debug("Participant {} is not available", participant.getName());
                return false; 
            }
        }
        logger.debug("All participants are available");
        return true; 
    }
    
    public Meeting findById(Long id) {
        logger.info("Fetching meeting with ID: {}", id);
        Meeting meeting = meetingRepository.findById(id).orElse(null);
        if (ObjectUtils.isEmpty(meeting)) {
            logger.warn("Meeting with ID: {} not found", id);
        } else {
            logger.info("Meeting found: {}", meeting.getTitle());
        }
        return meeting;
    }

}
