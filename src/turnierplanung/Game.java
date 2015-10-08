
package turnierplanung;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game class represents games of two opposing teams.
 * 
 * @author Michael Jungo
 */
public class Game {
    private final int id;
    private String name;
    private Team team1, team2;
    private int category, start, duration, earliestStart, latestStart;
    private boolean isFinal;
    private List<Spacing> spacings;
    
    /**
     * Constructor.
     * 
     * @param id the id of the Game
     * @param name the name of the Game
     * @param category the category the Game belongs to
     * @param duration the duration of the Game
     * @param earliestStart the earliest possible start of the Game
     * @param latestStart the latest possible start of the Game
     * @param team1 the first team competing in the Game
     * @param team2 the second team competing in the Game
     * @param isFinal whether the Game is a final Game
     */
    public Game(int id, String name, int category, int duration, int earliestStart,
                int latestStart, Team team1, Team team2, boolean isFinal) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.duration = duration;
        this.earliestStart = earliestStart;
        this.latestStart = latestStart;
        this.team1 = team1;
        this.team2 = team2;
        this.isFinal = isFinal;
        spacings = new ArrayList<Spacing>();
    }
    
    /**
     * 
     * @param s spacing to add
     */
    public void addSpacing(Spacing s) {
        if (!spacingExist(s)) spacings.add(s);
    }
    
    /**
     * Check whether the spacing already exists.
     * 
     * @param s spacing to check
     * @return true if the spacing exists
     */
    public boolean spacingExist(Spacing s) {
        for (Spacing spac : spacings) {
            if (spac.getGame1() == s.getGame1() && spac.getGame2() == s.getGame2())
                return true;
        }
        return false;
    }
    
    /**
     * 
     * @return the category the Game belongs to
     */
    public int getCategory() {
        return category;
    }
    
    /**
     * 
     * @return the duration of the Game
     */
    public int getDuration() {
        return duration;
    }
    
    /**
     * 
     * @return the earliest possible start
     */
    public int getEarliestStart() {
        return earliestStart;
    }
    
    /**
     * 
     * @return the Game id
     */
    public int getId() {
        return id;
    }
    
    /**
     * 
     * @return the latest possible start
     */
    public int getLatestStart() {
        return latestStart;
    }
    
    /**
     * 
     * @return the maximum time between two Games, -1 if the Game is a 
     *         final Game
     */
    public int getMaxLag() {
        if (this.isFinal) return -1;
        return (team1.getMaxLag() > team2.getMaxLag() ?
                team1.getMaxLag() : team2.getMaxLag());
    }
    
    /**
     * 
     * @return the minimum time between two Games, -1 if the Game is a
     *         final Game
     */
    public int getMinLag() {
        if (this.isFinal) return -1;
        return (team1.getMinLag() > team2.getMinLag() ?
                team1.getMinLag() : team2.getMinLag());
    }
    
    /**
     * 
     * @return the name of the Game
     */
    public String getName() {
        return name;
    }
    
    /**
     * 
     * @return the list of Spacings
     */
    public List<Spacing> getSpacings() {
        return spacings;
    }
    
    /**
     * 
     * @return the starting time of the Game
     */
    public int getStart() {
        return start;
    }
    
    /**
     * 
     * @return the first Team competing in the Game
     */
    public Team getTeam1() {
        return team1;
    }
    
    /**
     * 
     * @return the second Team competing in the Game
     */
    public Team getTeam2() {
        return team2;
    }
    
    /**
     * 
     * @return true if the Game is a final Game
     */
    public boolean isFinal() {
        return isFinal;
    }
    
    /**
     * 
     * @param cat category to set
     */
    public void setCategory(int cat) {
        category = cat;
    }
    
    /**
     * 
     * @param dur duration to set
     */
    public void setDuration(int dur) {
        duration = dur;
    }
    
    /**
     * 
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @param isFinal whether the Game is set to be a final Game
     */
    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    /**
     * 
     * @param time time to set
     */
    public void setStart(int time) {
        start = time;
    }
    
    /**
     * 
     * @param team Team to set as first Team
     */
    public void setTeam1(Team team) {
        team1 = team;
    }
    
    /**
     * 
     * @param team Team to set as second Team
     */
    public void setTeam2(Team team) {
        team2 = team;
    }
}
