package fr.astrotify.usecase.port.out;

import fr.astrotify.domain.AstroWeatherDailyData;

public interface FetchAstronomicalWeatherPort {
    AstroWeatherDailyData fetchTodayAstronomicalWeather();
    AstroWeatherDailyData fetchTomorrowAstronomicalWeather();
}
