package fr.astrotify.application.port.in;

public interface FetchCelestialBodyData {

    void sendCelestialBodyInfo(String celestialBody, String city);
}
