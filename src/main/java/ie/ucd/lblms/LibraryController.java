package ie.ucd.lblms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
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

    public Long currentId;

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

        for (int i = 0; i < userLoans.size(); i++)//for (Loan item : userLoans)
        {
            Long currentArtifactId = userLoans.get(i).getArtifactId();

            List<Loan> reservationsOnItem = loanRepository.findByArtifactId(currentArtifactId);
            for (int k = 0; k < reservationsOnItem.size(); k++)//for (Loan reservation : reservationsOnItem)
            {
                if (reservationsOnItem.get(k).getReturnDate().isBefore(LocalDate.now())) 
                { 
                    reservationsOnItem.remove(k); 
                    k++;
                }
            }
            
            // Should now only have either 0 or 1 or 2 reservations. One possible current loan and one possible future.
            if (reservationsOnItem.size() == 1) { userLoanArtifactIds.add(userLoans.get(i).getArtifactId()); }
            else if (reservationsOnItem.size() == 2)
            {
                for (int j = 0; j < reservationsOnItem.size(); j++)//for (Loan reservation : reservationsOnItem)
                {
                    // If the current user's reservation is within two weeks, then it is the current loan, and the other one is the future reservation.
                    if (reservationsOnItem.get(j).getUserId() == userSession.getUser().getUserId())
                    {
                        if (reservationsOnItem.get(j).getReturnDate().isBefore(LocalDate.now().plusWeeks(2))) { userLoanArtifactIds.add(userLoans.get(i).getArtifactId()); }
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
        List<Loan> userLoans = new ArrayList<Loan>(loanRepository.findByUserId(userSession.getUser().getUserId()));

        List<Long> userLoanArtifactIds = new ArrayList<>();

        for (int i = 0; i < userLoans.size(); i++)//for (Loan item : userLoans)
        {
            Long currentArtifactId = userLoans.get(i).getArtifactId();

            List<Loan> reservationsOnItem = loanRepository.findByArtifactId(currentArtifactId);
            for (int k = 0; k < reservationsOnItem.size(); k++)//for (Loan reservation : reservationsOnItem)
            {
                if (reservationsOnItem.get(k).getReturnDate().isBefore(LocalDate.now())) 
                { 
                    reservationsOnItem.remove(k);
                    k--; 
                }
            }
            
            if (reservationsOnItem.size() == 1) { userLoanArtifactIds.add(userLoans.get(i).getArtifactId()); }
            else if (reservationsOnItem.size() == 2)
            {  
                for (int j = 0; j < reservationsOnItem.size(); j++)//for (Loan reservation : reservationsOnItem)
                {
                    if (reservationsOnItem.get(j).getUserId() == userSession.getUser().getUserId())
                    {
                        if (reservationsOnItem.get(j).getReturnDate().isBefore(LocalDate.now().plusWeeks(2))) { userLoanArtifactIds.add(userLoans.get(i).getArtifactId()); }
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

        List<Loan> artifactHistory = new ArrayList<Loan>(loanRepository.findByArtifactId(artifactId));

        for (int i = 0; i < artifactHistory.size(); i++)//for (Loan artifactLoan : artifactHistory)
        {
            if (artifactHistory.get(i).getReturnDate().isBefore(LocalDate.now())) 
            { 
                artifactHistory.remove(i); 
                i--;
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
        List<Loan> artifactHistory = new ArrayList<Loan>(loanRepository.findByArtifactId(artifactId));

        for (int i = 0; i < artifactHistory.size(); i++)//for (Loan artifactLoan : artifactHistory)
        {
            if (artifactHistory.get(i).getReturnDate().isBefore(LocalDate.now())) 
            { 
                artifactHistory.remove(i); 
                i--;
            }
        }

        if (artifactHistory.size() == 1)
        {
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

    @GetMapping("/member_loans")
    public String viewLoans(Model model)
    {
        List<Loan> allUserLoans = new ArrayList<Loan>(loanRepository.findByUserId(userSession.getUser().getUserId()));
        List<Loan> pastUserLoans = new ArrayList<Loan>();
        List<Loan> currentUserLoans = new ArrayList<Loan>();
        List<Loan> futureUserLoans = new ArrayList<Loan>();
        
        for (int i = 0; i < allUserLoans.size(); i++)//for (Loan loan : allUserLoans)
        {
            if (allUserLoans.get(i).getReturnDate().isBefore(LocalDate.now().plusDays(1)))
                pastUserLoans.add(allUserLoans.get(i));
            else if (allUserLoans.get(i).getReturnDate().isBefore(LocalDate.now().plusWeeks(2).plusDays(1)))
                currentUserLoans.add(allUserLoans.get(i));
            else
                futureUserLoans.add(allUserLoans.get(i));
        }

        List<NamedLoan> pastUserLoansInfo = new ArrayList<NamedLoan>();
        List<NamedLoan> currentUserLoansInfo = new ArrayList<NamedLoan>();
        List<NamedLoan> futureUserLoansInfo = new ArrayList<NamedLoan>();

        for (int i = 0; i < pastUserLoans.size(); i++)//for (Loan loan : pastUserLoans)
        {
            Optional<Artifact> currentArtifact = artifactRepository.findById(pastUserLoans.get(i).getArtifactId());
            Long artifactId = pastUserLoans.get(i).getArtifactId();
            String artifactName = currentArtifact.get().getTitle();
            String artifactType = currentArtifact.get().getMediaType();
            LocalDate returnDate = pastUserLoans.get(i).getReturnDate();

            pastUserLoansInfo.add(new NamedLoan(artifactId, artifactName, artifactType, returnDate));
        }

        for (int i = 0; i < currentUserLoans.size(); i++)//for (Loan loan : pastUserLoans)
        {
            Optional<Artifact> currentArtifact = artifactRepository.findById(currentUserLoans.get(i).getArtifactId());
            Long artifactId = currentUserLoans.get(i).getArtifactId();
            String artifactName = currentArtifact.get().getTitle();
            String artifactType = currentArtifact.get().getMediaType();
            LocalDate returnDate = currentUserLoans.get(i).getReturnDate();

            currentUserLoansInfo.add(new NamedLoan(artifactId, artifactName, artifactType, returnDate));
        }

        for (int i = 0; i < futureUserLoans.size(); i++)//for (Loan loan : pastUserLoans)
        {
            Optional<Artifact> currentArtifact = artifactRepository.findById(futureUserLoans.get(i).getArtifactId());
            Long artifactId = futureUserLoans.get(i).getArtifactId();
            String artifactName = currentArtifact.get().getTitle();
            String artifactType = currentArtifact.get().getMediaType();
            LocalDate returnDate = futureUserLoans.get(i).getReturnDate();

            futureUserLoansInfo.add(new NamedLoan(artifactId, artifactName, artifactType, returnDate));
        }

        model.addAttribute("name", userSession.getUser().getUsername());
        model.addAttribute("pastLoans", pastUserLoansInfo);
        model.addAttribute("currentLoans", currentUserLoansInfo);
        model.addAttribute("futureLoans", futureUserLoansInfo);
        return "member_loans.html";
    }

    //----- Methods relating to librarians
    @GetMapping("/librarian_home")
    public String loadLibrarianHome(Model model)
    {
        model.addAttribute("name", librarianSession.getLibrarian().getUsername());
        return "librarian_home.html";
    }

    @GetMapping("/catalogue_of_members")
    public String searchMembers(Model model)
    {
        model.addAttribute("users", userRepository.findAll());
        return "catalogue_of_members.html";
    }

    @GetMapping("/view/{id}")
    public String viewNote(@PathVariable("id") String id, Model model) {
        long ID = Long.parseLong(id);
        model.addAttribute("note", userRepository.findById(ID));
        model.addAttribute("name", userRepository.findById(ID).getUsername());

        List<Loan> userLoans = loanRepository.findByUserId(ID);
        List<Long> userLoanArtifactIds = new ArrayList<>();
        List<Loan> pastUserLoans = new ArrayList<Loan>();


        for (int i = 0; i < userLoans.size(); i++)//for (Loan loan : allUserLoans)
        {
            if (userLoans.get(i).getReturnDate().isBefore(LocalDate.now().plusDays(1)))
                pastUserLoans.add(userLoans.get(i));
        }

        List<NamedLoan> pastUserLoansInfo = new ArrayList<NamedLoan>();

        for (int i = 0; i < pastUserLoans.size(); i++)//for (Loan loan : pastUserLoans)
        {
            Optional<Artifact> currentArtifact = artifactRepository.findById(pastUserLoans.get(i).getArtifactId());
            Long artifactId = pastUserLoans.get(i).getArtifactId();
            String artifactName = currentArtifact.get().getTitle();
            String artifactType = currentArtifact.get().getMediaType();
            LocalDate returnDate = pastUserLoans.get(i).getReturnDate();

            pastUserLoansInfo.add(new NamedLoan(artifactId, artifactName, artifactType, returnDate));
        }

        model.addAttribute("pastLoans", pastUserLoansInfo);

        for (int i = 0; i < userLoans.size(); i++)//for (Loan item : userLoans)
        {
            Long currentArtifactId = userLoans.get(i).getArtifactId();

            List<Loan> reservationsOnItem = loanRepository.findByArtifactId(currentArtifactId);
            for (int k = 0; k < reservationsOnItem.size(); k++)//for (Loan reservation : reservationsOnItem)
            {
                if (reservationsOnItem.get(k).getReturnDate().isBefore(LocalDate.now()))
                {
                    reservationsOnItem.remove(k);
                    k--;
                }
            }

            if (reservationsOnItem.size() == 1) { userLoanArtifactIds.add(userLoans.get(i).getArtifactId()); }
            else if (reservationsOnItem.size() == 2)
            {
                for (int j = 0; j < reservationsOnItem.size(); j++)//for (Loan reservation : reservationsOnItem)
                {
                    if (reservationsOnItem.get(j).getUserId() == ID)
                    {
                        if (reservationsOnItem.get(j).getReturnDate().isBefore(LocalDate.now().plusWeeks(2))) { userLoanArtifactIds.add(userLoans.get(i).getArtifactId()); }
                    }
                }
            }
        }

        model.addAttribute("userId", ID);

        model.addAttribute("currentUserArtifacts", artifactRepository.findByArtifactIdIn(userLoanArtifactIds));

        if (userLoanArtifactIds.isEmpty())
            model.addAttribute("catalogueArtifacts", artifactRepository.findAll());
        else
            model.addAttribute("catalogueArtifacts", artifactRepository.findByArtifactIdNotIn(userLoanArtifactIds));

        return "view_member.html";
    }
    @GetMapping("/librarianRenew")
    public String librarianRenewItem(@RequestParam(name="artifactId") Long artifactId, Model model)
    {
        List<Loan> artifactHistory = new ArrayList<Loan>(loanRepository.findByArtifactId(artifactId));

        for (int i = 0; i < artifactHistory.size(); i++)//for (Loan artifactLoan : artifactHistory)
        {
            if (artifactHistory.get(i).getReturnDate().isBefore(LocalDate.now()))
            {
                artifactHistory.remove(i);
                i--;
            }
        }

        if (artifactHistory.size() == 1)
        {
            Loan currentLoan = artifactHistory.get(0);
            loanRepository.save(new Loan(currentLoan.getUserId(), currentLoan.getArtifactId(), currentLoan.getReturnDate().plusWeeks(2)));
            model.addAttribute("message", "Renewal successful. Now due on " + currentLoan.getReturnDate().plusWeeks(2));

            return "librarian_reservation.html";
        }
        else
        {
            model.addAttribute("message", "Currently fully reserved. Can be reserved again on " + artifactHistory.get(1).getReturnDate());

            return "librarian_reservation.html";
        }
    }

    @GetMapping("/librarian_catalogue")
    public String librarianCatalogueView(Model model)
    {
        long ID = 1;
        model.addAttribute("note", userRepository.findById(ID));
        model.addAttribute("name", userRepository.findById(ID).getUsername());

        List<Loan> userLoans = loanRepository.findByUserId(ID);
        List<Long> userLoanArtifactIds = new ArrayList<>();

        for (int i = 0; i < userLoans.size(); i++)//for (Loan item : userLoans)
        {
            Long currentArtifactId = userLoans.get(i).getArtifactId();

            List<Loan> reservationsOnItem = loanRepository.findByArtifactId(currentArtifactId);
            for (int k = 0; k < reservationsOnItem.size(); k++)//for (Loan reservation : reservationsOnItem)
            {
                if (reservationsOnItem.get(k).getReturnDate().isBefore(LocalDate.now()))
                {
                    reservationsOnItem.remove(k);
                    k--;
                }
            }

        }

        model.addAttribute("currentUserArtifacts", artifactRepository.findByArtifactIdIn(userLoanArtifactIds));

        if (userLoanArtifactIds.isEmpty())
            model.addAttribute("catalogueArtifacts", artifactRepository.findAll());
        else
            model.addAttribute("catalogueArtifacts", artifactRepository.findByArtifactIdNotIn(userLoanArtifactIds));


        return "librarian_catalogue.html";
    }

    @GetMapping("/search_for_members")
    public String searchForMembers(@RequestParam(name="username") String username, Model model)
    {
        model.addAttribute("users", userRepository.findByUsernameContaining(username));

        return "catalogue_of_members.html";
    }


    @GetMapping("/search_for_items")
    public String searchForItems(@RequestParam(name="title") String title, Model model)
    {
        model.addAttribute("catalogueArtifacts", artifactRepository.findByTitleContaining(title));

        return "librarian_catalogue.html";
    }


    @GetMapping("/new_artifact")
    public String newArtifactPage()
    {
        return "new_artifact.html";
    }

    @GetMapping("/create_artifact")
    public String createArtifact(Artifact anArtifact)
    {
        artifactRepository.save(anArtifact);
        return "artifact_created.html";
    }
    
    @GetMapping("/delete_artifact")
    public String createArtifact(@RequestParam(name="artifactId") Long artifactId, Model model) {
        List<Loan> artifactHistory = loanRepository.findByArtifactId(artifactId);
        boolean stillOnLoan = false;

        for (int i = 0; i < artifactHistory.size(); i++) {
            if (artifactHistory.get(i).getReturnDate().isAfter(LocalDate.now().minusDays(1))) {
                stillOnLoan = true;
            }
        }
        if (!stillOnLoan)
        {
            artifactRepository.removeByArtifactId(artifactId);
            loanRepository.removeByArtifactId(artifactId);
            model.addAttribute("message", "Item successfully removed");
            return "artifact_deleted.html";
        }
        else
        {
            model.addAttribute("message", "Item currently reserved, so cannot be deleted");
            return "artifact_deleted.html";
        }
    }


    @GetMapping("/librarianReserved/{id}")
    public String reservedByLibrarian(@PathVariable("id") String id, Model model)
    {
        long ID = Long.parseLong(id);
        model.addAttribute("name", artifactRepository.findById(ID).getTitle());

        return "librarian_reserve.html";
    }

    @PostMapping("/reservedByLibrarian")
    public String libReserve(String username, Long artifactId, Model model)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            Long userId = user.get().getUserId();

            List<Loan> artifactHistory = new ArrayList<Loan>(loanRepository.findByArtifactId(artifactId));

            for (int i = 0; i < artifactHistory.size(); i++)//for (Loan artifactLoan : artifactHistory)
            {
                if (artifactHistory.get(i).getReturnDate().isBefore(LocalDate.now())) {
                    artifactHistory.remove(i);
                    i--;
                }
            }

            if (artifactHistory.isEmpty()) {
                loanRepository.save(new Loan(userId, artifactId));
                model.addAttribute("message", "Reservation successful.");
                return "librarian_reservation.html";
            } else if (artifactHistory.size() == 1) {
                model.addAttribute("message", "Currently out on loan. Reservation successful for when it returns on " + artifactHistory.get(0).getReturnDate().toString());
                Loan aLoan = new Loan(userId, artifactId);
                aLoan.renew();
                loanRepository.save(aLoan);
                return "librarian_reservation.html";
            } else {
                model.addAttribute("message", "Currently out on loan, and Reserved when it returns. Available for further reservation on " + artifactHistory.get(1).getReturnDate().toString());
                return "librarian_reservation.html";
            }
        }
        return "librarian_home.html";
    }

    @GetMapping("/librarian_settings/{id}")
    public String memberSettings(@PathVariable("id") String id, Model model)
    {
        long ID= Long.parseLong(id);
        model.addAttribute("user", userRepository.findById(ID));
        return "librarian_settings.html";
    }

    @GetMapping("/librarian_settings_change_password/{id}")
    public String librarianChangeMemberPassword(@PathVariable("id") String id, Model model){
        long ID= Long.parseLong(id);
        model.addAttribute("name", userRepository.findById(ID).getPassword());
        model.addAttribute(ID);
        setCurrentId(ID);
        return "librarian_password";
    }
    @PostMapping("/librarian_settings_changed_password")
    public String librarianChangedMemberPassword(String password) {
        long ID = getCurrentId();
        userRepository.findById(ID).setPassword(password);
        userRepository.save(userRepository.findById(ID));
        return "librarian_home.html";
    }

    public void setCurrentId(Long id){
        currentId = id;
    }
    public Long getCurrentId(){
        return currentId;
    }

    @GetMapping("/librarian_settings_change_username/{id}")
    public String librarianChangeMemberUsername(@PathVariable("id") String id, Model model){
        long ID= Long.parseLong(id);
        model.addAttribute("name", userRepository.findById(ID).getUsername());
        model.addAttribute(ID);
        setCurrentId(ID);
        return "librarian_username";
    }
    @PostMapping("/librarian_settings_changed_username")
    public String librarianChangedMemberUsername(String username) {
        long ID = getCurrentId();
        userRepository.findById(ID).setUsername(username);
        userRepository.save(userRepository.findById(ID));
        return "librarian_home.html";
    }

}