
package turnierplanung.parser;

import java.io.FileNotFoundException;

/**
 * The SpacingParser class provides functionalities to extract informations about
 * Spacings between Games.
 * 
 * @author Michael Jungo
 */
public class SpacingParser extends turnierplanung.parser.Parser {

    private String spacingInfo[];
    
    /**
     * Constructor.
     * 
     * @param fileName the path to the File
     * @throws FileNotFoundException 
     */
    public SpacingParser(String fileName) throws FileNotFoundException {
        super(fileName);
    }
    
    /**
     * Extracts the information from the current line.
     */
    public void parseInfo() {
        spacingInfo = line.split("\\t", -1);
    }
    
    /**
     * 
     * @return the first Game the Spacing applies to
     */
    public String getGame1() {
        return spacingInfo[0];
    }
    
    /**
     * 
     * @return the second Game the Spacing applies to
     */
    public String getGame2() {
        return spacingInfo[1];
    }
    
    /**
     * 
     * @return the space between the two Games
     */
    public int getSpace() {
        return Integer.parseInt(spacingInfo[2]);
    }
}
