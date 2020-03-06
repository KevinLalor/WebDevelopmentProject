package ie.ucd.lblms;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column (name = "user_name")
    private String username;
    @Column
    private String password;
    @ElementCollection
    @CollectionTable(name = "user_current_loans", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "current_loan")
    private List<Loan> currentLoans;
    @ElementCollection
    @CollectionTable(name = "user_past_loans", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "past_loan")
    private List<Loan> loanHistory;
    @Column(name = "librarion_status")
    private boolean librarian;

    public User() { }

    public User(String username, String password, List<Loan> currentLoans, List<Loan> loanHistory, boolean librarian)
    {
        this.username = username;
        this.password = password;
        this.currentLoans = currentLoans;
        this.loanHistory = loanHistory;
        this.librarian = librarian;
    }

    public boolean getLibrarian() { return librarian; }

    public void setLibrarian(boolean librarian) { this.librarian = librarian; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public List<Loan> getCurrentLoans() { return currentLoans; }

    public void setCurrentLoans(Loan newLoan) { currentLoans.add(newLoan); }

    public List<Loan> getLoanHistory() { return loanHistory; }

    public void setLoanHistory(Loan loan) { loanIsFinished(loan); }

    public void loanIsFinished(Loan loan)
    {
        currentLoans.remove(loan);
        loanHistory.add(loan);
    }
}
