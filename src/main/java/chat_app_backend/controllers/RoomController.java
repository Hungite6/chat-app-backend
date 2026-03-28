package chat_app_backend.controllers;


import chat_app_backend.entity.Message;
import chat_app_backend.entity.Room;
import chat_app_backend.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("http://localhost:3000")

public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    //  CREATE ROOM
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId) {

        // check room đã tồn tại chưa
        Optional<Room> existingRoom = roomRepository.findByRoomId(roomId);
        if (existingRoom.isPresent()) {
            return ResponseEntity.badRequest().body("Room already exists!");
        }
        Room room = new Room();
        room.setRoomId(roomId);

        Room savedRoom = roomRepository.save(room);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);


    }

    //  GET ROOM
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {

        Optional<Room> room = roomRepository.findByRoomId(roomId);

        if (room.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Room not found!");
        }

        return ResponseEntity.ok(room.get());
    }

    // GET MESSAGES OF ROOM
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size
    ) {

        Optional<Room> roomOpt = roomRepository.findByRoomId(roomId);

        if (roomOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Room not found!");
        }

        Room room = roomOpt.get();

        List<Message> messages = room.getMessages();

        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);

        List<Message> paginatedMessages = messages.subList(start, end);

        return ResponseEntity.ok(paginatedMessages);
    }

}
