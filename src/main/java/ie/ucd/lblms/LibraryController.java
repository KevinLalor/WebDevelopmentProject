package ie.ucd.lblms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

@Controller
public class LibraryController
{
    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserSession userSession;

    @Autowired
    private LibrarianSession librarianSession;

    @GetMapping("/")
    public String home() { return "index.html"; }

    //----- Methods Related to Public Features

    @GetMapping("/public_catalogue")
    public String publicCatalogue(Model model)
    {
        model.addAttribute("artifacts", artifactRepository.findAll());
        return "public_catalogue.html"; 
    }

    @GetMapping("/public_search")
    public String searchPublic(@RequestParam(name="title") String title, Model model)
    {
        model.addAttribute("artifacts", artifactRepository.findByTitleContaining(title));
        return "public_catalogue.html";
    }

    @GetMapping("/join")
    public String join()
    {
        return "join.html";
    }

    @PostMapping("/join")
    public String signUpUser(User aUser)
    {
        userRepository.save(aUser);
        return "welcome.html";
    }

    //----- Methods Related to Member Features

    @GetMapping("/member_home")
    public String loadMemberHome(Model model)
    {
        model.addAttribute("name", userSession.getUser().getUsername());
        return "member_home.html";
    }

    @GetMapping("/member_settings")
    public String loadMemberSettings(Model model)
    {
        model.addAttribute("name", userSession.getUser().getUsername());
        return "member_settings.html";
    }

    @GetMapping("/member_settings_change_username")
    public String changeMemberUsername(Model model)
    {
        model.addAttribute("name", userSession.getUser().getUsername());
        return "member_username.html";
    }

    @PostMapping("/member_settings_change_username")
    public String changedMemberUsername(String username)
    {
       userSession.getUser().setUsername(username);
       return "member_settings.html";
    }

    @GetMapping("/member_settings_change_password")
    public String changeMemberPassword(Model model)
    {
        model.addAttribute("name", userSession.getUser().getUsername());
        return "member_password.html";
    }

    @PostMapping("/member_settings_change_password")
    public String changedMemberPassword(String password)
    {
        userSession.getUser().setPassword(password);
        return "member_settings.html";
    }

    @GetMapping("/member_catalogue")
    public String memberCatalogue(Model model)
    {
        List<Loan> userLoans = loanRepository.findByUserId(userSession.getUser().getUserId());

        List<Long> userLoanArtifactIds = new ArrayList<>();

        for (Loan item : userLoans)
        {
            Long currentArtifactId = item.getArtifactId();

            List<Loan> reservationsOnItem = loanRepository.findByArtifactId(currentArtifactId);
            for (Loan reservation : reservationsOnItem)
            {
                if (reservation.getReturnDate().isBefore(LocalDate.now())) { reservationsOnItem.remove(reservation); }
            }
            
            // Should now only have either 0 or 1 or 2 reservations. One possible current loan and one possible future.
            if (reservationsOnItem.size() == 1) { userLoanArtifactIds.add(item.getArtifactId()); }
            else if (reservationsOnItem.size() == 2)
            {
                for (Loan reservation : reservationsOnItem)
                {
                    // If the current user's reservation is within two weeks, then it is the current loan, and the other one is the future reservation.
                    if (reservation.getUserId() == userSession.getUser().getUserId())
                    {
                        if (reservation.getReturnDate().isBefore(LocalDate.now().plusWeeks(2))) { userLoanArtifactIds.add(item.getArtifactId()); }
                    }
                }
            }
        }

        model.addAttribute("name", userSession.getUser().getUsername());
        model.addAttribute("userId", userSession.getUser().getUserId());

        model.addAttribute("currentUserArtifacts", artifactRepository.findByArtifactIdIn(userLoanArtifactIds));

        if (userLoanArtifactIds.isEmpty())
            model.addAttribute("catalogueArtifacts", artifactRepository.findAll());
        else
            model.addAttribute("catalogueArtifacts", artifactRepository.findByArtifactIdNotIn(userLoanArtifactIds));

        return "member_catalogue.html";
    }

