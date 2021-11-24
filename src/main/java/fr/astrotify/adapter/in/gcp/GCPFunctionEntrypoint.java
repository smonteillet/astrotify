package fr.astrotify.adapter.in.gcp;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import fr.astrotify.Main;

import java.util.Map;

public class GCPFunctionEntrypoint implements BackgroundFunction<GCPFunctionEntrypoint.PubSubMessage> {

    @Override
    public void accept(PubSubMessage pubSubMessage, Context context) {
        String astrotifyTelegramBotToken = new GCPSecretManager().getLatestVersionSecret("875152963263", "ASTROTIFY_TELEGRAM_BOT_TOKEN");
        String telegramChatId = System.getenv("TELEGRAM_CHAT_ID");
        String meteoblueUrl = System.getenv("METEOBLUE_URL");
        String skyLiveUrl = System.getenv("SKY_LIVE_URL");
        String city = System.getenv("CITY");
        String celestialBody = System.getenv("CELESTIAL_BODY");

       Main.buildCheckAstroWeather(
                astrotifyTelegramBotToken,
                telegramChatId,
                meteoblueUrl,
                skyLiveUrl
        ).sendAlertIfTonightHasGoodWeatherForAstro();

        Main.buildFetchCelestialBodyData(
                astrotifyTelegramBotToken,
                telegramChatId,
                meteoblueUrl,
                skyLiveUrl
        ).sendCelestialBodyInfo(celestialBody, city);
    }


    public static class PubSubMessage {
        String data;
        Map<String, String> attributes;
        String messageId;
        String publishTime;
    }
}

