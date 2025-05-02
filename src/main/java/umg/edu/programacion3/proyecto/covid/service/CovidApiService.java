package umg.edu.programacion3.proyecto.covid.service;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.log4j.Log4j2;
import umg.edu.programacion3.proyecto.covid.dto.*;
import umg.edu.programacion3.proyecto.covid.mapper.DtoMapper;
import umg.edu.programacion3.proyecto.covid.model.*;
import umg.edu.programacion3.proyecto.covid.persistence.JpaUtil;
import umg.edu.programacion3.proyecto.covid.util.CovidApiClient;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import umg.edu.programacion3.proyecto.covid.model.ExecutedReport;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Log4j2
public class CovidApiService {

    private final CovidApiClient client;
    private final Gson gson;

    public CovidApiService() {
        this.client = new CovidApiClient();
        this.gson = new Gson();
    }

    public void fetchAndPersistCovidData(String iso, String date) {
        log.info("Iniciando persistencia");
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            
            if (hasAlreadyExecuted(iso, date, em)) {
                log.info("⚠️ {} on {} was already processed. Skipping.", iso, date);
                tx.commit();
                return;
            }

            // 🔹 REGIONS
            String jsonRegions = client.getRegions();
            RegionResponse regionResponse = gson.fromJson(jsonRegions, RegionResponse.class);
            List<RegionDto> regions = regionResponse.getData();
            
            for (RegionDto dto : regions) {                
                Region entity = DtoMapper.toRegion(dto);
                em.persist(entity);
            }
            log.info("✅ Saved {} regions", regions.size());

            // 🔹 PROVINCES
            String jsonProvinces = client.getProvinces(iso);
            ProvinceResponse provinceResponse = gson.fromJson(jsonProvinces, ProvinceResponse.class);
            List<ProvinceDto> provinces = provinceResponse.getData();

            if (provinces != null && !provinces.isEmpty()) {
                for (ProvinceDto dto : provinces) {
                    Province entity = DtoMapper.toProvince(dto);
                    em.persist(entity);
                }
                log.info("✅ Saved {} provinces", provinces.size());
            } else {
                log.warn("❌ No provinces found for the specified region.");
            }

            // 🔹 REPORTS
            String jsonReports = client.getReport(iso, date);
            ReportResponse reportResponse = gson.fromJson(jsonReports, ReportResponse.class);
            List<ReportDto> reports = reportResponse.getData();

            if (reports != null && !reports.isEmpty()) {
                for (ReportDto dto : reports) {
                    Report entity = DtoMapper.toReport(dto);
                    em.persist(entity);
                }
                log.info("✅ Saved {} reports", reports.size());
            } else {
                log.warn("❌ No reports found for the specified region and date.");
            }

            saveExecutionRecord(iso, date, em);
            log.info("✅ {} on {} was processed and recorded.", iso, date);

            tx.commit();

        } catch (Exception e) {
            log.error("❌ Error during COVID API processing: {}", e.getMessage(), e);
            if (tx.isActive()) tx.rollback();
        } finally {
            em.close();
        }
    }

    public boolean hasAlreadyExecuted(String countryIso, String date, EntityManager em) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(e) FROM ExecutedReport e WHERE e.countryIso = :iso AND e.executionDate = :date",
            Long.class
        );
        query.setParameter("iso", countryIso);
        query.setParameter("date", LocalDate.parse(date));
        return query.getSingleResult() > 0;
    }
    
    public void saveExecutionRecord(String countryIso, String date, EntityManager em) {
        ExecutedReport record = new ExecutedReport();
        record.setCountryIso(countryIso);
        record.setExecutionDate(LocalDate.parse(date));
        em.persist(record);
    }
    
    public Map<String, Report> getReportsGroupedByProvince(String iso, String date) {
        Map<String, Report> reportMap = new TreeMap<>(); // orden alfabético por provincia
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();

        try {
            // Consulta todos los reportes por país y fecha
            TypedQuery<Report> query = em.createQuery(
                "SELECT r FROM Report r WHERE r.region = :iso AND r.date = :date", Report.class);
            query.setParameter("iso", iso);
            query.setParameter("date", date);

            List<Report> reports = query.getResultList();

            for (Report report : reports) {
                String province = report.getProvince();
                if (!reportMap.containsKey(province)) {
                    reportMap.put(province, report); // se guarda solo uno por provincia
                }
            }

            // Mostrar en consola
            System.out.println(" Reportes únicos por provincia (" + iso + " en " + date + "):");
            for (Map.Entry<String, Report> entry : reportMap.entrySet()) {
                System.out.println(entry.getKey() + "  Confirmados: " + entry.getValue().getConfirmed());
            }

        } catch (Exception e) {
            System.err.println("❌ Error al consultar reportes: " + e.getMessage());
        } finally {
            em.close();
        }

        return reportMap;
    }


}
