package ie.ucd.lblms;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table (name = "loans")
public class Loan
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long loanId;
    private Long userId;
    private Long artifactId;
    private LocalDate returnDate;

    public Loan() { }

    public Loan(Long userId, Long artifactId)
    {
        this.userId = userId;
        this.artifactId = artifactId;
        this.returnDate = LocalDate.now().plusWeeks(2);
    }

    public Long getLoanId() { return loanId; }

    public Long getMember() { return userId; }

    public void setMember(Long userId) { this.userId = userId; }

    public Long getArtifact() { return artifactId; }

    public void setArtifact(Long artifactId) { this.artifactId = artifactId; }

    public LocalDate getReturnDate() { return returnDate; }

    public void setDueDate(LocalDate dueDate) { this.returnDate = dueDate; }

    public void renew() { returnDate = returnDate.plusWeeks(2); }

    public void returnItem() { returnDate = LocalDate.now(); }
}
