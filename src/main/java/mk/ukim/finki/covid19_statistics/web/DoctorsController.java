package mk.ukim.finki.covid19_statistics.web;

import mk.ukim.finki.covid19_statistics.model.Diagnosis;
import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Specialty;
import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.DoctorCannotBeDeletedException;
import mk.ukim.finki.covid19_statistics.model.exceptions.DoctorWithSsnAlreadyExistsException;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.covid19_statistics.model.exceptions.LicenceNumberAlreadyExistsException;
import mk.ukim.finki.covid19_statistics.service.DoctorService;
import mk.ukim.finki.covid19_statistics.service.SpecialtyService;
import mk.ukim.finki.covid19_statistics.service.VisitService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/doctors")
public class DoctorsController {

    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final VisitService visitService;

    public DoctorsController(DoctorService doctorService, SpecialtyService specialtyService, VisitService visitService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.visitService = visitService;
    }

    @GetMapping
    public String getDoctorsPage(@RequestParam(required = false) String error,
                                 @RequestParam(required = false) String doctorName,
                                 @RequestParam(required = false) String doctorSurname,
                                 @RequestParam(required = false) String specialty,
                                 Model model) {
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Doctor> doctorList;
        List<Specialty> specialties = this.specialtyService.findAll();
        List<Visit> visits = this.visitService.findAll();

        if((doctorName == null || doctorName.isEmpty()) &&
                (doctorSurname == null || doctorSurname.isEmpty()) &&
                (specialty == null || specialty.isEmpty())){
            doctorList = this.doctorService.findAll();
        }
        else if((specialty != null) && (doctorName == null || doctorSurname == null) ){
            doctorList = this.doctorService.filter(specialty);
        }
        else{
                doctorList = this.doctorService.filterByNameAndSurname(doctorName, doctorSurname);
        }

        model.addAttribute("doctors", doctorList);
        model.addAttribute("specialties", specialties);
        model.addAttribute("visits", visits);
        model.addAttribute("bodyContent", "doctors");

        return "master-template";
    }

    @GetMapping("/add-form")
    public String addDoctorPage(Model model,@RequestParam(required = false) String error)
    {
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Specialty> specialties = this.specialtyService.findAll();
        model.addAttribute("specialties", specialties);

        model.addAttribute("bodyContent", "add-doctor");
        return "master-template";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveDoctor(
            @RequestParam (required = false) Long editSsn,
            @RequestParam Long ssn,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam Integer licenceNumber,
            @RequestParam String specialtyName) {
            try {
                this.doctorService.create(ssn, name, surname, licenceNumber, specialtyName);
                return "redirect:/doctors";
            } catch (InvalidArgumentException | DoctorWithSsnAlreadyExistsException | LicenceNumberAlreadyExistsException exception) {
                return "redirect:/doctors/add-form?error=" + exception.getMessage();
            }
        }





    @DeleteMapping("/{ssn}/delete")
    public String deleteDoctor(@PathVariable Long ssn) {
        try {
            this.doctorService.deleteBySsn(ssn);
            return "redirect:/doctors";
        } catch (DoctorCannotBeDeletedException exception) {
            return "redirect:/doctors?error=" + exception.getMessage();
        }
    }

}
