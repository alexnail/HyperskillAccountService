package account.entity;

import account.security.event.EventName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_events")
@Getter
@Setter
public class SecurityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private EventName action;

    private String subject;

    private String object;

    private String path;
}
