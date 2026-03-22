package chat_app_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderId;
    private String receiverId;

    private String content;

    private LocalDateTime timestamp;

    // liên kết với Room
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    @JsonIgnore
    private Room room;

    // constructor tiện dùng
    public Message(String senderId, String receiverId, String content, Room room) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.room = room;
    }
}