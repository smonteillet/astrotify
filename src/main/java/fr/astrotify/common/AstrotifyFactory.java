package fr.astrotify.common;

import fr.astrotify.adapter.MeteoBlueScrapper;
import fr.astrotify.adapter.TelegramNotifier;
import fr.astrotify.application.Astrotify;
import fr.astrotify.application.port.in.SendAlertIfTonightIsGoodForAstroUseCase;

import java.util.List;

public class AstrotifyFactory {

    public SendAlertIfTonightIsGoodForAstroUseCase buildMainUseCase(String telegramBotToken, String telegramChatId, String meteoBlueURL) {
        TelegramNotifier telegramNotifier = new TelegramNotifier(telegramBotToken, telegramChatId);
        return new Astrotify(
                List.of(telegramNotifier),
                new MeteoBlueScrapper(meteoBlueURL)
        );
    }
}
