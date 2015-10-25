
package tournamentscheduler;

/**
 * The Team class represents a team.
 * 
 * @author Michael Jungo
 */
public class Team {
    private String name;
    private int category, maxLag, minLag;
    
    /**
     * Constructor.
     * 
     * @param name the Team name
     * @param category the category the Team belongs to
     * @param minLag the minimum time between two Games of the Team
     * @param maxLag the maximum time between two Games of the Team
     */
    public Team(String name, int category, int minLag, int maxLag) {
        this.name = name;
        this.category = category;
        this.minLag = minLag;
        this.maxLag = maxLag;
    }
    
    /**
     * 
     * @return the category the Team belongs to
     */
    public int getCategory() {
        return category;
    }
    
    /**
     * 
     * @param cat category to be set
     */
    public void setCategory(int cat) {
        category = cat;
    }
        
    /**
     * 
     * @return the maximum time between two Games of the Team
     */
    public int getMaxLag() {
        return maxLag;
    }
    
    /**
     * 
     * @param max maximum time between two Games of the Team to be set
     */
    public void setMaxLag(int max) {
        maxLag = max;
    }
    
    /**
     * 
     * @return the minimum time between two Games of the Team
     */
    public int getMinLag() {
        return minLag;
    }
    
    /**
     * 
     * @param min minimum time between two Games of the Team to be set
     */
    public void setMinLag(int min) {
        minLag = min;
    }
    
    /**
     * 
     * @return the Team name
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @param name name to be set
     */
    public void setName(String name) {
        this.name = name;
    }
}
