package fr.astrotify.astro;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AstroNightlyData {

    private List<AstroHourlyData> astroHourlyDataList;

    public boolean isTonightGoodForAstro() {
        long amoutOfGoodHourTonight = astroHourlyDataList.stream()
                .filter(astroHourlyData -> astroHourlyData.getHour() >= 17)
                .filter(AstroHourlyData::isGoodForAstro)
                .count();
        return ((float) (amoutOfGoodHourTonight / astroHourlyDataList.size())) > 0.4f;
    }
}
