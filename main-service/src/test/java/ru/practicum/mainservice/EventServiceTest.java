package ru.practicum.mainservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.practicum.mainservice.event.dto.EventShortDto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventServiceTest {


    @Test
    public void shouldParseJson() {


        List<HashMap<String, ?>> listMap = listMap();
        assertEquals(listMap.size(), 2);

        assertTrue(listMap.get(0).get("hits").equals(6));


    }

    @Test
    public void shoudSetbla() {


        EventShortDto eventShortDto = EventShortDto.builder().id(1L).build();
        EventShortDto eventShortDto2 = EventShortDto.builder().id(2L).build();
        EventShortDto eventShortDto3 = EventShortDto.builder().id(3L).build();

        List<EventShortDto> dtos = List.of(eventShortDto, eventShortDto2, eventShortDto3);

        Map<String, Map<String, ?>> map = new HashMap<>();

        Map<String, ? super Object> inside1 = new HashMap<>();
        inside1.put("hits", new Integer(9));
        map.put("/event/1", inside1);

        Map<String, ? super Object> inside3 = new HashMap<>();
        inside3.put("hits", new Integer(90));
        map.put("/event/3", inside3);

        List<EventShortDto> result=setViews(dtos,map);
        System.out.println(result);


    }

    public List<HashMap<String, ?>> listMap() {
        String answer = "[\n" +
                "  {\n" +
                "    \"app\": \"ewm-main-service\",\n" +
                "    \"uri\": \"/events/1\",\n" +
                "    \"hits\": 6\n" +
                "  },\n" +
                "  {\n" +
                "    \"app\": \"ewm-main-service\",\n" +
                "    \"uri\": \"/events/5\",\n" +
                "    \"hits\": 10\n" +
                "  }\n" +
                "]";

        ObjectMapper objectMapper = new ObjectMapper();
        List<HashMap<String, ?>> views;
        try {


            views = objectMapper.readValue(answer, new TypeReference<List<HashMap<String, ?>>>() {
            });

        } catch (JsonProcessingException e) {

            views = null;
        }

        return views;
    }

    private List<EventShortDto> setViews(List<EventShortDto> dtos, Map<String, Map<String, ?>> views) {

        for (EventShortDto dto : dtos) {
            String key = "/event/" + dto.getId();

            Map<String, ?> insideMap = views.getOrDefault(key, new HashMap<>());

            Object v = insideMap.containsKey("hits") ? insideMap.get("hits") : 0;
            Integer view = (Integer) v;

            dto.setViews(view);

        }
        return dtos;
    }
}

