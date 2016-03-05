package Active;

import Passive.ContestantsBench;
import Passive.Playground;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Contestant extends Thread implements Comparable<Contestant>{
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

    public ContestantState getContestantState() {
        return state;
    }

    public void setContestantState(ContestantState state) {
        this.state = state;
    }

    public int getContestantTeam() {
        return team;
    }

    public int getContestantId() {
        return id;
    }

    public int getContestantStrength() {
        return strength;
    }
    
    public void setContestantStrength(int strength) {
        this.strength = strength;
    }
    
    @Override
    public void run() {
        // TODO: Replace true by terminating condition
        while(true) {
            switch(state) {
                case SEAT_AT_THE_BENCH:
                    followCoachAdvice();
                    break;
                case STAND_IN_POSITION:
                    getReady();
                    break;
                case DO_YOUR_BEST:
                    pullTheRope();
                    seatDown();
                    break;
            }
        }
    }

    // TODO: Implement
    private void followCoachAdvice() {
        
        state = ContestantState.STAND_IN_POSITION;
    }

    // TODO: Implement
    private void getReady() {}

    // TODO: Implement
    private void pullTheRope() {}

    // TODO: Implement
    private void seatDown() {}
    
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

    public int getStrength() {
        return strength;
    }
    
    // compare contestants by strength
    @Override
    public int compareTo(Contestant o) {
        return o.getStrength() - this.strength;
    }
    
}
