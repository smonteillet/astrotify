package fr.astrotify.application;

import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.application.port.out.SendAlertPort;
import fr.astrotify.application.port.in.SendAlertIfTonightIsGoodForAstroUseCase;
import fr.astrotify.domain.AstronomicalDailyData;

import java.util.List;

public class Astrotify implements SendAlertIfTonightIsGoodForAstroUseCase {

    private final List<SendAlertPort> notifierPorts;
    private final FetchAstronomicalDataPort fetchAstronomicalDataPort;

    public Astrotify(List<SendAlertPort> notifierPorts, FetchAstronomicalDataPort fetchAstronomicalDataPort) {
        this.notifierPorts = notifierPorts;
        this.fetchAstronomicalDataPort = fetchAstronomicalDataPort;
    }

    @Override
    public void sendAlertIfTonightIsGoodForAstro() {
        AstronomicalDailyData astronomicalData = fetchAstronomicalDataPort.fetchAstronomicalData();
        if (astronomicalData.isTonightGoodForAstronomicalObservation()) {
            String message = "⭐⭐⭐ Salut c'est Galilée !" +
                    "\n Il semblerait qu'une bonne soirée astro se profile ce soir, il faut sortir le téléscope 🔭. " +
                    "Pour plus de détail : " + astronomicalData.getSource();
            notifierPorts.forEach(notifier -> notifier.sendAlert(message));
        }
    }
}
