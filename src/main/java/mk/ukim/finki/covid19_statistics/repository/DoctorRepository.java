package mk.ukim.finki.covid19_statistics.repository;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findBySsn(Long ssn);
    Doctor findByLicenceNumber(Integer licenceNumber);
    @Query("SELECT d from Doctor d where d.name like %?1% and d.surname like %?2%")
    List<Doctor> findByNameIsLikeAndSurnameIsLike(String name, String surname);
    @Query("SELECT d from Doctor d where d.name like %?1%")
    List<Doctor> findByNameIsLike(String name);
    @Query("SELECT d from Doctor d where d.surname like %?1%")
    List<Doctor> findBySurnameIsLike(String surname);
    List<Doctor> findBySpecialty(Specialty specialty);
}
