package chat_app_backend.controllers;

import chat_app_backend.entity.Message;
import chat_app_backend.entity.Room;
import chat_app_backend.playload.MessageRequest;
import chat_app_backend.repositories.RoomRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@CrossOrigin("http://localhost:3000")
public class ChatController {



    private RoomRepository roomRepository;

    public ChatController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    ){
        System.out.println(">>> MESSAGE RECEIVED"); // debug

        //tim room trong DB
        Room room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        Message message = new Message();
        message.setContent(request.getContent());
        message.setSenderId(request.getSender());
        message.setTimestamp(LocalDateTime.now());

        message.setRoom(room);

        // init list nếu null
        if(room.getMessages() == null){
            room.setMessages(new ArrayList<>());
        }

        //add vao room
        room.getMessages().add(message);

        //luu DB
        roomRepository.save(room);
        return message;

    }
}
