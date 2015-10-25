
package tournamentscheduler.evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 * The Neighborhood class represents a Neighborhood of Sequences.
 * 
 * <p>
 * Two Sequences are Neighbors if they can be transformed into one another 
 * by moving a given amount of Games in a specific way.
 *  
 * @author Michael Jungo
 */
public class Neighborhood {
    private Sequence centralSequence;           // center of the Neighborhood
    private List<Sequence> neighborSequences;   // List of neighbour sequences
    
    /**
     * Constructor.
     * 
     * @param sequence the central Sequence of the Neighborhood
     */
    public Neighborhood(Sequence sequence) {
        centralSequence = sequence;
        neighborSequences = new ArrayList<Sequence>();
    }
    
    /**
     * 
     * @param s Sequence to be added to the Neighborhood
     */
    public void addSequence(Sequence s) {
        if (!sequenceExist(s)) neighborSequences.add(s);
    }
    
    /**
     * Checks whether the Sequence already exists.
     * 
     * @param s Sequence to check
     * @return true if the Sequence exists
     */
    public boolean sequenceExist(Sequence s) {
        if (getSequence(s.toString()) != null) return true;
        return false;
    }
    
    /**
     * 
     * @return the central Sequence of the Neighborhood
     */
    public Sequence getCentralSequence() {
        return centralSequence;
    }
    
    /**
     * 
     * @param id id of the Sequence
     * @return the Sequence with the {@code id} or null if it does not exist
     */
    public Sequence getSequence(String id) {
        for (Sequence s : neighborSequences) {
            if (s.toString().equals(id)) return s;
        }
        return null;
    }
    
    /**
     * 
     * @return the list of Sequences of the Neighborhood
     */
    public List<Sequence> getNeighborSequences() {
        return neighborSequences;
    }
}
