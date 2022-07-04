package mk.ukim.finki.covid19_statistics.service.impl;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Specialty;
import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.*;
import mk.ukim.finki.covid19_statistics.repository.DoctorRepository;
import mk.ukim.finki.covid19_statistics.repository.SpecialtyRepository;
import mk.ukim.finki.covid19_statistics.repository.VisitRepository;
import mk.ukim.finki.covid19_statistics.service.DoctorService;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final VisitRepository visitRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, SpecialtyRepository specialtyRepository, VisitRepository visitRepository) {
        this.doctorRepository = doctorRepository;
        this.specialtyRepository = specialtyRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findBySsn(Long ssn) {
        Doctor doctor = this.doctorRepository.findBySsn(ssn);
        return doctor;
    }

    @Override
    public Doctor findByLicenceNumber(Integer licenceNumber) {
        return findByLicenceNumber(licenceNumber);
    }

    @Override
    public Doctor findByNameAndSurname(String name, String surname) {
        return findByNameAndSurname(name, surname);
    }

    @Override
    public List<Doctor> findBySpecialty(String specialtyName) {
        if (specialtyName == null || specialtyName.isEmpty()){
            throw new InvalidArgumentException();
        }
        Specialty specialty = this.specialtyRepository.findByNameIsLike(specialtyName);
        return doctorRepository.findBySpecialty(specialty);
    }

    @Override
    public Doctor create(Long ssn, String name, String surname, Integer licenseNumber, String specialtyName) {
        if (ssn == null || name == null || name.isEmpty() || surname == null || surname.isEmpty()
            || licenseNumber == null || specialtyName == null || specialtyName.isEmpty()) {
            throw new InvalidArgumentException();
        }
        else if(doctorRepository.findBySsn(ssn) != null){
            throw new DoctorWithSsnAlreadyExistsException();
        }
        else if(doctorRepository.findByLicenceNumber(licenseNumber) != null){
            throw new LicenceNumberAlreadyExistsException();
        }
//        else if (specialtyRepository.findByName(specialtyName) == null) {
//            Specialty s = new Specialty(specialtyName);
//            specialtyRepository.save(s);
//            Doctor d = new Doctor(ssn, name, surname, licenseNumber, s);
//            doctorRepository.save(d);
//            return d;
//        }
        else{
            Specialty s = specialtyRepository.findByName(specialtyName);
            Doctor d = new Doctor(ssn, name, surname, licenseNumber, s);
            doctorRepository.save(d);
            return d;
        }
    }


    @Override
    @Transactional
    public Doctor deleteBySsn(Long ssn) {
        Doctor d = this.doctorRepository.findBySsn(ssn);
        List<Visit> visit = this.visitRepository.findByDoctor(d);
        if(visit.size()>0){
            throw new DoctorCannotBeDeletedException();
        }else
        this.doctorRepository.delete(d);
        return d;
    }


    @Override
    @Transactional
    public Doctor edit(Long editSsn, Long ssn, String name, String surname, Integer licenseNumber, String specialtyName) {
        if (ssn == null || name == null || name.isEmpty() || surname == null || surname.isEmpty() || licenseNumber == null
        || specialtyName == null || specialtyName.isEmpty()) {
            throw new WrongDataEnteredException();
        }
        else {
            Doctor doctor = this.doctorRepository.findBySsn(editSsn);
            Specialty specialty = this.specialtyRepository.findByName(specialtyName);
            doctor.setSsn(ssn);
            doctor.setName(name);
            doctor.setSurname(surname);
            doctor.setLicenceNumber(licenseNumber);
            doctor.setSpecialty(specialty);
            this.doctorRepository.save(doctor);
            return doctor;
        }
    }

    @Override
    public List<Doctor> filter( String specialty) {

        if(specialty == null || specialty.isEmpty()){
            return this.doctorRepository.findAll();
        }
        else{
            Specialty specialty1 = this.specialtyRepository.findByNameIsLike(specialty);
            return this.doctorRepository.findBySpecialty(specialty1);
        }
            }

    @Override
    public List<Doctor> filterByNameAndSurname(String doctorName, String doctorSurname) {
        if((doctorName == null || doctorName.isEmpty())  && (doctorSurname == null || doctorSurname.isEmpty())) {
            return this.doctorRepository.findAll();
        }
        else if((doctorName != null || doctorName.isEmpty()) && (doctorSurname == null || doctorSurname.isEmpty())){
            return this.doctorRepository.findByNameIsLike(doctorName);
        }
        else if((doctorName == null || doctorName.isEmpty()) && (doctorSurname != null || doctorSurname.isEmpty())){
            return this.doctorRepository.findBySurnameIsLike(doctorSurname);
        }
        else{
            return this.doctorRepository.findByNameIsLikeAndSurnameIsLike(doctorName,doctorSurname);
        }
    }
}
