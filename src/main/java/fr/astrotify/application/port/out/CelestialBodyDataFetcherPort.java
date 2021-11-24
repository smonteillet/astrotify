package fr.astrotify.application.port.out;

import fr.astrotify.domain.CelestialBody;

import java.time.LocalDate;

public interface CelestialBodyDataFetcherPort {
    CelestialBody fetchData(LocalDate date, String celestialBodyName);
}
