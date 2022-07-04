package mk.ukim.finki.covid19_statistics.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = {"/", "/home"})
public class HomeController {

    @GetMapping
    public String getHomePage(Model model) {

        model.addAttribute("bodyContent", "index");
        return "master-template";
    }

}
