package Active;

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
                    // TODO: Replace true by verifying if there is a winner
                    if(true) {
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

    // TODO: Implement
    private boolean assertTrialDecision() {
        return true;
    }

    // TODO: Implement
    private void declareGameWinner() {}

    // TODO: Implement
    private void declareMatchWinner() {}
    
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
