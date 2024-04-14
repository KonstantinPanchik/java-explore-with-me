package ru.practicum.mainservice.views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.mainservice.event.dto.EventFullDto;
import ru.practicum.mainservice.event.dto.EventMapper;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.statsClient.StatsClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class ViewsMapper {

    private static final StatsClient statsClient = new StatsClient();


    public static List<EventShortDto> toEventShortDtosWithViews(Collection<Event> events) {

        Map<String, Map<String, ?>> mapMap = toMap(events);

        List<EventShortDto> result = events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());

        setViewsEventShortDtoByMap(result, mapMap);

        return result;

    }

    public static List<EventFullDto> toEventFullDtosWithViews(Collection<Event> events) {

        Map<String, Map<String, ?>> mapMap = toMap(events);

        List<EventFullDto> result = events.stream().map(EventMapper::toFullDto).collect(Collectors.toList());

        setViewsEventFullDtoByMap(result, mapMap);

        return result;

    }


    private static List<Map<String, ?>> getViews(Collection<Event> events) {

        Comparator<LocalDateTime> localDateTimeComparator =
                (o1, o2) -> {
                    if (o1.equals(o2)) {
                        return 0;
                    }
                    if (o1.isBefore(o2)) {
                        return -1;
                    } else {
                        return 1;
                    }
                };

        LocalDateTime start = events.stream().map(Event::getCreatedOn)
                .min(localDateTimeComparator).orElse(LocalDateTime.now());

        LocalDateTime end = LocalDateTime.now();

        List<String> uris =
                events.stream().map(e -> "/events/" + e.getId()).collect(Collectors.toList());

        String answer = statsClient.getStat(start, end, uris, false).getBody();

        ObjectMapper objectMapper = new ObjectMapper();

        List<Map<String, ?>> views;

        try {
            views = objectMapper.readValue(answer, new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {

            views = null;
        }

        return views;
    }


    private static Map<String, Map<String, ?>> toMap(Collection<Event> events) {

        List<Map<String, ?>> list = getViews(events);

        Map<String, Map<String, ?>> result = new HashMap<>();
        if (list == null) {
            return result;
        }

        for (Map<String, ?> map : list) {
            result.put((String) map.get("uri"), map);
        }
        return result;

    }


    private static void setViewsEventShortDtoByMap(List<EventShortDto> dtos, Map<String, Map<String, ?>> views) {

        for (EventShortDto dto : dtos) {
            String key = "/events/" + dto.getId();

            Map<String, ?> insideMap = views.getOrDefault(key, new HashMap<>());

            Object v = insideMap.containsKey("hits") ? insideMap.get("hits") : 0;
            Integer view = (Integer) v;

            dto.setViews(view);

        }
    }

    private static void setViewsEventFullDtoByMap(List<EventFullDto> dtos, Map<String, Map<String, ?>> views) {

        for (EventFullDto dto : dtos) {
            setViewsEventFullDtoByMap(dto, views);

        }
    }


    private static void setViewsEventFullDtoByMap(EventFullDto eventFullDto, Map<String, Map<String, ?>> views) {

        String key = "/events/" + eventFullDto.getId();

        Map<String, ?> insideMap = views.getOrDefault(key, new HashMap<>());

        Object v = insideMap.containsKey("hits") ? insideMap.get("hits") : 0;
        Integer view = (Integer) v;

        eventFullDto.setViews(view);


    }


    public static void sendStat(String ip, long... ids) {
        for (long id : ids) {

            String app = "ewm-main-service";
            String uri = "/events/" + id;

            statsClient.postStat(app, uri, ip, LocalDateTime.now());
        }
    }

    public static void sendStat(String ip, List<Long> ids) {
        for (long id : ids) {

            String app = "ewm-main-service";
            String uri = "/events/" + id;

            statsClient.postStat(app, uri, ip, LocalDateTime.now());
        }
    }


}
