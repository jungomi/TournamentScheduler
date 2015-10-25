
package tournamentscheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tournamentscheduler.evaluation.PairConstraint;
import tournamentscheduler.evaluation.Sequence;
import tournamentscheduler.evaluation.SingleConstraint;

/**
 * The Plan class represents a Plan of Games realised by respect to 
 * certain constraints.
 * 
 * <p>
 * This class provides methods to create Sequences, evaluate and improve them.
 * 
 * @author Michael Jungo
 */
public class Plan {
    private static final int START_TIME = 0;
    private static final int MAX_LAG_WEIGHT = 1;
    private static final int MIN_LAG_WEIGHT = 1;
    private static final int EARLIEST_START_WEIGHT = 1;
    private static final int LATEST_START_WEIGHT = 1;
    private List<Team> teams;
    private List<Game> games;
    private List<Spacing> spacings;
    private List<SingleConstraint> singleConstraints;
    private List<PairConstraint> pairConstraints;
    private Game sigma;                             // fictive start
    private Game tau;                               // fictive end
    private Sequence sequence;                      // currently used sequence
    private Sequence bestSequence;
    
    /**
     * Constructor.
     */
    public Plan() {
        teams = new ArrayList<Team>();
        games = new ArrayList<Game>();
        spacings = new ArrayList<Spacing>();
        singleConstraints = new ArrayList<SingleConstraint>();
        pairConstraints = new ArrayList<PairConstraint>();
        // initialize fictive games
        sigma = new Game(0,"sigma", -1, 0, 0, 0, null, null, false);
        sigma.setStart(START_TIME);
        tau = new Game(-1, "tau", -1, 0, 0, -1, null, null, false);
        sequence = new Sequence();
        bestSequence = new Sequence();
    }
    
    /**
     * Applies a Sequence to the Plan and sets the start time of every Game.
     * 
     * @param seq Sequence to apply
     */
    public void applySequence(Sequence seq) {
        List<Game> games = seq.getGames();
        int nextStart = 0;
        for (Game game : games) {
            game.setStart(nextStart);
            nextStart += game.getDuration();
        }
        tau.setStart(nextStart);
        sequence = seq;
    }
    
    /**
     * Creates a first Sequence and applies it to the Plan.
     */
    public void createInitialSequence() {
        Sequence seq = new Sequence();
        Map<String, Integer> previousGameByTeam = new HashMap<String, Integer>();
        List<Game> games = new ArrayList<Game>(this.games);
        int currentTime = 0;
        while (!games.isEmpty()) {
            Game nextGame = findBestUpcomingGame(games, previousGameByTeam, currentTime);
            seq.addGame(nextGame);
            if (!nextGame.isFinal()) {
                previousGameByTeam.put(nextGame.getTeam1().getName(), currentTime);
                previousGameByTeam.put(nextGame.getTeam2().getName(), currentTime);
            }
            currentTime += nextGame.getDuration();
            games.remove(nextGame);
        }
        applySequence(seq);
        bestSequence = seq;
    }
    
    // Returns the best Game to insert at the current time
    private Game findBestUpcomingGame(List<Game> games,
                                      Map<String, Integer> previous, int time) {
        Game bestGame = null;
        int bestScore = -1;
        for (Game game : games) {
            if (game.isFinal()) continue;
            Team team1 = game.getTeam1();
            Team team2 = game.getTeam2();
            int minLagViolation = 0;
            int maxLagViolation = 0;
            int earliestStartViolation = 0;
            int latestStartViolation = 0;
            int score;
            Object previousTimeTeam1 = previous.get(team1.getName());
            Object previousTimeTeam2 = previous.get(team2.getName());
            
            earliestStartViolation += calculateEarliestStartViolation(game, time);
            latestStartViolation += calculateLatestStartViolation(game, time);
            if (previousTimeTeam1 != null) {
                minLagViolation += calculateMinLagViolation(team1, time, (int) previousTimeTeam1);
                maxLagViolation += calculateMaxLagViolation(team1, time, (int) previousTimeTeam1);
            }
            if (previousTimeTeam2 != null) {
                minLagViolation += calculateMinLagViolation(team2, time, (int) previousTimeTeam2);
                maxLagViolation += calculateMaxLagViolation(team2, time, (int) previousTimeTeam2);
            }

            score = MAX_LAG_WEIGHT * maxLagViolation 
                    + MIN_LAG_WEIGHT * minLagViolation 
                    + EARLIEST_START_WEIGHT * earliestStartViolation 
                    + LATEST_START_WEIGHT * latestStartViolation;
            if (bestScore < 0 || score < bestScore) {
                bestScore = score;
                bestGame = game;
            }
        }
        // no improvement found, use first game
        if (bestGame == null) {
            bestGame = games.get(0);
        }
        
        return bestGame;
    }
    
