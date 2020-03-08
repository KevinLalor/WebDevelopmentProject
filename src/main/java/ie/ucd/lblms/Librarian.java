package ie.ucd.lblms;
import javax.persistence.*;

@Entity
@Table(name = "librarians")
public class Librarian
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long librarianId;
    private String username;
    private String password;

    public Librarian() { }

    public Librarian(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public Long getLibrarianId() { return librarianId; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
