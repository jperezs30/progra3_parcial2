package umg.edu.programacion3.proyecto.covid;

import java.util.Map;
import umg.edu.programacion3.proyecto.covid.model.Report;
import umg.edu.programacion3.proyecto.covid.service.CovidApiService;

public class Main2 {
    public static void main(String[] args) {
        CovidApiService service = new CovidApiService();
        Map<String, Report> result = service.getReportsGroupedByProvince("GTM", "2022-04-16");
        result = service.getReportsGroupedByProvince("USA", "2022-04-16");

    }
}
