
package turnierplanung.evaluation;

import turnierplanung.Plan;

/**
 *
 * @author michael
 */
public class Evaluation {
    private Plan plan;
    private int[][] a;
    private int[][] f;
    private int numIntervals;
    
    public Evaluation(Plan plan, int numIntervals) {
        this.plan = plan;
        a = new int[numIntervals][];
        this.numIntervals = numIntervals;
    }
    
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
    
    public void insertIntervalA(int[] array, int numInterval) {
        a[numInterval] = array;
    }
    
    // for testing
    public void printIntervalF() {
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < f[i].length; j++) {
                System.out.format("f[%d][%d] = %7d\n", i, j, f[i][j]);
            }
        }
    }
    
    public int[][] getIntervalA() {
        return a;
    }
    
    public int[][] getIntervalF() {
        return f;
    }
    
    public int getNumInervals() {
        return numIntervals;
    }
}