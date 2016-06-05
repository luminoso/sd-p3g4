package serverSide;

import interfaces.InterfaceGeneralInformationRepository;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import others.CoachState;
import others.Constants;
import others.ContestantState;
import others.GameScore;
import others.RefereeState;
import others.Tuple;
import others.VectorTimestamp;

/**
 * This is an passive class that logs entities activity
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public class GeneralInformationRepository implements InterfaceGeneralInformationRepository {
    // locking condtions
    private final Lock lock;

    // File writers
    private PrintWriter printer;
    
    // variables to store current game status and update accordingly to changes
    private final List<Tuple<ContestantState, Integer>[]> teamsState;
    private final CoachState[] coachesState;        // coaches state tracking
    private RefereeState refereeState;              // referee state tracking
    private final List<Integer> team1Placement;     // list containing team contestants
    private final List<Integer> team2Placement;     // list containing team contestants
    private int gameNumber;                         // list containing scores of the game
    private int trialNumber;                        // list containing scores of the trial
    private int flagPosition;                       // current flag position
    private boolean headerPrinted;
    private int shutdownVotes;

    private final List<LineUpdate> updates;
    
    /**
     * Private constructor for the singleton
     */
    public GeneralInformationRepository() {
        lock = new ReentrantLock();

        try {
            printer = new PrintWriter(Constants.FILE_NAME);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneralInformationRepository.class.getName()).log(Level.SEVERE, null, ex);
            printer = null;
        }

        headerPrinted = false;

        teamsState = new LinkedList<>();
        teamsState.add(new Tuple[Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH]);
        teamsState.add(new Tuple[Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH]);

        coachesState = new CoachState[2];

        gameNumber = 1;
        trialNumber = 1;

        team1Placement = new LinkedList<>();
        team2Placement = new LinkedList<>();

        flagPosition = 0;

        shutdownVotes = 0;
        
        updates = new ArrayList<>();
    }

    @Override
    public void updateReferee(int status, VectorTimestamp vt) {
        lock.lock();

        refereeState = RefereeState.getStateById(status);

        if(headerPrinted)
            printLineUpdate(vt);
        
        lock.unlock();
    }

    @Override
    public void updateContestant(int id, int team, int status, int strength, VectorTimestamp vt) {
        lock.lock();

        this.teamsState.get(team-1)[id-1] = new Tuple<>(
                ContestantState.getStateById(status), 
                strength);

        if(headerPrinted)
            printLineUpdate(vt);
        
        lock.unlock();
    }

    @Override
    public void updateContestantStrength(int team, int id, int strength, VectorTimestamp vt) {
        lock.lock();

        ContestantState state = teamsState.get(team - 1)[id - 1].getFirst();

        this.teamsState.get(team - 1)[id - 1] = new Tuple<>(state, strength);
        
        if(headerPrinted)
            printLineUpdate(vt);

        lock.unlock();
    }

    @Override
    public void updateCoach(int team, int status, VectorTimestamp vt) {
        lock.lock();

        this.coachesState[team-1] = CoachState.getStateById(status);
        
        if(headerPrinted)
            printLineUpdate(vt);

        lock.unlock();
    }

    @Override
    public void setGameNumber(int gameNumber, VectorTimestamp vt) {
        lock.lock();

        this.gameNumber = gameNumber;
        
        if(headerPrinted)
            printLineUpdate(vt);

        lock.unlock();
    }

    @Override
    public void setTrialNumber(int trialNumber, VectorTimestamp vt) {
        lock.lock();

        this.trialNumber = trialNumber;
        
        if(headerPrinted)
            printLineUpdate(vt);

        lock.unlock();
    }

    @Override
    public void setFlagPosition(int flagPosition, VectorTimestamp vt) {
        lock.lock();

        this.flagPosition = flagPosition;
        
        if(headerPrinted)
            printLineUpdate(vt);

        lock.unlock();
    }

    @Override
    public void setTeamPlacement(int id, int team, VectorTimestamp vt) {
        lock.lock();

        switch (team) {
            case 1:
                team1Placement.add(id);
                break;
            case 2:
                team2Placement.add(id);
                break;
            default:
                System.out.println("Error: team number");
                break;
        }
        
        if(headerPrinted)
            printLineUpdate(vt);

        lock.unlock();
    }

    @Override
    public void resetTeamPlacement(int id, int team, VectorTimestamp vt) {
        lock.lock();

        switch (team) {
            case 1:
                team1Placement.remove(team1Placement.indexOf(id));
                break;
            case 2:
                team2Placement.remove(team2Placement.indexOf(id));
                break;
            default:
                System.out.println("Error: team number");
                break;
        }

        if(headerPrinted)
            printLineUpdate(vt);
        
        lock.unlock();
    }

    @Override
    public void printGameHeader(VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        lock.lock();

        formatter.format("Game %d%n", gameNumber);
        strb.append(printColumnHeader());
        
        updates.add(new LineUpdate(strb.toString(), vt.clone()));

        lock.unlock();
    }

    @Override
    public void printGameResult(GameScore score, VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        
        lock.lock();

        switch (score) {
            case VICTORY_TEAM_1_BY_KNOCKOUT:
                strb.append(printGameWinnerByKnockOut(gameNumber, 1, trialNumber));
                break;
            case VICTORY_TEAM_1_BY_POINTS:
                strb.append(printGameWinnerByPoints(gameNumber, 1));
                break;
            case VICTORY_TEAM_2_BY_KNOCKOUT:
                strb.append(printGameWinnerByKnockOut(gameNumber, 2, trialNumber));
                break;
            case VICTORY_TEAM_2_BY_POINTS:
                strb.append(printGameWinnerByPoints(gameNumber, 1));
                break;
            case DRAW:
                strb.append(printGameDraw(gameNumber));
                break;
        }

        lock.unlock();
    }

    @Override
    public void printMatchWinner(int team, int score1, int score2, VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        lock.lock();

        formatter.format("Match was won by team %d (%d-%d).%n", team, score1, score2);
        
        updates.add(new LineUpdate(strb.toString(), vt.clone()));

        lock.unlock();
    }

    @Override
    public void printMatchDraw(VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        lock.lock();

        formatter.format("Match was a draw.%n");
        
        updates.add(new LineUpdate(strb.toString(), vt.clone()));

        lock.unlock();
    }

    @Override
    public void printLegend(VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        lock.lock();

        formatter.format("Legend:%n");
        formatter.format("Ref Sta       – state of the referee%n");
        formatter.format("Coa # Stat    – state of the coach of team # (# - 1 .. 2)%n");
        formatter.format("Cont # Sta    – state of the contestant # (# - 1 .. 5) of team whose coach was listed to the immediate left%n");
        formatter.format("Cont # SG     – strength of the contestant # (# - 1 .. 5) of team whose coach was listed to the immediate left%n");
        formatter.format("TRIAL – ?     – contestant identification at the position ? at the end of the rope for present trial (? - 1 .. 3)%n");
        formatter.format("TRIAL – NB    – trial number%n");
        formatter.format("TRIAL – PS    – position of the centre of the rope at the beginning of the trial%n");
        formatter.format("VCk  0        – local clock of the referee%n");
        formatter.format("VCk  1        – local clock of the coach of team 1%n");
        formatter.format("VCk  2        – local clock of the contestant 1 of team 1%n");
        formatter.format("VCk  3        – local clock of the contestant 2 of team 1%n");
        formatter.format("VCk  4        – local clock of the contestant 3 of team 1%n");
        formatter.format("VCk  5        – local clock of the contestant 4 of team 1%n");
        formatter.format("VCk  6        – local clock of the contestant 5 of team 1%n");
        formatter.format("VCk  7        – local clock of the coach of team 2%n");
        formatter.format("VCk  8        – local clock of the contestant 1 of team 2%n");
        formatter.format("VCk  9        – local clock of the contestant 2 of team 2%n");
        formatter.format("VCk 10        – local clock of the contestant 3 of team 2%n");
        formatter.format("VCk 11        – local clock of the contestant 4 of team 2%n");
        formatter.format("VCk 12        – local clock of the contestant 5 of team 2%n");
        
        updates.add(new LineUpdate(strb.toString(), vt.clone()));

        lock.unlock();
    }

    @Override
    public void printHeader(VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        lock.lock();

        formatter.format("Game of the Rope - Description of the internal state%n");
        formatter.format("%n");
        strb.append(printColumnHeader());
        strb.append(printActiveEntitiesStates());
        strb.append(printEmptyResult());

        headerPrinted = true;

        System.out.println("Header printed");
        
        updates.add(new LineUpdate(strb.toString(), vt.clone()));
        
        lock.unlock();
    }

    /**
     * Prints game column header
     */
    private String printColumnHeader() {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        formatter.format("Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5       Trial                           VCk                 %n");
        formatter.format("Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS  0  1  2  3  4  5  6  7  8  9 10 11 12%n");

        return strb.toString();
    }

    private void printLineUpdate(VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format(printActiveEntitiesStates());
        formatter.format(printTrialResult(trialNumber, flagPosition));
        formatter.format(printVectorTimeStamp(vt));

        updates.add(new LineUpdate(strb.toString(), vt.clone()));
    }
    
    /**
     * Prints active entities states
     */
    private String printActiveEntitiesStates() {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        formatter.format("%3s", refereeState);

        // Printing teams state
        for (int i = 0; i < coachesState.length; i++) {
            formatter.format("  %4s", coachesState[i]);
            
            for (int j = 0; j < teamsState.get(i).length; j++) {
                formatter.format(" %3s %2d", teamsState.get(i)[j].getFirst(), teamsState.get(i)[j].getSecond());
            }
        }

        return strb.toString();
    }

    /**
     * Prints an empty result
     */
    private String printEmptyResult() {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format(" - - - . - - - -- --%n");

        return strb.toString();
    }

    /**
     * Prints trial result
     *
     * @param trialNumber number of the trial
     * @param flagPosition position of the flag
     */
    private String printTrialResult(int trialNumber, int flagPosition) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        for (int i = 0; i < Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND; i++) {
            if (i >= team1Placement.size()) {
                formatter.format(" -");
            } else {
                formatter.format(" %1d", team1Placement.get(i));
            }
        }

        formatter.format(" .");

        for (int i = 0; i < Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND; i++) {
            if (i >= team2Placement.size()) {
                formatter.format(" -");
            } else {
                formatter.format(" %1d", team2Placement.get(i));
            }
        }

        formatter.format(" %2d %2d", trialNumber, flagPosition);

        return strb.toString();
    }

    private String printVectorTimeStamp(VectorTimestamp vt) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);
        
        int[] vtArray = vt.toIntArray();
        
        for(int i = 0; i < vtArray.length; i++)
            formatter.format(" %2d", vtArray[i]);
        
        formatter.format("%n");
        
        return strb.toString();
    }
    
    /**
     * Prints a game winner by knock out
     *
     * @param game number of the game
     * @param team number of the team
     * @param trials in how many trials
     */
    private String printGameWinnerByKnockOut(int game, int team, int trials) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format("Game %d was won by team %d by knock out in %d trials.%n", game, team, trials);

        return strb.toString();
    }

    /**
     * Prints that a game was won by points
     *
     * @param game number of the game
     * @param team that won the game
     */
    private String printGameWinnerByPoints(int game, int team) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format("Game %d was won by team %d by points.%n", game, team);

        return strb.toString();
    }

    /**
     * Print that the game was a draw
     *
     * @param game number that was a draw
     */
    private String printGameDraw(int game) {
        StringBuilder strb = new StringBuilder();
        Formatter formatter = new Formatter(strb);

        formatter.format("Game %d was a draw.%n", game);

        return strb.toString();
    }

    @Override
    public void close() {
        lock.lock();
        
        LineUpdate[] tempUpdates = updates.toArray(new LineUpdate[updates.size()]);
        
        for(int i = tempUpdates.length-2; i >= 1; i--) {
            for(int j = 0; j < i; j++) {
                if(tempUpdates[j].compareTo(tempUpdates[j+1]) > 0) {
                    LineUpdate temp = tempUpdates[j];
                    tempUpdates[j] = tempUpdates[j+1];
                    tempUpdates[j+1] = temp;
                }
            }
        }
        
        for(LineUpdate up : tempUpdates)
            printer.print(up.getLine());
        
        printer.flush();
        printer.close();

        System.out.println("Finished printing");
        
        lock.unlock();
    }

    @Override
    public boolean shutdown() {
        boolean result = false;

        lock.lock();

        shutdownVotes++;

        if (shutdownVotes == (1 + 2 * (1 + Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH))) {
            result = true;
            close();
        }

        lock.unlock();

        return result;
    }
}
