package fr.astrotify.adapter.out;

import fr.astrotify.usecase.port.out.CelestialBodyDataFetcherPort;
import fr.astrotify.domain.CelestialBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TheSkyLiveScrapper implements CelestialBodyDataFetcherPort {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String theSkyLiveURL;

    public TheSkyLiveScrapper(String theSkyLiveURL) {

        this.theSkyLiveURL = theSkyLiveURL;
    }

    @Override
    public CelestialBody fetchData(LocalDate date, String celestialBodyName) {
        String theSkyLiveUrl = computeUrl(date);
        Document document = getDocument(theSkyLiveUrl);
        String rise = document.select("span#circumstances").get(0).child(0).text();
        String set = document.select("span#circumstances").get(0).child(2).text();
        String magnitude = document.select("span#magnitude").text();
        String distanceToEarth = document.select("span#distearth").parents().get(0).text();
        String distanceToSun = document.select("span#distsun").parents().get(0).text();
        return CelestialBody.builder()
                .rise(rise)
                .set(set)
                .magnitude(magnitude)
                .dataSource(theSkyLiveUrl)
                .name(celestialBodyName)
                .distanceToEarth(distanceToEarth)
                .distanceToSun(distanceToSun)
                .build();
    }

    private String computeUrl(LocalDate date) {
        return (theSkyLiveURL.replace("{date}", date.format(DATE_FORMAT)));
    }

    private Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to access to url", e);
        }
    }

}
