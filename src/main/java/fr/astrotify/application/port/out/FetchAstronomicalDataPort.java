package fr.astrotify.application.port.out;

import fr.astrotify.domain.AstroWeatherDailyData;

public interface FetchAstronomicalDataPort {
    AstroWeatherDailyData fetchTodayAstronomicalData();
    AstroWeatherDailyData fetchTomorrowAstronomicalData();
}