    // Returns earliest start violation
    private int calculateEarliestStartViolation(Game game, int time) {
        if (time < game.getEarliestStart()) {
            return game.getEarliestStart() - time;
        }
        return 0;
    }
    
    // Returns latest start violation
    private int calculateLatestStartViolation(Game game, int time) {
        if (time > game.getLatestStart()) {
            return time - game.getLatestStart();
        }
        return 0;
    }
    
    // Returns maximum time violation
    private int calculateMaxLagViolation(Team team, int time, int previousTime) {
        if (time - previousTime > team.getMaxLag()) {
            return (time - previousTime) - team.getMaxLag();
        }
        return 0;
    }
    
    // Returns mimimum time violation
    private int calculateMinLagViolation(Team team, int time, int previousTime) {
        if (time - previousTime < team.getMinLag()) {
            return team.getMinLag() - (time - previousTime);
        }
        return 0;
    }
    
    /**
     * Improves the current Sequence.
     * 
     * @param range the range of the Neighborhoods
     * @return true if an improved sequence has been found
     */
    public boolean improveSequence(int range) {
        List<Sequence> neighbors = createNeighborhood(sequence, range);
        evaluateNeighborhood(neighbors);
        Sequence seq = findBestNeighborSequence(neighbors); 
        if(seq == null) {
        	System.err.println("no feasible neighbor");
        }
        if (seq != null  && seq.getScore()< this.getScore()) {
            applySequence(seq);
            sequence.setScore(seq.getScore());
            if(Constants.DEBUG) {
                System.out.format("Improved sequence found: Score: %d\n",  getScore());
            }
            return true;
        }
        else {
            return false;

        }
    }
    
    // Evaluates every Sequence in the Neighborhood
    private void evaluateNeighborhood(List<Sequence> neighbors) {
        Sequence current = sequence;
		for (Sequence s : neighbors) {
			applySequence(s);
			if (isValid()) {
				evaluate();
				s.setScore(this.getScore());
			}
		}
        // restore sequence
        applySequence(current);    
    }
    
    /**
     * Creates a Neighborhood of the Sequence.
     * 
     * @param seq central Sequence of the Neighborhood
     * @param range the range of the Neighborhood
     * @return the List of Neighbors
     */
    public List<Sequence> createNeighborhood(Sequence seq, int range) {
    	List<Sequence> neighbors = new ArrayList<Sequence>();
    	
    	for (SingleConstraint c : singleConstraints) {
            Game game = c.getGame();
            // too early, move forward
            if (c.earliestStartPenalty() > 0) {
                Sequence copy;
                for (int i = 1; i <= range; i++) {
                    copy = new Sequence(seq);
                    for (int j = 1; j <= i; j++) {
                    	if(Constants.DEBUG) {
                    		System.out.println("Neighbor with Move Forward game" + game.getName());
                    		System.out.println("   " + copy.toString());
                    	}
                        copy.moveGameForward(game);
                    }
                    neighbors.add(copy);
                }
            }
            // too late, move backward
            if (c.latestStartPenalty() > 0) {
                Sequence copy;
                for (int i = 1; i <= range; i++) {
                    copy = new Sequence(seq);
                    for (int j = 1; j <= i; j++) {
                    	if(Constants.DEBUG) {
                    		System.out.println("Neighbor with Move Backward game" + game.getName());
                       		System.out.println("   " + copy.toString());
                    	}
                        copy.moveGameBackward(game);
                    }
                    neighbors.add(copy);
                }
            }
        }
        for (PairConstraint c : pairConstraints) {
            Game game1 = c.getGame1();
            Game game2 = c.getGame2();
            // games too far apart, move closer
            if (c.maxLagPenalty() > 0) {
                Sequence copy1;     // move earlier game
                Sequence copy2;     // move later game
                Sequence copy3;     // move both
                for (int i = 1; i <= range; i++) {
                    copy1 = new Sequence(seq);
                    copy2 = new Sequence(seq);
                    copy3 = new Sequence(seq);
                    for (int j = 1; j <= i; j++) {
                        copy1.moveGameForward(game1);
                        copy2.moveGameBackward(game2);
                        copy3.moveGameForward(game1);
                        copy3.moveGameBackward(game2);
                        if(Constants.DEBUG) {
                    		System.out.println("Neighbor with 1) Foward " + game1.getName() + "; 2) Backward " + game2.getName() + "; and 1) and 2) together");
                       		System.out.println("   1) " + copy1.toString());
                    		System.out.println("   2) " + copy2.toString());
                    		System.out.println("   3) " + copy3.toString());
                        }
                    }
                    neighbors.add(copy1);
                    neighbors.add(copy2);
                    neighbors.add(copy3);
                }
            }
            // games too close, move apart
            if (c.minLagPenalty() > 0) {
                Sequence copy1;     // move earlier game
                Sequence copy2;     // move later game
                Sequence copy3;     // move both
                for (int i = 1; i <= range; i++) {
                    copy1 = new Sequence(seq);
                    copy2 = new Sequence(seq);
                    copy3 = new Sequence(seq);
                    for (int j = 1; j <= i; j++) {
                        copy1.moveGameBackward(game1);
                        copy2.moveGameForward(game2);
                        copy3.moveGameBackward(game1);
                        copy3.moveGameForward(game2);
                    }
                    if(Constants.DEBUG) {
                		System.out.println("Neighbor with 1) Backward " + game1.getName() + "; 2) Forward " + game2.getName() + "; and 1) and 2) together");
                 		System.out.println("   1) " + copy1.toString());
                		System.out.println("   2) " + copy2.toString());
                		System.out.println("   3) " + copy3.toString());
                    }
                    neighbors.add(copy1);
                    neighbors.add(copy2);
                    neighbors.add(copy3);
                }
            }
        }
        
        
        return neighbors;
    }
    
