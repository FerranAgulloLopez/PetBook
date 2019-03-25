package service.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import service.main.exception.NotFoundException;
import service.main.service.ServerService;

@Controller
public class WebController {

    @Autowired
    private ServerService serverService;

    @GetMapping("/mailconfirmation/{email}")
    public String mailconfirmation(@PathVariable String email, Model model) {
        try {
            serverService.ConfirmEmail(email);
            model.addAttribute("email", email);
            return "mailconfirmation";
        } catch (NotFoundException e) {
            return "notfoundexception";
        }
    }
}
