package ie.ucd.lblms;

import java.time.LocalDate;

public class NamedLoan
{
    private Long artifactId;
    private String artifactName;
    private String artifactType;
    private LocalDate returnDate;

    public NamedLoan(Long artifactId, String artifactName, String artifactType, LocalDate returnDate)
    {
        this.artifactId = artifactId;
        this.artifactName = artifactName;
        this.artifactType = artifactType;
        this.returnDate = returnDate;
    }

    public Long getArtifactId() { return artifactId; };

    public String getArtifactName() { return artifactName; }

    public String getArtifactType() { return artifactType; }

    public String getReturnDate() { return returnDate.toString(); }
}
