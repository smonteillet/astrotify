package fr.astrotify;

import fr.astrotify.astro.AstroNightlyDataScrapper;
import fr.astrotify.notifier.Notifier;
import fr.astrotify.notifier.TelegramNotifier;

import java.util.List;
import java.util.logging.Logger;

public class Astrotify {

    private static final Logger LOGGER = Logger.getLogger(Astrotify.class.getName());
    public static final double ASTRO_NIGHT_SCORE_THRESHOLD = 70d;

    private final AstroNightlyDataScrapper astroDailyDataScrapper;
    private final List<Notifier> notifiers;
    private final String meteoblueUrl;

    public Astrotify(AstroNightlyDataScrapper astroDailyDataScrapper, List<Notifier> notifiers, String meteoblueUrl) {
        this.astroDailyDataScrapper = astroDailyDataScrapper;
        this.notifiers = notifiers;
        this.meteoblueUrl = meteoblueUrl;
    }


    public void run() {
        LOGGER.info("Launching Astrotify ...");
        boolean isGoodNightForAstro = astroDailyDataScrapper.getTodayAstroNightlyData(meteoblueUrl).isTonightGoodForAstro();
        LOGGER.info("Good night for astronomy : " + isGoodNightForAstro);
        if (isGoodNightForAstro) {
            String message = "⭐⭐⭐ Salut c'est Galilée !" +
                    "\n Il semblerait qu'une bonne soirée astro se profile ce soir, il faut sortir le téléscope 🔭. " +
                    "Pour plus de détail : " + meteoblueUrl;
            notifiers.forEach(notifier -> notifier.notify(message));
        }
        LOGGER.info("Astrotify is done for today.");
    }

    public static Astrotify build(String telegramBotToken, String telegramChatId, String meteoblueUrl) {
        AstroNightlyDataScrapper astroNightlyDataScrapper = new AstroNightlyDataScrapper();
        TelegramNotifier telegramNotifier = new TelegramNotifier(telegramBotToken, telegramChatId);
        return new Astrotify(
                astroNightlyDataScrapper,
                List.of(telegramNotifier),
                meteoblueUrl
        );
    }
}
