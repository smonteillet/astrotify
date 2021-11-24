package fr.astrotify.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AstroWeatherHourlyData {


    public static final int CLOUD_THRESHOLD = 35;
    public static final int SEEING_THRESHOLD = 3;
    private int hour;
    private int lowCloud;
    private int midCloud;
    private int highCloud;
    private int seeing;
    private List<String> celestialBodies;

    public boolean hasGoodAstroWeather() {
        return lowCloud <= CLOUD_THRESHOLD &&
                midCloud <= CLOUD_THRESHOLD &&
                highCloud <= CLOUD_THRESHOLD &&
                seeing >= SEEING_THRESHOLD;
    }
}
