package Active;

import Passive.ContestantsBench;
import Passive.Playground;

/**
 * General Description:
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Contestant extends Thread {
    
    // Final fields
    private final int team;                 // Contestant team
    private final int id;                   // Contestant identification in team
    
    // Internal state fields
    private ContestantState state;          // Contestant state
    private int strength;                   // Contestant strength
    
    /**
     * 
     * @param name
     * @param team
     * @param id
     * @param strength 
     */
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
    
    /**
     * Contestant checks if is selected to the game. 
     * If so, goes to the playground.
     */
    private void followCoachAdvice() {
        ContestantsBench.getInstance().getContestant();
        Playground.getInstance().addContestant();
            
        state = ContestantState.STAND_IN_POSITION;
    }
    
    // TODO: Implement	
    private void getReady() {
        this.state = ContestantState.DO_YOUR_BEST;
    }
 
    // TODO: Implement
    private void pullTheRope() {
        try {
            Thread.sleep((long) (Math.random()*3000));
        } catch (InterruptedException ex) {
            // TODO: Treat exception
        }
        
        Playground.getInstance().finishedPullingRope();
    }

    /**
     * If contestant was playing moves to his bench and changes his 
     * state to seat at the bench
     */
    private void seatDown() {
        Playground.getInstance().getContestant();
        ContestantsBench.getInstance().addContestant();
        
        this.state = ContestantState.SEAT_AT_THE_BENCH;
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
