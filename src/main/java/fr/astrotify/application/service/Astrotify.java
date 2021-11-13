package fr.astrotify.application.service;

import fr.astrotify.application.port.in.SendAlertIfTonightIsGoodForAstro;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.domain.AstronomicalDailyData;

import java.util.List;
import java.util.logging.Logger;

public class Astrotify implements SendAlertIfTonightIsGoodForAstro {

    private static final Logger LOGGER = Logger.getLogger(Astrotify.class.getName());

    private final List<SendAlertPort> sendAlertPorts;
    private final FetchAstronomicalDataPort fetchAstronomicalDataPort;

    public Astrotify(List<SendAlertPort> sendAlertPorts, FetchAstronomicalDataPort fetchAstronomicalDataPort) {
        this.sendAlertPorts = sendAlertPorts;
        this.fetchAstronomicalDataPort = fetchAstronomicalDataPort;
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
        }
        else {
            LOGGER.info("Tonight is a not good night for astronomical observation.");
        }
    }

    private String getAlertMessage(AstronomicalDailyData astronomicalData)
    {
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
