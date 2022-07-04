package mk.ukim.finki.covid19_statistics.repository;

import mk.ukim.finki.covid19_statistics.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findBySsn(Long ssn);
    List<Patient> findByNameAndSurname(String name, String surname);
    Patient findByUhid(Long uhid);
    @Query("SELECT p from Patient p where p.name like %?1%")
    List<Patient> findAllByNameLike(String name);
    @Query("SELECT p from Patient p where p.surname like %?1%")
    List<Patient> findAllBySurnameLike(String surname);
    @Query("SELECT p from Patient p where p.name like %?1% and p.surname like %?2%")
    List<Patient> findAllByNameLikeAndAndSurnameLike(String name, String surname);
    Patient findBySsnAndNameAndSurname(Long ssn,String name, String surname);
}
