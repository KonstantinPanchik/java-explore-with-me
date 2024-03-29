package ru.practicum.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.statsserver.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Integer> {


    @Query("select new ru.practicum.statsDto.ViewStats(h.app, h.uri, count(h.ip))" +
            " FROM EndpointHit h" +
            " WHERE h.timestamp>?1 and h.timestamp<?2 and h.uri in ?3" +
            " GROUP BY h.app, h.uri" +
            " ORDER BY count(h.ip) desc")
    List<ViewStats> getStatWithUrisNoUniq(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.statsDto.ViewStats(h.app, h.uri, count(distinct h.ip))" +
            " FROM EndpointHit h" +
            " WHERE h.timestamp>?1 and h.timestamp<?2 and h.uri in ?3" +
            " GROUP BY h.app, h.uri" +
            " ORDER BY count(distinct h.ip) desc")
    List<ViewStats> getStatWithUrisUniq(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.statsDto.ViewStats(h.app, h.uri, count(distinct h.ip))" +
            " FROM EndpointHit h" +
            " GROUP BY h.app, h.uri" +
            " ORDER BY count(distinct h.ip) desc")
    List<ViewStats> getStatWithOutUrisUniq(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statsDto.ViewStats(h.app, h.uri, count(h.ip))" +
            " FROM EndpointHit h" +
            " GROUP BY h.app, h.uri" +
            " ORDER BY count(h.ip) desc")
    List<ViewStats> getStatWithOutUrisNoUniq(LocalDateTime start, LocalDateTime end);


}
