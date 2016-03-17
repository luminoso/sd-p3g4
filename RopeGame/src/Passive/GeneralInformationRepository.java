package Passive;

import Active.Coach;
import Active.Contestant;
import Active.Referee;
import Passive.RefereeSite.GameScore;
import Passive.RefereeSite.TrialScore;
import RopeGame.Constants;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class GeneralInformationRepository {
    private static GeneralInformationRepository instance;
    
    private final Lock lock;
    private PrintWriter printer;
    
    private final Set<Contestant>[] teams;
    private final Set<Coach> coaches;
    private Referee referee;
    
    private final List<Integer> team1Placement;     // list containing team contestants
    private final List<Integer> team2Placement;     // list containing team contestants
    
    private final List<GameScore> gameScore;        // list containing scores of the game
    private final List<TrialScore> trialScore;      // list containing scores of the trial
    
    private int flagPosition;                       // current flag position
    
    public static synchronized GeneralInformationRepository getInstance() {
        if(instance == null)
            instance = new GeneralInformationRepository();
        
        return instance;
    }
    
    private GeneralInformationRepository() {
        lock = new ReentrantLock();
        
        try {
            printer = new PrintWriter(Constants.FILE_NAME);
        } catch (FileNotFoundException ex) {
            printer = null;
        }
        
        teams = new Set[2];
        teams[0] = new TreeSet<>();
        teams[1] = new TreeSet<>();
        coaches = new TreeSet<>();
        
        gameScore = new LinkedList<>();
        trialScore = new LinkedList<>();
        
        team1Placement = new LinkedList<>();
        team2Placement = new LinkedList<>();
        
        flagPosition = 0;
    }
    
    public void addReferee(Referee referee) {
        lock.lock();
        
        this.referee = referee;
        
        lock.unlock();
    }
    
    public void addContestant(Contestant contestant) {
        lock.lock();
        
        this.teams[contestant.getContestantTeam()-1].add(contestant);
        
        lock.unlock();
    }
    
    public void addCoach(Coach coach) {
        lock.lock();
        
        this.coaches.add(coach);
        
        lock.unlock();
    }
    
    public void setGameScore(List<GameScore> gameScore) {
        lock.lock();
        
        this.gameScore.clear();
        this.gameScore.addAll(gameScore);
    
        lock.unlock();
    }
    
    public void setTrialScore(List<TrialScore> trialScore) {
        lock.lock();
        
        this.trialScore.clear();
        this.trialScore.addAll(trialScore);
    
        lock.unlock();
    }
    
    public void setFlagPosition(int flagPosition) {
        lock.lock();
        
        this.flagPosition = flagPosition;
    
        lock.unlock();
    }
    
    public void setTeamPlacement() {
        Contestant contestant = (Contestant) Thread.currentThread();
        
        lock.lock();
        
        if(contestant.getContestantTeam() == 1)
            team1Placement.add(contestant.getContestantId());
        else if(contestant.getContestantTeam() == 2)
            team2Placement.add(contestant.getContestantId());
    
        lock.unlock();
    }
    
    public void resetTeamPlacement() {
        lock.lock();
        
        team1Placement.clear();
        team2Placement.clear();
    
        lock.unlock();
    }
    
    public void printGameHeader() {
        lock.lock();
        
        printer.printf("Game %1d%n", gameScore.size() + 1);
        printColumnHeader();
        printer.flush();
    
        lock.unlock();
    }
    
    public void printLineUpdate() {
        lock.lock();
        
        printActiveEntitiesStates();
        printTrialResult(trialScore.size() + 1, flagPosition);
    
        lock.unlock();
    }
    
    public void printGameResult() {
        lock.lock();
        
        switch(gameScore.get(gameScore.size()-1)) {
            case VICTORY_TEAM_1_BY_KNOCKOUT:
                printGameWinnerByKnockOut(gameScore.size(), 1, trialScore.size());
                break;
            case VICTORY_TEAM_1_BY_POINTS:
                printGameWinnerByPoints(gameScore.size(), 1);
                break;
            case VICTORY_TEAM_2_BY_KNOCKOUT:
                printGameWinnerByKnockOut(gameScore.size(), 2, trialScore.size());
                break;
            case VICTORY_TEAM_2_BY_POINTS:
                printGameWinnerByPoints(gameScore.size(), 1);
                break;
            case DRAW:
                printGameDraw(gameScore.size());
                break;
        }
    
        lock.unlock();
    }
    
    public void printMatchWinner(int team, int score1, int score2) {
        lock.lock();
        
        printer.printf("Match was won by team %d (%d-%d).%n", team, score1, score2);
        printer.flush();
    
        lock.unlock();
    }
    
    public void printMatchDraw() {
        lock.lock();
        
        printer.printf("Match was a draw.%n");
        printer.flush();
    
        lock.unlock();
    }
    
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
    
    public void printHeader(){
        lock.lock();
        
        printer.printf("Game of the Rope - Description of the internal state%n");
        printer.printf("%n");
        printColumnHeader();       
        printActiveEntitiesStates();
        printEmptyResult();
        printer.flush();
    
        lock.unlock();
    }
    
    private void printColumnHeader() {
        lock.lock();
        
        printer.printf("Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Trial%n");
        printer.printf("Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS%n");
        printer.flush();
    
        lock.unlock();
    }
    
    private void printActiveEntitiesStates() {
        lock.lock();
        
        // Printing referee state
        printer.printf("%3s", referee.getRefereeState());
        
        // Printing teams state
        for(Coach coach : coaches) {
            printer.printf("  %4s", coach.getCoachState());
            
            for(Contestant contestant : teams[coach.getCoachTeam()-1]) {
                printer.printf(" %3s %2d", contestant.getContestantState(), contestant.getContestantStrength());
            }
        }
        
        printer.flush();
    
        lock.unlock();
    }
    
    private void printEmptyResult() {
        lock.lock();
        
        printer.printf(" - - - . - - - -- --%n");    
        printer.flush();
    
        lock.unlock();
    }
    
    private void printTrialResult(int trialNumber, int flagPosition) {
        lock.lock();
        
        for(int i = 0; i < Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND; i++) {
            if(i >= team1Placement.size())
                printer.printf(" -");
            else 
                printer.printf(" %1d", team1Placement.get(i));
        }
        
        printer.printf(" .");
        
        for(int i = 0; i < Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND; i++) {
            if(i >= team2Placement.size())
                printer.printf(" -");
            else 
                printer.printf(" %1d", team2Placement.get(i));
        }
        
        printer.printf(" %2d %2d%n", trialNumber, flagPosition);
        
        printer.flush();
    
        lock.unlock();
    }
    
    private void printGameWinnerByKnockOut(int game, int team, int trials) {
        lock.lock();
        
        printer.printf("Game %d was won by team %d by knock out in %d trials.%n", game, team, trials);
        printer.flush();
    
        lock.unlock();
    }
    
    private void printGameWinnerByPoints(int game, int team) {
        lock.lock();
        
        printer.printf("Game %d was won by team %d by points.%n", game, team);
        printer.flush();
    
        lock.unlock();
    }
    
    private void printGameDraw(int game) {
        lock.lock();
        
        printer.printf("Game %d was a draw.%n", game);
        printer.flush();
    
        lock.unlock();
    }    

    public void close() {
        lock.lock();
        
        printer.flush();
        printer.close();
    
        lock.unlock();
    }
} 
