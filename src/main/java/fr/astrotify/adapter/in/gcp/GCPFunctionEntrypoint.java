package fr.astrotify.adapter.in.gcp;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import fr.astrotify.AppInstances;

import java.util.Map;

public class GCPFunctionEntrypoint implements BackgroundFunction<GCPFunctionEntrypoint.PubSubMessage> {

    @Override
    public void accept(PubSubMessage pubSubMessage, Context context) {
        // TODO fetch projectId elsewhere. Maybe from System.getenv(...)
        String astrotifyTelegramBotToken = new GCPSecretManager().getLatestVersionSecret("875152963263", "ASTROTIFY_TELEGRAM_BOT_TOKEN");
        String telegramChatId = pubSubMessage.attributes.get("TELEGRAM_CHAT_ID");
        String functionType = pubSubMessage.attributes.get("FUNCTION");
        String meteoblueUrl = pubSubMessage.attributes.get("METEOBLUE_URL");
        if (functionType.equals("CHECK_WEATHER")) {
            AppInstances.buildCheckAstroWeatherUseCase(
                    astrotifyTelegramBotToken,
                    telegramChatId,
                    meteoblueUrl
            ).sendAlertIfTonightHasGoodWeatherForAstro();
        } else if (functionType.equals("CELESTIAL_BODY")) {
            String skyLiveUrl = pubSubMessage.attributes.get("SKY_LIVE_URL");
            String city = pubSubMessage.attributes.get("CITY");
            String celestialBody = pubSubMessage.attributes.get("CELESTIAL_BODY");
            AppInstances.buildFetchCelestialBodyEphemerideUseCase(
                    astrotifyTelegramBotToken,
                    telegramChatId,
                    meteoblueUrl,
                    skyLiveUrl
            ).sendCelestialBodyEphemeride(celestialBody, city);
        } else {
            throw new IllegalArgumentException("Function type is not prodived or is unknown : " + functionType);
        }
    }


    public static class PubSubMessage {
        String data;
        Map<String, String> attributes;
        String messageId;
        String publishTime;
    }
}

