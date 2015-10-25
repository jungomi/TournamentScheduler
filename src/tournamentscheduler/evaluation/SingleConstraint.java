
package tournamentscheduler.evaluation;

import tournamentscheduler.Constants;
import tournamentscheduler.Game;
import tournamentscheduler.Plan;

/**
 * The SingleConstraint class represents a constraint for a game.
 * 
 * @author Michael Jungo
 */
public class SingleConstraint {
    // constant identifiers
    private static final int EARLIEST_START = 1;
    private static final int LATEST_START = 2;
    
    private Game game;     // focused game
    private Evaluation eval;
    private Plan plan;
    private Game sigma;     // fictive start
    private Game tau;       // fictive end
    
    /**
     * Constructor.
     * 
     * @param eval the Evaluation used
     * @param plan the Plan the constraint applies to
     * @param game the Game the constraint applies to
     */
    public SingleConstraint(Evaluation eval, Plan plan, Game game) {
        this.eval = eval;
        this. plan = plan;
        this.game = game;
        sigma = plan.getFictiveStart();
        tau = plan.getFictiveEnd();
    }
    
    /**
     * 
     * @return true if the Game is after the fictive start
     */
    public boolean isAfterFictiveStart() {
        return game.getStart() >= sigma.getStart() + sigma.getDuration();

    }
    
    /**
     * 
     * @return true if the Game is before the fictive end
     */
    public boolean isBeforeFictiveEnd() {
        return tau.getStart() >= game.getStart() + game.getDuration();
    }
    
    
    /**
     * Returns whether the Game is after the fictive start and before the 
     * fictive end.
     * 
     * @return true if satisfied
     */
    public boolean strongConstraintSatisfied() {
        return isAfterFictiveStart() && isBeforeFictiveEnd();
        
    }
    
    /**
     * Calculates the penalty for the violation of the earliest possible start.
     * 
     * @return the penalty
     */
    public int earliestStartPenalty() {
        if (game.getEarliestStart() == 0) return 0;
        int violation = game.getEarliestStart() - (game.getStart() - sigma.getStart());
        if (violation < 0) return 0;
        int[] fEarliest = eval.getIntervalF()[EARLIEST_START];
        if (violation < fEarliest.length) return fEarliest[violation];
        
        int[] aEarliest = eval.getIntervalA()[EARLIEST_START];
        return fEarliest[fEarliest.length - 1] 
                +(violation - fEarliest.length + 1) * aEarliest[aEarliest.length - 1];
    }
    
    /**
     * Calculates the penalty for the violation of the latest possible start.
     * 
     * @return the penalty
     */
    public int latestStartPenalty() {
        if (game.getLatestStart() == 0) return 0;
        int violation = -game.getLatestStart() - (sigma.getStart() - game.getStart());
        if (violation < 0) return 0;
        int[] fLatest = eval.getIntervalF()[LATEST_START];
        if (violation < fLatest.length) return fLatest[violation];
        
        int[] aLatest = eval.getIntervalA()[LATEST_START];
        return fLatest[fLatest.length - 1] 
                +(violation - fLatest.length + 1) * aLatest[aLatest.length - 1];
    }
    
    /**
     * Returns the sum of the latest and earliest start penalties.
     * 
     * @return the sum
     */
    public int sumOfPenalties() {
    	int ep = earliestStartPenalty();
    	int lp = latestStartPenalty();
    	if (Constants.DEBUG & ep+lp > 0) {
        	System.out.format("%4s: earliest penalty: %7d \t latest penalty: %7d\n", 
                                   game.getName(), ep, lp);
        }
        return ep + lp;
    }
    
    /**
     * 
     * @return the Game the constraint applies to
     */
    public Game getGame() {
        return game;
    }
}
