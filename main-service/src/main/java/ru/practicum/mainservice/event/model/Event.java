package ru.practicum.mainservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.location.Location;
import ru.practicum.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column
    private String annotation;

    @Column(name = "create_date")
    private LocalDateTime createdOn;

    @Column(name = "confirmed_Requests")
    private int confirmedRequests;

    @Column
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column
    private boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Enumerated(value = EnumType.STRING)
    @Column
    private State state;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    private User initiator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
    private Set<Compilation> compilations = new HashSet<>();

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id")
    private Location location;




}
