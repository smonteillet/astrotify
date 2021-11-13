package fr.astrotify;

import fr.astrotify.adapter.out.MeteoBlueScrapper;
import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.application.service.Astrotify;

import java.util.List;
import java.util.logging.Logger;

/**
 * This class provides a simple execution of Astrotify without test assertions since we don't want to mock meteo blue scrapping (not a real test).
 * It will fetch astronomical data from Muret (France) city and will log alert to the console if necessary.
 */
public class MainIT {

    private static final Logger LOGGER = Logger.getLogger(MainIT.class.getName());
    public static final String METEOBLUE_URL = "https://www.meteoblue.com/en/weather/outdoorsports/seeing/muret_france_2991153,TELEGRAM_CHAT_ID=-750321988";

    public static void main(String[] args) {
        FetchAstronomicalDataPort meteoBlueScrapper = new MeteoBlueScrapper(METEOBLUE_URL);
        SendAlertPort sendAlertPort = LOGGER::info;
        new Astrotify(List.of(sendAlertPort), meteoBlueScrapper).sendAlertIfTonightIsGoodForAstro();
    }
}
