package Active;

/**
 *
 * @author Eduardo Sousa
 */
public class Referee extends Thread {
    private RefereeState state;     // Referee state
    
    public Referee(String name) {
        super(name);
        
        this.state = RefereeState.START_OF_THE_MATCH;
    }
    
    @Override
    public void run() {
        
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
