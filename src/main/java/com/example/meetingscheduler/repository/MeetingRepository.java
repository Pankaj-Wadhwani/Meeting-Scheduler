package com.example.meetingscheduler.repository;

import com.example.meetingscheduler.entity.Meeting;
import com.example.meetingscheduler.entity.Room;
import com.example.meetingscheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByRoomAndStartTimeBetween(Room room, LocalDateTime startTime, LocalDateTime endTime);
    List<Meeting> findByParticipantsAndStartTimeBetween(User user, LocalDateTime startTime, LocalDateTime endTime);
}
