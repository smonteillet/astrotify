package fr.astrotify.application;

import fr.astrotify.application.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.application.service.Astrotify;
import fr.astrotify.domain.AstroWeatherDailyData;
import fr.astrotify.domain.AstroWeatherHourlyData;
import fr.astrotify.domain.CelestialBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AstrotifyTest {

    @Mock
    SendAlertPort sendAlertPort;

    @Mock
    FetchAstronomicalDataPort fetchAstronomicalDataPort;

    @Mock
    AstroWeatherDailyData astronomicalDailyData;

    @Mock
    CelestialBodyDataFetcherPort celestialBodyDataFetcherPort;

    @InjectMocks
    Astrotify astrotify;

    @Nested
    class SendAlertIfTonightHasGoodWeatherForAstro {
        @Test
        void alertNotSent_whenTonightWeatherIsNotGoodForAstro() {
            // given
            when(astronomicalDailyData.doesTonightHaveGoodAstroWeather()).thenReturn(false);
            when(fetchAstronomicalDataPort.fetchTodayAstronomicalData()).thenReturn(astronomicalDailyData);
            // when
            astrotify.sendAlertIfTonightHasGoodWeatherForAstro();
            // then
            verifyNoInteractions(sendAlertPort);
        }

        @Test
        void alertSent_whenTonightWeatherIsGoodForAstro() {
            // given
            when(astronomicalDailyData.doesTonightHaveGoodAstroWeather()).thenReturn(true);
            when(astronomicalDailyData.getSource()).thenReturn("URL");
            when(astronomicalDailyData.getTonightAvailableCelestialBodies()).thenReturn(List.of("Lune", "Jupiter", "Saturne"));
            when(fetchAstronomicalDataPort.fetchTodayAstronomicalData()).thenReturn(astronomicalDailyData);
            // when
            astrotify.sendAlertIfTonightHasGoodWeatherForAstro();
            // then
            verify(sendAlertPort).sendAlert(
                    "⭐⭐⭐ Salut c'est Galilée !\n" +
                            "Il semblerait qu'une bonne soirée astro se profile ce soir, il faut sortir le téléscope 🔭. \n" +
                            "Voici les astres présents dans la soirée :\n" +
                            " - Lune\n" +
                            " - Jupiter\n" +
                            " - Saturne\n" +
                            "Pour plus de détail : URL"
            );
        }
    }


    @Nested
    class SendCelestialBodyInfo {

        @Test
        void noWeatherData() {
            // given
            CelestialBody celestialBody = CelestialBody.builder()
                    .name("Leonard Comet")
                    .distanceToSun("110 Million Km")
                    .distanceToEarth("150 Million Km")
                    .dataSource("URL")
                    .magnitude("8")
                    .rise("00:15")
                    .set("19:38")
                    .build();
            when(celestialBodyDataFetcherPort.fetchData(any(), eq("Leonard Comet"))).thenReturn(celestialBody);
            when(fetchAstronomicalDataPort.fetchTomorrowAstronomicalData()).thenReturn(AstroWeatherDailyData.builder()
                    .astroWeatherHourlyDataList(new ArrayList<>())
                    .build());
            //when
            astrotify.sendCelestialBodyInfo("Leonard Comet", "Muret");
            //then
            verify(sendAlertPort).sendAlert(
                    "Ephemeride Leonard Comet pour demain sur Muret:" +
                            "\n- Levée : 00:15" +
                            "\n- Couchée : 19:38" +
                            "\n- Magnitude : 8" +
                            "\n- Distance Terre : 150 Million Km" +
                            "\n- Distance Soleil : 110 Million Km" +
                            "\nPlus d'info : URL"
            );
        }

        @Test
        void someWheatherDataGoodAndSomeNot() {
            // given
            CelestialBody celestialBody = CelestialBody.builder()
                    .name("Leonard Comet")
                    .distanceToSun("110 Million Km")
                    .distanceToEarth("150 Million Km")
                    .dataSource("URL")
                    .magnitude("8")
                    .rise("00:15")
                    .set("19:38")
                    .build();
            when(celestialBodyDataFetcherPort.fetchData(any(), eq("Leonard Comet"))).thenReturn(celestialBody);
            List<AstroWeatherHourlyData> astroWeatherHourlyDataList = List.of(
                    weatherHourly(true, 1),
                    weatherHourly(true, 2),
                    weatherHourly(false, 3),
                    weatherHourly(true, 21)
            );
            when(fetchAstronomicalDataPort.fetchTomorrowAstronomicalData()).thenReturn(
                    AstroWeatherDailyData.builder().astroWeatherHourlyDataList(astroWeatherHourlyDataList).build()
            );
            //when
            astrotify.sendCelestialBodyInfo("Leonard Comet", "Muret");
            //then
            verify(sendAlertPort).sendAlert(
                    "Ephemeride Leonard Comet pour demain sur Muret:" +
                            "\n- Levée : 00:15" +
                            "\n- Couchée : 19:38" +
                            "\n- Magnitude : 8" +
                            "\n- Distance Terre : 150 Million Km" +
                            "\n- Distance Soleil : 110 Million Km" +
                            "\n- Tranches horaires pour observer :" +
                            "\n\t- 1h-2h" +
                            "\n\t- 2h-3h" +
                            "\nPlus d'info : URL"
            );
        }

        private AstroWeatherHourlyData weatherHourly(boolean goodForAstro, int hour) {
            int cloud = AstroWeatherHourlyData.CLOUD_THRESHOLD + 1;
            int seeing = AstroWeatherHourlyData.SEEING_THRESHOLD - 1;
            if (goodForAstro) {
                cloud = AstroWeatherHourlyData.CLOUD_THRESHOLD - 1;
                seeing = AstroWeatherHourlyData.SEEING_THRESHOLD + 1;
            }
            return AstroWeatherHourlyData.builder()
                    .lowCloud(cloud)
                    .midCloud(cloud)
                    .highCloud(cloud)
                    .seeing(seeing)
                    .hour(hour)
                    .build();
        }
    }


}