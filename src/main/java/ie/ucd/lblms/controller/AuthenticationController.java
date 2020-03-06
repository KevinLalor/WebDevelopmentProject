package ie.ucd.lblms.controller;

import ie.ucd.lblms.UserData;
import ie.ucd.lblms.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AuthenticationController {
    @Autowired
    UserData userData;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/index")
    public String indexView(Model model) {
        model.addAttribute("user", userData.getUser());
        return "index";
    }

    @PostMapping("/index")
    public void index(String username, String password, HttpServletResponse response) throws IOException {

    }
}
