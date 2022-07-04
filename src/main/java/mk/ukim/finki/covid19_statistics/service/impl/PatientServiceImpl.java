package mk.ukim.finki.covid19_statistics.service.impl;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Patient;
import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.CannotDeletePatientException;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.covid19_statistics.model.exceptions.PatientAlreadyExistsException;
import mk.ukim.finki.covid19_statistics.model.exceptions.UhidAlreadyExistsException;
import mk.ukim.finki.covid19_statistics.repository.DoctorRepository;
import mk.ukim.finki.covid19_statistics.repository.PatientRepository;
import mk.ukim.finki.covid19_statistics.repository.VisitRepository;
import mk.ukim.finki.covid19_statistics.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final VisitRepository visitRepository;

    public PatientServiceImpl(PatientRepository patientRepository, DoctorRepository doctorRepository, VisitRepository visitRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    public Patient create(Long ssn, String name, String surname, Long uhid, Long doctorEmbg) {
        if (ssn == null || name == null || name.isEmpty() || surname == null || surname.isEmpty()
                || uhid == null || doctorEmbg == null) {
            throw new InvalidArgumentException();
        }
        else if(patientRepository.findBySsn(ssn) != null){
            throw new PatientAlreadyExistsException();
        }
        else if(patientRepository.findByUhid(uhid) != null){
            throw new UhidAlreadyExistsException();
        }
        else {
            Doctor d = this.doctorRepository.findBySsn(doctorEmbg);
            Patient p = new Patient(ssn, name, surname, uhid, d);
            this.patientRepository.save(p);
            return p;
        }
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient findBySsn(Long ssn) {
        return this.patientRepository.findBySsn(ssn);
    }

    @Override
    public List<Patient> findPatient(Long ssn, String name, String surname) {
        if (ssn != null && name == null && surname == null)
            return Arrays.asList(patientRepository.findBySsn(ssn));
        if (ssn == null && name != null && surname != null)
            return patientRepository.findByNameAndSurname(name, surname);
        if (ssn != null && name != null && surname != null)
            return Arrays.asList(patientRepository.findBySsnAndNameAndSurname(ssn, name, surname));
        else
            return patientRepository.findAll();
    }

    @Override
    public Patient findByUhid(Long uhid) {
        return patientRepository.findByUhid(uhid);
    }



    @Override
    public List<Patient> filter(String name, String surname) {
        if((name == null || name.isEmpty()) && (surname == null || surname.isEmpty()))
        {
            return this.patientRepository.findAll();
        }
        else if((name != null || !name.isEmpty()) && (surname == null || surname.isEmpty()))
        {
            return this.patientRepository.findAllByNameLike(name);
        }
        else if((name == null || name.isEmpty()) && (surname != null || !surname.isEmpty()))
        {
            return this.patientRepository.findAllBySurnameLike(surname);
        }
        else
        return this.patientRepository.findAllByNameLikeAndAndSurnameLike(name,surname);
    }

    @Override
    public Patient delete(Long ssn) {
        Patient patient = this.patientRepository.findBySsn(ssn);
        List<Visit> visits = this.visitRepository.findByPatientSsn(ssn);

        if(visits.size() > 0){
            throw new CannotDeletePatientException();
        }
        else
        this.patientRepository.delete(patient);
        return patient;
    }
}
