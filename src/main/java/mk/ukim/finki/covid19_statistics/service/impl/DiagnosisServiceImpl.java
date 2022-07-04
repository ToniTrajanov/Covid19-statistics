package mk.ukim.finki.covid19_statistics.service.impl;


import mk.ukim.finki.covid19_statistics.model.*;

import mk.ukim.finki.covid19_statistics.model.exceptions.CannotDeleteDiagnosisException;
import mk.ukim.finki.covid19_statistics.model.exceptions.VisitNotFoundException;
import mk.ukim.finki.covid19_statistics.repository.DiagnosisRepository;
import mk.ukim.finki.covid19_statistics.repository.ReceiptRepository;
import mk.ukim.finki.covid19_statistics.repository.VisitRepository;
import mk.ukim.finki.covid19_statistics.service.DiagnosisService;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final VisitRepository visitRepository;
    private final ReceiptRepository receiptRepository;

    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository, VisitRepository visitRepository, ReceiptRepository receiptRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.visitRepository = visitRepository;
        this.receiptRepository = receiptRepository;
    }

    @Override
    public List<Diagnosis> findAll() {
        return diagnosisRepository.findAll();
    }

    @Override
    public Diagnosis findByDiagnoseId(Long diagnoseId) {
        return diagnosisRepository.findByDiagnoseId(diagnoseId);
    }

    @Override
    public List<Diagnosis> findByName(String name) {
        return this.diagnosisRepository.findByName(name);
    }

    @Override
    public Diagnosis findByVisit(Long visitId) {
        Visit visit = this.visitRepository.findById(visitId).orElseThrow(()-> new VisitNotFoundException());
        return this.diagnosisRepository.findByVisits(visit);
    }




    @Override
    @Transactional
    public Diagnosis edit(Long id, String name, LocalDateTime term, String description) {
        Diagnosis diagnosis = this.diagnosisRepository.findByDiagnoseId(id);
        Visit visit = this.visitRepository.findByTerm(term);
        diagnosis.setVisits(visit);
        diagnosis.setName(name);
        diagnosis.setDescription(description);
        this.diagnosisRepository.save(diagnosis);
        return diagnosis;
    }


    @Override
    @Transactional
    public Diagnosis create(String name, LocalDateTime term, String description) {
        Visit visit = this.visitRepository.findByTerm(term);
        Diagnosis d = new Diagnosis(name, visit,description);
        this.diagnosisRepository.save(d);
        return d;
    }

    @Override
    public Diagnosis delete(Long id) {
        Diagnosis diagnosis = this.diagnosisRepository.findByDiagnoseId(id);
        List<Receipt> receipts = this.receiptRepository.findAllByDiagnosis(diagnosis);

        if(receipts.size() > 0){
            throw new CannotDeleteDiagnosisException();
        }

        this.diagnosisRepository.delete(diagnosis);
        return diagnosis;
    }

    @Override
    public List<Diagnosis> filter(String diagnoseName) {
        if(diagnoseName == null)
        {
            return this.diagnosisRepository.findAll();
        }
        else{
            return this.diagnosisRepository.findByName(diagnoseName);
        }

    }

}
