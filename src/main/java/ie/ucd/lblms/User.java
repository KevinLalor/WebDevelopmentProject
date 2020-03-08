package ie.ucd.lblms;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    @Column(columnDefinition = "boolean default false")
    private boolean librarian;
    
    public User() { }

    public User(String username, String password, boolean librarian)
    {
        this.username = username;
        this.password = password;
        this.librarian = librarian;
    }

    public Long getUserId() { return userId; }

    public boolean getLibrarian() { return librarian; }

    public void setLibrarian(boolean librarian) { this.librarian = librarian; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
