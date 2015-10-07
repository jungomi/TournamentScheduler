
package turnierplanung.parser;

import java.io.FileNotFoundException;

/**
 *
 * @author michael
 */
public class GameParser extends turnierplanung.parser.Parser {
    
    private String gameInfo[];

    public GameParser(String fileName) throws FileNotFoundException {
        super(fileName);
    }
    
    public void parseInfo() {
        gameInfo = line.split("\\t", -1);
    }
    
    public String getName() {
        return gameInfo[0];
    }
    
    public String getTeam1() {
        return gameInfo[1];
    }
    
    public String getTeam2() {
        return gameInfo[2];
    }
    
    public int getEarliestStart() {
        return Integer.parseInt(gameInfo[3]);
    }
    
    public int getLatestStart() {
        return Integer.parseInt(gameInfo[4]);
    }
    
    public int getCategory() {
        return Integer.parseInt(gameInfo[5]);
    }
    
    public boolean isFinal() {
        return (Integer.parseInt(gameInfo[6]) == 1);
    }
}