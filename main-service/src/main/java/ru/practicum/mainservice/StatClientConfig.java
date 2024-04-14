package ru.practicum.mainservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statsClient.StatsClient;

@Configuration
public class StatClientConfig {

    @Bean
    StatsClient statsClient() {
        return new StatsClient();
    }
}
