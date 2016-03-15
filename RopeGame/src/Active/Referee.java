package Active;

import Passive.Playground;
import Passive.RefereeSite;
import Passive.RefereeSite.GameScore;
import Passive.RefereeSite.TrialScore;
import RopeGame.Constants;
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
        
        state = RefereeState.START_OF_THE_MATCH;
    }

    public RefereeState getRefereeState() {
        return state;
    }

    public void setRefereeState(RefereeState state) {
        this.state = state;
    }
    
    @Override
    public void run() {
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
                    assertTrialDecision();
                    
                    if(checkForGameWinner()) {
                        declareGameWinner();
                    } else {
                        callTrial();
                    }
                    break;
                case END_OF_A_GAME:
                    if(checkForMatchWinner()) {
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

    /** 
     * Reset trial points and flag position when starting a new game
     */
    private void announceNewGame() {
        RefereeSite refereesite = RefereeSite.getInstance();
        refereesite.resetTrialPoints();
        
        this.setRefereeState(RefereeState.START_OF_A_GAME);
        
    }

    // TODO: Implement
    private void callTrial() {
    
        this.setRefereeState(RefereeState.TEAMS_READY);
    }

    // TODO: Implement
    private void startTrial() {
        
        this.setRefereeState(RefereeState.WAIT_FOR_TRIAL_CONCLUSION);
    }

    /**
     * Decides the trial winner and steps the flag accordingly 
     * @return true if all trials over, false if more trials to play
     */
    private void assertTrialDecision() {
        Playground playground = Playground.getInstance();
        RefereeSite site = RefereeSite.getInstance();
        
        int flagPosition = playground.getFlagPosition();
        
        if(flagPosition == 0) {
            site.addTrialPoint(TrialScore.DRAW);
        } else if(flagPosition < 0) {
            site.addTrialPoint(TrialScore.VICTORY_TEAM_1);
        } else {
            site.addTrialPoint(TrialScore.VICTORY_TEAM_2);
        }
    }
    
    /**
     * Decides the Game winner and sets the gamePoints accordingly
     * @return true if more games to play, false if all games ended
     */
    private void declareGameWinner() {
        RefereeSite site = RefereeSite.getInstance();
        List<TrialScore> trialPoints = site.getTrialPoints();
        
        int team1 = 0;
        int team2 = 0;
       
        for(TrialScore score : trialPoints){
            if(score == TrialScore.VICTORY_TEAM_1) {
                team1++;
            } else if(score == TrialScore.VICTORY_TEAM_2) {
                team2++;
            }
        }
        
        if(team1 == team2){
            site.addGamePoint(GameScore.DRAW);
            // TODO: logger: draw
        } else if(team1 > team2){
            if(site.getRemainingTrials() == 0) {
                site.addGamePoint(GameScore.VICTORY_TEAM_1_BY_POINTS);
            } else {
                site.addGamePoint(GameScore.VICTORY_TEAM_1_BY_KNOCKOUT);
            }
            // TODO: logger: team1 wins the Game
        } else {
            if(site.getRemainingTrials() == 0) {
                site.addGamePoint(GameScore.VICTORY_TEAM_1_BY_POINTS);
            } else {
                site.addGamePoint(GameScore.VICTORY_TEAM_1_BY_KNOCKOUT);
            }
            // TODO logger: team2 wins the Game
        }
        
        this.setRefereeState(RefereeState.END_OF_A_GAME);
    }

    // TODO: Implement
    private void declareMatchWinner() {
        RefereeSite refereesite = RefereeSite.getInstance();
       
        this.setRefereeState(RefereeState.END_OF_THE_MATCH);
    }

    private boolean checkForGameWinner() {
        RefereeSite site = RefereeSite.getInstance();
        List<TrialScore> trialScore = site.getTrialPoints();
        
        if(site.getRemainingTrials() == 0)
            return true;
        
        if(trialScore.size() >= (Math.floor(Constants.NUMBER_OF_TRIALS/2) + 1)) {
            int team1 = 0;
            int team2 = 0;
            
            for(TrialScore score : trialScore) {
                if(score == TrialScore.VICTORY_TEAM_1) {
                    team1++;
                } else {
                    team2++;
                }
            }
            
            if(Math.abs(team1 - team2) > site.getRemainingTrials())
                return true;
        }
        
        return false;
    }
    
    private boolean checkForMatchWinner(){
        RefereeSite site = RefereeSite.getInstance();
        List<GameScore> gamePoints = site.getGamePoints();
        
        int team1 = 0;
        int team2 = 0;
        
        for(GameScore score : gamePoints){
            if(score == GameScore.VICTORY_TEAM_1_BY_POINTS || 
                    score == GameScore.VICTORY_TEAM_1_BY_KNOCKOUT) {
                team1++;
            } else if(score == GameScore.VICTORY_TEAM_2_BY_POINTS || 
                    score == GameScore.VICTORY_TEAM_2_BY_KNOCKOUT) {
                team2++;
            }
        }
        
        return team1 == (Math.floor(Constants.NUMBER_OF_GAMES/2)+1) || 
                team2 == (Math.floor(Constants.NUMBER_OF_GAMES/2)+1) || 
                site.getRemainingGames() == 0;
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
}
