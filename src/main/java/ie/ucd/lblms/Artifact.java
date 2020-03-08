package ie.ucd.lblms;

import javax.persistence.*;

@Entity
@Table (name = "artifacts")
public class Artifact
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long artifactId;
    private String title;
    private String mediaType;

    public Artifact() { }

    public Artifact(String title, String mediaType, int totalAvailable, int countOnLoan, boolean available)
    {
        this.title = title;
        this.mediaType = mediaType;
    }

    public Long getArtifactId() { return artifactId; }
    
    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }
    
    public String getMediaType() { return mediaType; }
    
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
}
