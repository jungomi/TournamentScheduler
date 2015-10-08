
package turnierplanung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import turnierplanung.evaluation.PairConstraint;
import turnierplanung.evaluation.Sequence;
import turnierplanung.evaluation.SingleConstraint;

/**
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
    
    private int calculateEarliestStartViolation(Game game, int time) {
        if (time < game.getEarliestStart()) {
            return game.getEarliestStart() - time;
        }
        return 0;
    }
    
    private int calculateLatestStartViolation(Game game, int time) {
        if (time > game.getLatestStart()) {
            return time - game.getLatestStart();
        }
        return 0;
    }
    
    private int calculateMaxLagViolation(Team team, int time, int previousTime) {
        if (time - previousTime > team.getMaxLag()) {
            return (time - previousTime) - team.getMaxLag();
        }
        return 0;
    }
    
    private int calculateMinLagViolation(Team team, int time, int previousTime) {
        if (time - previousTime < team.getMinLag()) {
            return team.getMinLag() - (time - previousTime);
        }
        return 0;
    }
    
    private void evaluateNeighborhood(List<Sequence> neighbors) {
        Sequence current = sequence;
		for (Sequence s : neighbors) {
			applySequence(s);
			if (isValid()) {
				evaluate();
				s.setScore(this.getScore());
			}
		}
        applySequence(current);                 // restore sequence    
    }
    
    private Game findBestUpcomingGame(List<Game> games, Map<String, Integer> previous, int time) {
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
            Object previousTimeTeam1 = previous.get(team1.getId());
            Object previousTimeTeam2 = previous.get(team2.getId());
            
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
    
    private Sequence findBestNeighborSequence(List<Sequence> neighbors) {
        Sequence best = null;
        int bestScore = Integer.MAX_VALUE;
        for (Sequence s : neighbors) {
            int score = s.getScore();
            if (score < 0 ) continue;               // ignore invalid sequences
            if (score < bestScore) {
                bestScore = score;
                best = s;
            }
        }
        return best;
    }
    
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
    
    public void createInitialSequence() {
        Sequence seq = new Sequence();
        Map<String, Integer> previousGameByTeam = new HashMap<String, Integer>();
        List<Game> games = new ArrayList<Game>(this.games);
        int currentTime = 0;
        while (!games.isEmpty()) {
            Game nextGame = findBestUpcomingGame(games, previousGameByTeam, currentTime);
            //if (nextGame == null) break;
            seq.addGame(nextGame);
            if (!nextGame.isFinal()) {
                previousGameByTeam.put(nextGame.getTeam1().getId(), currentTime);
                previousGameByTeam.put(nextGame.getTeam2().getId(), currentTime);
            }
            currentTime += nextGame.getDuration();
            games.remove(nextGame);
        }
        applySequence(seq);
        bestSequence = seq;
    }
    
    public List<Sequence> createNeighborhood(Sequence seq, int range) {
    	List<Sequence> neighbors = new ArrayList<Sequence>();
    	//forward shift (list of games)
    	//backward shift (list of games)
    	//moveApart (list of game tuples)
    	//moveTogether (list of game tuples)
    	
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
    
    public boolean improveSequence(int range) {
        List<Sequence> neighbors = createNeighborhood(sequence, range);
        evaluateNeighborhood(neighbors);
        Sequence seq = findBestNeighborSequence(neighbors); 
        if(seq == null) {
        	System.err.println("no feasible neighbor?");
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
            //System.out.format("Locally optimal sequence: %s (Score: %d)\n", sequence.toString(), getScore());
            /*
            Constants.DEBUG = true;
            sequence.setScore(-1);
            evaluate();
            Constants.DEBUG = false; //*/
            return false;

        }
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
    
    public void addSingleConstraint(SingleConstraint c) {
        if (!singleConstraintExist(c)) singleConstraints.add(c);
    }
    
    public void addPairConstraint(PairConstraint c) {
        if (!pairConstraintExist(c)) pairConstraints.add(c);
    }
    
    public void addGame(Game g) {
        if (!gameExist(g)) games.add(g);
    }
    
    
    public void addSpacing(Spacing s) {
        if (!spacingExist(s)) spacings.add(s);
    }
    
    public void addTeam(Team t) {
        if (!teamExist(t)) teams.add(t);
    }
    
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
            System.out.format("New best sequence: %s (Score: %d)\n", sequence, sequence.getScore());
        }
    }
    
    public boolean gameExist(Game g) {
        if (getGame(g.getName()) != null) return true;
        return false;
    }
    
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
 
    public boolean pairConstraintExist(PairConstraint c) {
        for (PairConstraint constr : pairConstraints) {
            if (constr.getGame1() == c.getGame1() && constr.getGame2() == c.getGame2()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean singleConstraintExist(SingleConstraint c) {
        for (SingleConstraint constr : singleConstraints) {
            if (constr.getGame() == c.getGame()) {
                return true;
            }
        }
        return false;
    }
    
    
    public boolean spacingExist(Spacing s) {
        for (Spacing spac : spacings) {
            if (spac.getGame1() == s.getGame1() && spac.getGame2() == s.getGame2())
                return true;
        }
        return false;
    }
    
    public boolean teamExist(Team t) {
        return getTeam(t.getId()) != null;
    }
    
    public Sequence getBestSequence() {
        return bestSequence;
    }
    
    public Sequence getCurrentSequence() {
        return sequence;
    }
    
    public Game getFictiveEnd() {
        return tau;
    }
    
    public Game getFictiveStart() {
        return sigma;
    }
    
    public Game getGame(String id) {
        for (Game g : games) {
            if (g.getName().equals(id)) return g;
        }
        return null;
    }
    
    
    public List<PairConstraint> getPairConstraints() {
        return pairConstraints;
    }
    
    public List<SingleConstraint> getSingleConstraints() {
        return singleConstraints;
    }
    
    public int getScore() {
        return sequence.getScore();
    }
    
    
    public Team getTeam(String id) {
        for (Team t : teams) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }
    
    public List<Game> getGames() {
        return games;
    }
    
    public List<Spacing> getSpacings() {
        return spacings;
    }
    
    public List<Team> getTeams() {
        return teams;
    }
    
}