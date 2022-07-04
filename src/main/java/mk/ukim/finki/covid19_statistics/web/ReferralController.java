package mk.ukim.finki.covid19_statistics.web;

import mk.ukim.finki.covid19_statistics.model.Doctor;
import mk.ukim.finki.covid19_statistics.model.Referral;
import mk.ukim.finki.covid19_statistics.model.Visit;
import mk.ukim.finki.covid19_statistics.model.exceptions.*;
import mk.ukim.finki.covid19_statistics.service.DoctorService;
import mk.ukim.finki.covid19_statistics.service.PatientService;
import mk.ukim.finki.covid19_statistics.service.ReferralService;
import mk.ukim.finki.covid19_statistics.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Controller
@RequestMapping("/referrals")
public class ReferralController {

    private final ReferralService referralService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final VisitService visitService;

    public ReferralController(ReferralService referralService, DoctorService doctorService,
                              PatientService patientService, VisitService visitService) {
        this.referralService = referralService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.visitService = visitService;
    }


    @GetMapping
    public String getReferralPage(@RequestParam(required = false) Long patientSsn,
                                  @RequestParam(required = false) String error,
                                  Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Referral> referrals;

        if(patientSsn == null){
            referrals = this.referralService.findAll();
        }
        else{
            referrals = this.referralService.filter(patientSsn);
        }
        model.addAttribute("hasError", true);
        model.addAttribute("error",error);
        model.addAttribute("referrals", referrals);
        model.addAttribute("bodyContent", "referral");

        return "master-template";
    }
    @GetMapping("/add-referral")
    public String getReferralAddTemplate(Model model, @RequestParam(required = false) String error){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<Doctor> doctors = this.doctorService.findAll();

        model.addAttribute("doctors", doctors);
        model.addAttribute("bodyContent", "add-referral");

        return "master-template";
    }
    @PostMapping("/add")
    public String addReferral(@RequestParam(required = false) Long id,
                                  @RequestParam(required = false) String localDate,
                                  @RequestParam String patientName,
                                  @RequestParam String patientSurname,
                                  @RequestParam Long patientSsn,
                                  @RequestParam Long doctorForward,
                                  @RequestParam(required = false) Long doctorTo)
    {
        if(id == null){
            try {
                this.referralService.create(LocalDateTime.parse(localDate), patientName, patientSurname, patientSsn, doctorForward, doctorTo);
                this.visitService.create(LocalDateTime.parse(localDate), patientName,patientSurname,patientSsn,doctorTo);
                return "redirect:/referrals";
            }catch (WrongDataEnteredException | DoctorNotSelectedException| TermIsNotAllowedException|
                    PatientDoesNotExistException exception){
                return "redirect:/referrals/add-referral?error=" +exception.getMessage();
            }
        }
        else{
            try {
                LocalDateTime referralTerm = this.referralService.findById(id).getTerm();
                    Long idVisit = this.visitService.findAll().stream()
                            .filter(i -> i.getTerm().isEqual(referralTerm)).findFirst().get().getId();
                this.referralService.edit(id, LocalDateTime.parse(localDate), patientName, patientSurname, patientSsn, doctorForward, doctorTo);
                Visit visit = this.visitService.findById(idVisit);
                if(visit.getTerm().isEqual(referralTerm))
                this.visitService.edit2(idVisit,LocalDateTime.parse(localDate), patientName,patientSurname,patientSsn,doctorTo);
                    return "redirect:/referrals";

                } catch (WrongDataEnteredException | DoctorNotSelectedException |
                    TermIsNotAllowedException| PatientDoesNotExistException
                    | InvalidArgumentException | TermIsNotAvailableException exception){
                return "redirect:/referrals?error=" +exception.getMessage();
                }
            }


    }

    @GetMapping("/{id}/edit-form")
    public String editReferral(@PathVariable Long id, Model model){
        Referral referral = this.referralService.findById(id);
        List<Doctor> doctors = this.doctorService.findAll();
        model.addAttribute("referral", referral);
        model.addAttribute("doctors", doctors);
        model.addAttribute("bodyContent", "add-referral");
        return "master-template";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteReferral(@PathVariable Long id){

        try {
            this.referralService.deleteById(id);
            }
        catch (CannotDeleteVisitWithDiagnosisException exception){
            return "redirect:/referrals?error=" + exception.getMessage();
        }
        return "redirect:/referrals";
    }


    @GetMapping("/{id}/pdf/generate")
    public void generatePDF(HttpServletResponse response, @PathVariable Long id) throws IOException {
        response.setContentType("application/pdf");



        Long patientEmbg = this.referralService.findById(id).getSsnPatient().getSsn();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Upat_" + patientEmbg + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.referralService.export(response, id);

    }
}
