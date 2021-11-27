package fr.astrotify.usecase.port.in;

public interface FetchCelestialBodyEphemerideUseCase {

    void sendCelestialBodyEphemeride(String celestialBody, String city);
}
