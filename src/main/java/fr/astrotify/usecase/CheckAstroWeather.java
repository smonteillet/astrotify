package fr.astrotify.usecase;

import fr.astrotify.usecase.port.in.CheckAstroWeatherUseCase;
import fr.astrotify.usecase.port.out.FetchAstronomicalWeatherPort;
import fr.astrotify.usecase.port.out.SendAlertPort;
import fr.astrotify.domain.AstroWeatherDailyData;

import java.util.logging.Logger;

public class CheckAstroWeather implements CheckAstroWeatherUseCase {

    private static final Logger LOGGER = Logger.getLogger(CheckAstroWeather.class.getName());

    private final SendAlertPort sendAlertPort;
    private final FetchAstronomicalWeatherPort fetchAstronomicalWeatherPort;

    public CheckAstroWeather(SendAlertPort sendAlertPort, FetchAstronomicalWeatherPort fetchAstronomicalWeatherPort) {
        this.sendAlertPort = sendAlertPort;
        this.fetchAstronomicalWeatherPort = fetchAstronomicalWeatherPort;
    }

    @Override
    public void sendAlertIfTonightHasGoodWeatherForAstro() {
        LOGGER.info("Fetching astronomical weather for tonight");
        AstroWeatherDailyData todayAstroWeather = fetchAstronomicalWeatherPort.fetchTodayAstronomicalWeather();
        if (todayAstroWeather.doesTonightHaveGoodAstroWeather()) {
            LOGGER.info("Tonight has good weather for astronomical observation.");
            String alertMessage = getAlertMessage(todayAstroWeather);
            LOGGER.info("Sending alerts");
            sendAlertPort.sendAlert(alertMessage);
        } else {
            LOGGER.info("Tonight doesn't have good weather for astronomical observation.");
        }
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
