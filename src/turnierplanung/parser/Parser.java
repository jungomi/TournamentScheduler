
package turnierplanung.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author michael
 */
public class Parser {
    private final FileReader fRead;
    private final BufferedReader bufRead;
    protected String line;
    
    public Parser(String fileName) throws FileNotFoundException {
        fRead = new FileReader(fileName);
        bufRead = new BufferedReader(fRead);
    }
    
    public boolean next() {
        try {
            line = bufRead.readLine();
        } catch (IOException ex) {
            return false;
        }
        if (line == null) return false;
        return true;
    }
    
    public String getLine() {
        return line;
    }
}