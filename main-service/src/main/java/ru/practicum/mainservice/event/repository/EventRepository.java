package ru.practicum.mainservice.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.user.model.User;

import java.time.LocalDateTime;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {


    List<Event> findAllByInitiator(User initiator, Pageable pageable);


    @Query("SELECT e " +
            "FROM Event as e " +
            "Where (UPPER(e.annotation) like UPPER(concat('%' ,?1,'%'))" +
            "or UPPER(e.description) like UPPER(concat('%' ,?1,'%'))) " +
            "AND e.eventDate>?2 " +
            "AND e.eventDate<?3 " +
            "AND e.paid IN ?4 " +
            "AND e.category in ?5 " +
            "AND e.state=?6")
    List<Event> filteredEventsNotOnlyAvailable(String text,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               List<Boolean> paids,
                                               List<Category> categories,
                                               State state,
                                               Pageable pageable);

    @Query("SELECT e " +
            "FROM Event as e " +
            "Where (UPPER(e.annotation) like UPPER(concat('%' ,?1,'%')) " +
            "or UPPER(e.description) like UPPER(concat('%' ,?1,'%'))) " +
            "AND e.eventDate>?2 " +
            "AND e.eventDate<?3 " +
            "AND e.paid IN ?4  " +
            "AND e.category in ?5 " +
            "AND (e.confirmedRequests<e.participantLimit or e.participantLimit=0)" +
            "AND e.state=?6")
    List<Event> filteredEventsOnlyAvailable(String text,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            List<Boolean> paids,
                                            List<Category> categories,
                                            State state,
                                            Pageable pageable);

    @Query("SELECT e " +
            "FROM Event as e " +
            "Where (UPPER(e.annotation) like UPPER(concat('%' ,?1,'%'))" +
            "or UPPER(e.description) like UPPER(concat('%' ,?1,'%'))) " +
            "AND e.eventDate>?2  " +
            "AND e.paid IN ?3 " +
            "AND e.category in ?4 " +
            "AND e.state=?5")
    List<Event> filteredEventsNotOnlyAvailableNoRangeDate(String text,
                                                          LocalDateTime rangeStart,
                                                          List<Boolean> paids,
                                                          List<Category> categories,
                                                          State state,
                                                          Pageable pageable);

    @Query("SELECT e " +
            "FROM Event as e " +
            "Where (UPPER(e.annotation) like UPPER(concat('%' ,?1,'%')) " +
            "or UPPER(e.description) like UPPER(concat('%' ,?1,'%'))) " +
            "AND e.eventDate>?2 " +
            "AND e.paid IN ?3 " +
            "AND e.category in ?4 " +
            "AND (e.confirmedRequests<e.participantLimit or e.participantLimit=0)" +
            "AND e.state=?5")
    List<Event> filteredEventsOnlyAvailableNoRangeDate(String text,
                                                       LocalDateTime rangeStart,
                                                       List<Boolean> paids,
                                                       List<Category> categories,
                                                       State state,
                                                       Pageable pageable);


    @Query("SELECT e " +
            "FROM Event as e " +
            "WHERE e.initiator IN ?1 " +
            "AND e.category in ?2 " +
            "AND e.state IN ?3 " +
            "AND e.eventDate>=?4 " +
            "AND e.eventDate<=?5 ")
    List<Event> forAdmin(List<User> initiators,
                         List<Category> categories,
                         List<State> states,
                         LocalDateTime rangeStart,
                         LocalDateTime rangeEnd,
                         Pageable pageable);

}
