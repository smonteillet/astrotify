package fr.astrotify.adapter.out;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import fr.astrotify.usecase.port.out.SendAlertPort;

import java.util.logging.Logger;

public class TelegramMessageSender implements SendAlertPort {
    private static final Logger LOGGER = Logger.getLogger(TelegramMessageSender.class.getName());

    private final String telegramBotToken;
    private final String telegramChatId;

    public TelegramMessageSender(String telegramBotToken, String telegramChatId) {
        this.telegramBotToken = telegramBotToken;
        this.telegramChatId = telegramChatId;
    }

    @Override
    public void sendAlert(String message) {
        LOGGER.info("Sending message to telegram...");
        TelegramBot bot = new TelegramBot(telegramBotToken);
        bot.execute(new SendMessage(telegramChatId, message));
        LOGGER.info("Sending message to telegram DONE");
    }
}
