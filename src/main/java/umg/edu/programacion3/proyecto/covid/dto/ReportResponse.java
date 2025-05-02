package umg.edu.programacion3.proyecto.covid.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReportResponse {
    private List<ReportDto> data;
}
