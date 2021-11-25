package fr.astrotify;

import fr.astrotify.adapter.out.MeteoBlueScrapper;
import fr.astrotify.adapter.out.TelegramMessageSender;
import fr.astrotify.adapter.out.TheSkyLiveScrapper;
import fr.astrotify.application.port.in.CheckAstroWeatherUseCase;
import fr.astrotify.application.port.in.CelestialBodyEphemerideUseCase;
import fr.astrotify.application.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.application.service.AstroWeatherUseCaseService;
import fr.astrotify.application.port.out.FetchAstronomicalWeatherPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.application.service.CelestialBodyEphemerideService;

// since we don't rely on a DI framework, we have to build top application instances ourselves.
public class AppInstances {

    public static CheckAstroWeatherUseCase buildCheckAstroWeatherUseCase(String telegramBotToken, String telegramChatId, String meteoBlueURL) {
        SendAlertPort telegramNotifier = new TelegramMessageSender(telegramBotToken, telegramChatId);
        FetchAstronomicalWeatherPort meteoBlueScrapper = new MeteoBlueScrapper(meteoBlueURL);
        return new AstroWeatherUseCaseService(telegramNotifier,meteoBlueScrapper);
    }

    public static CelestialBodyEphemerideUseCase buildCelestialBodyEphemerideUseCase(String telegramBotToken, String telegramChatId, String meteoBlueURL,
                                                                                     String theSkyLiveURL) {
        SendAlertPort telegramNotifier = new TelegramMessageSender(telegramBotToken, telegramChatId);
        FetchAstronomicalWeatherPort meteoBlueScrapper = new MeteoBlueScrapper(meteoBlueURL);
        CelestialBodyDataFetcherPort theSkyLiveScrapper = new TheSkyLiveScrapper(theSkyLiveURL);
        return new CelestialBodyEphemerideService(telegramNotifier,meteoBlueScrapper, theSkyLiveScrapper);
    }



}
