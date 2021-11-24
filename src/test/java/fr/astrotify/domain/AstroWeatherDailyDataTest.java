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


class AstroWeatherDailyDataTest {


    private static final int TONIGHT_HOUR = 23;
    private static final int LAST_NIGHT_HOUR = 2;

    @Nested
    public class GetTonightCelestialBodies {

        @Test
        public void returnsSingleCelestialBodyForTonight() {
            // given
            AstroWeatherDailyData astronomicalDailyData = AstroWeatherDailyData.builder()
                    .astroWeatherHourlyDataList(List.of(
                            AstroWeatherHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Jupiter")).build()
                    )).build();
            // when
            List<String> tonightAvailableCelestialBodies = astronomicalDailyData.getTonightAvailableCelestialBodies();
            // then
            assertThat(tonightAvailableCelestialBodies).containsExactly("Jupiter");
        }

        @Test
        public void returnsMultipleCelestialBodiesForTonight() {
            // given
            AstroWeatherDailyData astronomicalDailyData = AstroWeatherDailyData.builder()
                    .astroWeatherHourlyDataList(List.of(
                            AstroWeatherHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Jupiter")).build(),
                            AstroWeatherHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Saturn")).build()
                    )).build();
            // when
            List<String> tonightAvailableCelestialBodies = astronomicalDailyData.getTonightAvailableCelestialBodies();
            // then
            assertThat(tonightAvailableCelestialBodies).containsExactly("Jupiter", "Saturn");
        }

        @Test
        public void returnsNoDuplicatedCelestialBodies() {
            // given
            AstroWeatherDailyData astroWeatherDailyData = AstroWeatherDailyData.builder()
                    .astroWeatherHourlyDataList(List.of(
                            AstroWeatherHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Jupiter")).build(),
                            AstroWeatherHourlyData.builder().hour(TONIGHT_HOUR).celestialBodies(List.of("Jupiter", "Saturn")).build()
                    )).build();
            // when
            List<String> tonightAvailableCelestialBodies = astroWeatherDailyData.getTonightAvailableCelestialBodies();
            // then
            assertThat(tonightAvailableCelestialBodies).containsExactly("Jupiter", "Saturn");
        }

        @Test
        public void doesntReturnCelestialBodyFromLastNight() {
            // given
            AstroWeatherDailyData astroWeatherDailyData = AstroWeatherDailyData.builder()
                    .astroWeatherHourlyDataList(List.of(
                            AstroWeatherHourlyData.builder().hour(LAST_NIGHT_HOUR).celestialBodies(List.of("Lune")).build()
                    )).build();
            // when
            List<String> tonightAvailableCelestialBodies = astroWeatherDailyData.getTonightAvailableCelestialBodies();
            // then
            assertThat(tonightAvailableCelestialBodies).isEmpty();
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public  class IsTonightGoodForAstronomicalObservation {

        public static final boolean NOT_GOOD_FOR_ASTRO = false;
        public static final boolean GOOD_FOR_ASTRO = true;
        public static final int BAD_CLOUD_SCORE = AstroWeatherHourlyData.CLOUD_THRESHOLD + 1;
        public static final int GOOD_CLOUD_SCORE = AstroWeatherHourlyData.CLOUD_THRESHOLD - 1;
        public static final int BAD_SEEING_SCORE = AstroWeatherHourlyData.SEEING_THRESHOLD - 1;
        public static final int GOOD_SEEING_SCORE = AstroWeatherHourlyData.SEEING_THRESHOLD + 1;

        @ParameterizedTest
        @MethodSource("testData")
        void isTonightGoodForAstronomicalObservation(boolean isTonightGoodForAstronomicalObservation, List<AstroWeatherHourlyData> astroWeatherHourlyDataList) {
            // given
            AstroWeatherDailyData astroWeatherDailyData = AstroWeatherDailyData.builder()
                    .astroWeatherHourlyDataList(astroWeatherHourlyDataList)
                    .build();
            // when then
            assertThat(astroWeatherDailyData.doesTonightHaveGoodAstroWeather()).isEqualTo(isTonightGoodForAstronomicalObservation);
        }

        Stream<Arguments> testData() {
            return Stream.of(
                    Arguments.of(NOT_GOOD_FOR_ASTRO, List.of()),
                    Arguments.of(GOOD_FOR_ASTRO, List.of(
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(BAD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(BAD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(GOOD_FOR_ASTRO, List.of(
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(BAD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(NOT_GOOD_FOR_ASTRO, List.of(
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(BAD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    )),
                    Arguments.of(GOOD_FOR_ASTRO, List.of(
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(BAD_SEEING_SCORE)
                                    .hour(LAST_NIGHT_HOUR).build(),
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(BAD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build(),
                            AstroWeatherHourlyData.builder()
                                    .lowCloud(GOOD_CLOUD_SCORE).midCloud(GOOD_CLOUD_SCORE).highCloud(GOOD_CLOUD_SCORE)
                                    .seeing(GOOD_SEEING_SCORE)
                                    .hour(TONIGHT_HOUR).build()
                    ))
            );
        }
    }
}