    // Returns the best Sequence in the Neighborhood
    private Sequence findBestNeighborSequence(List<Sequence> neighbors) {
        Sequence best = null;
        int bestScore = Integer.MAX_VALUE;
        for (Sequence s : neighbors) {
            int score = s.getScore();
            // ignore invalid sequences
            if (score < 0 ) continue;
            if (score < bestScore) {
                bestScore = score;
                best = s;
            }
        }
        return best;
    }

    /**
     * Creates a new Sequence by shuffling the given Sequence.
     * 
     * @param seq Sequence to be shuffled
     * @param num number of shuffles
     * @return the new Sequence
     */
    public Sequence shuffleSequence(Sequence seq, int num) {
        Sequence newSeq = new Sequence(seq);
        List<Game> games = seq.getGames();
        int numGames = games.size();
        List<Integer> exclude = new ArrayList<>();
        // exclude final games in the shuffle
        for (Game g : games) {
            if (g.isFinal()) exclude.add(games.indexOf(g));
        }
        for (int i = 0; i < num; i++) {
            int index1 = getRandomWithExclusion(numGames, exclude);
            int index2 = getRandomWithExclusion(numGames, exclude);
            while (index1 == index2) {
                index2 = getRandomWithExclusion(numGames, exclude);
            }
            newSeq.swapGames(games.get(index1), games.get(index2));
        }
        
        return newSeq;
    }
    
    // Returns a random number between 0 and bound excluding numbers from exclude.
    private int getRandomWithExclusion(int bound, List<Integer> exclude) {
        int random = Constants.RAND.nextInt(bound - exclude.size());
        for (int ex : exclude) {
            if (random < ex) break;
            random++;
        }
        return random;
        
    }
    
    /**
     * Evaluates the current Sequence.
     */
    public void evaluate() {
        int score = sequence.getScore();
        // sequence has already been evaluated
        if (score > 0) {
            sequence.setScore(score);
            return;
        }
        
        int sum = 0;
        for (SingleConstraint c : singleConstraints) {
            sum += c.sumOfPenalties();
        }
        for (PairConstraint c : pairConstraints) {
            sum += c.sumOfPenalties();
        }
        sequence.setScore(sum);
        int bestSequenceScore = bestSequence.getScore();
        if (bestSequenceScore < 0 || sum < bestSequenceScore) {
            bestSequence = sequence;
            System.out.format("New best sequence: %s (Score: %d)\n", sequence,
                              sequence.getScore());
        }
    }
    
