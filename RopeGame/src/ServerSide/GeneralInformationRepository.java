package ServerSide;

import ClientSide.Coach;
import ClientSide.Coach.CoachState;
import ClientSide.Contestant;
import ClientSide.Contestant.ContestantState;
import ClientSide.Referee;
import ClientSide.Referee.RefereeState;
import Others.InterfaceCoach;
import Others.InterfaceContestant;
import Others.InterfaceGeneralInformationRepository;
import Others.InterfaceReferee;
import Others.Tuple;
import RopeGame.Constants;
import ServerSide.RefereeSite.GameScore;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * General Description: This is an passive class that logs entities activity
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class GeneralInformationRepository implements InterfaceGeneralInformationRepository {

    /**
     * 
     */
    private static GeneralInformationRepository instance;

    /**
     * 
     */
    private final Lock lock;
    
    /**
     * 
     */
    private PrintWriter printer;

    /**
     * 
     */
    private final List<Tuple<ContestantState, Integer>[]> teamsState;
    
    /**
     * 
     */
    private final CoachState[] coachesState;
    
    /**
     * 
     */
    private RefereeState refereeState;

    /**
     * 
     */
    private final List<Integer> team1Placement;     // list containing team contestants
    
    /**
     * 
     */
    private final List<Integer> team2Placement;     // list containing team contestants

    /**
     * 
     */
    private int gameNumber;                         // list containing scores of the game
    
    /**
     * 
     */
    private int trialNumber;                        // list containing scores of the trial

    /**
     * 
     */
    private int flagPosition;                       // current flag position

    /**
     * 
     * @return 
     */
    public static synchronized GeneralInformationRepository getInstance() {
        if (instance == null) {
            instance = new GeneralInformationRepository();
        }

        return instance;
    }

    /**
     * Private constructor
     */
    private GeneralInformationRepository() {
        lock = new ReentrantLock();

        try {
            printer = new PrintWriter(Constants.FILE_NAME);
        } catch (FileNotFoundException ex) {
            printer = null;
        }

        teamsState = new LinkedList<>();
        teamsState.add(new Tuple[Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH]);
        teamsState.add(new Tuple[Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH]);

        coachesState = new CoachState[2];

        gameNumber = 1;
        trialNumber = 1;

        team1Placement = new LinkedList<>();
        team2Placement = new LinkedList<>();

        flagPosition = 0;
    }

    /**
     * Adds a Referee to General Information Repository
     *
     * @param referee Referee to add
     */
    @Override
    public void addReferee(InterfaceReferee referee) {
        lock.lock();

        refereeState = referee.getRefereeState();

        lock.unlock();
    }

    /**
     * Adds a Referee to General Information Repository
     *
     * @param contestant Contestant to add
     */
    @Override
    public void addContestant(InterfaceContestant contestant) {
        lock.lock();

        int team = contestant.getContestantTeam()-1;
        int id = contestant.getContestantId()-1;

        this.teamsState.get(team)[id] = new Tuple<>(contestant.getContestantState(), contestant.getContestantStrength());

        lock.unlock();
    }

    /**
     * Adds a Coach to General Information Repository
     *
     * @param coach coach that will be added to the information repository
     */
    @Override
    public void addCoach(InterfaceCoach coach) {
        lock.lock();

        int team = coach.getCoachTeam() - 1;

        this.coachesState[team] = coach.getCoachState();

        lock.unlock();
    }

    /**
     * Sets a game score
     *
     * @param gameNumber
     */
    @Override
    public void setGameNumber(int gameNumber) {
        lock.lock();

        this.gameNumber = gameNumber;

        lock.unlock();
    }

    /**
     * Sets a trial score score
     *
     * @param trialNumber
     */
    @Override
    public void setTrialNumber(int trialNumber) {
        lock.lock();

        this.trialNumber = trialNumber;

        lock.unlock();
    }

    /**
     * Sets flag position
     *
     * @param flagPosition to set
     */
    @Override
    public void setFlagPosition(int flagPosition) {
        lock.lock();

        this.flagPosition = flagPosition;

        lock.unlock();
    }

    /**
     * Sets a team placement
     */
    @Override
    public void setTeamPlacement() {
        InterfaceContestant contestant = (InterfaceContestant) Thread.currentThread();

        lock.lock();

        if (contestant.getContestantTeam() == 1) {
            team1Placement.add(contestant.getContestantId());
        } else if (contestant.getContestantTeam() == 2) {
            team2Placement.add(contestant.getContestantId());
        }

        lock.unlock();
    }

    /**
     * Resets team placement
     */
    @Override
    public void resetTeamPlacement() {
        lock.lock();

        team1Placement.clear();
        team2Placement.clear();

        lock.unlock();
    }

    /**
     * Print game header
     */
    @Override
    public void printGameHeader() {
        lock.lock();

        printer.printf("Game %1d%n", gameNumber);
        printColumnHeader();
        printer.flush();

        lock.unlock();
    }

    /**
     * Fully prints a line with all the updates
     */
    @Override
    public void printLineUpdate() {
        Thread thread = Thread.currentThread();

        lock.lock();

        if (thread.getClass() == Contestant.class) {
            addContestant((Contestant) thread);
        } else if (thread.getClass() == Coach.class) {
            addCoach((Coach) thread);
        } else {
            addReferee((Referee) thread);
        }

        printActiveEntitiesStates();
        printTrialResult(trialNumber, flagPosition);

        printer.flush();

        lock.unlock();
    }

    /**
     * Fully prints the game result
     */
    @Override
    public void printGameResult(GameScore score) {
        lock.lock();

        switch (score) {
            case VICTORY_TEAM_1_BY_KNOCKOUT:
                printGameWinnerByKnockOut(gameNumber, 1, trialNumber);
                break;
            case VICTORY_TEAM_1_BY_POINTS:
                printGameWinnerByPoints(gameNumber, 1);
                break;
            case VICTORY_TEAM_2_BY_KNOCKOUT:
                printGameWinnerByKnockOut(gameNumber, 2, trialNumber);
                break;
            case VICTORY_TEAM_2_BY_POINTS:
                printGameWinnerByPoints(gameNumber, 1);
                break;
            case DRAW:
                printGameDraw(gameNumber);
                break;
        }

        lock.unlock();
    }

    /**
     * Print Match winner
     *
     * @param team that won
     * @param score1 score team 1
     * @param score2 score team 2
     */
    @Override
    public void printMatchWinner(int team, int score1, int score2) {
        lock.lock();

        printer.printf("Match was won by team %d (%d-%d).%n", team, score1, score2);
        printer.flush();

        lock.unlock();
    }

    /**
     * Prints that was a draw
     */
    @Override
    public void printMatchDraw() {
        lock.lock();

        printer.printf("Match was a draw.%n");
        printer.flush();

        lock.unlock();
    }

    /**
     * Prints game logger legend
     */
    @Override
    public void printLegend() {
        lock.lock();

        printer.printf("Legend:%n");
        printer.printf("Ref Sta – state of the referee%n");
        printer.printf("Coa # Stat - state of the coach of team # (# - 1 .. 2)%n");
        printer.printf("Cont # Sta – state of the contestant # (# - 1 .. 5) of team whose coach was listed to the immediate left%n");
        printer.printf("Cont # SG – strength of the contestant # (# - 1 .. 5) of team whose coach was listed to the immediate left%n");
        printer.printf("TRIAL – ? – contestant identification at the position ? at the end of the rope for present trial (? - 1 .. 3)%n");
        printer.printf("TRIAL – NB – trial number%n");
        printer.printf("TRIAL – PS – position of the centre of the rope at the beginning of the trial%n");
        printer.flush();

        lock.unlock();
    }

    /**
     * Print General Information Repository header
     */
    @Override
    public void printHeader() {
        lock.lock();

        printer.printf("Game of the Rope - Description of the internal state%n");
        printer.printf("%n");
        printColumnHeader();
        printActiveEntitiesStates();
        printEmptyResult();
        printer.flush();

        lock.unlock();
    }

    /**
     * Prints game column header
     */
    private void printColumnHeader() {
        lock.lock();

        printer.printf("Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Trial%n");
        printer.printf("Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS%n");
        printer.flush();

        lock.unlock();
    }

    /**
     * Prints active entities states
     *
     * @return a single string with all states
     */
    private void printActiveEntitiesStates() {
        lock.lock();

        printer.printf("%3s", refereeState);

        // Printing teams state
        for (int i = 0; i < coachesState.length; i++) {
            printer.printf("  %4s", coachesState[i]);

            for (int j = 0; j < teamsState.get(i).length; j++) {
                printer.printf(" %3s %2d", teamsState.get(i)[j].getLeft(), teamsState.get(i)[j].getRight());
            }
        }

        lock.unlock();
    }

    /**
     * Prints an empty result
     */
    private void printEmptyResult() {
        lock.lock();

        printer.printf(" - - - . - - - -- --%n");
        printer.flush();

        lock.unlock();
    }

    /**
     * Prints trial result
     *
     * @param trialNumber number of the trial
     * @param flagPosition position of the flag
     * @return a String with all the information in a single string
     */
    private void printTrialResult(int trialNumber, int flagPosition) {
        lock.lock();
        for (int i = 0; i < Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND; i++) {
            if (i >= team1Placement.size()) {
                printer.printf(" -");
            } else {
                printer.printf(" %1d", team1Placement.get(i));
            }
        }

        printer.printf(" .");

        for (int i = 0; i < Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND; i++) {
            if (i >= team2Placement.size()) {
                printer.printf(" -");
            } else {
                printer.printf(" %1d", team2Placement.get(i));
            }
        }

        printer.printf(" %2d %2d%n", trialNumber, flagPosition);

        lock.unlock();
    }

    /**
     * Prints a game winner by knock out
     *
     * @param game number of the game
     * @param team number of the team
     * @param trials in how many trials
     */
    private void printGameWinnerByKnockOut(int game, int team, int trials) {
        lock.lock();

        printer.printf("Game %d was won by team %d by knock out in %d trials.%n", game, team, trials);
        printer.flush();

        lock.unlock();
    }

    /**
     * Prints that a game was won by points
     *
     * @param game number of the game
     * @param team that won the game
     */
    private void printGameWinnerByPoints(int game, int team) {
        lock.lock();

        printer.printf("Game %d was won by team %d by points.%n", game, team);
        printer.flush();

        lock.unlock();
    }

    /**
     * Print that the game was a draw
     *
     * @param game
     */
    private void printGameDraw(int game) {
        lock.lock();

        printer.printf("Game %d was a draw.%n", game);
        printer.flush();

        lock.unlock();
    }

    /**
     * Closes log file
     */
    @Override
    public void close() {
        lock.lock();

        printer.flush();
        printer.close();

        lock.unlock();
    }
}
