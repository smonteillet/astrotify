package fr.astrotify.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AstronomicalDailyData {
    private List<AstronomicalHourlyData> astronomicalHourlyDataList;
    private String source;

    public boolean isTonightGoodForAstronomicalObservation() {
        if (getAstronomicalHourlyDataList().isEmpty()) {
            return false;
        }
        long amoutOfGoodHoursTonight = getAstronomicalHourlyDataList().stream()
                .filter(AstronomicalHourlyData::isGoodForAstronomicalObservation)
                .count();
        return ((float) amoutOfGoodHoursTonight / getAstronomicalHourlyDataList().size()) > 0.4f;
    }
}
