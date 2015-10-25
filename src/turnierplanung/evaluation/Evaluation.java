
package turnierplanung.evaluation;

/**
 * The Evaluation class represents a list of values to use for an evaluation.
 * 
 * @author Michael Jungo
 */
public class Evaluation {
    private int[][] a;
    private int[][] f;
    private final int numIntervals;
    
    /**
     * Constructor.
     * 
     * @param numIntervals the number of intervals
     */
    public Evaluation(int numIntervals) {
        a = new int[numIntervals][];
        this.numIntervals = numIntervals;
    }
    
    /**
     * Calculates the function values.
     */
    public void calculateIntervalF() {
        f = new int[a.length][];
        for (int i = 0; i < f.length; i++) {
            f[i] = new int[a[i].length + 1];
            f[i][0] = 0;
            for (int j = 1; j < f[i].length; j++) {
                f[i][j] = f[i][j-1] + a[i][j-1];
            }
        }
    }
    
    /**
     * Inserts the interval used to calculate the function values.
     * 
     * @param array the interval to be inserted
     * @param numInterval the number of the interval
     */
    public void insertIntervalA(int[] array, int numInterval) {
        a[numInterval] = array;
    }
    
    /**
     * 
     * @return the interval used to calculate the function values
     */
    public int[][] getIntervalA() {
        return a;
    }
    
    /**
     * 
     * @return the interval of function values
     */
    public int[][] getIntervalF() {
        return f;
    }
    
    /**
     * 
     * @return the number of intervals
     */
    public int getNumInervals() {
        return numIntervals;
    }
}