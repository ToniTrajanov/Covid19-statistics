package mk.ukim.finki.covid19_statistics.web;

import mk.ukim.finki.covid19_statistics.model.Diagnosis;
import mk.ukim.finki.covid19_statistics.model.Receipt;
import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.DiagnosisNotFoundException;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.covid19_statistics.model.exceptions.NothingFoundException;
import mk.ukim.finki.covid19_statistics.service.DiagnosisService;
import mk.ukim.finki.covid19_statistics.service.ReceiptService;
import mk.ukim.finki.covid19_statistics.service.VisitService;
import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.util.*;


@Controller
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;
    private final DiagnosisService diagnosisService;
    private final VisitService visitService;

    public ReceiptController(ReceiptService receiptService, DiagnosisService diagnosisService, VisitService visitService) {
        this.receiptService = receiptService;
        this.diagnosisService = diagnosisService;
        this.visitService = visitService;
    }

    @GetMapping
    public String viewReceipts(Model model,@RequestParam(required = false) String error, @RequestParam(required = false) Long patientSsn){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Receipt> receiptList;
        List<Visit> visits = this.visitService.findAll();
        List<Diagnosis> diagnoses = this.diagnosisService.findAll();

        if(patientSsn == null){
            receiptList = this.receiptService.findAll();
        }
        else{
            try {
                receiptList = this.receiptService.filter(patientSsn);
            } catch (NothingFoundException exception){
                return "redirect:/receipts?error=" + exception.getMessage();
            }
        }


        model.addAttribute("receipts", receiptList);
        model.addAttribute("diagnose", diagnoses);
        model.addAttribute("visits", visits);
        model.addAttribute("bodyContent", "receipts");

        return "master-template";
    }
    @GetMapping("/{id}/add-form")
    public String addReceiptPage(Model model, @RequestParam(required = false) String error, @PathVariable Long id)
    {
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        Diagnosis diagnose = this.diagnosisService.findByDiagnoseId(id);
        List<Receipt> receipts = this.receiptService.findAll();
        List<Visit> visits = this.visitService.findAll();
        model.addAttribute("receipts", receipts);
        model.addAttribute("diagnose", diagnose);
        model.addAttribute("visits", visits);

        model.addAttribute("bodyContent", "add-receipt");
        return "master-template";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addReceipt(
            @RequestParam(required = false) Long id,
            @RequestParam String therapy,
            @RequestParam String date,
            @RequestParam String patientName,
            @RequestParam String patientSurname,
            @RequestParam String doctorName,
            @RequestParam String doctorSurname,
            @RequestParam String doctorSpecialty,
            @RequestParam String diagnoseName,
            @RequestParam Long diagnoseId) {
        if(id == null) {
            try {
                this.receiptService.create(therapy, LocalDateTime.parse(date), patientName, patientSurname,
                        doctorName, doctorSurname, doctorSpecialty, diagnoseName, diagnoseId);
                return "redirect:/receipts";
            } catch (DiagnosisNotFoundException exception) {
                return "redirect:/receipts?error=" + exception.getMessage();
            }
        }
        else{
            try{
                this.receiptService.edit(id,therapy, LocalDateTime.parse(date), patientName, patientSurname,
                        doctorName, doctorSurname, doctorSpecialty, diagnoseName, diagnoseId);
                return "redirect:/receipts";
            }catch (DiagnosisNotFoundException exception) {
                return "redirect:/receipts?error=" + exception.getMessage();
            }
        }
    }

    @GetMapping("/{id}/edit-form")
    public String editDiagnose(@PathVariable Long id, Model model){
        Receipt receipt = this.receiptService.findById(id);
        Long idDiagnose = this.receiptService.findById(id).getDiagnosis().getDiagnoseId();
        Diagnosis diagnosis = this.diagnosisService.findByDiagnoseId(idDiagnose);
        List<Visit> visits = this.visitService.findAll();
        LocalDateTime dateTime = LocalDateTime.now();
        model.addAttribute("receipt", receipt);
        model.addAttribute("diagnose", diagnosis);
        model.addAttribute("date", dateTime);
        model.addAttribute("visits", visits);
        model.addAttribute("bodyContent", "add-receipt");

        return "master-template";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteReceipt(@PathVariable Long id) {
        this.receiptService.delete(id);
        return "redirect:/receipts";
    }

    @GetMapping("/{id}/generate/pdf")
    public void generatePDF(HttpServletResponse response, @PathVariable Long id) throws IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        List<Visit> visits = this.visitService.findAll();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Recept_" + currentDateTime +".pdf";
        response.setHeader(headerKey, headerValue);

        this.receiptService.export(response, id, visits);
    }

}
