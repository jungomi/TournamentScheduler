
package turnierplanung;

/**
 *
 * @author michael
 */
public class Team {
    private String id;
    private int category, earliestStart, latestStart, maxLag, minLag;
    
    public Team(String id, int category, int minLag, int maxLag) {
        this.id = id;
        this.category = category;
//        this.earliestStart = earliestStart;
//        this.latestStart = latestStart;
        this.minLag = minLag;
        this.maxLag = maxLag;
    }
    
    public int getCategory() {
        return category;
    }
    
//    public int getEarliestStart() {
//        return earliestStart;
//    }
    
    public String getId() {
        return id;
    }
    
//    public int getLatestStart() {
//        return latestStart;
//    }
    
    public int getMaxLag() {
        return maxLag;
    }
    
    public int getMinLag() {
        return minLag;
    }
    
    public void setCategory(int cat) {
        category = cat;
    }
    
//    public void setEarliestStart(int earliestStart) {
//        this.earliestStart = earliestStart;
//    }
    
    public void setId(String id) {
        this.id = id;
    }
    
//    public void setLatestStart(int latestStart) {
//        this.latestStart = latestStart;
//    }
    
    public void setMaxLag(int max) {
        maxLag = max;
    }
    
    public void setMinLag(int min) {
        minLag = min;
    }
}
