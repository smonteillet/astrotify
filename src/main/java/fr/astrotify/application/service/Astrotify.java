package fr.astrotify.application.service;

import fr.astrotify.application.port.in.SendAstroAlert;
import fr.astrotify.application.port.out.CelestialBodyDataFetcher;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.domain.AstronomicalDailyData;
import fr.astrotify.domain.CelestialBody;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class Astrotify implements SendAstroAlert {

    private static final Logger LOGGER = Logger.getLogger(Astrotify.class.getName());

    private final List<SendAlertPort> sendAlertPorts;
    private final FetchAstronomicalDataPort fetchAstronomicalDataPort;
    private final CelestialBodyDataFetcher celestialBodyDataFetcher;

    public Astrotify(List<SendAlertPort> sendAlertPorts,
                     FetchAstronomicalDataPort fetchAstronomicalDataPort,
                     CelestialBodyDataFetcher celestialBodyDataFetcher) {
        this.sendAlertPorts = sendAlertPorts;
        this.fetchAstronomicalDataPort = fetchAstronomicalDataPort;
        this.celestialBodyDataFetcher = celestialBodyDataFetcher;
    }

    @Override
    public void sendAlertIfTonightIsGoodForAstro() {
        LOGGER.info("Fetching astronomical data for tonight");
        AstronomicalDailyData astronomicalData = fetchAstronomicalDataPort.fetchAstronomicalData();
        if (astronomicalData.isTonightGoodForAstronomicalObservation()) {
            LOGGER.info("Tonight is a good night for astronomical observation.");
            String alertMessage = getAlertMessage(astronomicalData);
            LOGGER.info("Sending alerts");
            sendMessage(alertMessage);
        } else {
            LOGGER.info("Tonight is a not good night for astronomical observation.");
        }
    }

    @Override
    public void sendCelestialBodyInfoMessageForTomorrow(String celestialBodyName, String city) {
        LOGGER.info("Fetching info for celestial body " + celestialBodyName + "...");
        CelestialBody celestialBody = celestialBodyDataFetcher.fetchData(LocalDate.now().plusDays(1), celestialBodyName);
        LOGGER.info("Data fetched : " + celestialBody);
        String message = "Ephemeride " + celestialBodyName + " pour demain sur " + city + ":" +
                "\n- Levée : " + celestialBody.getRise() +
                "\n- Couchée : " + celestialBody.getSet() +
                "\n- Magnitude : " + celestialBody.getMagnitude() +
                "\n- Distance Terre : " + celestialBody.getDistanceToEarth() +
                "\n- Distance Soleil : " + celestialBody.getDistanceToSun() +
                "\nPlus d'info : " + celestialBody.getDataSource();

        sendMessage(message);
    }

    private String getAlertMessage(AstronomicalDailyData astronomicalData) {
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

    private void sendMessage(String message)
    {
        sendAlertPorts.forEach(sendAlertPort -> sendAlertPort.sendAlert(message));
    }


}
