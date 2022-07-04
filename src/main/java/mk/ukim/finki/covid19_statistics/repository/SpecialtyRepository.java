package mk.ukim.finki.covid19_statistics.repository;

import mk.ukim.finki.covid19_statistics.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    @Query("SELECT s from Specialty s where s.name like %?1%")
    Specialty findByNameIsLike(String specialtyName);
    Specialty findByName(String specialtyName);
    Specialty deleteByName(String specialtyName);
}
