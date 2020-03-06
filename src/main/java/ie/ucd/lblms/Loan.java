package ie.ucd.lblms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Loan {
    private String startDate;
    private String endDate;
    private boolean completed;
    private Artifact artifact;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public Loan() { }

    public Loan(String startDate, String endDate, boolean completed, Artifact artifact)
    {
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
        this.artifact = artifact;
    }

    public String getStartDate() { return dateFormat.format(startDate); }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return dateFormat.format(endDate); }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }

    public Artifact getArtifact() { return artifact; }

    public void setArtifact(Artifact artifact) { this.artifact = artifact; }

    public String renewLoan(Loan loan)
    {
        if(loan.getArtifact().getAvailable())
        {
            Calendar newEndDate = Calendar.getInstance();
            newEndDate.add(Calendar.DATE, 7);
            loan.setEndDate(dateFormat.format(newEndDate));
            return "done";
        }
        else 
        { 
            return "not available for loan"; 
        }
    }

}
