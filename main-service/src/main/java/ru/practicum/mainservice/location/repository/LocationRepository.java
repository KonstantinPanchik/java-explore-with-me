package ru.practicum.mainservice.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainservice.location.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {


    @Query("select l FROM Location l WHERE l.lat=?1 and l.lon=?2")
    Optional<Location> findByLatAndLon(Float lat, Float lon);
}
