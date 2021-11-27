package fr.astrotify.usecase;

import fr.astrotify.usecase.CheckAstroWeather;
import fr.astrotify.usecase.port.out.FetchAstronomicalWeatherPort;
import fr.astrotify.usecase.port.out.SendAlertPort;
import fr.astrotify.domain.AstroWeatherDailyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AstroWeatherServiceTest {


    @Mock
    SendAlertPort sendAlertPort;

    @Mock
    FetchAstronomicalWeatherPort fetchAstronomicalWeatherPort;

    @Mock
    AstroWeatherDailyData astronomicalDailyData;

    @InjectMocks
    CheckAstroWeather astroWeatherService;

    @Test
    void alertNotSent_whenTonightWeatherIsNotGoodForAstro() {
        // given
        when(astronomicalDailyData.doesTonightHaveGoodAstroWeather()).thenReturn(false);
        when(fetchAstronomicalWeatherPort.fetchTodayAstronomicalWeather()).thenReturn(astronomicalDailyData);
        // when
        astroWeatherService.sendAlertIfTonightHasGoodWeatherForAstro();
        // then
        verifyNoInteractions(sendAlertPort);
    }

    @Test
    void alertSent_whenTonightWeatherIsGoodForAstro() {
        // given
        when(astronomicalDailyData.doesTonightHaveGoodAstroWeather()).thenReturn(true);
        when(astronomicalDailyData.getSource()).thenReturn("URL");
        when(astronomicalDailyData.getTonightAvailableCelestialBodies()).thenReturn(List.of("Lune", "Jupiter", "Saturne"));
        when(fetchAstronomicalWeatherPort.fetchTodayAstronomicalWeather()).thenReturn(astronomicalDailyData);
        // when
        astroWeatherService.sendAlertIfTonightHasGoodWeatherForAstro();
        // then
        verify(sendAlertPort).sendAlert(
                "⭐⭐⭐ Salut c'est Galilée !\n" +
                        "Il semblerait qu'une bonne soirée astro se profile ce soir, il faut sortir le téléscope 🔭. \n" +
                        "Voici les astres présents dans la soirée :\n" +
                        " - Lune\n" +
                        " - Jupiter\n" +
                        " - Saturne\n" +
                        "Pour plus de détail : URL"
        );
    }
}