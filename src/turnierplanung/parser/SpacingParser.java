
package turnierplanung.parser;

import java.io.FileNotFoundException;

/**
 *
 * @author michael
 */
public class SpacingParser extends turnierplanung.parser.Parser {

    private String spacingInfo[];
    
    public SpacingParser(String fileName) throws FileNotFoundException {
        super(fileName);
    }
    
    public void parseInfo() {
        spacingInfo = line.split("\\t", -1);
    }
    
    public String getGame1() {
        return spacingInfo[0];
    }
    
    public String getGame2() {
        return spacingInfo[1];
    }
    
    public int getSpace() {
        return Integer.parseInt(spacingInfo[2]);
    }
    
}
