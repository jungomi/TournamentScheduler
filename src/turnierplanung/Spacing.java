
package turnierplanung;

/**
 *
 * @author michael
 */
public class Spacing {
    private Game game1;
    private Game game2;
    private int space;
    
    public Spacing(Game g1, Game g2, int space) {
        game1 = g1;
        game2 = g2;
        this.space = space;
            
    }
    
    public int getSpace() {
        return space;
    }
    
    public Game getGame1() {
        return game1;
    }
    
    public Game getGame2() {
        return game2;
    }
    
    public void setSpace(int s) {
        space = s;
    }
    
    public void setGame1(Game g) {
        game1 = g;
    }
    
    public void setGame2(Game g) {
        game2 = g;
    }
    
}
