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

    @GetMapping("/")
    public String home() { return "index.html"; }

    //----- Methods Related to Public Features

    @GetMapping("/public_catalogue")
    public String publicCatalgoue(Model model) 
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

    @GetMapping("/member_catalogue")
    public String memberCatalgoue(Model model) 
    {
        List<Loan> userLoans = loanRepository.findByUserId(userSession.getUser().getUserId());

        List<Long> userLoanArtifactIds = new ArrayList<>();

        for (Loan item : userLoans)
        {
            userLoanArtifactIds.add(item.getArtifact());
        }

        model.addAttribute("name", userSession.getUser().getUsername());
        model.addAttribute("userId", userSession.getUser().getUserId());
        model.addAttribute("currentUserArtifacts", artifactRepository.findByArtifactIdIn(userLoanArtifactIds));
        model.addAttribute("catalogueArtifacts", artifactRepository.findByArtifactIdNotIn(userLoanArtifactIds));

        if (userLoans.isEmpty())
            model.addAttribute("catalogueArtifacts", artifactRepository.findAll());

        return "member_catalogue.html";
    }

    @GetMapping("/member_search")
    public String searchMember(@RequestParam(name="title") String title, Model model)
    {
        List<Loan> userLoans = loanRepository.findByUserId(userSession.getUser().getUserId());

        List<Long> userLoanArtifactIds = new ArrayList<>();

        for (Loan item : userLoans)
        {
            Long currentArtifactId = item.getArtifact();


            // New Stuff
            List<Loan> reservationsOnItem = loanRepository.findByArtifactId(currentArtifactId);
            for (Loan reservation : reservationsOnItem)
            {
                if (reservation.getDueDate().isBefore(LocalDate.now()))
                {
                    reservationsOnItem.remove(reservation);
                }
            }

            if (reservationsOnItem.size() == 1)
                userLoanArtifactIds.add(item.getArtifact());
            else
            {
                Loan thisUserReservation = new Loan(0l, 0l);
                Loan otherUserReservation = new Loan(0l, 0l);
                for (Loan reservation : reservationsOnItem)
                {
                    if (reservation.getMember() == userSession.getUser().getUserId())
                        thisUserReservation = reservation;
                    else
                        otherUserReservation = reservation;
                }

                if (thisUserReservation.getDueDate().isBefore(otherUserReservation.getDueDate()))
                    userLoanArtifactIds.add(item.getArtifact()); 
            }
            //-----
        }

        model.addAttribute("name", userSession.getUser().getUsername());
        model.addAttribute("userId", userSession.getUser().getUserId());
        model.addAttribute("currentUserArtifacts", artifactRepository.findByArtifactIdInAndTitleContaining(userLoanArtifactIds, title));
        model.addAttribute("catalogueArtifacts", artifactRepository.findByArtifactIdNotInAndTitleContaining(userLoanArtifactIds, title));

        if (userLoans.isEmpty())
            model.addAttribute("catalogueArtifacts", artifactRepository.findAll());

        return "member_catalogue.html";
    }

    @GetMapping("/reserve")
    public String reserveItem(@RequestParam(name="artifactId") Long artifactId, Model model)
    {
        Long userId = userSession.getUser().getUserId();

        List<Loan> artifactHistory = loanRepository.findByArtifactId(artifactId);

        for (Loan artifactLoan : artifactHistory)
        {
            if (artifactLoan.getDueDate().isBefore(LocalDate.now()))
            {
                artifactHistory.remove(artifactLoan);
            }
        }

        if (artifactHistory.isEmpty())
        {
            loanRepository.save(new Loan(userId, artifactId));
            model.addAttribute("message", "Reservation successful.");
            return "reservation.html";
        }
        else if (artifactHistory.size() == 1)
        {
            model.addAttribute("message", "Currently out on loan. Reservation successful for when it returns on " + artifactHistory.get(0).getDueDate().toString());
            Loan aLoan = new Loan(userId, artifactId);
            aLoan.renew();
            loanRepository.save(aLoan);
            return "reservation.html";
        }
        return "hello world";
    }
}