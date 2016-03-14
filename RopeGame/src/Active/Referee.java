package Active;

import Passive.Playground;
import Passive.RefereeSite;
import java.util.List;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Referee extends Thread {
    private RefereeState state;     // Referee state
    
    public Referee(String name) {
        super(name);
        
        this.state = RefereeState.START_OF_THE_MATCH;
    }

    public RefereeState getRefereeState() {
        return state;
    }

    public void setRefereeState(RefereeState state) {
        this.state = state;
    }
    
    @Override
    public void run() {
        boolean finishedTrial = false;
        // TODO: Replace true by terminating condition
        while(true) {
            switch(state) {
                case START_OF_THE_MATCH:
                    announceNewGame();
                    break;
                case START_OF_A_GAME:
                    callTrial();
                    break;
                case TEAMS_READY:
                    startTrial();
                    break;
                case WAIT_FOR_TRIAL_CONCLUSION:
                    finishedTrial = assertTrialDecision();
                    
                    if(finishedTrial == true) {
                        declareGameWinner();
                    } else {
                        callTrial();
                    }
                    break;
                case END_OF_A_GAME:
                    if(allGamesPlayed()) {
                        declareMatchWinner();
                    } else {
                        announceNewGame();
                    }
                    break;
                case END_OF_THE_MATCH:
                    break;
            }
        }
    }

    // TODO: Implement
    private void announceNewGame() {}

    // TODO: Implement
    private void callTrial() {}

    // TODO: Implement
    private void startTrial() {}

    /**
     * Decides the trial winner and steps the flag accordingly 
     * @return true if all trials over, false if more trials to play
     */
    private boolean assertTrialDecision() {
        Playground playground = Playground.getInstance();
        List<Contestant>[] teams = playground.getTeams();
        
        int teamA = 0, teamB = 0; // strength sum for each tam
        
        for (int i = 0; i < 2; i++) { // sum strengths for each team
            for (Contestant contestant : teams[i]) {
                teamA += contestant.getContestantStrength();
            }
        }
        
        RefereeSite refereesite = RefereeSite.getInstance();
        refereesite.setTrialRound(refereesite.getTrialRound() + 1);

        int [] trialpoints = refereesite.getTrialPoints();
        if(teamA == teamB){
            trialpoints[0] += 1;
            trialpoints[1] += 1;
            //TODO: logger trial draw
        } else if(teamA > teamB){
            trialpoints[0] += 1;
            //TODO: logger teamA wins trial
        } else {
            trialpoints[1] += 1;
            // TODO: logger teamB wins trial
        }
        
        refereesite.setTrialPoints(trialpoints);
        
        // check for knockout or winning Game by points
        return trialpoints[0] == 4
                || trialpoints[1] == 4
                || (trialpoints[0] + trialpoints[1] == 6);
 
    }

    private boolean allGamesPlayed(){
        RefereeSite refereesite = RefereeSite.getInstance();
        int[] gamePoints = refereesite.getGamePoints();
        return gamePoints[0] == 2
               || gamePoints[1] == 2
               || (gamePoints[0] + gamePoints[1] == 3);
    }
    
    /**
     * Decides the Game winner and sets the gamePoints accordingly
     * @return true if more games to play, false if all games ended
     */
    private void declareGameWinner() {
        RefereeSite refereesite = RefereeSite.getInstance();

       int[] trialpoints = refereesite.getTrialPoints();
       int[] gamePoints = refereesite.getGamePoints();
       
       int teamA = trialpoints[0];
       int teamB = trialpoints[1];
       
       if(teamA == teamB){
           gamePoints[0] += 1;
           gamePoints[1] += 1;
           // TODO: logger: draw
       } else if(teamA > teamB){
           gamePoints[0] += 1;
           // TODO: logger: teamA wins the Game
       } else {
           gamePoints[1] += 1;
           // TODO logger: teamB wins the Game
       }
       
       refereesite.setGamePoints(gamePoints);
       
    }

    // TODO: Implement
    private void declareMatchWinner() {
        RefereeSite refereesite = RefereeSite.getInstance();
        Playground playground = Playground.getInstance();
        
        int[] gamePoints = refereesite.getGamePoints();
        
        int teamA = gamePoints[0];
        int teamB = gamePoints[1];
        
        if(teamA == teamB){
           // match draw
       } else if(teamA > teamB){
           gamePoints[0] += 1;
           // teamA wins the match
       } else {
           gamePoints[1] += 1;
           // teamB wins the match
       }

    }
    
    public enum RefereeState {
        START_OF_THE_MATCH (1, "SOM"),
        START_OF_A_GAME (2, "SOG"),
        TEAMS_READY (3, "TRD"),
        WAIT_FOR_TRIAL_CONCLUSION (4, "WTC"),
        END_OF_A_GAME (5, "EOG"),
        END_OF_THE_MATCH (6, "EOM");
        
        private int id;
        private String state;
        
        RefereeState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        public int getId() {
            return id;
        }

        public String getState() {
            return state;
        }
    }
    
    public enum VictoryType {
        VictoryByPoints (1, "by points"),
        VictoryByKnockout (2, "by knocout"),
        Draw (3, "was a draw");
        
        private int id;
        private String type;
        
        VictoryType(int id, String type){
            this.id = id;
            this.type = type;
        }
        
        public String getType() {
            return type;
        }
    }
}
