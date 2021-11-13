package fr.astrotify.adapter.in.gcp;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.logging.Logger;

public class GCPSecretManager {

    private static final Logger LOGGER = Logger.getLogger(GCPSecretManager.class.getName());

    public String getSecret(String projectId, String secretId, String version) {
        LOGGER.info("Fetching google secret  " + secretId + " for project " + projectId + " with version " + version + "...");
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, version);
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            String botToken = response.getPayload().getData().toStringUtf8();
            LOGGER.info("Secret fetched OK");
            return botToken;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not fetch secret " + secretId, e);
        }
    }

    public String getLatestVersionSecret(String projectId, String secretId) {
        return this.getSecret(projectId, secretId, "latest");
    }
}
