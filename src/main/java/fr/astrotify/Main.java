package fr.astrotify;

import fr.astrotify.adapter.out.MeteoBlueScrapper;
import fr.astrotify.adapter.out.TelegramMessageSender;
import fr.astrotify.adapter.out.TheSkyLiveScrapper;
import fr.astrotify.application.port.in.CheckAstroWeather;
import fr.astrotify.application.port.in.FetchCelestialBodyData;
import fr.astrotify.application.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.application.service.Astrotify;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;

import java.util.List;

// since we don't rely on a DI framework, we have to build application instances ourselves.
public class Main {

    public static CheckAstroWeather buildCheckAstroWeather(String telegramBotToken, String telegramChatId, String meteoBlueURL, String theSkyLiveURL) {
        return buildAstrotify(telegramBotToken, telegramChatId, meteoBlueURL, theSkyLiveURL);
    }

    public static FetchCelestialBodyData buildFetchCelestialBodyData(String telegramBotToken, String telegramChatId, String meteoBlueURL,
                                                                     String theSkyLiveURL) {
        return buildAstrotify(telegramBotToken, telegramChatId, meteoBlueURL, theSkyLiveURL);
    }

    private static Astrotify buildAstrotify(String telegramBotToken, String telegramChatId, String meteoBlueURL, String theSkyLiveURL) {
        SendAlertPort telegramNotifier = new TelegramMessageSender(telegramBotToken, telegramChatId);
        FetchAstronomicalDataPort meteoBlueScrapper = new MeteoBlueScrapper(meteoBlueURL);
        CelestialBodyDataFetcherPort celestialBodyDataFetcherPort = new TheSkyLiveScrapper(theSkyLiveURL);
        return new Astrotify(telegramNotifier, meteoBlueScrapper, celestialBodyDataFetcherPort);
    }

}
