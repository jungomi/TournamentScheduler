
package turnierplanung.evaluation;

import turnierplanung.Constants;
import turnierplanung.Game;
import turnierplanung.Plan;

/**
 *
 * @author Michael Jungo
 */
public class SingleConstraint {
    // constant identifier
    private static final int EARLIEST_START = 1;
    private static final int LATEST_START = 2;
    private static final int MAX_LAG = 3;
    private static final int MIN_LAG = 4;
    
    private Game game;     // focused game
    private Evaluation eval;
    private Plan plan;
    private Game sigma;     // fictive start
    private Game tau;       // fictive end
    
    public SingleConstraint(Evaluation eval, Plan plan, Game game) {
        this.eval = eval;
        this. plan = plan;
        this.game = game;
        sigma = plan.getFictiveStart();
        tau = plan.getFictiveEnd();
    }
    
    public boolean isAfterFictiveStart() {
        return game.getStart() >= sigma.getStart() + sigma.getDuration();

    }
    
    public boolean isBeforeFictiveEnd() {
        return tau.getStart() >= game.getStart() + game.getDuration();
    }
    
    
    public boolean strongConstraintSatisfied() {
        return isAfterFictiveStart() && isBeforeFictiveEnd();
        
    }
    
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
    
    public int sumOfPenalties() {
    	int ep = earliestStartPenalty();
    	int lp = latestStartPenalty();
    	if (Constants.DEBUG & ep+lp > 0) {
        	System.out.format("%4s: earliest penalty: %7d \t latest penalty: %7d\n", 
                                   game.getName(), ep, lp);
        }
        return ep + lp;
    }
    
    public Game getGame() {
        return game;
    }
}
