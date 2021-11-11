package fr.astrotify.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AstronomicalHourlyData {
    private int hour;
    private int lowCloud;
    private int midCloud;
    private int highCloud;
    private int seeing;
    private List<String> celestialBodies;

    public boolean isGoodForAstronomicalObservation() {
        return lowCloud <= 35 && midCloud <= 35 && highCloud <= 35 && seeing >= 3;
    }
}
