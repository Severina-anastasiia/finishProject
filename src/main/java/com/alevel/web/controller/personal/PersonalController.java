package com.alevel.web.controller.personal;

import com.alevel.facade.BookFacade;
import com.alevel.web.data.response.personal.PersonalDashboardChartData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/personal")
public class PersonalController {
    private final BookFacade bookFacade;

    public PersonalController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @GetMapping
    public String main() {
        return "page/personal/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "page/personal/dashboard";
    }

    @GetMapping("/dashboard/chart")
    public @ResponseBody ResponseEntity<PersonalDashboardChartData> generateChart() {
        return ResponseEntity.ok(bookFacade.generatePersonalDashboardChartData());
    }
}
