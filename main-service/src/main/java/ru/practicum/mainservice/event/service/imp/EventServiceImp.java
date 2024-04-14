package ru.practicum.mainservice.event.service.imp;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.category.repository.CategoryRepository;
import ru.practicum.mainservice.category.service.CategoryService;
import ru.practicum.mainservice.event.dto.*;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.EventSort;
import ru.practicum.mainservice.event.model.State;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.event.service.EventService;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.exception.NotFoundException;
import ru.practicum.mainservice.location.Location;
import ru.practicum.mainservice.location.repository.LocationRepository;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;
import ru.practicum.mainservice.user.service.UserService;
import ru.practicum.mainservice.views.ViewsMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class EventServiceImp implements EventService {


    UserService userService;
    CategoryService categoryService;

    LocationRepository locationRepository;
    EventRepository eventRepository;

    UserRepository userRepository;
    CategoryRepository categoryRepository;


    //private
    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) {

        Location location = checkExistOrCreateAndSave(newEventDto.getLocation());
        User initiator = userService.getById(userId);
        Category category = categoryService.get(newEventDto.getCategory());

        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(initiator);
        event.setLocation(location);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);

        event = eventRepository.save(event);

        return EventMapper.toFullDto(event);

    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {

        Event event = checkExist(eventId);
        User author = userService.getById(userId);

        if (!(author.equals(event.getInitiator()))) {
            throw new ConflictException("You can't change this event");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(checkExistOrCreateAndSave(updateEventUserRequest.getLocation()));
        }

        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryService.get(updateEventUserRequest.getCategory()));
        }

        EventUpdateUserState action = updateEventUserRequest.getStateAction();

        if (action == null || action.equals(EventUpdateUserState.CANCEL_REVIEW)
                || action.equals(EventUpdateUserState.PUBLISH_EVENT)) {
            throw new ConflictException("This action available only for admin");
        }

        EventMapper.updateEvent(event, updateEventUserRequest);

        eventRepository.save(event);


        return ViewsMapper.toEventFullDtosWithViews(List.of(event)).get(0);

    }

    @Override
    public List<EventShortDto> getAuthorEvents(Long userId, int from, int size) {

        User initiator = userService.getById(userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));

        List<Event> events = eventRepository.findAllByInitiator(initiator, pageable);

        return ViewsMapper.toEventShortDtosWithViews(events);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {

        Event event = checkExist(eventId);
        User author = userService.getById(userId);

        if (!(author.equals(event.getInitiator()))) {
            throw new ConflictException("You can't see this event");
        }

        return ViewsMapper.toEventFullDtosWithViews(List.of(event)).get(0);

    }


    //public
    @Override
    public EventFullDto getEvent(Long eventId, String ip) {

        Event event = checkExist(eventId);

        if (!(event.getState().equals(State.PUBLISHED))) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }


        EventFullDto result = ViewsMapper.toEventFullDtosWithViews(List.of(event)).get(0);

        ViewsMapper.sendStat(ip, eventId);

        return result;
    }

    @Override
    public List<EventShortDto> getPublishedFilteredEvents(String text,
                                                          List<Long> categories,
                                                          Boolean paid,
                                                          LocalDateTime rangeStart,
                                                          LocalDateTime rangeEnd,
                                                          boolean onlyAvailable,
                                                          EventSort eventSort,
                                                          Integer from,
                                                          Integer size,
                                                          String ip) {

        Pageable pageable = PageRequest.of(from / size, size);

        List<Category> categoriesEntities = categoryService.getIn(categories);

        List<Event> events;

        List<Boolean> paids = paid == null ? List.of(true, false) : List.of(paid);


        if (onlyAvailable) {
            if (rangeStart == null) {
                events = eventRepository.filteredEventsOnlyAvailableNoRangeDate(text,
                        LocalDateTime.now(),
                        paids,
                        categoriesEntities,
                        State.PUBLISHED,
                        pageable);

            } else {
                events = eventRepository.filteredEventsOnlyAvailable(text,
                        rangeStart,
                        rangeEnd,
                        paids,
                        categoriesEntities,
                        State.PUBLISHED,
                        pageable);
            }

        } else {
            if (rangeStart == null) {
                events = eventRepository.filteredEventsNotOnlyAvailableNoRangeDate(text,
                        LocalDateTime.now(),
                        paids,
                        categoriesEntities,
                        State.PUBLISHED,
                        pageable);

            } else {
                events = eventRepository.filteredEventsNotOnlyAvailable(text,
                        rangeStart,
                        rangeEnd,
                        paids,
                        categoriesEntities,
                        State.PUBLISHED,
                        pageable);

            }
        }


        List<EventShortDto> eventShortDtos = ViewsMapper.toEventShortDtosWithViews(events);

        ViewsMapper.sendStat(ip, events.stream().map(Event::getId).collect(Collectors.toList()));

        switch (eventSort) {

            case VIEWS:
                return eventShortDtos.stream()
                        .sorted(Comparator.comparingInt(EventShortDto::getViews))
                        .collect(Collectors.toList());
            case EVENT_DATE:
                return eventShortDtos.stream()
                        .sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
            default:
                return eventShortDtos;
        }
    }

    //Admin


    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users,
                                               List<State> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               int from,
                                               int size) {

        List<User> usersFromDb = usersFromIdsOrAll(users);
        List<Category> categoriesFromDb = categoryFromIdsOrAll(categories);
        states = checkStates(states);

        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.forAdmin(usersFromDb, categoriesFromDb, states,
                rangeStart, rangeEnd, pageable);


        return ViewsMapper.toEventFullDtosWithViews(events);
    }

    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventUserRequest adminRequest) {

        Event event = checkExist(eventId);

        EventUpdateUserState action = adminRequest.getStateAction();

        if (action == null || action.equals(EventUpdateUserState.CANCEL_REVIEW)
                || action.equals(EventUpdateUserState.SEND_TO_REVIEW)) {
            throw new ConflictException("This action available only for user");
        }

        if (action.equals(EventUpdateUserState.PUBLISH_EVENT)
                && event.getPublishedOn().isBefore(event.getEventDate().plusHours(1))) {
            throw new ConflictException("Published date must be one hour before eventDate");
        }

        if (adminRequest.getLocation() != null) {
            event.setLocation(checkExistOrCreateAndSave(adminRequest.getLocation()));
        }

        if (adminRequest.getCategory() != null) {
            event.setCategory(categoryService.get(adminRequest.getCategory()));
        }

        EventMapper.updateEvent(event, adminRequest);

        eventRepository.save(event);

        return ViewsMapper.toEventFullDtosWithViews(List.of(event)).get(0);


    }


    private List<User> usersFromIdsOrAll(List<Long> users) {
        if (users == null || users.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.findAllById(users);
        }
    }

    private List<Category> categoryFromIdsOrAll(List<Long> categories) {
        if (categories == null || categories.isEmpty() || categories.get(0) == 0) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.findAllById(categories);
        }
    }

    private List<State> checkStates(List<State> categories) {

        if (categories == null || categories.isEmpty()) {
            categories = new ArrayList<>();

            Collections.addAll(categories, State.values());
        }
        return categories;
    }


    private Event checkExist(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found"));
    }


    private Location checkExistOrCreateAndSave(Location checkedLocation) {

        Float lat = checkedLocation.getLat();
        Float lon = checkedLocation.getLon();

        return locationRepository.findByLatAndLon(lat, lon)
                .orElseGet(() -> locationRepository.save(checkedLocation));
    }


}
