package fr.astrotify.application.service;

import fr.astrotify.application.port.in.CheckAstroWeather;
import fr.astrotify.application.port.in.FetchCelestialBodyData;
import fr.astrotify.application.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.domain.AstroWeatherDailyData;
import fr.astrotify.domain.AstroWeatherHourlyData;
import fr.astrotify.domain.CelestialBody;

import java.time.LocalDate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Astrotify implements CheckAstroWeather, FetchCelestialBodyData {

    private static final Logger LOGGER = Logger.getLogger(Astrotify.class.getName());

    private final SendAlertPort sendAlertPort;
    private final FetchAstronomicalDataPort fetchAstronomicalDataPort;
    private final CelestialBodyDataFetcherPort celestialBodyDataFetcherPort;

    public Astrotify(SendAlertPort sendAlertPort,
                     FetchAstronomicalDataPort fetchAstronomicalDataPort,
                     CelestialBodyDataFetcherPort celestialBodyDataFetcherPort) {
        this.sendAlertPort = sendAlertPort;
        this.fetchAstronomicalDataPort = fetchAstronomicalDataPort;
        this.celestialBodyDataFetcherPort = celestialBodyDataFetcherPort;
    }

    @Override
    public void sendAlertIfTonightHasGoodWeatherForAstro() {
        LOGGER.info("Fetching astronomical weather for tonight");
        AstroWeatherDailyData todayAstroWeather = fetchAstronomicalDataPort.fetchTodayAstronomicalData();
        if (todayAstroWeather.doesTonightHaveGoodAstroWeather()) {
            LOGGER.info("Tonight has good weather for astronomical observation.");
            String alertMessage = getAlertMessage(todayAstroWeather);
            LOGGER.info("Sending alerts");
            sendAlertPort.sendAlert(alertMessage);
        } else {
            LOGGER.info("Tonight doesn't have good weather for astronomical observation.");
        }
    }

    @Override
    public void sendCelestialBodyInfo(String celestialBodyName, String city) {
        LOGGER.info("Fetching info for celestial body " + celestialBodyName + "...");
        CelestialBody celestialBody = celestialBodyDataFetcherPort.fetchData(LocalDate.now().plusDays(1), celestialBodyName);
        LOGGER.info("Data fetched : " + celestialBody);

        int riseHour = Integer.parseInt(celestialBody.getRise().substring(0, 2));
        int setHour = Integer.parseInt(celestialBody.getSet().substring(0, 2));
        String goodObservationHours = fetchAstronomicalDataPort.fetchTomorrowAstronomicalData()
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

    private String getAlertMessage(AstroWeatherDailyData astronomicalData) {
        String celestialBodiesStr = "";
        if (!astronomicalData.getTonightAvailableCelestialBodies().isEmpty()) {
            celestialBodiesStr = "Voici les astres présents dans la soirée :\n - " +
                    String.join("\n - ", astronomicalData.getTonightAvailableCelestialBodies()) + "\n";
        }
        return "⭐⭐⭐ Salut c'est Galilée !\n" +
                "Il semblerait qu'une bonne soirée astro se profile ce soir, il faut sortir le téléscope 🔭. \n" +
                celestialBodiesStr +
                "Pour plus de détail : " +
                astronomicalData.getSource();
    }

}
