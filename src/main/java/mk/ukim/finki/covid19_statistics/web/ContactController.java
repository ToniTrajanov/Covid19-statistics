package mk.ukim.finki.covid19_statistics.web;

import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private JavaMailSender javaMailSender;


    @GetMapping
    private String getPage(@RequestParam(required = false) String success, Model model){
        if (success != null && !success.isEmpty()) {
            model.addAttribute("isSuccess", true);
            model.addAttribute("success", success);
        }
        model.addAttribute("bodyContent", "contact");
        return "master-template";
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam String email,
                            @RequestParam String subject,
                            @RequestParam String message,Model model, String error){
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        try{
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(email);
        msg.setTo("mojterminkovid19@gmail.com");
        msg.setSubject(String.format("Email from: %s", email));
        msg.setText(message);

        SimpleMailMessage reply = new SimpleMailMessage();
        reply.setFrom("mojterminkovid19@gmail.com");
        reply.setTo(email);
        reply.setSubject("Благодариме на интересот");
        reply.setText("Почитувани, овој меил е испратен автоматски. Вашата порака моментално се разгледува. Ве молиме бидете трпеливи.");

        javaMailSender.send(msg);
        javaMailSender.send(reply);

        return "redirect:/contact?success=" + "Thanks for filling out our form!";
        }
        catch (InvalidArgumentException ex)
        {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
            return "contact";
        }
    }
}
