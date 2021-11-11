package fr.astrotify.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AstronomicalDailyDataTest {


    public static final boolean NOT_GOOD_FOR_ASTRO = false;
    public static final boolean GOOD_FOR_ASTRO = true;
    public static final int BAD_CLOUD_SCORE = 36;
    public static final int GOOD_CLOUD_SCORE = 35;
    public static final int BAD_SEEING_SCORE = 2;
    public static final int GOOD_SEEING_SCORE = 3;

    @ParameterizedTest
    @MethodSource("testData")
    void isTonightGoodForAstronomicalObservation(boolean isTonightGoodForAstronomicalObservation, List<AstronomicalHourlyData> astronomicalHourlyDataList) {
        AstronomicalDailyData astronomicalDailyData = AstronomicalDailyData.builder()
                .astronomicalHourlyDataList(astronomicalHourlyDataList)
                .build();
        assertThat(astronomicalDailyData.isTonightGoodForAstronomicalObservation()).isEqualTo(isTonightGoodForAstronomicalObservation);
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(NOT_GOOD_FOR_ASTRO, List.of()),
                Arguments.of(GOOD_FOR_ASTRO, List.of(
                        AstronomicalHourlyData.builder().lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(GOOD_SEEING_SCORE).build()
                )),
                Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                        AstronomicalHourlyData.builder().lowCloud(GOOD_CLOUD_SCORE).midCloud(BAD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(GOOD_SEEING_SCORE).build()
                )),
                Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                        AstronomicalHourlyData.builder().lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(BAD_SEEING_SCORE).build()
                )),
                Arguments.of(GOOD_FOR_ASTRO, List.of(
                        AstronomicalHourlyData.builder().lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(BAD_SEEING_SCORE).build(),
                        AstronomicalHourlyData.builder().lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(GOOD_SEEING_SCORE).build(),
                        AstronomicalHourlyData.builder().lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(GOOD_SEEING_SCORE).build()
                )),
                Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                        AstronomicalHourlyData.builder().lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(BAD_SEEING_SCORE).build(),
                        AstronomicalHourlyData.builder().lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(GOOD_SEEING_SCORE).build(),
                        AstronomicalHourlyData.builder().lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE).seeing(GOOD_SEEING_SCORE).build()
                ))
        );
    }
}