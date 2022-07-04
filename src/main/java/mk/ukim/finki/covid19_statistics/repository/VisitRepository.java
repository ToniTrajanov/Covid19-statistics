package mk.ukim.finki.covid19_statistics.repository;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientSsn(Long patientSsn);
    List<Visit> findByPatientUhid(Long patientUhid);
    List<Visit> findByDoctorSsn(Long doctorSsn);
    Visit findByTerm(LocalDateTime term);
    List<Visit> findByPatientSsnAndDoctorSsn(Long patientSsn, Long doctorSsn);
    List<Visit> findByPatientName(String name);
    List<Visit> findByDoctor(Doctor doctor);
}
