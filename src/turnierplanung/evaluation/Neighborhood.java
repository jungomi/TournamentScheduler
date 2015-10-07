
package turnierplanung.evaluation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author michael
 */
public class Neighborhood {
    private Sequence centralSequence;           // centre of the Neighborhood
    private List<Sequence> neighborSequences;  // List of neighbour sequences
    
    public Neighborhood(Sequence sequence) {
        centralSequence = sequence;
        neighborSequences = new ArrayList<Sequence>();
    }
    
    public void addSequence(Sequence s) {
        if (!sequenceExist(s)) neighborSequences.add(s);
    }
    
    public boolean sequenceExist(Sequence s) {
        if (getSequence(s.toString()) != null) return true;
        return false;
    }
    
    public Sequence getCentralSequence() {
        return centralSequence;
    }
    
    public Sequence getSequence(String id) {
        for (Sequence s : neighborSequences) {
            if (s.toString().equals(id)) return s;
        }
        return null;
    }
    
    public List<Sequence> getNeighborSequences() {
        return neighborSequences;
    }
}
