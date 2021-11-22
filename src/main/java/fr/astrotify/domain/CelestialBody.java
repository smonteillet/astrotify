package fr.astrotify.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CelestialBody {

    private String rise;
    private String set;
    private String magnitude;

}