    /**
     * 
     * @return true if the Plan is valid
     */
    public boolean isValid() {
        for (SingleConstraint c : singleConstraints) {
            if (!c.strongConstraintSatisfied()) {
                return false;
            }
        }
        for (PairConstraint c : pairConstraints) {
            if (!c.strongConstraintSatisfied()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 
     * @param c SingleConstraint to be added to the Plan
     */
    public void addSingleConstraint(SingleConstraint c) {
        if (!singleConstraintExist(c)) singleConstraints.add(c);
    }
    
    /**
     * 
     * @param c PairConstraint to be added to the Plan
     */
    public void addPairConstraint(PairConstraint c) {
        if (!pairConstraintExist(c)) pairConstraints.add(c);
    }
    
    /**
     * 
     * @param g Game to be added to the Plan
     */
    public void addGame(Game g) {
        if (!gameExist(g)) games.add(g);
    }
    
    /**
     * 
     * @param s Spacing to be added to the Plan
     */
    public void addSpacing(Spacing s) {
        if (!spacingExist(s)) spacings.add(s);
    }
    
    /**
     * 
     * @param t Team to be added to the Plan
     */
    public void addTeam(Team t) {
        if (!teamExist(t)) teams.add(t);
    }
    
    /**
     * 
     * @return the best sequence
     */
    public Sequence getBestSequence() {
        return bestSequence;
    }
    
    /**
     * 
     * @return the current Sequence
     */
    public Sequence getCurrentSequence() {
        return sequence;
    }
    
    /**
     * 
     * @return the fictive end Game
     */
    public Game getFictiveEnd() {
        return tau;
    }
    
    /**
     * 
     * @return the fictive start Game
     */
    public Game getFictiveStart() {
        return sigma;
    }
    
    /**
     * 
     * @return the List of PairConstraints
     */
    public List<PairConstraint> getPairConstraints() {
        return pairConstraints;
    }
    
    /**
     * Checks whether the PairConstraint already exists.
     * @param c PairConstraint to be checked
     * @return true if the PairConstraint exists
     */
    public boolean pairConstraintExist(PairConstraint c) {
        for (PairConstraint constr : pairConstraints) {
            if (constr.getGame1() == c.getGame1() && constr.getGame2() == c.getGame2()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @return the List of SingleConstraints
     */
    public List<SingleConstraint> getSingleConstraints() {
        return singleConstraints;
    }
    
    /**
     * Checks whether the SingleConstraint already exists.
     * @param c SingleConstraint to be checked
     * @return true if the SingleConstraint exists
     */
    public boolean singleConstraintExist(SingleConstraint c) {
        for (SingleConstraint constr : singleConstraints) {
            if (constr.getGame() == c.getGame()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @return the score of the current Sequence
     */
    public int getScore() {
        return sequence.getScore();
    }
    
    /**
     * 
     * @return the List of Teams
     */
    public List<Team> getTeams() {
        return teams;
    }
    
    /**
     * 
     * @param name name of the Team
     * @return the Team with the {@code name} or null if it does not exist
     */
    public Team getTeam(String name) {
        for (Team t : teams) {
            if (t.getName().equals(name)) return t;
        }
        return null;
    }
    
    /**
     * Checks whether the Team already exists.
     * @param t Team to be checked
     * @return true if the Team exists
     */
    public boolean teamExist(Team t) {
        return getTeam(t.getName()) != null;
    }
    
    /**
     * 
     * @return the List of Games
     */
    public List<Game> getGames() {
        return games;
    }
    
    /**
     * 
     * @param name name of the Game
     * @return the Game with the {@code name} or null if it does not exist
     */
    public Game getGame(String name) {
        for (Game g : games) {
            if (g.getName().equals(name)) return g;
        }
        return null;
    }
    
    /**
     * Checks whether the Game already exists.
     * @param g Game to be checked
     * @return true if the Game exists
     */
    public boolean gameExist(Game g) {
        if (getGame(g.getName()) != null) return true;
        return false;
    }
    
    /**
     * 
     * @return the List of Spacings
     */
    public List<Spacing> getSpacings() {
        return spacings;
    }
    
    /**
     * Checks whether the Spacing already exists.
     * @param s Spacing to be checked
     * @return true if the Spacing exists
     */
    public boolean spacingExist(Spacing s) {
        for (Spacing spac : spacings) {
            if (spac.getGame1() == s.getGame1() && spac.getGame2() == s.getGame2())
                return true;
        }
        return false;
    }
}
