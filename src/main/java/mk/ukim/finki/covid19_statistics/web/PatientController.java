package mk.ukim.finki.covid19_statistics.web;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Patient;
import mk.ukim.finki.covid19_statistics.model.exceptions.*;
import mk.ukim.finki.covid19_statistics.service.DoctorService;
import mk.ukim.finki.covid19_statistics.service.PatientService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    public PatientController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping
    public String getPatientsPage(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) String surname,
                                    @RequestParam(required = false) String error,
                                  Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Patient> patientList;
        if((name == null || name.isEmpty()) && (surname == null || surname.isEmpty())) {
            patientList = this.patientService.findAll();
        }
        else {
            patientList = this.patientService.filter(name, surname);
        }

        model.addAttribute("patients", patientList);
        model.addAttribute("bodyContent", "patients");

        return "master-template";
    }

    @GetMapping("/add-form")
    public String getAddForm(Model model,@RequestParam(required = false) String error){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Doctor> doctors = this.doctorService.findAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("bodyContent", "add-patient");

        return "master-template";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String savePatient(
            @RequestParam Long ssn,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam Long uhid,
            @RequestParam Long doctorSsn) {
        try {
            this.patientService.create(ssn, name, surname, uhid, doctorSsn);
            return "redirect:/patients";
        }catch (InvalidArgumentException | PatientAlreadyExistsException | UhidAlreadyExistsException exception){
            return "redirect:/patients/add-form?error=" + exception.getMessage();
        }
    }

    @DeleteMapping("/{ssn}/delete")
    public String deletePatient(@PathVariable Long ssn){
        try {
            this.patientService.delete(ssn);
            return "redirect:/patients";
        } catch (CannotDeletePatientException exception){
            return "redirect:/patients?error=" + exception.getMessage();
        }

    }
}