    @GetMapping("/member_search")
    public String searchMember(@RequestParam(name="title") String title, Model model)
    {
        List<Loan> userLoans = loanRepository.findByUserId(userSession.getUser().getUserId());

        List<Long> userLoanArtifactIds = new ArrayList<>();

        for (Loan item : userLoans)
        {
            Long currentArtifactId = item.getArtifactId();

            List<Loan> reservationsOnItem = loanRepository.findByArtifactId(currentArtifactId);
            for (Loan reservation : reservationsOnItem)
            {
                if (reservation.getReturnDate().isBefore(LocalDate.now())) { reservationsOnItem.remove(reservation); }
            }
            
            if (reservationsOnItem.size() == 1) { userLoanArtifactIds.add(item.getArtifactId()); }
            else if (reservationsOnItem.size() == 2)
            {
                Long currentUserId = userSession.getUser().getUserId();
                    
                for (Loan reservation : reservationsOnItem)
                {
                    if (reservation.getUserId() == userSession.getUser().getUserId())
                    {
                        if (reservation.getReturnDate().isBefore(LocalDate.now().plusWeeks(2))) { userLoanArtifactIds.add(item.getArtifactId()); }
                    }
                }
            }
        }

        model.addAttribute("name", userSession.getUser().getUsername());
        model.addAttribute("userId", userSession.getUser().getUserId());

        model.addAttribute("currentUserArtifacts", artifactRepository.findByArtifactIdInAndTitleContaining(userLoanArtifactIds, title));

        if (userLoanArtifactIds.isEmpty())
            model.addAttribute("catalogueArtifacts", artifactRepository.findAll());
        else
            model.addAttribute("catalogueArtifacts", artifactRepository.findByArtifactIdNotInAndTitleContaining(userLoanArtifactIds, title));

        return "member_catalogue.html";
    }

    @GetMapping("/reserve")
    public String reserveItem(@RequestParam(name="artifactId") Long artifactId, Model model)
    {
        Long userId = userSession.getUser().getUserId();

        List<Loan> artifactHistory = loanRepository.findByArtifactId(artifactId);

        for (Loan artifactLoan : artifactHistory)
            if (artifactLoan.getReturnDate().isBefore(LocalDate.now())) { artifactHistory.remove(artifactLoan); }

        if (artifactHistory.isEmpty())
        {
            loanRepository.save(new Loan(userId, artifactId));
            model.addAttribute("message", "Reservation successful.");
            return "reservation.html";
        }
        else if (artifactHistory.size() == 1)
        {
            model.addAttribute("message", "Currently out on loan. Reservation successful for when it returns on " + artifactHistory.get(0).getReturnDate().toString());
            Loan aLoan = new Loan(userId, artifactId);
            aLoan.renew();
            loanRepository.save(aLoan);
            return "reservation.html";
        }
        else
        {
            model.addAttribute("message", "Currently out on loan, and Reserved when it returns. Available for further reservation on " + artifactHistory.get(1).getReturnDate().toString());
            return "reservation.html";
        }
    }

    @GetMapping("/renew")
    public String renewItem(@RequestParam(name="artifactId") Long artifactId, Model model)
    {
        List<Loan> artifactHistory = loanRepository.findByArtifactId(artifactId);

        for (Loan artifactLoan : artifactHistory)
            if (artifactLoan.getReturnDate().isBefore(LocalDate.now())) { artifactHistory.remove(artifactLoan); }

        if (artifactHistory.size() == 1)
        {

            //loanRepository.setFixedReturnDateFor(artifactHistory.get(0).getLoanId());
            model.addAttribute("message", "Renewal successful. Now due on ");
            Loan currentLoan = artifactHistory.get(0);
            loanRepository.save(new Loan(currentLoan.getUserId(), currentLoan.getArtifactId(), currentLoan.getReturnDate().plusWeeks(2)));
            model.addAttribute("message", "Renewal successful. Now due on " + currentLoan.getReturnDate().plusWeeks(2));

            return "reservation.html";
        }
        else
        {
            model.addAttribute("message", "Currently fully reserved. Can be reserved again on " + artifactHistory.get(1).getReturnDate());

            return "reservation.html";
        }
    }

    //----Methods relating to librarians
    @GetMapping("/librarian_home")
    public String loadLibrarianHome(Model model)
    {
        model.addAttribute("name", librarianSession.getLibrarian().getUsername());
        return "librarian_home.html";
    }
}