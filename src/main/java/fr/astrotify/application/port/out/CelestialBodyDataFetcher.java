package fr.astrotify.application.port.out;

import fr.astrotify.domain.CelestialBody;

import java.time.LocalDate;

public interface CelestialBodyDataFetcher {
    CelestialBody fetchData(LocalDate date);
}
