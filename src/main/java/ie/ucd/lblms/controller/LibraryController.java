package ie.ucd.lblms.controller;

//import ie.ucd.lblms.ArtifactRepository;
//import ie.ucd.lblms.LoanRepository;
import ie.ucd.lblms.UserData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LibraryController {
    @Autowired
    private UserData userData;

    //@Autowired
    //private LoanRepository loanRepository;

    //@Autowired
    //private ArtifactRepository artifactRepository;

    private boolean isLoggedIn() { return userData.getUser() != null; }
    private boolean isLibrarian() { return userData.isLibrarian(); }

    @GetMapping("/")
    public void home(HttpServletResponse response) throws IOException {
        if (isLoggedIn()){
            if (isLibrarian()){
                response.sendRedirect("/librarian");
            }
            else{
                response.sendRedirect("/member");
            }
        }
        else{
            response.sendRedirect("/index");
        }
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletResponse response) throws IOException {
        if (isLoggedIn()){
            if (isLibrarian()){
                response.sendRedirect("/librarian");
                return null;
            }
            else{
                response.sendRedirect("/member");
                return null;
            }
        }
        model.addAttribute("user", userData.getUser());
        return "content";
    }

}