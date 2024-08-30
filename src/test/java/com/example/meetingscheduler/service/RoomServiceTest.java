package com.example.meetingscheduler.service;

import com.example.meetingscheduler.entity.Room;
import com.example.meetingscheduler.repository.RoomRepository;
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
public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    public RoomServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRooms() {
        Room room1 = new Room();
        room1.setId(1L);
        room1.setName("Conference Room A");
        room1.setCapacity(20);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setName("Conference Room B");
        room2.setCapacity(30);

        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));

        assertEquals(2, roomService.getAllRooms().size());
    }

    @Test
    public void testGetRoomById() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Conference Room A");
        room.setCapacity(20);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        assertTrue(roomService.getRoomById(1L).isPresent());
        assertEquals("Conference Room A", roomService.getRoomById(1L).get().getName());
    }

    @Test
    public void testSaveRoom() {
        Room room = new Room();
        room.setId(1L);
        room.setName("Conference Room A");
        room.setCapacity(20);

        when(roomRepository.save(any(Room.class))).thenReturn(room);

        assertEquals("Conference Room A", roomService.saveRoom(room).getName());
    }

    
}
