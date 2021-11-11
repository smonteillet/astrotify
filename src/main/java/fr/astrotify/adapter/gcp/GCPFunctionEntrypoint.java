package fr.astrotify.adapter.gcp;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import fr.astrotify.common.AstrotifyFactory;

import java.util.Map;

public class GCPFunctionEntrypoint implements BackgroundFunction<GCPFunctionEntrypoint.PubSubMessage> {

    @Override
    public void accept(PubSubMessage pubSubMessage, Context context) {
        new AstrotifyFactory().buildMainUseCase(
                new GCPSecretManager().getLatestVersionSecret("875152963263", "ASTROTIFY_TELEGRAM_BOT_TOKEN"),
                System.getenv("TELEGRAM_CHAT_ID"),
                System.getenv("METEOBLUE_URL")
        ).sendAlertIfTonightIsGoodForAstro();
    }


    public static class PubSubMessage {
        String data;
        Map<String, String> attributes;
        String messageId;
        String publishTime;
    }
}

