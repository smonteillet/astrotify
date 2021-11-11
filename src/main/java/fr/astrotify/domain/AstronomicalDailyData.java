package fr.astrotify.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class AstronomicalDailyData {
    private List<AstronomicalHourlyData> astronomicalHourlyDataList;
    private String source;

    public boolean isTonightGoodForAstronomicalObservation() {
        List<AstronomicalHourlyData> nightHourlyData = getNightHourlyData();
        if (nightHourlyData.isEmpty()) {
            return false;
        }
        long amoutOfGoodHoursTonight = nightHourlyData.stream()
                .filter(AstronomicalHourlyData::isGoodForAstronomicalObservation)
                .count();
        return ((float) amoutOfGoodHoursTonight / nightHourlyData.size()) > 0.4f;
    }

    public List<String> getTonightAvailableCelestialBodies() {
        return getNightHourlyData().stream()
                .flatMap(astronomicalHourlyData -> astronomicalHourlyData.getCelestialBodies().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<AstronomicalHourlyData> getNightHourlyData()
    {
        return astronomicalHourlyDataList.stream()
                .filter(astronomicalHourlyData -> astronomicalHourlyData.getHour() >= 17)
                .collect(Collectors.toList());
    }
}
