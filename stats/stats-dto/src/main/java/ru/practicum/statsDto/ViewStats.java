package ru.practicum.statsDto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStats {

    String app;

    String uri;

    long hits;

    public ViewStats(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
