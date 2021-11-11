package fr.astrotify.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class AstronomicalDailyDataTest {


    private static final int TONIGHT_HOUR = 23;
    private static final int LAST_NIGHT_HOUR = 2;

    @Nested
    public class GetTonightCelestialBodies {

        @Test
        public void returnsMultipleCelestialBodiesForTonight() {
            // given
            AstronomicalDailyData astronomicalDailyData = AstronomicalDailyData.builder()
                    .astronomicalHourlyDataList(List.of(
                            AstronomicalHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Jupiter")).build(),
                            AstronomicalHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Saturn")).build()
                    )).build();
            // when
            List<String> tonightAvailableCelestialBodies = astronomicalDailyData.getTonightAvailableCelestialBodies();
            // then
            assertThat(tonightAvailableCelestialBodies).containsExactly("Jupiter", "Saturn");
        }

        @Test
        public void returnsNoDuplicatedCelestialBodies() {
            // given
            AstronomicalDailyData astronomicalDailyData = AstronomicalDailyData.builder()
                    .astronomicalHourlyDataList(List.of(
                            AstronomicalHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Jupiter")).build(),
                            AstronomicalHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Jupiter", "Saturn")).build()
                    )).build();
            // when
            List<String> tonightAvailableCelestialBodies = astronomicalDailyData.getTonightAvailableCelestialBodies();
            // then
            assertThat(tonightAvailableCelestialBodies).containsExactly("Jupiter", "Saturn");
        }

        @Test
        public void doesntReturnCelestialBodyFromLastNight() {
            // given
            AstronomicalDailyData astronomicalDailyData = AstronomicalDailyData.builder()
                    .astronomicalHourlyDataList(List.of(
                            AstronomicalHourlyData.builder().hour(LAST_NIGHT_HOUR).celestialBodies(List.of("Lune")).build()
                    )).build();
            // when
            List<String> tonightAvailableCelestialBodies = astronomicalDailyData.getTonightAvailableCelestialBodies();
            // then
            assertThat(tonightAvailableCelestialBodies).isEmpty();
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public  class IsTonightGoodForAstronomicalObservation {

        public static final boolean NOT_GOOD_FOR_ASTRO = false;
        public static final boolean GOOD_FOR_ASTRO = true;
        public static final int BAD_CLOUD_SCORE = 36;
        public static final int GOOD_CLOUD_SCORE = 35;
        public static final int BAD_SEEING_SCORE = 2;
        public static final int GOOD_SEEING_SCORE = 3;

        @ParameterizedTest
        @MethodSource("testData")
        void isTonightGoodForAstronomicalObservation(boolean isTonightGoodForAstronomicalObservation, List<AstronomicalHourlyData> astronomicalHourlyDataList) {
            // given
            AstronomicalDailyData astronomicalDailyData = AstronomicalDailyData.builder()
                    .astronomicalHourlyDataList(astronomicalHourlyDataList)
                    .build();
            // when then
            assertThat(astronomicalDailyData.isTonightGoodForAstronomicalObservation()).isEqualTo(isTonightGoodForAstronomicalObservation);
        }

        Stream<Arguments> testData() {
            return Stream.of(
                    Arguments.of(NOT_GOOD_FOR_ASTRO, List.of()),
                    Arguments.of(GOOD_FOR_ASTRO, List.of(
                            AstronomicalHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                            AstronomicalHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(BAD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                            AstronomicalHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(BAD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(GOOD_FOR_ASTRO, List.of(
                            AstronomicalHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(BAD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstronomicalHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstronomicalHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                            AstronomicalHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(BAD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstronomicalHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstronomicalHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(GOOD_FOR_ASTRO, List.of(
                            AstronomicalHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(BAD_SEEING_SCORE)
                                    .hour(LAST_NIGHT_HOUR).build(),
                            AstronomicalHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstronomicalHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    ))
            );
        }
    }
}