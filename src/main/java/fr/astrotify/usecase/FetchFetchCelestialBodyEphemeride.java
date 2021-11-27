package fr.astrotify.usecase;

import fr.astrotify.usecase.port.in.FetchCelestialBodyEphemerideUseCase;
import fr.astrotify.usecase.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.usecase.port.out.FetchAstronomicalWeatherPort;
import fr.astrotify.usecase.port.out.SendAlertPort;
import fr.astrotify.domain.AstroWeatherHourlyData;
import fr.astrotify.domain.CelestialBody;

import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FetchFetchCelestialBodyEphemeride implements FetchCelestialBodyEphemerideUseCase {

    private static final Logger LOGGER = Logger.getLogger(FetchFetchCelestialBodyEphemeride.class.getName());

    private final SendAlertPort sendAlertPort;
    private final FetchAstronomicalWeatherPort fetchAstronomicalWeatherPort;
    private final CelestialBodyDataFetcherPort celestialBodyDataFetcherPort;

    public FetchFetchCelestialBodyEphemeride(SendAlertPort sendAlertPort,
                                             FetchAstronomicalWeatherPort fetchAstronomicalWeatherPort,
                                             CelestialBodyDataFetcherPort celestialBodyDataFetcherPort) {
        this.sendAlertPort = sendAlertPort;
        this.fetchAstronomicalWeatherPort = fetchAstronomicalWeatherPort;
        this.celestialBodyDataFetcherPort = celestialBodyDataFetcherPort;
    }

    @Override
    public void sendCelestialBodyEphemeride(String celestialBodyName, String city) {
        LOGGER.info("Fetching info for celestial body " + celestialBodyName + "...");
        CelestialBody celestialBody = celestialBodyDataFetcherPort.fetchData(LocalDate.now().plusDays(1), celestialBodyName);
        LOGGER.info("Data fetched : " + celestialBody);

        int riseHour = Integer.parseInt(celestialBody.getRise().substring(0, 2));
        int setHour = Integer.parseInt(celestialBody.getSet().substring(0, 2));
        String goodObservationHours = fetchAstronomicalWeatherPort.fetchTomorrowAstronomicalWeather()
                .getAstroWeatherHourlyDataList()
                .stream()
                .filter(AstroWeatherHourlyData::hasGoodAstroWeather)
                .filter(hourlyAstroData -> hourlyAstroData.getHour() >= riseHour && hourlyAstroData.getHour() <= setHour)
                .map(hourlyAstroData -> hourlyAstroData.getHour() + "h-" + (hourlyAstroData.getHour() + 1) + "h")
                .collect(Collectors.joining("\n\t- "));
        if (!goodObservationHours.isEmpty()) {
            goodObservationHours = "\n- Tranches horaires pour observer :\n\t- " + goodObservationHours;
        }
        String message = "Ephemeride " + celestialBodyName + " pour demain sur " + city + ":" +
                "\n- Levée : " + celestialBody.getRise() +
                "\n- Couchée : " + celestialBody.getSet() +
                "\n- Magnitude : " + celestialBody.getMagnitude() +
                "\n- Distance Terre : " + celestialBody.getDistanceToEarth() +
                "\n- Distance Soleil : " + celestialBody.getDistanceToSun() +
                goodObservationHours +
                "\nPlus d'info : " + celestialBody.getDataSource();

        sendAlertPort.sendAlert(message);
    }
}
