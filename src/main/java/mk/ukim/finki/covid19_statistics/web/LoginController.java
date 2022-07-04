package mk.ukim.finki.covid19_statistics.web;

import mk.ukim.finki.covid19_statistics.model.User;
import mk.ukim.finki.covid19_statistics.model.exceptions.InvalidUserCredentialException;
import mk.ukim.finki.covid19_statistics.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage(@RequestParam(required = false)String error, Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }


    @PostMapping
    public String login(HttpServletRequest req, Model model){
        User user;
        try{
            user = this.authService.login(req.getParameter("username"), req.getParameter("password"));
            req.getSession().setAttribute("user", user);
            return "redirect:/home";
        }catch (InvalidUserCredentialException exception){
            return "redirect:/login?error=" + exception.getMessage();
        }
    }
}
