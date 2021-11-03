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

    public double getHourScore() {
        float cloudCoef = 0.8f;
        float seeingCoef = 0.05f;
        float moonCoef = 0.15f;
        float cloudAverage = (lowCloud + midCloud + highCloud) / 3f;
        double cloudScore = ((100 - cloudAverage) * cloudCoef);
        if (lowCloud > 35 || midCloud > 35 || highCloud > 35) {
            cloudScore = cloudScore * ((100 - cloudAverage) / 100);
        }
        double seeingScore = (((seeing - 1) * 25) * seeingCoef);
        double moonScore = moonIsPresent ? (int) ((100 - moonIllumination) * moonCoef) : (int) (moonCoef * 100);
        return cloudScore + seeingScore + moonScore;
    }

    public boolean isGoodForAstro() {
        return lowCloud <= 35 && midCloud <= 35 && highCloud <= 35 && seeing >= 3;
    }
}
