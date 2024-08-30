package com.example.meetingscheduler.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.meetingscheduler.entity.Room;
import com.example.meetingscheduler.repository.RoomRepository;

@Service
public class RoomService {

	private final Logger logger = LogManager.getLogger(this.getClass().getName());
	
	@Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        logger.info("Fetching all rooms");
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        logger.info("Fetching room with ID: {}", id);
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            logger.info("Room found: {}", room.get().getName());
        } else {
            logger.warn("Room with ID: {} not found", id);
        }
        return room;
    }

    public Room saveRoom(Room room) {
        logger.info("Saving room with name: {}", room.getName());
        Room savedRoom = roomRepository.save(room);
        logger.info("Room saved successfully with ID: {}", savedRoom.getId());
        return savedRoom;
    }

    public void deleteRoom(Long id) {
        logger.info("Deleting room with ID: {}", id);
        roomRepository.deleteById(id);
        logger.info("Room with ID: {} deleted successfully", id);
    }
}
