package ie.ucd.lblms;


public class Artifact {
    private Long id;
    private String title;
    private String type;
    private int totalAvailable;
    private int totalAmount;
    private boolean available;

    public Artifact() {}
    public Artifact(String title, String type, int totalAvailable, int totalAmount, boolean available){
        this.title = title;
        this.type = type;
        this.totalAvailable =totalAvailable;
        this.totalAmount = totalAmount;
        this.available = available;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public int getTotalAvailable(){
        return totalAvailable;
    }
    public void setTotalAvailable(int totalAvailable){
        this.totalAvailable = totalAvailable;
    }
    public int getTotalAmount(){
        return totalAmount;
    }
    public void setTotalAmount(int totalAmount){
        this.totalAmount = totalAmount;
    }
    public boolean isAvailable(){
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
