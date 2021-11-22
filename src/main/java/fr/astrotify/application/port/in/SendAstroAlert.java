package fr.astrotify.application.port.in;

public interface SendAstroAlert {
    void sendAlertIfTonightIsGoodForAstro();
    void sendCelestialBodyDataAlert();
}
