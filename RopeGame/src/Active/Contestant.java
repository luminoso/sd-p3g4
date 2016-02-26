package Active;

/**
 *
 * @author Eduardo Sousa
 */
public class Contestant extends Thread {
    public Contestant(String name) {
        super(name);
    }
    
    
    public enum ContestantState {
        SEAT_AT_THE_BENCH (1, "STB"),
        STAND_IN_POSITION (2, "SIP"),
        DO_YOUR_BEST (3, "DYB");
        
        private int id;
        private String state;
        
        ContestantState(int id, String state) {
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
