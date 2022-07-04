package mk.ukim.finki.covid19_statistics.service.impl;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Patient;
import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.*;
import mk.ukim.finki.covid19_statistics.repository.*;
import mk.ukim.finki.covid19_statistics.service.VisitService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

import java.time.LocalDateTime;

@Service
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ReferralRepository referralRepository;
    private final DiagnosisRepository diagnosisRepository;

    public VisitServiceImpl(VisitRepository visitRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, ReferralRepository referralRepository, DiagnosisRepository diagnosisRepository) {
        this.visitRepository = visitRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.referralRepository = referralRepository;
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public List<Visit> findByPatientSsn(Long patientSsn) {
        return visitRepository.findByPatientSsn(patientSsn);
    }

    @Override
    public List<Visit> findByPatientUhid(Long patientUhid) {
        return visitRepository.findByPatientUhid(patientUhid);
    }

    @Override
    public List<Visit> findByDoctorSsn(Long doctorSsn) {
        return visitRepository.findByDoctorSsn(doctorSsn);
    }

    @Override
    public Visit findByTerm(LocalDateTime term) {
        return visitRepository.findByTerm(term);
    }

    @Override
    public List<Visit> findByPatientSsnAndDoctorSsn(Long patientSsn, Long doctorSsn) {
        return visitRepository.findByPatientSsnAndDoctorSsn(patientSsn, doctorSsn);
    }

    @Override
    public List<Visit> findAll() {
        return this.visitRepository.findAll();
    }

    @Override
    public List<Visit> filterTwo(Long patientSsn) {
        if(patientSsn == null){
            return this.visitRepository.findAll();
        }
        else{
            List<Visit> visits = this.visitRepository.findByPatientSsn(patientSsn);
            return visits;
        }
    }

    @Override
    public List<Visit> filter(Long patientSsn, Long doctorSsn) {
        if(patientSsn != null && doctorSsn != null){
            List<Visit> visits = this.visitRepository.findByPatientSsnAndDoctorSsn(patientSsn,doctorSsn);
            return visits;
        }
        else if(patientSsn != null && doctorSsn == null){
            List<Visit> visits = this.visitRepository.findByPatientSsn(patientSsn);
            return visits;
        }
        else if(patientSsn == null && doctorSsn != null){
            List<Visit> visits = this.visitRepository.findByDoctorSsn(doctorSsn);
            return visits;
        }
        else
        return this.visitRepository.findAll();
    }

    @Override
    public Visit create(LocalDateTime term, String patientName, String patientSurname, Long patientSsn,
                        Long doctorSsn) {

        if (term == null || patientSsn == null  || patientName == null || patientName.isEmpty() ||
            patientSurname == null || patientSurname.isEmpty() || doctorSsn == null || doctorSsn == 0) {
            throw new InvalidArgumentException();
        }

        if (visitRepository.findByTerm(term) != null && visitRepository.findByDoctorSsn(doctorSsn)!= null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
            String formatDateTime = term.format(formatter);
            throw new TermIsNotAvailableException(formatDateTime);
        }
        if(!term.isAfter(LocalDateTime.now()) && !term.isEqual(LocalDateTime.now())){
            throw new TermIsNotAllowedException();
        }
        if(patientRepository.findBySsnAndNameAndSurname(patientSsn,patientName,patientSurname) == null){
            throw new PatientDoesNotExistException();
        }




        Patient p = patientRepository.findBySsn(patientSsn);
        Doctor d = this.doctorRepository.findBySsn(doctorSsn);
        Visit v = new Visit(term, d, p);
        visitRepository.save(v);
        return v;
    }

    @Override
    public Visit delete(Long id) {
        Visit visit = this.visitRepository.findById(id).orElseThrow(VisitNotFoundException::new);
        LocalDateTime visitTerm = visit.getTerm();
        if(this.diagnosisRepository.findAll().stream()
                .anyMatch(i -> i.getVisits().contains(visit))){
            throw new CannotDeleteVisitWithDiagnosisException();
        }
        if(this.referralRepository.findAll().stream()
                .anyMatch(i -> i.getTerm().isEqual(visitTerm))){
            throw new CannotDeleteVisitException();
        }
        else
        this.visitRepository.delete(visit);
        return visit;
    }

    @Override
    public Visit findById(Long id) {
        return this.visitRepository.findById(id).orElseThrow(()-> new VisitNotFoundException());
    }

    @Override
    public Visit edit(Long id, LocalDateTime term, String patientName, String patientSurname, Long patientSsn, Long doctorSsn) {
        Visit visit = this.visitRepository.findById(id).orElseThrow(() -> new VisitNotFoundException());
        if (term == null || patientSsn == null  || patientName == null || patientName.isEmpty() ||
                patientSurname == null || patientSurname.isEmpty() || doctorSsn == null || doctorSsn == 0) {
            throw new InvalidArgumentException();
        }
        if(referralRepository.findAll().stream().anyMatch(i->i.getTerm().isEqual(visit.getTerm()))){
            throw new CannotEditVisitException();
        }
        if(patientRepository.findBySsnAndNameAndSurname(patientSsn,patientName,patientSurname) == null){
            throw new PatientDoesNotExistException();
        }
        Patient patient = this.patientRepository.findBySsn(patientSsn);
        if(term.isBefore(LocalDateTime.now())){
            throw new TermIsNotAllowedException();
        }
        if(diagnosisRepository.findAll().stream().anyMatch(i->i.getVisits().contains(visit)))
        {
            if(term.isAfter(visit.getTerm())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formatDateTime = term.format(formatter);
                throw new TermIsNotAvailableException(formatDateTime);
            }
        }


        visit.setTerm(term);
        visit.setPatient(patient);
        Doctor doctor = this.doctorRepository.findBySsn(doctorSsn);
        visit.setDoctor(doctor);
        this.visitRepository.save(visit);
        return visit;
    }

    @Override
    public Visit edit2(Long id, LocalDateTime term, String patientName, String patientSurname, Long patientSsn, Long doctorSsn) {
        Visit visit = this.visitRepository.findById(id).orElseThrow(() -> new VisitNotFoundException());
        if (term == null || patientSsn == null  || patientName == null || patientName.isEmpty() ||
                patientSurname == null || patientSurname.isEmpty() || doctorSsn == null || doctorSsn == 0) {
            throw new InvalidArgumentException();
        }
        if(patientRepository.findBySsnAndNameAndSurname(patientSsn,patientName,patientSurname) == null){
            throw new PatientDoesNotExistException();
        }
        Patient patient = this.patientRepository.findBySsn(patientSsn);
        if(term.isBefore(LocalDateTime.now())){
            throw new TermIsNotAllowedException();
        }
        if(diagnosisRepository.findAll().stream().anyMatch(i->i.getVisits().contains(visit)))
        {
            if(term.isAfter(visit.getTerm())) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formatDateTime = term.format(formatter);
                throw new TermIsNotAvailableException(formatDateTime);
            }
        }


        visit.setTerm(term);
        visit.setPatient(patient);
        Doctor doctor = this.doctorRepository.findBySsn(doctorSsn);
        visit.setDoctor(doctor);
        this.visitRepository.save(visit);
        return visit;
    }

}
