package ie.ucd.lblms.controller;

import ie.ucd.lblms.User;
import ie.ucd.lblms.UserData;
import ie.ucd.lblms.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

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
        Optional<User> result = userRepository.findUsernameAndPassword(username, password);
        if (result.isPresent()) {
            userData.setUser(result.get());
            response.sendRedirect("/index");
        }
        else {
            response.sendRedirect("/invalidUser");
        }

    }
//--------------UNCOMMENT THIS OUT WHEN FAILURE PAGE IS MADE--------------------------
/*
    @GetMapping("/invalidUser")
    public String failureView() { return "failure"; }
*/
//-------------UNCOMMENT THIS OUT IF HAVE TIME TO ADD LOGOUT FEATURE-------------------
/*
  @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {
      userData.setUser(null);
      response.sendRedirect("/");
  }
*/
}
