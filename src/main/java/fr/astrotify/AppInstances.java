package fr.astrotify;

import fr.astrotify.adapter.out.MeteoBlueScrapper;
import fr.astrotify.adapter.out.TelegramMessageSender;
import fr.astrotify.adapter.out.TheSkyLiveScrapper;
import fr.astrotify.usecase.port.in.CheckAstroWeatherUseCase;
import fr.astrotify.usecase.port.in.FetchCelestialBodyEphemerideUseCase;
import fr.astrotify.usecase.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.usecase.CheckAstroWeather;
import fr.astrotify.usecase.port.out.FetchAstronomicalWeatherPort;
import fr.astrotify.usecase.port.out.SendAlertPort;
import fr.astrotify.usecase.FetchFetchCelestialBodyEphemeride;

// since we don't rely on a DI framework, we have to build use case instances ourselves.
public class AppInstances {

    public static CheckAstroWeatherUseCase buildCheckAstroWeatherUseCase(String telegramBotToken, String telegramChatId, String meteoBlueURL) {
        SendAlertPort telegramNotifier = new TelegramMessageSender(telegramBotToken, telegramChatId);
        FetchAstronomicalWeatherPort meteoBlueScrapper = new MeteoBlueScrapper(meteoBlueURL);
        return new CheckAstroWeather(telegramNotifier,meteoBlueScrapper);
    }

    public static FetchCelestialBodyEphemerideUseCase buildFetchCelestialBodyEphemerideUseCase(String telegramBotToken, String telegramChatId, String meteoBlueURL,
                                                                                               String theSkyLiveURL) {
        SendAlertPort telegramNotifier = new TelegramMessageSender(telegramBotToken, telegramChatId);
        FetchAstronomicalWeatherPort meteoBlueScrapper = new MeteoBlueScrapper(meteoBlueURL);
        CelestialBodyDataFetcherPort theSkyLiveScrapper = new TheSkyLiveScrapper(theSkyLiveURL);
        return new FetchFetchCelestialBodyEphemeride(telegramNotifier,meteoBlueScrapper, theSkyLiveScrapper);
    }



}
