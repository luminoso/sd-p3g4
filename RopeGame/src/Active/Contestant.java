package Active;

import Passive.ContestantsBench;
import Passive.Playground;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    /**
     * Contestant checks if is selected to the game. 
     * If so, goes to the playground.
     */
    private void followCoachAdvice() {
        ContestantsBench bench = ContestantsBench.getInstance(this.team);
        int[] selectedContestants = bench.getSelectedContestants();
        
        if(Arrays.asList(selectedContestants).contains(this.id)){
            Playground playground = Playground.getInstance();
            playground.addContestantToTeam(this.team, this);
            state = ContestantState.STAND_IN_POSITION;
        } else {
            state = ContestantState.SEAT_AT_THE_BENCH;
        }
        
    }
    
    // TODO: Implement	
    private void getReady() {
        
        if(this.state == ContestantState.STAND_IN_POSITION){
            this.state = ContestantState.DO_YOUR_BEST;
        }
            
    
    }
 
    // TODO: Implement
    private void pullTheRope() {
        
        if(this.state == ContestantState.DO_YOUR_BEST){
            try {
                Thread.sleep((long) (Math.random()*3000));
            } catch (InterruptedException ex) {
                Logger.getLogger(Contestant.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    
    }

    /**
     * If contestant was playing moves to his bench and changes his 
     * state to seat at the bench
     */
    private void seatDown() {

        if(this.state == ContestantState.DO_YOUR_BEST){
            ContestantsBench bench = ContestantsBench.getInstance(this.team);
            bench.addContestant(this);
            this.state = ContestantState.SEAT_AT_THE_BENCH;
        }
    
    }

    public void setStrength(int strength) {
        this.strength = strength;
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

    /**
     * This method returns the contestant strength
     * @return strength value
     */
    public int getStrength() {
        return strength;
    }
    
    /**
     * Compares this contestant strength to another contestant strength
     * @param contestant Contestant to compare to
     * @return Strength difference
     */
    @Override
    public int compareTo(Contestant contestant) {
        return contestant.getStrength() - this.strength;
    }
    
}
