package umg.edu.programacion3.proyecto.covid;

import jakarta.persistence.EntityManager;
import java.util.Map;
import umg.edu.programacion3.proyecto.covid.config.Scheduler;
import umg.edu.programacion3.proyecto.covid.model.Report;
import umg.edu.programacion3.proyecto.covid.persistence.JpaUtil;
import umg.edu.programacion3.proyecto.covid.service.CovidApiService;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        CovidApiService service = new CovidApiService();
        Scheduler scheduler = new Scheduler(service);
        scheduler.iniciar();

        System.out.println("🟢 App started. Waiting for scheduler...");
        

    }
}
