package mk.ukim.finki.covid19_statistics.repository;

import mk.ukim.finki.covid19_statistics.model.Diagnosis;
import mk.ukim.finki.covid19_statistics.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findAllByDiagnosis(Diagnosis diagnosis);
}
