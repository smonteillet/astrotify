package fr.astrotify.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class AstroWeatherDailyData {
    private List<AstroWeatherHourlyData> astroWeatherHourlyDataList;
    private String source;

    public boolean doesTonightHaveGoodAstroWeather() {
        List<AstroWeatherHourlyData> nightHourlyData = getNightHourlyData();
        if (nightHourlyData.isEmpty()) {
            return false;
        }
        long amoutOfGoodHoursTonight = nightHourlyData.stream()
                .filter(AstroWeatherHourlyData::hasGoodAstroWeather)
                .count();
        return ((float) amoutOfGoodHoursTonight / nightHourlyData.size()) > 0.4f;
    }

    public List<String> getTonightAvailableCelestialBodies() {
        return getNightHourlyData().stream()
                .flatMap(astronomicalHourlyData -> astronomicalHourlyData.getCelestialBodies().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<AstroWeatherHourlyData> getNightHourlyData()
    {
        return astroWeatherHourlyDataList.stream()
                .filter(astronomicalHourlyData -> astronomicalHourlyData.getHour() >= 17)
                .collect(Collectors.toList());
    }
}
