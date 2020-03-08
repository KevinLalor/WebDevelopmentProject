package ie.ucd.lblms;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {
    private User user;
    private boolean loginSuccess;

    public UserSession()
    {
        loginSuccess = true;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public boolean isLoginSuccess() { return loginSuccess; }

    public void setLoginSuccess(boolean loginSuccess) { this.loginSuccess = loginSuccess; }
}
