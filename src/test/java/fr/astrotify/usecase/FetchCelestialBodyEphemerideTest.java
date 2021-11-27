package fr.astrotify.usecase;

import fr.astrotify.usecase.FetchFetchCelestialBodyEphemeride;
import fr.astrotify.usecase.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.usecase.port.out.FetchAstronomicalWeatherPort;
import fr.astrotify.usecase.port.out.SendAlertPort;
import fr.astrotify.domain.AstroWeatherDailyData;
import fr.astrotify.domain.AstroWeatherHourlyData;
import fr.astrotify.domain.CelestialBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchCelestialBodyEphemerideTest {

    @Mock
    SendAlertPort sendAlertPort;

    @Mock
    FetchAstronomicalWeatherPort fetchAstronomicalWeatherPort;

    @Mock
    CelestialBodyDataFetcherPort celestialBodyDataFetcherPort;

    @InjectMocks
    FetchFetchCelestialBodyEphemeride fetchCelestialBodyEphemeride;

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
        when(fetchAstronomicalWeatherPort.fetchTomorrowAstronomicalWeather()).thenReturn(AstroWeatherDailyData.builder()
                .astroWeatherHourlyDataList(new ArrayList<>())
                .build());
        //when
        fetchCelestialBodyEphemeride.sendCelestialBodyEphemeride("Leonard Comet", "Muret");
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
    void someGoodWeatherHoursAndSomeNot() {
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
        when(fetchAstronomicalWeatherPort.fetchTomorrowAstronomicalWeather()).thenReturn(
                AstroWeatherDailyData.builder().astroWeatherHourlyDataList(astroWeatherHourlyDataList).build()
        );
        //when
        fetchCelestialBodyEphemeride.sendCelestialBodyEphemeride("Leonard Comet", "Muret");
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