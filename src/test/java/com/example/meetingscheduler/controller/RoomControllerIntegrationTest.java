package com.example.meetingscheduler.controller;

import com.example.meetingscheduler.entity.Room;
import com.example.meetingscheduler.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(RoomController.class)
public class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    public void testGetAllRooms() throws Exception {
        Room room1 = new Room();
        room1.setId(1L);
        room1.setName("Conference Room A");
        room1.setCapacity(20);

        Room room2 = new Room();
        room2.setId(2L);
        room2.setName("Conference Room B");
        room2.setCapacity(30);

        when(roomService.getAllRooms()).thenReturn(Arrays.asList(room1, room2));

        mockMvc.perform(get("/rooms"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].name").value("Conference Room A"))
               .andExpect(jsonPath("$[1].name").value("Conference Room B"));
    }

    @Test
    public void testGetRoomById() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setName("Conference Room A");
        room.setCapacity(20);

        when(roomService.getRoomById(1L)).thenReturn(Optional.of(room));

        mockMvc.perform(get("/rooms/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value("Conference Room A"));
    }

    @Test
    public void testCreateRoom() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setName("Conference Room A");
        room.setCapacity(20);

        when(roomService.saveRoom(any(Room.class))).thenReturn(room);

        mockMvc.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Conference Room A\", \"capacity\": 20}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.name").value("Conference Room A"));
    }

    @Test
    public void testUpdateRoom() throws Exception {
        Room room = new Room();
        room.setId(1L);
        room.setName("Conference Room A");
        room.setCapacity(20);

        when(roomService.getRoomById(1L)).thenReturn(Optional.of(room));
        when(roomService.saveRoom(any(Room.class))).thenReturn(room);

        mockMvc.perform(put("/rooms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Room\", \"capacity\": 25}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Updated Room"));
    }

    @Test
    public void testDeleteRoom() throws Exception {
        when(roomService.getRoomById(1L)).thenReturn(Optional.of(new Room()));

        mockMvc.perform(delete("/rooms/1"))
               .andExpect(status().isNoContent());
    }
}
