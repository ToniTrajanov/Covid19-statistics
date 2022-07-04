package mk.ukim.finki.covid19_statistics.web;


import mk.ukim.finki.covid19_statistics.model.Specialty;
import mk.ukim.finki.covid19_statistics.model.exceptions.CannotDeleteSpecialtyException;
import mk.ukim.finki.covid19_statistics.model.exceptions.DoctorWithSsnAlreadyExistsException;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import mk.ukim.finki.covid19_statistics.model.exceptions.LicenceNumberAlreadyExistsException;
import mk.ukim.finki.covid19_statistics.service.SpecialtyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public String getSpecialtiesPage(@RequestParam(required = false) String error,
                                 Model model) {
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Specialty> specialties = this.specialtyService.findAll();
        model.addAttribute("specialties", specialties);
        model.addAttribute("bodyContent", "specialty");
        return "master-template";
    }

    @GetMapping("/add-form")
    public String getAddForm(@RequestParam(required = false) String error, Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        model.addAttribute("bodyContent", "add-specialty");
        return "master-template";
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveDoctor(
            @RequestParam String specialty) {
        try {
            this.specialtyService.save(specialty);
            return "redirect:/specialties";
        }catch (InvalidArgumentException exception){
            return "redirect:/specialties/add-form?error=" + exception.getMessage();
        }
    }




    @DeleteMapping("/{id}/delete")
    public String deleteSpecialty(@PathVariable Long id){
        try {
            this.specialtyService.deleteById(id);
            return "redirect:/specialties";
        } catch (CannotDeleteSpecialtyException exception){
            return "redirect:/specialties?error=" + exception.getMessage();
        }
    }
}
