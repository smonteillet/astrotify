package fr.astrotify;

import fr.astrotify.adapter.out.MeteoBlueScrapper;
import fr.astrotify.adapter.out.TelegramMessageSender;
import fr.astrotify.adapter.out.TheSkyLiveScrapper;
import fr.astrotify.application.port.out.CelestialBodyDataFetcher;
import fr.astrotify.application.service.Astrotify;
import fr.astrotify.application.port.in.SendAstroAlert;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;

import java.util.List;

public class Main {

    // since we don't rely on a DI framework, we have to build application instance ourselves.
    public SendAstroAlert buildAppInstance(String telegramBotToken, String telegramChatId, String meteoBlueURL, String theSkyLiveURL) {
        SendAlertPort telegramNotifier = new TelegramMessageSender(telegramBotToken, telegramChatId);
        FetchAstronomicalDataPort meteoBlueScrapper = new MeteoBlueScrapper(meteoBlueURL);
        CelestialBodyDataFetcher celestialBodyDataFetcher =  new TheSkyLiveScrapper(theSkyLiveURL);
        return new Astrotify(List.of(telegramNotifier), meteoBlueScrapper, celestialBodyDataFetcher);
    }

}
