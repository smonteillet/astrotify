package fr.astrotify.astro;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AstroNightlyDataTest {


    @Test
    void checkThatWorstScenarioToUseTelescope_isGoodForTonightAstro() {

        AstroHourlyData hour21 = AstroHourlyData.builder()
                .hour(21)
                .lowCloud(30).midCloud(30).highCloud(30)
                .moonIllumination(50).moonIsPresent(true)
                .seeing(3).build();
        AstroHourlyData hour22 = AstroHourlyData.builder()
                .hour(22)
                .lowCloud(30).midCloud(30).highCloud(30)
                .moonIllumination(50).moonIsPresent(true)
                .seeing(3).build();
        AstroHourlyData hour23 = AstroHourlyData.builder()
                .hour(23)
                .lowCloud(30).midCloud(30).highCloud(30)
                .moonIllumination(50).moonIsPresent(true)
                .seeing(3).build();

        AstroNightlyData astroNightlyData = AstroNightlyData.builder()
                .astroHourlyDataList(List.of(
                        hour21, hour22, hour23
                )).build();
        assertThat(astroNightlyData.isTonightGoodForAstro()).isTrue();
    }

    @Test
    void checkThatBestScenario_isGoodForTonightAstro() {
        AstroHourlyData hour21 = AstroHourlyData.builder()
                .hour(21)
                .lowCloud(0).midCloud(0).highCloud(0)
                .moonIllumination(0).moonIsPresent(false)
                .seeing(5).build();
        AstroHourlyData hour22 = AstroHourlyData.builder()
                .hour(22)
                .lowCloud(0).midCloud(0).highCloud(0)
                .moonIllumination(0).moonIsPresent(false)
                .seeing(5).build();
        AstroHourlyData hour23 = AstroHourlyData.builder()
                .hour(23)
                .lowCloud(0).midCloud(0).highCloud(0)
                .moonIllumination(0).moonIsPresent(false)
                .seeing(5).build();

        AstroNightlyData astroNightlyData = AstroNightlyData.builder()
                .astroHourlyDataList(List.of(
                        hour21, hour22, hour23
                )).build();
        assertThat(astroNightlyData.isTonightGoodForAstro()).isTrue();
    }

    @Test
    void checkThatWorstScenario_isNotGoodForTonightAstro() {
        AstroHourlyData hour21 = AstroHourlyData.builder()
                .hour(21)
                .lowCloud(100).midCloud(100).highCloud(100)
                .moonIllumination(100).moonIsPresent(true)
                .seeing(1).build();
        AstroHourlyData hour22 = AstroHourlyData.builder()
                .hour(22)
                .lowCloud(100).midCloud(100).highCloud(100)
                .moonIllumination(100).moonIsPresent(true)
                .seeing(1).build();
        AstroHourlyData hour23 = AstroHourlyData.builder()
                .hour(23)
                .lowCloud(100).midCloud(100).highCloud(100)
                .moonIllumination(100).moonIsPresent(true)
                .seeing(1).build();

        AstroNightlyData astroNightlyData = AstroNightlyData.builder()
                .astroHourlyDataList(List.of(
                        hour21, hour22, hour23
                )).build();
        assertThat(astroNightlyData.isTonightGoodForAstro()).isFalse();
    }

    @Test
    void checkThat01112021data_isNotGoodForTonightAstro() {
        AstroHourlyData hour18 = AstroHourlyData.builder()
                .hour(18).moonIllumination(15).moonIsPresent(false).seeing(4)
                .lowCloud(36).midCloud(23).highCloud(0).build();
        AstroHourlyData hour19 = AstroHourlyData.builder()
                .hour(19).moonIllumination(15).moonIsPresent(false).seeing(4)
                .lowCloud(38).midCloud(26).highCloud(0).build();
        AstroHourlyData hour20 = AstroHourlyData.builder()
                .hour(20).moonIllumination(15).moonIsPresent(false).seeing(3)
                .lowCloud(46).midCloud(27).highCloud(0).build();
        AstroHourlyData hour21 = AstroHourlyData.builder()
                .hour(21).moonIllumination(15).moonIsPresent(false).seeing(4)
                .lowCloud(58).midCloud(31).highCloud(0).build();
        AstroHourlyData hour22 = AstroHourlyData.builder()
                .hour(22).moonIllumination(15).moonIsPresent(false).seeing(4)
                .lowCloud(63).midCloud(39).highCloud(0).build();
        AstroHourlyData hour23 = AstroHourlyData.builder()
                .hour(23).moonIllumination(15).moonIsPresent(false).seeing(4)
                .lowCloud(52).midCloud(55).highCloud(0).build();

        AstroNightlyData astroNightlyData = AstroNightlyData.builder()
                .astroHourlyDataList(List.of(hour18,hour19,hour20,hour21,hour22,hour23))
                .build();
        assertThat(astroNightlyData.isTonightGoodForAstro()).isFalse();
    }
}