package fr.astrotify.notifier;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.logging.Logger;

public class TelegramNotifier implements Notifier {

    private static final Logger LOGGER = Logger.getLogger(TelegramNotifier.class.getName());

    private String telegramBotToken;
    private String telegramChatId;

    public TelegramNotifier(String telegramBotToken, String telegramChatId) {
        this.telegramBotToken = telegramBotToken;
        this.telegramChatId = telegramChatId;
    }

    @Override
    public void notify(String message) {
        LOGGER.info("Sending message to telegram...");
        TelegramBot bot = new TelegramBot(telegramBotToken);
        bot.execute(new SendMessage(telegramChatId, message));
        LOGGER.info("Sending message to telegram DONE");
    }
}
