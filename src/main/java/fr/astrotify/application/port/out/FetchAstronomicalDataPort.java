package fr.astrotify.application.port.out;

import fr.astrotify.domain.AstronomicalDailyData;

public interface FetchAstronomicalDataPort {
    AstronomicalDailyData fetchAstronomicalData();
}
