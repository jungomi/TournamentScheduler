
package turnierplanung.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import turnierplanung.Game;

/**
 *
 * @author michael
 */
public class Sequence {
    private List<Game> games;
    private int score;
    
    public Sequence() {
        games = new ArrayList<Game>();
        score = -1;
    }
    
    // copy given sequence
    public Sequence(Sequence seq) {
        games = new ArrayList<Game>(seq.games);
        score = -1;
    }
    
    public void addGame(Game game) {
        games.add(game);
    }
    
    public void addGame(Game game, int pos) {
        games.add(pos, game);
    }
    
    public List<Game> getGames() {
        return games;
    }
    
    public int getScore() {
        return score;
    }
    
    public void moveGameBackward(Game game) {
        int index = games.indexOf(game);
        if (index > 0) {
            Collections.swap(games, index, index - 1);
        }
    }
    
    public void moveGameForward(Game game) {
        int index = games.indexOf(game);
        if (index < games.size() - 1) {
            Collections.swap(games, index, index + 1);
        }
    }
    
    public void swapGames(Game game1, Game game2) {
        Collections.swap(games, games.indexOf(game1), games.indexOf(game2));
    }
    
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
