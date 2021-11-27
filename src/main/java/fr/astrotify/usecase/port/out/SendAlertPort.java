package fr.astrotify.usecase.port.out;

@FunctionalInterface
public interface SendAlertPort {
    void sendAlert(String message);
}
