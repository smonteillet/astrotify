package fr.astrotify.application.port.in;

public interface SendAstroAlert {
    void sendAlertIfTonightIsGoodForAstro();
    void sendCelestialBodyInfoMessageForTomorrow(String celestialBodyName, String city);
}
