package fr.astrotify.application.port.out;

import fr.astrotify.domain.AstroWeatherDailyData;

public interface FetchAstronomicalWeatherPort {
    AstroWeatherDailyData fetchTodayAstronomicalWeather();
    AstroWeatherDailyData fetchTomorrowAstronomicalWeather();
}
