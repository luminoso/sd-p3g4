package Active;

/**
 *
 * @author Eduardo Sousa
 */
public class Contestant extends Thread {
    private ContestantState state;  // Contestant state
    private int team;               // Contestant team
    private int id;                 // Contestant identification in team
    private int strength;           // Contestant strength
    
    public Contestant(String name, int team, int id, int strength) {
        super(name);
        
        this.state = ContestantState.SEAT_AT_THE_BENCH;
        
        this.team = team;
        this.id = id;
        this.strength = strength;
    }
    
    @Override
    public void run() {
        
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
