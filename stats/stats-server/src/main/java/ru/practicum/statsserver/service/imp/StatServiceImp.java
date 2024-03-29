package ru.practicum.statsserver.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.statsDto.EndpointHitDto;
import ru.practicum.statsDto.ViewStats;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.model.EndpointHitMapper;
import ru.practicum.statsserver.repository.StatRepository;
import ru.practicum.statsserver.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatServiceImp implements StatService {


    StatRepository repository;

    @Autowired
    public StatServiceImp(StatRepository repository) {
        this.repository = repository;
    }

    @Override
    public EndpointHit postHit(EndpointHitDto endpointHitDto) {
        return repository.save(EndpointHitMapper.from(endpointHitDto));
    }

    @Override
    public List<ViewStats> getStat(LocalDateTime start, LocalDateTime end, boolean unique, List<String> uris) {

        if (uris == null || uris.isEmpty()) {
            return unique
                    ? repository.getStatWithOutUrisUniq(start, end)
                    : repository.getStatWithOutUrisNoUniq(start, end);
        } else {
            return unique
                    ? repository.getStatWithUrisUniq(start, end, uris)
                    : repository.getStatWithUrisNoUniq(start, end, uris);
        }
    }
}

