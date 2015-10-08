package turnierplanung;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import turnierplanung.evaluation.Evaluation;
import turnierplanung.evaluation.PairConstraint;
import turnierplanung.evaluation.Sequence;
import turnierplanung.evaluation.SingleConstraint;
import turnierplanung.parser.GameParser;
import turnierplanung.parser.SpacingParser;

/**
 * The TurnierPlanung class parses information about games, creates a plan and
 * searches for a good sequence respecting the given constraints.
 * 
 * @author Michael Jungo
 */
public final class TurnierPlanung {
    private static final int DEFAULT_GAME_DURATION = 1;
    private static final int NUM_SHUFFLE = 0;
    private static final int TIME_LIMIT = 60;
    private static final int NEIGHBOR_RANGE = 0;
    private static String gameFileName = "../inputSa.txt";
    private static String spacingFileName = "../inputSa-Min.txt";
    private static int minLag = 7;
    private static int maxLagStd = 9;
    private static int timeLimit = TIME_LIMIT;
    private static int numShuffle = NUM_SHUFFLE;
    private static int neighborRange = NEIGHBOR_RANGE;
    
    private TurnierPlanung() {
    }
    
    // Prints the teams, games and spacing of the plan
    private static void printPlan(Plan p) {
        System.out.println("*** Teams ***");
        for (Team t : p.getTeams()) {
            System.out.format("Team: %s - maxLag: %d\n", t.getId(), t.getMaxLag());
        }
        System.out.println("*** Games ***");
        for (Game g : p.getGames()) {
            String t1 = "TBA";
            String t2 = "TBA";
            if (g.getTeam1() != null) t1 = g.getTeam1().getId();
            if (g.getTeam2() != null) t2 = g.getTeam2().getId();
            System.out.format("Game: %s - %s vs. %s\n", g.getName(), t1, t2);
        }
        System.out.println("*** Spacings ***");
        for (Spacing s : p.getSpacings()) {
            System.out.format("%s before %s (%d Units)\n", s.getGame1().getName(),
                              s.getGame2().getName(), s.getSpace());
        }
    }
    
    // Calculates default lags
    private static void calculateLags(Plan p) {
        for (Team t : p.getTeams()) {
            int count = 0;
            for (Game g : p.getGames()) {
                if (g.getTeam1() == t || g.getTeam2() == t) count++;
            }
            int curMinLag = minLag;
            int curMaxLag = maxLagStd * (count - 1);
            t.setMinLag(curMinLag);
            t.setMaxLag(curMaxLag);
        }
    }
    
    // Parses game informations from a file
    private static void parseGame(String fileName, Plan plan) throws FileNotFoundException {
        GameParser parser = new GameParser(fileName);
        int id = 1;
        while (parser.next()) {
            parser.parseInfo();
            String name = parser.getName();
            String team1 = parser.getTeam1();
            String team2 = parser.getTeam2();
            int category = parser.getCategory();
            int earliest = parser.getEarliestStart();
            int latest = parser.getLatestStart();
            boolean isFinal = parser.isFinal();

            Team t1 = null;
            Team t2 = null;
            if (!team1.equals("")) {
                t1 = new Team(team1, category, 0, 0);
                plan.addTeam(t1);
            }
            if (!team2.equals("")) {
                t2 = new Team(team2, category, 0, 0);
                plan.addTeam(t2);
            }

            Game g = new Game(id, name, category, DEFAULT_GAME_DURATION,
                              earliest, latest, plan.getTeam(team1),
                              plan.getTeam(team2), isFinal);
            plan.addGame(g);
            id++;
        }
        calculateLags(plan);
    }
    
    // Parses spacing informations from a file
    private static void parseSpacing(String fileName, Plan plan) throws FileNotFoundException {
        SpacingParser parser = new SpacingParser(fileName);
        while (parser.next()) {
            parser.parseInfo();
            String game1 = parser.getGame1();
            String game2 = parser.getGame2();
            int space = parser.getSpace();
            Game g1 = plan.getGame(game1);
            Game g2 = plan.getGame(game2);

            Spacing s = new Spacing(g1, g2, space);
            g1.addSpacing(s);
            g2.addSpacing(s);
            plan.addSpacing(s);
        }
    }
    
    /**
     * Creates a plan with games, teams, constraints and a sequence.
     * The sequence is improved during a given time limit.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) gameFileName = args[0];
        if (args.length > 1) spacingFileName = args[1];
        if (args.length > 2) timeLimit = Integer.parseInt(args[2]);
        if (args.length > 3) neighborRange = Integer.parseInt(args[3]);
        if (args.length > 4) numShuffle = Integer.parseInt(args[4]);
        Plan plan = new Plan();
        
        try {         
            parseGame(gameFileName, plan);
            parseSpacing(spacingFileName, plan);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TurnierPlanung.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        
        int numGames = plan.getGames().size();
        if (neighborRange <= 0) neighborRange = numGames/3;
        if (numShuffle <= 0) numShuffle = numGames/4;
        
        // ensure graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                Constants.DEBUG = true;
                plan.getBestSequence().setScore(-1);
                plan.applySequence(plan.getBestSequence());
                plan.evaluate();
                Constants.DEBUG = false; //*/
                System.out.format("Best Solution found: %s (Score: %d)\n",
                                   plan.getBestSequence().toString(),
                                   plan.getBestSequence().getScore());
            }
        });
        
        // For testing
        printPlan(plan);
        
        Evaluation eval = new Evaluation(plan, 5);
        int a[][] = {{10000, 40000, 50000, 50000, 50000},
                     {1, 10, 100, 10000, 20000, 50000},
                     {1, 10, 100, 10000, 20000, 50000},
                     {1, 100, 2000, 20000, 50000, 50000},
                     {100, 5000, 50000, 50000, 50000, 50000}};
        for (int i = 0; i < a.length; i++) {
            eval.insertIntervalA(a[i], i);
        }
        eval.calculateIntervalF();
        // for testing
        eval.printIntervalF();
        
        for (Game g1 : plan.getGames()) {
            SingleConstraint singleConstr = new SingleConstraint(eval, plan, g1);
            plan.addSingleConstraint(singleConstr);
            for (Game g2 : plan.getGames()) {
                if (g1 == g2) continue;
                PairConstraint pairConstr = new PairConstraint(eval, plan, g1, g2);
                plan.addPairConstraint(pairConstr);
            }
        }
        plan.createInitialSequence();
        plan.evaluate();
        System.out.format("Initial sequence: %s (score: %d)\n", plan.getCurrentSequence().toString(), plan.getCurrentSequence().getScore());
        Sequence seq = plan.getCurrentSequence();
        long start = System.currentTimeMillis();
        long end = start + timeLimit * 1000;
        while (System.currentTimeMillis() < end) {
            while (plan.improveSequence(neighborRange)) {}
            seq = plan.shuffleSequence(seq, numShuffle);
            plan.applySequence(seq);
            plan.evaluate();
        }
    }
    
}
