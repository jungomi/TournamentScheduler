
package tournamentscheduler.parser;

import java.io.FileNotFoundException;

/**
 * The GameParser class provides functionalities to extract informations about
 * Games.
 * 
 * @author Michael Jungo
 */
public class GameParser extends tournamentscheduler.parser.Parser {
    
    private String gameInfo[];
    
    /**
     * Constructor.
     * 
     * @param fileName the path to the File
     * @throws FileNotFoundException if fileName does not exist. 
     */
    public GameParser(String fileName) throws FileNotFoundException {
        super(fileName);
    }
    
    /**
     * Extracts the information from the current line.
     */
    public void parseInfo() {
        gameInfo = line.split("\\t", -1);
    }
    
    /**
     * 
     * @return the name of the Game
     */
    public String getName() {
        return gameInfo[0];
    }
    
    /**
     * 
     * @return the first Team participating in the Game
     */
    public String getTeam1() {
        return gameInfo[1];
    }
    
    /**
     * 
     * @return the second Team participating in the Game
     */
    public String getTeam2() {
        return gameInfo[2];
    }
    
    /**
     * 
     * @return the earliest possible start of the Game
     */
    public int getEarliestStart() {
        return Integer.parseInt(gameInfo[3]);
    }
    
    /**
     * 
     * @return the latest possible start of the Game
     */
    public int getLatestStart() {
        return Integer.parseInt(gameInfo[4]);
    }
    
    /**
     * 
     * @return the category the Game belongs to
     */
    public int getCategory() {
        return Integer.parseInt(gameInfo[5]);
    }
    
    /**
     * 
     * @return true if the Game is a final Game
     */
    public boolean isFinal() {
        return (Integer.parseInt(gameInfo[6]) == 1);
    }
}