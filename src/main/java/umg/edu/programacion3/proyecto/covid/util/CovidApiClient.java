package umg.edu.programacion3.proyecto.covid.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static umg.edu.programacion3.proyecto.covid.config.AppProperties.get;

public class CovidApiClient {

    private static final String API_KEY = get("api.key");
    private static final String API_HOST = get("api.host");
    private final HttpClient client;

    public CovidApiClient() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Fetches all regions from the API
     */
    public String getRegions() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/regions"))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Fetches provinces for a specific country ISO code (e.g. GTM)
     */
    public String getProvinces(String iso) throws IOException, InterruptedException {        

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://" + API_HOST + "/provinces?iso=" + iso))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Fetches COVID-19 report by ISO code and date (e.g. GTM, 2022-04-16)
     */
    public String getReport(String iso, String date) throws IOException, InterruptedException {
        String url = String.format("https://%s/reports?iso=%s&date=%s", API_HOST, iso, date);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
