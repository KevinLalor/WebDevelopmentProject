package ie.ucd.lblms;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class LibrarianSession {
    private Librarian librarian;
    private boolean loginSuccess;

    public LibrarianSession()
    {
        loginSuccess = true;
    }

    public Librarian getLibrarian() { return librarian; }

    public void setLibrarian(Librarian librarian) { this.librarian = librarian; }

    public boolean isLoginSuccess() { return loginSuccess; }

    public void setLoginSuccess(boolean loginSuccess) { this.loginSuccess = loginSuccess; }
}