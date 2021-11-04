package fr.astrotify.astro;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AstroHourlyData {
    private int hour;
    private int lowCloud;
    private int midCloud;
    private int highCloud;
    private int seeing;
    private boolean moonIsPresent;
    private int moonIllumination;

    public boolean isGoodForAstro() {
        return this.lowCloud <= 35 && this.midCloud <= 35 && this.highCloud <= 35 && this.seeing >= 3;
    }
}
