package umg.edu.programacion3.proyecto.covid.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
/**
 *
 * @author Fernando
 */

@Entity
@Data
public class ExecutedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String countryIso;

    private LocalDate executionDate;
}

