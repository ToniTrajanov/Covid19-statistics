package mk.ukim.finki.covid19_statistics.web;


import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Patient;
import mk.ukim.finki.covid19_statistics.model.User;
import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.covid19_statistics.model.exceptions.PatientDoesNotExistException;
import mk.ukim.finki.covid19_statistics.model.exceptions.TermIsNotAllowedException;
import mk.ukim.finki.covid19_statistics.model.exceptions.TermIsNotAvailableException;
import mk.ukim.finki.covid19_statistics.service.DoctorService;
import mk.ukim.finki.covid19_statistics.service.PatientService;
import mk.ukim.finki.covid19_statistics.service.UserService;
import mk.ukim.finki.covid19_statistics.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping
public class MyAppointmentController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final VisitService visitService;
    private final UserService userService;

    public MyAppointmentController(PatientService patientService, DoctorService doctorService, VisitService visitService, UserService userService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.visitService = visitService;
        this.userService = userService;
    }

    @GetMapping("/myappointments")
    public String getAppointmentPage(@RequestParam(required = false) Long patientSsn, Model model) {

        List<Doctor> doctorList = this.doctorService.findAll();
        List<Patient> patientList = this.patientService.findAll();
        List<Visit> visitList;
        List<User> users = this.userService.findAll();

        if(this.patientService.findBySsn(patientSsn) == null){
            visitList = this.visitService.findAll();
        }
        else{
            visitList = this.visitService.filterTwo(patientSsn);
        }

        model.addAttribute("doctors", doctorList);
        model.addAttribute("patients", patientList);
        model.addAttribute("visits", visitList);
        model.addAttribute("users", users);


        model.addAttribute("bodyContent", "myappointments");
        return "master-template";
    }

    @GetMapping("/add-appointment-form")
    public String addAppointment(@RequestParam(required = false) String error, Model model){
        if(error != null && !error.isEmpty()){
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
    @PostMapping("/add")
    public String makeAppointment(@RequestParam String name,
                                  @RequestParam String surname,
                                  @RequestParam Long ssn,
                                  @RequestParam String localDate,
                                  @RequestParam Long doctorSsn){
            try {
                this.visitService.create(LocalDateTime.parse(localDate), name, surname, ssn, doctorSsn);
                return "redirect:/myappointments";
            }catch(PatientDoesNotExistException | TermIsNotAvailableException |
                    InvalidArgumentException | TermIsNotAllowedException exception) {
                return "redirect:/add-appointment-form?error="+ exception.getMessage();
            }
    }
}