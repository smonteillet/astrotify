package fr.astrotify;

import fr.astrotify.adapter.out.MeteoBlueScrapper;
import fr.astrotify.adapter.out.TheSkyLiveScrapper;
import fr.astrotify.usecase.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.usecase.port.out.FetchAstronomicalWeatherPort;
import fr.astrotify.usecase.port.out.SendAlertPort;
import fr.astrotify.usecase.CheckAstroWeather;
import fr.astrotify.usecase.FetchFetchCelestialBodyEphemeride;

import java.util.logging.Logger;

/**
 * This class provides a simple execution of Astrotify without test assertions since we don't want to mock meteo blue scrapping (not a real test).
 * It will fetch astronomical data from Muret (France) city and will log alert to the console if necessary.
 */
public class MainIT {

    private static final Logger LOGGER = Logger.getLogger(MainIT.class.getName());
    public static final String METEOBLUE_URL = "https://www.meteoblue.com/en/weather/outdoorsports/seeing/muret_france_2991153";
    public static final String SKY_LIVE_URL = "https://theskylive.com/planetarium?localdata=43.46667%7C1.35%7CMuret+(FR)%7CEurope%2FParis%7C0&obj=cometleonard&date={date}";

    public static void main(String[] args) {
        FetchAstronomicalWeatherPort meteoBlueScrapper = new MeteoBlueScrapper(METEOBLUE_URL);
        CelestialBodyDataFetcherPort celestialBodyDataFetcherPort = new TheSkyLiveScrapper(SKY_LIVE_URL);
        SendAlertPort sendAlertPort = LOGGER::info;

        CheckAstroWeather astroWeatherService = new CheckAstroWeather(sendAlertPort,meteoBlueScrapper);
        astroWeatherService.sendAlertIfTonightHasGoodWeatherForAstro();

        FetchFetchCelestialBodyEphemeride fetchCelestialBodyEphemeride = new FetchFetchCelestialBodyEphemeride(sendAlertPort, meteoBlueScrapper,
                celestialBodyDataFetcherPort);
        fetchCelestialBodyEphemeride.sendCelestialBodyEphemeride("Comète Léonard", "Muret");
    }
}
