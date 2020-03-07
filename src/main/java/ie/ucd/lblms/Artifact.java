package ie.ucd.lblms;

import javax.persistence.*;

@Entity
@Table (name = "artifact")
public class Artifact
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column (name = "artifact_id")
    private Long id;
    @Column (name = "title")
    private String title;
    @Column (name = "media_type")
    private String mediaType;

    public Artifact() { }

    public Artifact(String title, String mediaType, int totalAvailable, int countOnLoan, boolean available)
    {
        this.title = title;
        this.mediaType = mediaType;
    }
    
    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }
    
    public String getMediaType() { return mediaType; }
    
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
}
