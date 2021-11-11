package fr.astrotify.application;

import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.domain.AstronomicalDailyData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AstrotifyTest {

    @Mock
    SendAlertPort sendAlertPort;

    @Mock
    FetchAstronomicalDataPort fetchAstronomicalDataPort;

    @Mock
    AstronomicalDailyData astronomicalDailyData;

    @Test
    void alertNotSent_whenTonightIsNotGoodForAstro() {
        // given
        when(astronomicalDailyData.isTonightGoodForAstronomicalObservation()).thenReturn(false);
        when(fetchAstronomicalDataPort.fetchAstronomicalData()).thenReturn(astronomicalDailyData);
        Astrotify astrotify = new Astrotify(List.of(sendAlertPort), fetchAstronomicalDataPort);
        // when
        astrotify.sendAlertIfTonightIsGoodForAstro();
        // then
        verifyNoInteractions(sendAlertPort);
    }

    @Test
    void alertSent_whenTonightIsGoodForAstro() {
        // given
        when(astronomicalDailyData.isTonightGoodForAstronomicalObservation()).thenReturn(true);
        when(astronomicalDailyData.getSource()).thenReturn("URL");
        when(fetchAstronomicalDataPort.fetchAstronomicalData()).thenReturn(astronomicalDailyData);
        Astrotify astrotify = new Astrotify(List.of(sendAlertPort), fetchAstronomicalDataPort);
        // when
        astrotify.sendAlertIfTonightIsGoodForAstro();
        // then
        verify(sendAlertPort).sendAlert("⭐⭐⭐ Salut c'est Galilée !" +
                "\n Il semblerait qu'une bonne soirée astro se profile ce soir, il faut sortir le téléscope 🔭. " +
                "Pour plus de détail : URL");
    }

}