package ru.practicum.statsserver;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class StatRepositoryTest {


    StatRepository statRepository;

    @Autowired
    public StatRepositoryTest(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    @Test
    public void shoudGet() {
        LocalDateTime testTime = LocalDateTime.of(2010, 2, 1, 12, 12, 12);

        EndpointHit hit1 = EndpointHit.builder()
                .ip("109")
                .app("testApp1")
                .timestamp(testTime)
                .uri("testEndpoint1")
                .build();

        EndpointHit hit2 = EndpointHit.builder()
                .ip("109")
                .app("testApp1")
                .timestamp(testTime)
                .uri("testEndpoint1")
                .build();

        EndpointHit hit3 = EndpointHit.builder()
                .ip("109")
                .app("testApp2")
                .timestamp(testTime)
                .uri("testEndpoint2")
                .build();

        EndpointHit hit4 = EndpointHit.builder()
                .ip("109")
                .app("testApp1")
                .timestamp(testTime)
                .uri("testEndpoint2")
                .build();

        statRepository.save(hit1);
        statRepository.save(hit2);
        statRepository.save(hit3);
        statRepository.save(hit4);

        List<ViewStats> result = statRepository
                .getStatWithUrisNoUniq(testTime.minusDays(10),
                        testTime.plusDays(10),
                        List.of("testEndpoint2", "testEndpoint1"));

        assertEquals(result.size(), 3);
    }

}
