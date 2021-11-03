package fr.astrotify.astro;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

public class AstroNightlyDataScrapper {

    public AstroNightlyData getTodayAstroNightlyData(String meteoBlueUrl)
    {
        List<AstroHourlyData> astroHourlyDataFromToday = getMeteoBlueDocument(meteoBlueUrl)
                .select("tr.night").stream()
                .filter(hourRow -> hourRow.attr("data-day").equals("0")) // just  current day
                .map(hourRow -> AstroHourlyData.builder()
                        .hour(toInt(hourRow.child(0).text()))
                        .lowCloud(toInt(hourRow.child(1).text()))
                        .midCloud(toInt(hourRow.child(2).text()))
                        .highCloud(toInt(hourRow.child(3).text()))
                        .seeing(toInt(hourRow.child(5).text()))
                        .moonIllumination(toInt(hourRow.ownerDocument().select("div.illumination").first().text().replace("%", "")))
                        .moonIsPresent(hourRow.select("pre.touch").first().text().contains("L"))
                        .build())
                .collect(Collectors.toList());
        return AstroNightlyData.builder()
                .astroHourlyDataList(astroHourlyDataFromToday)
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
