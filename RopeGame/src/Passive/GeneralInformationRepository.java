package Passive;

import Active.Coach;
import Active.Coach.CoachState;
import Active.Contestant;
import Active.Contestant.ContestantState;
import Active.Referee;
import Active.Referee.RefereeState;
import Others.Tuple;
import Passive.RefereeSite.GameScore;
import Passive.RefereeSite.TrialScore;
import RopeGame.Constants;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * General Description:
 * This is an passive class that logs entities activity
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class GeneralInformationRepository {
    private static GeneralInformationRepository instance;
    
    private final Lock lock;
    private PrintWriter printer;
    
    private List<Tuple<ContestantState, Integer>[]> teamsState;
    private CoachState[] coachesState;
    private RefereeState refereeState;
    
    private final List<Integer> team1Placement;     // list containing team contestants
    private final List<Integer> team2Placement;     // list containing team contestants
    
    private final List<GameScore> gameScore;        // list containing scores of the game
    private final List<TrialScore> trialScore;      // list containing scores of the trial
    
    private int flagPosition;                       // current flag position
    
    private final Condition canPrintGameResult;        // current 
    private boolean benchesFinishedPrinting;
    
    private String lastline;
    
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
        
        teamsState = new LinkedList<>();
        teamsState.add(new Tuple[5]);
        teamsState.add(new Tuple[5]);
        
        coachesState = new CoachState[2];
        
        gameScore = new LinkedList<>();
        trialScore = new LinkedList<>();
        
        team1Placement = new LinkedList<>();
        team2Placement = new LinkedList<>();
        
        canPrintGameResult = lock.newCondition();
        benchesFinishedPrinting = false;
        
        flagPosition = 0;
    }
    
    public void addReferee(Referee referee) {
        lock.lock();
        
        refereeState = referee.getRefereeState();
        
        lock.unlock();
    }
    
    public void addContestant(Contestant contestant) {
        lock.lock();
        
        int team = contestant.getContestantTeam() - 1;
        int id = contestant.getContestantId() - 1; 
        
        this.teamsState.get(team)[id] = new Tuple<>(contestant.getContestantState(), contestant.getContestantStrength());
        
        lock.unlock();
    }
    
    public void addCoach(Coach coach) {
        lock.lock();
        
        int team = coach.getCoachTeam() - 1;
        
        this.coachesState[team] = coach.getCoachState();
        
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
        
        benchesFinishedPrinting = true;
        canPrintGameResult.signal();
        
        printer.printf("Game %1d%n", gameScore.size() + 1);
        printColumnHeader();
        printer.flush();
    
        lock.unlock();
    }
    
    public void printLineUpdate() {
        Thread thread = Thread.currentThread();
        
        lock.lock();
        
        if(thread.getClass() == Contestant.class)
            addContestant((Contestant) thread);
        else if(thread.getClass() == Coach.class)
            addCoach((Coach) thread);
        else
            addReferee((Referee) thread);
        
        String string = "";
        
        string += printActiveEntitiesStates();
        string += printTrialResult(trialScore.size() + 1, flagPosition);
        
        printer.print(string);
        printer.flush();

        this.lastline = string;
    
        lock.unlock();
    }
    
    public void printGameResult() {
        lock.lock();
        
        /*
        if(!benchesFinishedPrinting){
            try {
                canPrintGameResult.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(GeneralInformationRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
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
        
        /*
        if(!benchesFinishedPrinting){
            try {
                canPrintGameResult.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(GeneralInformationRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        
        printer.printf("Match was won by team %d (%d-%d).%n", team, score1, score2);
        printer.flush();
    
        lock.unlock();
    }
    
    public void printMatchDraw() {
        lock.lock();
        
        /*
        if(!benchesFinishedPrinting){
            try {
                canPrintGameResult.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(GeneralInformationRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
        
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
        //printEmptyResult();
        printer.flush();
    
        lock.unlock();
    }
    
    private void printColumnHeader() {
        lock.lock();
        
        benchesFinishedPrinting = false;
        
        printer.printf("Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Trial%n");
        printer.printf("Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS%n");
        printer.flush();
        
        lock.unlock();
    }
    
    private String printActiveEntitiesStates() {
        lock.lock();
        
        String string = "";
        
        // Printing referee state
        string += String.format("%3s", refereeState);
        
        // Printing teams state
        for(int i = 0; i < coachesState.length; i++) {
            string += String.format("  %4s", coachesState[i]);
            
            for(int j = 0; j < teamsState.get(i).length; j++) {
                string += String.format(" %3s %2d", teamsState.get(i)[j].getX(), teamsState.get(i)[j].getY());
            }
        }
    
        lock.unlock();
        
        return string;
    }
    
    private void printEmptyResult() {
        lock.lock();
        
        printer.printf(" - - - . - - - -- --%n");    
        printer.flush();
    
        lock.unlock();
    }
    
    private String printTrialResult(int trialNumber, int flagPosition) {
        lock.lock();
        
        String string = "";
        
        for(int i = 0; i < Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND; i++) {
            if(i >= team1Placement.size())
                string += String.format(" -");
            else 
                string += String.format(" %1d", team1Placement.get(i));
        }
        
        string += " .";
        
        for(int i = 0; i < Constants.NUMBER_OF_PLAYERS_AT_PLAYGROUND; i++) {
            if(i >= team2Placement.size())
                string += String.format(" -");
            else 
                string += String.format(" %1d", team2Placement.get(i));
        }
        
        string += String.format(" %2d %2d%n", trialNumber, flagPosition);
        
        lock.unlock();
        
        return string;
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
