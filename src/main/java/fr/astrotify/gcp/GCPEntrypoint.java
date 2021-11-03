package fr.astrotify.gcp;


import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import fr.astrotify.Astrotify;

import java.util.Map;

public class GCPEntrypoint implements BackgroundFunction<GCPEntrypoint.PubSubMessage> {

    @Override
    public void accept(GCPEntrypoint.PubSubMessage pubSubMessage, Context context) {
        Astrotify.build(
                new GCPSecretManager().getLatestVersionSecret("875152963263", "ASTROTIFY_TELEGRAM_BOT_TOKEN"),
                System.getenv("TELEGRAM_CHAT_ID"),
                System.getenv("METEOBLUE_URL")
        ).run();

    }

    public static class PubSubMessage {
        String data;
        Map<String, String> attributes;
        String messageId;
        String publishTime;
    }
}
