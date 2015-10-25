
package turnierplanung.evaluation;

import turnierplanung.Constants;
import turnierplanung.Game;
import turnierplanung.Plan;
import turnierplanung.Spacing;

/**
 * The PairConstraint class represents a constraint for a pair of games.
 * 
 * @author Michael Jungo
 */
public class PairConstraint {
    // constant identifiers
    private static final int MAX_LAG = 3;
    private static final int MIN_LAG = 4;
    
    private Game game1;     // focused game
    private Game game2;     // complementary game
    private Evaluation eval;
    private Plan plan;
    private Game sigma;     // fictive start
    private Game tau;       // fictive end
    private int maxLag;
    private int minLag;
    
    /**
     * Constructor.
     * 
     * @param eval the Evaluation used
     * @param plan the plan the constraint applies to
     * @param game1 the first Game of the pair the constraint applies to
     * @param game2 the second Game of the pair the constraint applies to
     */
    public PairConstraint(Evaluation eval, Plan plan, Game game1, Game game2) {
        this.eval = eval;
        this. plan = plan;
        this.game1 = game1;
        this.game2 = game2;
        sigma = plan.getFictiveStart();
        tau = plan.getFictiveEnd();
        calculateLag();
    }
    
    /**
     * 
     * @return true if the final Game is after the regular
     */
    public boolean isFinalAfterRegular() {
        if (game1.isFinal() && !game2.isFinal() && game1.getCategory() == game2.getCategory()) {
            return game1.getStart() >= game2.getStart() + game2.getDuration();
        }
        else if (!game1.isFinal() && game2.isFinal() && game1.getCategory() == game2.getCategory()) {
            return game2.getStart() >= game1.getStart() + game1.getDuration();
        }
        return true;
    }
    
    /**
     * 
     * @return true if the first Game is before the second
     */
    public boolean isGame1First() {
        return game1.getStart() - game2.getStart() < 0;
    }
    
    /**
     * 
     * @return true if the Games are not overlapping
     */
    public boolean isNotOverlapping() {
        if (isGame1First()) {
            return game2.getStart() >= game1.getStart() + game1.getDuration();
        }
        else {
            return game1.getStart() >= game2.getStart() + game2.getDuration();
        }
           
    }
    
    /**
     * 
     * @return true if the spacing between the Games is complied
     */
    public boolean isSpacingCorrect() {
        for (Spacing s : game1.getSpacings()) {
            if (s.getGame1() == game1 && s.getGame2() == game2
                && game2.getStart() < game1.getStart() + s.getSpace()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns whether the final Game is after the regular, the Games are not
     * overlapping and the spacing between them is complied.
     *  
     * @return true if satisfied
     */
    public boolean strongConstraintSatisfied() {
        return isFinalAfterRegular() && isNotOverlapping() && isSpacingCorrect();
        
    }
    
    /**
     * Calculates the penalty for the violation of the maximum time between
     * the two Games.
     * 
     * @return the penalty
     */
    public int maxLagPenalty() {
        if (maxLag == 0 || !isGame1First()) return 0;
        int violation = -maxLag + (game2.getStart() - game1.getStart());
        if (violation < 0) return 0;
        int[] fMaxLag = eval.getIntervalF()[MAX_LAG];
        if (violation < fMaxLag.length) return fMaxLag[violation];
        
        int[] aMaxLag = eval.getIntervalA()[MAX_LAG];
        return fMaxLag[fMaxLag.length - 1] 
                +(violation - fMaxLag.length + 1) * aMaxLag[aMaxLag.length - 1];
    }
    
    /**
     * Calculates the penalty for the violation of the minimum time between
     * the two Games.
     * 
     * @return the penalty
     */
    public int minLagPenalty() {
        if (minLag == 0 || !isGame1First()) return 0;
        int violation = minLag - (game2.getStart() - game1.getStart());
        if (violation < 0) return 0;
        int[] fMinLag = eval.getIntervalF()[MIN_LAG];
        if (violation < fMinLag.length) return fMinLag[violation];

        int[] aMinLag = eval.getIntervalA()[MIN_LAG];
        return fMinLag[fMinLag.length - 1] 
                +(violation - fMinLag.length + 1) * aMinLag[aMinLag.length - 1];
    }
    
    /**
     * Returns the sum of the minimum and maximum time penalties.
     * 
     * @return the sum
     */
    public int sumOfPenalties() {
    	int maxL = maxLagPenalty();
    	int minL = minLagPenalty();
    	if (Constants.DEBUG && maxL + minL > 0) {
        	System.out.format("%4s - %4s: minLag penalty: %7d \t maxLag penalty: %7d\n", 
                                   game1.getName(), game2.getName(), minL, maxL);
        }
        return minL + maxL;
    }
    
    /**
     * 
     * @return the first Game
     */
    public Game getGame1() {
        return game1;
    }
    
    /**
     * 
     * @return the second Game
     */
    public Game getGame2() {
        return game2;
    }
    
    /**
     * Calculates the minimum and maximum time allowed between the two Games.
     * These only exist when a Team is present participating in both Games.
     */
    private void calculateLag() {
        if (game1.isFinal() || game2.isFinal()) {
            maxLag = 0;
            minLag = 0;
            return;
        }
        if (game1.getTeam1() == game2.getTeam1() || game1.getTeam1() == game2.getTeam2()) {
            maxLag = game1.getTeam1().getMaxLag();
            minLag = game1.getTeam1().getMinLag();
        } 
        else if(game1.getTeam2() == game2.getTeam1() || game1.getTeam2() == game2.getTeam2()) {
            maxLag = game1.getTeam2().getMaxLag();
            minLag = game1.getTeam2().getMinLag();
        }
        else {
            maxLag = 0;
            minLag = 0;
        }
    }
}
