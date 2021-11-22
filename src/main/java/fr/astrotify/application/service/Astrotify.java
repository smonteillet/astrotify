package fr.astrotify.application.service;

import fr.astrotify.application.port.in.SendAstroAlert;
import fr.astrotify.application.port.out.CelestialBodyDataFetcher;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.domain.AstronomicalDailyData;
import fr.astrotify.domain.CelestialBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
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
            sendAlertPorts.forEach(sendAlertPort -> sendAlertPort.sendAlert(alertMessage));
        } else {
            LOGGER.info("Tonight is a not good night for astronomical observation.");
        }
    }

    @Override
    public void sendCelestialBodyDataAlert() {
        LOGGER.info("Sending info for celestial body leonard comet ...");
        CelestialBody celestialBody = celestialBodyDataFetcher.fetchData(LocalDate.now().plusDays(1));
        String message = "Info Comète Léonard pour demain :" +
                "\n- Levée : " + celestialBody.getRise() +
                "\n- Couchée : " + celestialBody.getSet() +
                "\n- Magnitude : " + celestialBody.getMagnitude();
        sendAlertPorts.forEach(sendAlertPort -> sendAlertPort.sendAlert(message));
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


}
