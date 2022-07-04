package mk.ukim.finki.covid19_statistics.service;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


import mk.ukim.finki.covid19_statistics.model.Receipt;
import mk.ukim.finki.covid19_statistics.model.Visit;

import javax.servlet.http.HttpServletResponse;

public interface ReceiptService {
    List<Receipt> findAll();
    Receipt findById(Long id);
    Receipt create(String therapy, LocalDateTime date, String patientName,
                   String patientSurname, String doctorName, String doctorSurname,
                   String doctorSpecialty, String diagnoseName, Long diagnoseId);
    Receipt edit(Long id, String therapy, LocalDateTime date, String patientName, String patientSurname, String doctorName,
                 String doctorSurname, String doctorSpecialty, String diagnoseName, Long diagnoseId);
    Receipt delete(Long id);
    List<Receipt> filter(Long ssn);
    void export(HttpServletResponse response, Long idReceipt, List<Visit> visits) throws IOException;
}
