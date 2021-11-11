package fr.astrotify.adapter;

import fr.astrotify.application.port.out.FetchAstronomicalDataPort;
import fr.astrotify.domain.AstronomicalDailyData;
import fr.astrotify.domain.AstronomicalHourlyData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

public class MeteoBlueScrapper implements FetchAstronomicalDataPort {

    private final String meteoblueUrl;

    public MeteoBlueScrapper(String meteoblueUrl) {
        this.meteoblueUrl = meteoblueUrl;
    }


    @Override
    public AstronomicalDailyData fetchAstronomicalData() {
        List<AstronomicalHourlyData> astronomicalHourlyDataList = getMeteoBlueDocument(meteoblueUrl)
                .select("tr.night").stream()
                .filter(hourRow -> hourRow.attr("data-day").equals("0")) // just  current day
                .map(hourRow -> AstronomicalHourlyData.builder()
                        .hour(toInt(hourRow.child(0).text()))
                        .lowCloud(toInt(hourRow.child(1).text()))
                        .midCloud(toInt(hourRow.child(2).text()))
                        .highCloud(toInt(hourRow.child(3).text()))
                        .seeing(toInt(hourRow.child(5).text()))
                        .build())
                .collect(Collectors.toList());
        return AstronomicalDailyData.builder()
                .astronomicalHourlyDataList(astronomicalHourlyDataList)
                .source(meteoblueUrl)
                .build();
    }

    private Document getMeteoBlueDocument(String meteoblueUrl) {
        try {
            return Jsoup.connect(meteoblueUrl).get();
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to access to meteo blue url", e);
        }
    }

    private int toInt(String s) {
        return Integer.parseInt(s);
    }
}
