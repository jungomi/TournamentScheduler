
package turnierplanung;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author michael
 */
public class Game {
    private int id;
    private String name;
    private Team team1, team2;
    private int category, start, duration, earliestStart, latestStart;
    private boolean isFinal;
    private List<Spacing> spacings;
    
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
//        if (team1 != null && team2 != null) {
//            earliestStart = (team1.getEarliestStart() > team2.getEarliestStart() ? 
//                              team1.getEarliestStart() : team2.getEarliestStart());
//            latestStart = (team1.getLatestStart() > team2.getLatestStart() ? 
//                              team2.getLatestStart() : team1.getLatestStart());
//        }
    }
    
    public void addSpacing(Spacing s) {
        if (!spacingExist(s)) spacings.add(s);
    }
    
    public boolean spacingExist(Spacing s) {
        for (Spacing spac : spacings) {
            if (spac.getGame1() == s.getGame1() && spac.getGame2() == s.getGame2())
                return true;
        }
        return false;
    }
    
    public int getCategory() {
        return category;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public int getEarliestStart() {
        return earliestStart;
    }
    
    public int getId() {
        return id;
    }
    
    public int getLatestStart() {
        return latestStart;
    }
    
    public int getMaxLag() {
        if (this.isFinal) return -1;
        return (team1.getMaxLag() > team2.getMaxLag() ?
                team1.getMaxLag() : team2.getMaxLag());
    }
    
    public int getMinLag() {
        if (this.isFinal) return -1;
        return (team1.getMinLag() > team2.getMinLag() ?
                team1.getMinLag() : team2.getMinLag());
    }
    
    public String getName() {
        return name;
    }
    
    public List<Spacing> getSpacings() {
        return spacings;
    }
    
    public int getStart() {
        return start;
    }
    
    public Team getTeam1() {
        return team1;
    }
    
    public Team getTeam2() {
        return team2;
    }
    
    public boolean isFinal() {
        return isFinal;
    }
    
    public void setCategory(int cat) {
        category = cat;
    }
    
    public void setDuration(int dur) {
        duration = dur;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }
    
    public void setStart(int time) {
        start = time;
    }
    
    public void setTeam1(Team team) {
        team1 = team;
    }
    
    public void setTeam2(Team team) {
        team2 = team;
    }
}
