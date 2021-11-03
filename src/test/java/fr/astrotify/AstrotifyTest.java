package fr.astrotify;

import fr.astrotify.astro.AstroHourlyData;
import fr.astrotify.astro.AstroNightlyData;
import fr.astrotify.astro.AstroNightlyDataScrapper;
import fr.astrotify.notifier.Notifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AstrotifyTest {

    @Mock
    AstroNightlyDataScrapper astroNightlyDataScrapper;

    @Mock
    Notifier notifier;

    @Test
    void goodNightScore_shallNotify() {
        AstroHourlyData hour21 = AstroHourlyData.builder()
                .hour(21)
                .lowCloud(0).midCloud(0).highCloud(0)
                .moonIllumination(0).moonIsPresent(false)
                .seeing(5).build();
        AstroHourlyData hour22 = AstroHourlyData.builder()
                .hour(22)
                .lowCloud(0).midCloud(0).highCloud(0)
                .moonIllumination(0).moonIsPresent(false)
                .seeing(5).build();
        AstroHourlyData hour23 = AstroHourlyData.builder()
                .hour(23)
                .lowCloud(0).midCloud(0).highCloud(0)
                .moonIllumination(0).moonIsPresent(false)
                .seeing(5).build();
        AstroNightlyData astroNightlyData = AstroNightlyData.builder()
                .astroHourlyDataList(List.of(
                        hour21, hour22, hour23
                )).build();
        when(astroNightlyDataScrapper.getTodayAstroNightlyData("url")).thenReturn(astroNightlyData);
        Astrotify astrotify = new Astrotify(astroNightlyDataScrapper, List.of(notifier), "url");
        astrotify.run();
        verify(notifier).notify("⭐⭐⭐ Salut c'est Galilée !" +
                "\n Il semblerait qu'une bonne soirée astro se profile ce soir, il faut sortir le téléscope 🔭. " +
                "Pour plus de détail : url");
    }

    @Test
    void badNightScore_shallNotNotify() {
        AstroHourlyData hour21 = AstroHourlyData.builder()
                .hour(21)
                .lowCloud(100).midCloud(100).highCloud(100)
                .moonIllumination(100).moonIsPresent(true)
                .seeing(1).build();
        AstroHourlyData hour22 = AstroHourlyData.builder()
                .hour(22)
                .lowCloud(100).midCloud(100).highCloud(100)
                .moonIllumination(100).moonIsPresent(true)
                .seeing(1).build();
        AstroHourlyData hour23 = AstroHourlyData.builder()
                .hour(23)
                .lowCloud(100).midCloud(100).highCloud(100)
                .moonIllumination(100).moonIsPresent(true)
                .seeing(1).build();
        AstroNightlyData astroNightlyData = AstroNightlyData.builder()
                .astroHourlyDataList(List.of(
                        hour21, hour22, hour23
                )).build();
        when(astroNightlyDataScrapper.getTodayAstroNightlyData("url")).thenReturn(astroNightlyData);
        Astrotify astrotify = new Astrotify(astroNightlyDataScrapper, List.of(notifier), "url");
        astrotify.run();
        verifyNoInteractions(notifier);
    }
}