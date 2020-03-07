package ie.ucd.lblms;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table (name = "loan")
public class Loan
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column (name= "loan_id")
    private Long id;
    @Column (name = "member")
    private String member;
    @Column (name = "artifact")
    private String artifact;
    @Column (name = "due_date")
    private LocalDate dueDate;
    @Column (name = "completed")
    private boolean completed;

    public Loan() { }

    public Loan(String dueDate, boolean completed, String artifact)
    {
        this.dueDate = LocalDate.now().plusWeeks(2);
        this.completed = completed;
        this.artifact = artifact;
    }

    public String getMember() { return member; }

    public void setMember(String member) { this.member = member; }

    public String getArtifact() { return artifact; }

    public void setArtifact(String artifact) { this.artifact = artifact; }

    public String getDueDate() { return dueDate.toString(); }

    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean getCompleted() { return completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }
}
