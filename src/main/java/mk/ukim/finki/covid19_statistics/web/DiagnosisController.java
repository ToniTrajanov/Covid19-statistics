package mk.ukim.finki.covid19_statistics.web;

import mk.ukim.finki.covid19_statistics.model.Diagnosis;

import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.CannotDeleteDiagnosisException;
import mk.ukim.finki.covid19_statistics.service.DiagnosisService;
import mk.ukim.finki.covid19_statistics.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.*;
@Controller
@RequestMapping("/diagnoses")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;
    private final VisitService visitService;

    public DiagnosisController(DiagnosisService diagnosisService, VisitService visitService) {
        this.diagnosisService = diagnosisService;
        this.visitService = visitService;

    }
    @GetMapping
    public String getDiagnosisPage(@RequestParam(required = false) String diagnoseName,
                                   @RequestParam(required = false) String error,
                                   Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Visit> visits = this.visitService.findAll();
        List<Diagnosis> diagnoses;
        if(diagnoseName == null || diagnoseName.isEmpty()) {
            diagnoses = this.diagnosisService.findAll();
        }
        else
        {
            diagnoses = this.diagnosisService.filter(diagnoseName);
        }
            model.addAttribute("visits", visits);
        model.addAttribute("diagnoses", diagnoses);
        model.addAttribute("bodyContent", "diagnosis");

        return "master-template";
    }

    @GetMapping("/add-form")
    public String getAddForm(Model model, @RequestParam(required = false)String error){
        if(error!=null && !error.isEmpty()){
            model.addAttribute("hasError",true);
            model.addAttribute("error",error);
        }

        LocalDateTime dateTime = LocalDateTime.now();
        List<Visit> visits = this.visitService.findAll();
        model.addAttribute("visits", visits);
        model.addAttribute("date", dateTime);
        model.addAttribute("bodyContent", "add-diagnosis");

        return "master-template";
    }

    @PostMapping("/add")
    public String addDiagnose(@RequestParam(required = false) Long id,
                              @RequestParam String diagnoseName,
                              @RequestParam String termOfVisit,
                              @RequestParam String diagnoseDescription){
        if(id != null){
            this.diagnosisService.edit(id, diagnoseName, LocalDateTime.parse(termOfVisit), diagnoseDescription);
        }
        else {
            this.diagnosisService.create(diagnoseName, LocalDateTime.parse(termOfVisit), diagnoseDescription);
        }
        return "redirect:/diagnoses";
    }
    @GetMapping("/{id}/edit-form")
    public String editDiagnose(@PathVariable Long id, Model model){
        Diagnosis diagnose = this.diagnosisService.findByDiagnoseId(id);
        List<Visit> visits = this.visitService.findAll();
        LocalDateTime dateTime = LocalDateTime.now();
        model.addAttribute("diagnose", diagnose);
        model.addAttribute("date", dateTime);
        model.addAttribute("visits", visits);
        model.addAttribute("bodyContent", "add-diagnosis");

        return "master-template";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteDiagnosis(@PathVariable Long id) {
        try {
            this.diagnosisService.delete(id);
            return "redirect:/diagnoses";
        } catch (CannotDeleteDiagnosisException exception){
            return "redirect:/diagnoses?error=" + exception.getMessage();
        }

    }
}
