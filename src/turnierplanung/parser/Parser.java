
package turnierplanung.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * The Parser class is providing functionalities to read a File and 
 * process its content.
 * 
 * @author Michael Jungo
 */
public class Parser {
    private final FileReader fRead;
    private final BufferedReader bufRead;
    protected String line;
    
    /**
     * Constructor.
     * 
     * @param fileName the path to the File
     * @throws FileNotFoundException 
     */
    public Parser(String fileName) throws FileNotFoundException {
        fRead = new FileReader(fileName);
        bufRead = new BufferedReader(fRead);
    }
    
    /**
     * Reads the next line.
     * 
     * @return true if there is a next line
     */
    public boolean next() {
        try {
            line = bufRead.readLine();
        } catch (IOException ex) {
            return false;
        }
        if (line == null) return false;
        return true;
    }
    
    /**
     * 
     * @return the current line
     */
    public String getLine() {
        return line;
    }
}