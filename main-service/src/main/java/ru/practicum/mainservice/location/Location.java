package ru.practicum.mainservice.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Max(value = 90)
    @Min(value = -90)
    @NotNull
    @Column(name = "lat")
    private float lat;


    @Max(value = 180)
    @Min(value = -180)
    @NotNull
    @Column(name = "lon")
    private float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Float.compare(location.lat, lat) == 0 && Float.compare(location.lon, lon) == 0 && id.equals(location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lon);
    }
}
