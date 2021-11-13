package fr.astrotify.application.port.out;

@FunctionalInterface
public interface SendAlertPort {
    void sendAlert(String message);
}
