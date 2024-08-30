package com.example.meetingscheduler.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.meetingscheduler.entity.Room;
import com.example.meetingscheduler.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

	private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        logger.info("GET /rooms - Fetching all rooms");
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            logger.warn("GET /rooms - No rooms found");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.info("GET /rooms - {} rooms found", rooms.size());
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable("id") Long id) {
        logger.info("GET /rooms/{} - Fetching room by ID", id);
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isPresent()) {
            logger.info("GET /rooms/{} - Room found: {}", id, room.get().getName());
            return new ResponseEntity<>(room.get(), HttpStatus.OK);
        } else {
            logger.warn("GET /rooms/{} - Room not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        logger.info("POST /rooms - Creating new room: {}", room.getName());
        try {
            Room savedRoom = roomService.saveRoom(room);
            logger.info("POST /rooms - Room created successfully with ID: {}", savedRoom.getId());
            return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("POST /rooms - Error creating room: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable("id") Long id, @RequestBody Room room) {
        logger.info("PUT /rooms/{} - Updating room", id);
        Optional<Room> existingRoom = roomService.getRoomById(id);
        if (existingRoom.isPresent()) {
            room.setId(id);
            try {
                Room updatedRoom = roomService.saveRoom(room);
                logger.info("PUT /rooms/{} - Room updated successfully", id);
                return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
            } catch (Exception e) {
                logger.error("PUT /rooms/{} - Error updating room: {}", id, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.warn("PUT /rooms/{} - Room not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("id") Long id) {
        logger.info("DELETE /rooms/{} - Deleting room", id);
        Optional<Room> existingRoom = roomService.getRoomById(id);
        if (existingRoom.isPresent()) {
            try {
                roomService.deleteRoom(id);
                logger.info("DELETE /rooms/{} - Room deleted successfully", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e) {
                logger.error("DELETE /rooms/{} - Error deleting room: {}", id, e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.warn("DELETE /rooms/{} - Room not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
