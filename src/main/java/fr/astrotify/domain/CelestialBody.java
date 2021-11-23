package fr.astrotify.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CelestialBody {

    private String name;
    private String rise;
    private String set;
    private String magnitude;
    private String distanceToEarth;
    private String distanceToSun;
    private String dataSource;


}
