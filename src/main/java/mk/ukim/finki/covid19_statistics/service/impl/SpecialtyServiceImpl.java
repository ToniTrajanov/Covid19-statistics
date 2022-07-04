package mk.ukim.finki.covid19_statistics.service.impl;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Specialty;
import mk.ukim.finki.covid19_statistics.model.exceptions.CannotDeleteSpecialtyException;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.covid19_statistics.model.exceptions.SpecialtyAlreadyExistsException;
import mk.ukim.finki.covid19_statistics.model.exceptions.SpecialtyNotFoundException;
import mk.ukim.finki.covid19_statistics.repository.DoctorRepository;
import mk.ukim.finki.covid19_statistics.repository.SpecialtyRepository;
import mk.ukim.finki.covid19_statistics.service.SpecialtyService;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Arrays;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final DoctorRepository doctorRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, DoctorRepository doctorRepository) {
        this.specialtyRepository = specialtyRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Specialty> findAll() {
        return specialtyRepository.findAll();
    }

    @Override
    public Specialty findByName(String specialtyName) {
        return specialtyRepository.findByName(specialtyName);
    }

    @Override
    public Specialty findById(Long id) {
        return this.specialtyRepository.findById(id).orElseThrow(()-> new SpecialtyNotFoundException());
    }

    @Override
    public Specialty deleteById(Long id) {
        Specialty specialty = this.specialtyRepository.findById(id).orElseThrow(()-> new SpecialtyNotFoundException());
        List<Doctor> doctors = this.doctorRepository.findBySpecialty(specialty);
        if(doctors.size() > 0){
            throw new CannotDeleteSpecialtyException();
        }
        else
        this.specialtyRepository.deleteById(id);
        return specialty;
    }

    @Override
    public Specialty save(String specialtyName) {
        if (specialtyName == null || specialtyName.isEmpty()){
            throw new InvalidArgumentException();
        }
        if(specialtyRepository.findByName(specialtyName) != null){
            throw new SpecialtyAlreadyExistsException(specialtyName);
        }
        Specialty s = new Specialty(specialtyName);
        specialtyRepository.save(s);
        return s;
    }

    @Override
    public Specialty delete(String specialtyName) {
        if (specialtyName == null || specialtyName.isEmpty()){
            throw new IllegalArgumentException();
        }
        Specialty specialty = this.specialtyRepository.findByName(specialtyName);
        List<Doctor> doctors = this.doctorRepository.findBySpecialty(specialty);
        if(doctors.size() > 0){
            throw new CannotDeleteSpecialtyException();
        }
        return specialtyRepository.deleteByName(specialtyName);
    }
}
