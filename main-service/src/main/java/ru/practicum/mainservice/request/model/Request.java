package ru.practicum.mainservice.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.request.status.RequestStatus;
import ru.practicum.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "create_date")
    private LocalDateTime created;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;


}
