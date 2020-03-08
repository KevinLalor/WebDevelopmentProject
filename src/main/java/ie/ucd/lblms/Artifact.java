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
    private boolean inLibrary;

    public Artifact() { }

    public Artifact(String title, String mediaType)
    {
        this.title = title;
        this.mediaType = mediaType;
        this.inLibrary = true;
    }

    public Long getArtifactId() { return artifactId; }
    
    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }
    
    public String getMediaType() { return mediaType; }
    
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public boolean getInLibrary() { return inLibrary; }

    public void setInLibrary(boolean inLibrary) { this.inLibrary = inLibrary; }
}
