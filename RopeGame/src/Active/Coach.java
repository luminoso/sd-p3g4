package Active;

/**
 *
 * @author Eduardo Sousa
 */
public class Coach extends Thread {
    public Coach(String name) {
        super(name);
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
