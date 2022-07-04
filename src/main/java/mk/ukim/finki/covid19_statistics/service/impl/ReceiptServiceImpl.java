package mk.ukim.finki.covid19_statistics.service.impl;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import mk.ukim.finki.covid19_statistics.model.Diagnosis;
import mk.ukim.finki.covid19_statistics.model.Receipt;
import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.DiagnosisNotFoundException;

import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.covid19_statistics.model.exceptions.NothingFoundException;
import mk.ukim.finki.covid19_statistics.model.exceptions.ReceiptNotFoundException;
import mk.ukim.finki.covid19_statistics.repository.DiagnosisRepository;
import mk.ukim.finki.covid19_statistics.repository.ReceiptRepository;
import mk.ukim.finki.covid19_statistics.repository.VisitRepository;
import mk.ukim.finki.covid19_statistics.service.ReceiptService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final DiagnosisRepository diagnosisRepository;
    private final ReceiptRepository receiptRepository;
    private final VisitRepository visitRepository;

    public ReceiptServiceImpl(DiagnosisRepository diagnosisRepository, ReceiptRepository receiptRepository, VisitRepository visitRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.receiptRepository = receiptRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    public List<Receipt> findAll() {
        return this.receiptRepository.findAll();
    }

    @Override
    public Receipt findById(Long id) {
        return this.receiptRepository.findById(id).orElseThrow(()-> new ReceiptNotFoundException());
    }

    @Override
    public Receipt create(String therapy, LocalDateTime date, String patientName, String patientSurname, String doctorName, String doctorSurname, String doctorSpecialty, String diagnoseName, Long diagnoseId) {
        if(therapy.isEmpty() || therapy == null){
            throw new InvalidArgumentException();
        }
        Diagnosis diagnose = this.diagnosisRepository.findByDiagnoseId(diagnoseId);
        Receipt receipt = new Receipt(therapy, diagnose);
        this.receiptRepository.save(receipt);
        return receipt;
    }


    @Override
    public Receipt edit(Long id, String therapy, LocalDateTime date, String patientName, String patientSurname, String doctorName,
                        String doctorSurname, String doctorSpecialty, String diagnoseName, Long diagnoseId) {
        Receipt receipt = this.receiptRepository.findById(id).orElseThrow(() -> new ReceiptNotFoundException());
        receipt.setTherapy(therapy);
        Diagnosis d = this.diagnosisRepository.findById(diagnoseId).orElseThrow(()-> new DiagnosisNotFoundException());
        receipt.setDiagnosis(d);
        this.receiptRepository.save(receipt);
        return receipt;
    }


    @Override
    public Receipt delete(Long id) {
        Receipt r = this.receiptRepository.findById(id).orElseThrow(()-> new DiagnosisNotFoundException());
        this.receiptRepository.delete(r);
        return r;
    }

    @Override
    public List<Receipt> filter(Long ssn) {
        if(ssn == null){
            return this.receiptRepository.findAll();
        }
        else{
            List<Visit> visits = this.visitRepository.findByPatientSsn(ssn);
            List<Receipt> receipts = null;
            for (Visit visit: visits ) {
               receipts = this.receiptRepository.findAll().stream().filter(i->i.getDiagnosis().getVisits().contains(visit)).collect(Collectors.toList());
            }
            if(receipts == null) {
                throw new NothingFoundException();
            }
            return receipts;
        }
    }


    @Override
    public void export(HttpServletResponse response, Long idReceipt, List<Visit> visits) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();

        String patientNameAndSurname = null;
        LocalDateTime term = null;
        String doctorNameAndSurname = null;
        String specialty = null;
        String therapy = this.receiptRepository.findById(idReceipt).get().getTherapy();
        String diagnoseName = this.receiptRepository.findById(idReceipt).get().getDiagnosis().getName();
        for(int i = 0; i < visits.size(); i++){
            if(this.receiptRepository.findById(idReceipt).get().getDiagnosis().getVisits().contains(visits.get(i))){
                patientNameAndSurname = visits.get(i).getPatient().getNameAndSurname();
                term = visits.get(i).getTerm();
                doctorNameAndSurname = visits.get(i).getDoctor().getNameAndSurname();
                specialty = visits.get(i).getDoctor().getSpecialty().getName();
            }
        }

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String termVisit = term.format(format);







        Paragraph paragraph2 = new Paragraph("Информации за рецептот: ");
        Paragraph paragraph7 = new Paragraph("Терапија:" + therapy);
        Paragraph paragraph = new Paragraph("Пациент: " + patientNameAndSurname);
        Paragraph paragraph3 = new Paragraph("Термин: " + termVisit);
        Paragraph paragraph4 = new Paragraph("Доктор: " + doctorNameAndSurname);
        Paragraph paragraph5 = new Paragraph("Специјалност на доктор: " + specialty);
        Paragraph paragraph6 = new Paragraph("Дијагноза: " + diagnoseName);


        paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph2.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph4.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph6.setAlignment(Paragraph.ALIGN_LEFT);



        paragraph2.setSpacingAfter(15);
        document.add(paragraph2);
        document.add(paragraph7);
        document.add(paragraph);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);

        document.close();
    }
}
