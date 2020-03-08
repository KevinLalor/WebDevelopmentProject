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
    private LibrarianRepository librarianRepository;

    @Autowired
    private UserSession userSession;

    @Autowired
    private LibrarianSession librarianSession;

    @GetMapping("/member_sign_in")
    public String memberSignInPage(Model model)
    {
        if (!userSession.isLoginSuccess()) {
            model.addAttribute("error", "Username and Password not correct");
            userSession.setLoginSuccess(true);
        }
        return "member_login.html";
    }

    @GetMapping("/librarian_sign_in")
    public String librarianSignInPage(Model model)
    {
        if (!librarianSession.isLoginSuccess()) {
            model.addAttribute("error", "Username and Password not correct");
            librarianSession.setLoginSuccess(true);
        }
        return "librarian_login.html";
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

    @PostMapping("/librarian_login_attempt")
    public void doLibrarianLogin(String username, String password, HttpServletResponse response) throws Exception {
        Optional<Librarian> librarian = librarianRepository.findLibrarianByUsernameAndPassword(username, password);
        if (librarian.isPresent())
        {
            librarianSession.setLibrarian(librarian.get());
            response.sendRedirect("/librarian_home");
        }
        else
        {
            librarianSession.setLoginSuccess(false);
            response.sendRedirect("/librarian_sign_in");
        }

    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws Exception {
        librarianSession.setLibrarian(null);
        userSession.setUser(null);
        response.sendRedirect("/");
    }
}