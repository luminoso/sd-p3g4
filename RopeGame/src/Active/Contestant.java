package Active;

import Passive.ContestantsBench;
import Passive.GeneralInformationRepository;
import Passive.Playground;
import Passive.RefereeSite;

/**
 * General Description:
 * This is an active class implements the Contestant and his interactions in the passive classes
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Contestant extends Thread implements Comparable<Contestant>{
    
    // Final fields
    private final int team;                 // Contestant team
    private final int id;                   // Contestant identification in team
    
    // Internal state fields
    private ContestantState state;          // Contestant state
    private int strength;                   // Contestant strength
    
    /**
     * Creates a Contestant instantiation 
     * @param name Name of the contestant
     * @param team Team of the contestant
     * @param id Id of the contestant
     * @param strength Strength of the contestant
     */
    public Contestant(String name, int team, int id, int strength) {
        super(name);
        
        state = ContestantState.SEAT_AT_THE_BENCH;
        
        this.team = team;
        this.id = id;
        this.strength = strength;
    }

    /**
     * Get the current Contestant state
     * @return Contestant state
     */
    public ContestantState getContestantState() {
        return state;
    }

    /**
     * Sets the current Contestant state
     * @param state ContestantState
     */
    public void setContestantState(ContestantState state) {
        this.state = state;
    }

    /**
     * Gets the Contestant team number
     * @return contestant team number
     */
    public int getContestantTeam() {
        return team;
    }

    /**
     * Gets the Contestant id
     * @return contestant id number
     */
    public int getContestantId() {
        return id;
    }

    /**
     * Gets the Contestant strength
     * @return contestant strength 
     */
    public int getContestantStrength() {
        return strength;
    }
    
    /**
     * Sets the Contestant strength
     * @param strength contestant strength
     */
    public void setContestantStrength(int strength) {
        this.strength = strength;
    }
    
    /**
     * Runs this object thread
     */
    @Override
    public void run() {
        seatDown();
        
        while(!RefereeSite.getInstance().hasMatchEnded()) {
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
        
        if(!RefereeSite.getInstance().hasMatchEnded())
            Playground.getInstance().addContestant();
    }
    
    /**
     * Contestant gets ready.
     * Changes the Contestant state to DO_YOUR_BEST
     */
    private void getReady() {
        this.setContestantState(ContestantState.DO_YOUR_BEST);
        GeneralInformationRepository.getInstance().printLineUpdate();
    }
 
    /**
     * Contestant pulls the rope
     */
    private void pullTheRope() {
        Playground.getInstance().pullRope();
    }

    /**
     * If contestant was playing moves to his bench and changes his 
     * state to SEAT_AT_THE_BENCH
     */
    private void seatDown() {
        Playground.getInstance().getContestant();
        ContestantsBench.getInstance().addContestant();
    }

    /**
     * Compares this Contestant to another Contestant.
     * Comparable implementation.
     * @param contestant another Contestant to compare to
     * @return contestant difference
     */
    @Override
    public int compareTo(Contestant contestant) {
        return this.id - contestant.id;
    }
    
    /**
     * Enums of possible Contestant states
     */
    public enum ContestantState {
        SEAT_AT_THE_BENCH (1, "STB"),
        STAND_IN_POSITION (2, "SIP"),
        DO_YOUR_BEST (3, "DYB");
        
        private final int id;
        private final String state;
        
        /**
         * Create a ContestantState enum
         * @param id of the enum Contestant state
         * @param state Initials of the Contestant state
         */
        ContestantState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        /**
         * Gets the ID of the ContestantState enum
         * @return id of the Contestant state
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the enum Contestant state
         * @return Contestant state enum string
         */
        public String getState() {
            return state;
        }
        
        /**
         * Converts current Contestant state to String
         * @return String describing Contestant sate
         */
        @Override
        public String toString() {
            return state;
        }
    }
}
