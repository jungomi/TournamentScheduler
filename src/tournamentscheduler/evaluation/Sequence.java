
package tournamentscheduler.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tournamentscheduler.Game;

/**
 * The Sequence class represents an order for a list of games.
 * 
 * @author Michael Jungo
 */
public class Sequence {
    private List<Game> games;
    private int score;
    
    /**
     * Constructor.
     */
    public Sequence() {
        games = new ArrayList<Game>();
        score = -1;
    }
    
    /**
     * Constructor to copy a Sequence.
     * 
     * @param seq sequence to be copied
     */
    public Sequence(Sequence seq) {
        games = new ArrayList<Game>(seq.games);
        score = -1;
    }
    
    /**
     * 
     * @param game game to be added to the sequence
     */
    public void addGame(Game game) {
        games.add(game);
    }
    
    /**
     * 
     * @param game game to be added to the sequence
     * @param pos position to be added at
     */
    public void addGame(Game game, int pos) {
        games.add(pos, game);
    }
    
    /**
     * Moves a Game backwards in the sequence.
     * 
     * @param game game to be moved
     */
    public void moveGameBackward(Game game) {
        int index = games.indexOf(game);
        if (index > 0) {
            Collections.swap(games, index, index - 1);
        }
    }
    
    /**
     * Moves a Game forwards in the sequence
     * 
     * @param game game to be moved
     */
    public void moveGameForward(Game game) {
        int index = games.indexOf(game);
        if (index < games.size() - 1) {
            Collections.swap(games, index, index + 1);
        }
    }
    
    /**
     * 
     * @param game1 first game to be swapped
     * @param game2 second game to be swapped
     */
    public void swapGames(Game game1, Game game2) {
        Collections.swap(games, games.indexOf(game1), games.indexOf(game2));
    }
    
    /**
     * 
     * @return the list of games of the sequence
     */
    public List<Game> getGames() {
        return games;
    }
    
    /**
     * 
     * @return the score of the sequence
     */
    public int getScore() {
        return score;
    }
    
    /**
     * 
     * @param score score to be set
     */
    public void setScore(int score) {
        this.score = score;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for (Game g : games) {
            sb.append(prefix);
            prefix = ",";
            sb.append(g.getName());
            
        }
        return sb.toString();
    }
}
