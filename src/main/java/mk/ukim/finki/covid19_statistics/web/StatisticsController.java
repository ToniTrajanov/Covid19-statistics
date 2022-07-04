package mk.ukim.finki.covid19_statistics.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Controller
@RequestMapping(value = { "/statistics"})
public class StatisticsController {

    @GetMapping
    public String getStatisticsPage(Model model) {
        model.addAttribute("bodyContent", "statistics");

        LocalDate today = LocalDate.now();

        String formattedDate = today.format(DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG));
        System.out.println("LONG format: " + formattedDate);
        model.addAttribute("today", formattedDate);
        return "master-template";
    }
}
