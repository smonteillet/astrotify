package fr.astrotify;

import fr.astrotify.adapter.out.MeteoBlueScrapper;
import fr.astrotify.adapter.out.TelegramMessageSender;
import fr.astrotify.application.service.Astrotify;
import fr.astrotify.application.port.in.SendAlertIfTonightIsGoodForAstro;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;

import java.util.List;

public class Main {

    // since we don't rely on a DI framework, we have to build application instance ourselves.
    public SendAlertIfTonightIsGoodForAstro buildAppInstance(String telegramBotToken, String telegramChatId, String meteoBlueURL) {
        SendAlertPort telegramNotifier = new TelegramMessageSender(telegramBotToken, telegramChatId);
        FetchAstronomicalDataPort meteoBlueScrapper = new MeteoBlueScrapper(meteoBlueURL);
        return new Astrotify(List.of(telegramNotifier), meteoBlueScrapper);
    }

}
