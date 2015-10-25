
package tournamentscheduler;

/**
 * The Spacing class represents a spacing between two games.
 * 
 * @author Michael Jungo
 */
public class Spacing {
    private Game game1;
    private Game game2;
    private int space;
    
    /**
     * Constructor.
     * 
     * @param g1 the first Game the Spacing applies to
     * @param g2 the second Game the Spacing applies to
     * @param space the space between the two Games
     */
    public Spacing(Game g1, Game g2, int space) {
        game1 = g1;
        game2 = g2;
        this.space = space;
            
    }
    
    /**
     * 
     * @return the first Game the Spacing applies to
     */
    public Game getGame1() {
        return game1;
    }
    
    /**
     * 
     * @param g Game to be set as first Game
     */
    public void setGame1(Game g) {
        game1 = g;
    }
    
    /**
     * 
     * @return the second Game the Spacing applies to
     */
    public Game getGame2() {
        return game2;
    }
    
    /**
     * 
     * @param g Game to be set as second Game
     */
    public void setGame2(Game g) {
        game2 = g;
    }
    
    /**
     * 
     * @return the space between the two Games
     */
    public int getSpace() {
        return space;
    }
    
    /**
     * 
     * @param s space to be set
     */
    public void setSpace(int s) {
        space = s;
    }
}
