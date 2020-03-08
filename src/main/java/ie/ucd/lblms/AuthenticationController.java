package ie.ucd.lblms;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSession userSession;

    /*@GetMapping("/login")
    public String login(Model model)
    {
        model.addAttribute("title", "BlogIt: Login");
        if (userSession.isLoginFailed()) {
            model.addAttribute("error", "Username and Password not correct");
            userSession.setLoginFailed(false);
        }
        return "login.html";
    }*/

    @GetMapping("/member_sign_in")
    public String memberSignInPage(Model model)
    {
        if (!userSession.isLoginSuccess()) {
            model.addAttribute("error", "Username and Password not correct");
            userSession.setLoginSuccess(true);
        }
        return "member_login.html";
    }

    //---------

    @PostMapping("/member_login_attempt")
    public void doLogin(String username, String password, HttpServletResponse response) throws Exception {
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) 
        {
            userSession.setUser(user.get());
            response.sendRedirect("/member_home");
        } 
        else 
        {
            userSession.setLoginSuccess(false);
            response.sendRedirect("/member_sign_in");
        }

    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws Exception {
        userSession.setUser(null);
        response.sendRedirect("/");
    }
}