package umg.edu.programacion3.proyecto.covid.config;

import static umg.edu.programacion3.proyecto.covid.config.AppProperties.getInt;
import umg.edu.programacion3.proyecto.covid.service.CovidApiService;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Scheduler {

    private final CovidApiService service;

    public Scheduler(CovidApiService service) {
        this.service = service;
    }

    public void iniciar() {
        int delaySeconds = getInt("scheduler.initial.delay", 15);
        String date = AppProperties.get("covid.report.date");

        Thread thread = new Thread(() -> {
            try {
                log.info("[Hilo] en espera " + delaySeconds + " segundos...");
                Thread.sleep(delaySeconds * 1000L); // convertir segundos a milisegundos

                log.info("[Hilo] Inicio de consumo API...");
                service.fetchAndPersistCovidData("GTM", date);

            } catch (InterruptedException e) {
                log.info("❌ Error en hilo: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        thread.start(); // arranca el hilo
    }

}
