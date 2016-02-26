package Active;

/**
 *
 * @author Eduardo Sousa
 */
public class Coach extends Thread {
    private CoachState state;   // Coach state
    private int team;           // Coach team
    
    
    public Coach(String name, int team) {
        super(name);
        
        this.state = CoachState.WAIT_FOR_REFEREE_COMMAND;
        
        this.team = team;
    }
    
    @Override
    public void run() {
        
    }
    
    public enum CoachState {
        WAIT_FOR_REFEREE_COMMAND (1, "WFRC"),
        ASSEMBLE_TEAM (2, "AETM"),
        WATCH_TRIAL (3, "WHTL");
        
        private int id;
        private String state;
        
        CoachState(int id, String state) {
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
