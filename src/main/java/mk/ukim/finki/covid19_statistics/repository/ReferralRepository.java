package mk.ukim.finki.covid19_statistics.repository;

import mk.ukim.finki.covid19_statistics.model.Patient;
import mk.ukim.finki.covid19_statistics.model.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
    @Query("SELECT r from Referral r where r.id = ?1")
    Referral findByIdd(Long id);
    List<Referral> findBySsnPatient(Patient patient);
}
