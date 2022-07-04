package mk.ukim.finki.covid19_statistics.service;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Patient;
import mk.ukim.finki.covid19_statistics.model.Visit;
import java.util.*;
import java.time.LocalDateTime;

public interface VisitService {
    List<Visit> findByPatientSsn(Long patientSsn);
    List<Visit> findByPatientUhid(Long patientUhid);
    List<Visit> findByDoctorSsn(Long doctorSsn);
    Visit findByTerm(LocalDateTime term);
    List<Visit> findByPatientSsnAndDoctorSsn(Long patientSsn, Long doctorSsn);
    List<Visit> findAll();
    List<Visit> filterTwo(Long patientSsn);
    List<Visit> filter(Long patientSsn, Long doctorSssn);
    Visit create(LocalDateTime term, String patientName, String patientSurname, Long patientSsn,
                 Long doctorSsn);
    Visit delete(Long id);
    Visit findById(Long id);
    Visit edit(Long id,LocalDateTime term, String patientName, String patientSurname, Long patientSsn, Long doctorSsn);
    Visit edit2(Long id,LocalDateTime term, String patientName, String patientSurname, Long patientSsn, Long doctorSsn);
}
