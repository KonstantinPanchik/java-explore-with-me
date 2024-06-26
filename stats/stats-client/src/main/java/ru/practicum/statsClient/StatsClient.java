package ru.practicum.statsClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsDto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {

    private final String host;

    private final RestTemplate restTemplate;

    @Autowired
    public StatsClient(String host, RestTemplate restTemplate) {
        this.host = host;
        this.restTemplate = restTemplate;
    }


    public boolean postStat(String app, String uri, String ip, LocalDateTime dateTime) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .app(app)
                .ip(ip)
                .uri(uri)
                .timestamp(dateTime)
                .build();

        ResponseEntity<EndpointHitDto> entity = restTemplate
                .postForEntity(host + "/hit", endpointHitDto, EndpointHitDto.class);

        return entity.getStatusCode().is2xxSuccessful();
    }

    public ResponseEntity<String> getStat(LocalDateTime start, LocalDateTime end, List<String> urisList, boolean unique) {

        String url = host + "/stats?start={start}&end={end}&unique={unique}&uris={uris}";

        String uris = urisList == null || urisList.isEmpty() ? ""
                : urisList.stream().reduce((x, y) -> x + "," + y).get();

        Map<String, ?> map = Map.of(
                "start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "unique", unique,
                "uris", uris
        );

        try {
            return restTemplate.getForEntity(url, String.class, map);

        } catch (RestClientException exception) {

            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("[]");
        }
    }

}
