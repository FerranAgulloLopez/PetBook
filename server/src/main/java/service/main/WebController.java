package service.main;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import service.main.config.JwtConfig;
import service.main.exception.NotFoundException;
import service.main.service.ServerService;

@Controller
public class WebController {

    @Autowired
    private ServerService serverService;

    @GetMapping("/mailConfirmation/{token}")
    public String mailconfirmation(@PathVariable String token, Model model) {
        try {
            String userMail = decodeToken(token);
            serverService.ConfirmEmail(userMail);
            model.addAttribute("email", userMail);
            return "mailconfirmation";
        } catch (Exception e) {
            return "notfoundexception";
        }
    }

    private String decodeToken(String token) {
        JwtConfig config = new JwtConfig();
        Claims claims = Jwts.parser()
                .setSigningKey(config.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
