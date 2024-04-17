package ru.practicum.mainservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statsClient.StatsClient;

@Configuration
public class StatClientConfig {

    @Value("${stat.client.url}")
    String url;

    @Bean
    StatsClient statsClient() {
        return new StatsClient(url, new RestTemplate());
    }
}
