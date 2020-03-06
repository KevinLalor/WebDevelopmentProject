package ie.ucd.lblms;

import javax.persistence.*;

public class Artifact
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column (name = "artifact_id")
    private Long id;
    @Column
    private String title;
    @Column
    private String type;
    @Column
    private int totalAvailable;
    @Column
    private int totalAmount;
    @Column
    private boolean available;

    public Artifact() { }

    public Artifact(String title, String type, int totalAvailable, int totalAmount, boolean available)
    {
        this.title = title;
        this.type = type;
        this.totalAvailable = totalAvailable;
        this.totalAmount = totalAmount;
        this.available = available;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title) { this.title = title; }
    
    public String getType() { return type; }
    
    public void setType(String type) { this.type = type; }
    
    public int getTotalAvailable() { return totalAvailable; }

    public void setTotalAvailable(int totalAvailable) { this.totalAvailable = totalAvailable; }
    
    public int getTotalAmount() { return totalAmount; }
    
    public void setTotalAmount(int totalAmount) { this.totalAmount = totalAmount; }
    
    public boolean getAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }
}
