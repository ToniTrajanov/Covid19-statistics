package mk.ukim.finki.covid19_statistics.service;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Specialty;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<Doctor> findAll();
    Doctor findBySsn(Long ssn);
    Doctor findByLicenceNumber(Integer licenceNumber);
    Doctor findByNameAndSurname(String name, String surname);
    List<Doctor> findBySpecialty(String specialty);
    Doctor create(Long ssn, String name, String surname, Integer licenseNumber, String specialtyName);
    Doctor deleteBySsn(Long ssn);
    Doctor edit(Long editSsn, Long ssn, String name, String surname, Integer licenseNumber, String specialtyName);
    List<Doctor> filter(String specialty);
    List<Doctor> filterByNameAndSurname(String doctorName, String doctorSurname);
}
