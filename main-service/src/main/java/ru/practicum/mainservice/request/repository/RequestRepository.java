package ru.practicum.mainservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.status.RequestStatus;
import ru.practicum.mainservice.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {


    Optional<Request> findByEventAndRequester(Event event, User requester);

    List<Request> findAllByRequester(User requester);

    List<Request> findAllByEvent(Event event);

    List<Request> findAllByEventAndStatus(Event event, RequestStatus status);
}
