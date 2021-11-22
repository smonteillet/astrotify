package fr.astrotify.adapter.out;

import fr.astrotify.application.port.out.CelestialBodyDataFetcher;
import fr.astrotify.domain.CelestialBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TheSkyLiveScrapper implements CelestialBodyDataFetcher {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final String URL = "https://theskylive.com/planetarium?localdata=43.46667%7C1.35%7CMuret+(FR)" +
            "%7CEurope%2FParis%7C0&obj=cometleonard&h=20&m=00&date={date}";

    @Override
    public CelestialBody fetchData(LocalDate date) {
        Document document = getDocument(computeUrl(date));
        String rise = document.select("span#circumstances").get(0).child(1).text();
        String set = document.select("span#circumstances").get(0).child(2).text();
        String magnitude = document.select("span#magnitude").text();
        return CelestialBody.builder()
                .rise(rise)
                .set(set)
                .magnitude(magnitude)
                .build();
    }

    private String computeUrl(LocalDate date) {
        return (URL.replace("{date}", date.format(DATE_FORMAT)));
    }

    private Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to access to url", e);
        }
    }

}
