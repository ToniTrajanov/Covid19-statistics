package mk.ukim.finki.covid19_statistics.web;


import mk.ukim.finki.covid19_statistics.model.*;
import mk.ukim.finki.covid19_statistics.model.exceptions.*;
import mk.ukim.finki.covid19_statistics.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping(value = {"/appointments"})
public class ViewAppointmentsController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final VisitService visitService;
    private final ReferralService referralService;
    private final DiagnosisService diagnosisService;

    public ViewAppointmentsController(PatientService patientService, DoctorService doctorService, VisitService visitService, ReferralService referralService, DiagnosisService diagnosisService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.visitService = visitService;
        this.referralService = referralService;
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public String getAppointmentPage(@RequestParam(required = false) Long patientSsn,
                                     @RequestParam(required = false) Long doctorSsn,
                                     Model model, @RequestParam(required = false) String error) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Doctor> doctorList = this.doctorService.findAll();
        List<Patient> patientList = this.patientService.findAll();
        List<Visit> visitList;
        List<Diagnosis> diagnoses = this.diagnosisService.findAll();
        List<Referral> referrals = this.referralService.findAll();

        if (patientSsn == null && doctorSsn == null) {
            visitList = this.visitService.findAll();
        } else {

            visitList = this.visitService.filter(patientSsn, doctorSsn);
        }
        model.addAttribute("doctors", doctorList);
        model.addAttribute("patients", patientList);
        model.addAttribute("visits", visitList);
        model.addAttribute("referrals", referrals);
        model.addAttribute("diagnosis", diagnoses);

        model.addAttribute("bodyContent", "appointment");
        return "master-template";
    }

    @GetMapping("/add-appointment-form")
    public String addAppointment(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Doctor> doctorList = this.doctorService.findAll();
        List<Patient> patientList = this.patientService.findAll();

        model.addAttribute("doctors", doctorList);
        model.addAttribute("patients", patientList);
        model.addAttribute("bodyContent", "add-appointment");
        return "master-template";
    }


    @PostMapping("/addByDoctor")
    public String makeAppointment(@RequestParam Long id,
                                  @RequestParam String name,
                                  @RequestParam String surname,
                                  @RequestParam Long ssn,
                                  @RequestParam String localDate,
                                  @RequestParam Long doctorSsn){
        try {
            this.visitService.edit(id,LocalDateTime.parse(localDate), name, surname, ssn, doctorSsn);
            return "redirect:/appointments";
        }catch(PatientDoesNotExistException |
                InvalidArgumentException | TermIsNotAllowedException | TermIsNotAvailableException | CannotEditVisitException exception) {
            return "redirect:/appointments?error="+ exception.getMessage();
        }
    }

    @GetMapping("/{id}/edit-form")
    public String editAppointment(@PathVariable Long id, Model model){
        Visit visit = this.visitService.findById(id);
        List<Patient> patients = this.patientService.findAll();
        List<Doctor> doctors = this.doctorService.findAll();
        LocalDateTime dateTime = LocalDateTime.now();
        model.addAttribute("date", dateTime);
        model.addAttribute("visit", visit);
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);
        model.addAttribute("bodyContent", "add-appointment");

        return "master-template";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteAppointment(@PathVariable Long id) {
        try{
        this.visitService.delete(id);
        }catch (CannotDeleteVisitException |CannotDeleteVisitWithDiagnosisException exception){
            return "redirect:/appointments?error=" + exception.getMessage();
        }
        return "redirect:/appointments";
    }
}
