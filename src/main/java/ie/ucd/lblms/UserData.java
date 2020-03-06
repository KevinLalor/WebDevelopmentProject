package ie.ucd.lblms;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserData {
    private User user;

    public User getUser(){return user;}
    public void setUser(User user){this.user = user;}
    public boolean isLibrarian() {
        return (user.getLibrarian());
    }
}
