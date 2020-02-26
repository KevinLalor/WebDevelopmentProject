package ie.ucd.lblms;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String username;
    private String password;
    private ArrayList<Loan> currentLoans = new ArrayList<>();
    private ArrayList<Loan> loanHistory = new ArrayList<>();
    private boolean librarian;

    public User(){}
    public User(String username, String password, ArrayList<Loan> currentLoans, ArrayList<Loan> loanHistory, boolean librarian){
        this.username = username;
        this.password = password;
        this.currentLoans = currentLoans;
        this.loanHistory = loanHistory;
        this.librarian = librarian;
    }

    public boolean isLibrarian() {
        return librarian;
    }

    public void setLibrarian(boolean librarian) {
        this.librarian = librarian;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() { return username; }

    public void addLoan(Loan newLoan){
        currentLoans.add(newLoan);
    }
    public void loanIsFinished(Loan loan) {
        currentLoans.remove(loan);
        loanHistory.add(loan);
    }
}
