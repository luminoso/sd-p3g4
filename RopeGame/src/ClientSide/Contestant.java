package ClientSide;

import Others.Bench;
import Others.Ground;
import Others.InterfaceContestant;
import Others.Site;
import ServerSide.ContestantsBench;
import ServerSide.GeneralInformationRepository;
import ServerSide.Playground;
import ServerSide.RefereeSite;

/**
 * General Description:
 * This is an active class implements the Contestant and his interactions in the passive classes
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Contestant extends Thread implements Comparable<Contestant>, InterfaceContestant{
    

    private final Bench bench;
    private final Ground playground;
    private final Site refereeSite;
    
 // Internal state fields
    private ContestantState state;          // Contestant state
    private int strength;                   // Contestant strength
    private int team;                 // Contestant team
    private int id;                   // Contestant identification in team
    
    /**
     * Creates a Contestant instantiation for running local
     * @param name Name of the contestant
     * @param team Team of the contestant
     * @param id Id of the contestant
     * @param strength Strength of the contestant
     */
    public Contestant(String name, int team, int id, int strength) {
        this(name,team,id,strength,true);
    }
    
    /**
     * Creates a Contestant instantiation for running in a distributed enviroment
     *
     * @param name Name of the contestant
     * @param team Team of the contestant
     * @param id Id of the contestant
     * @param strength Strength of the contestant
     * @param runlocal
     */
    public Contestant(String name, int team, int id, int strength, boolean runlocal) {
        super(name);

        state = ContestantState.SEAT_AT_THE_BENCH;

        this.team = team;
        this.id = id;
        this.strength = strength;

        if (runlocal) {
            this.bench = ContestantsBench.getInstance(team);
            this.refereeSite = RefereeSite.getInstance();
            this.playground = Playground.getInstance();
        } else {
            this.bench = ContestantsBenchStub.getInstance(team);
            this.refereeSite = RefereeSiteStub.getInstance();
            this.playground = PlaygroundStub.getInstance();
        }
    }

    /**
     * Get the current Contestant state
     * @return Contestant state
     */
    @Override
    public ContestantState getContestantState() {
        return state;
    }

    /**
     * Sets the current Contestant state
     * @param state ContestantState
     */
    @Override
    public void setState(ContestantState state) {
        this.state = state;
    }

    /**
     * Gets the Contestant team number
     * @return contestant team number
     */
    @Override
    public int getTeam() {
        return team;
    }

     /**
     * Sets the current Contestant team
     * @param team of the contestant
     */
    @Override
    public void setTeam(int team) {
        this.team = team;
    }
    
    /**
     * Gets the Contestant id
     * @return contestant id number
     */
    @Override
    public int getContestatId() {
        return id;
    }

    /**
     * Sets the current Contestant id
     * @param id of the contestant
     */
    @Override
    public void setContestantId(int id) {
        this.id = id;
    }

    /**
     * Gets the Contestant strength
     * @return contestant strength 
     */
    @Override
    public int getStrength() {
        return strength;
    }
    
    /**
     * Sets the Contestant strength
     * @param strength contestant strength
     */
    @Override
    public void setStrength(int strength) {
        this.strength = strength;
    }
    
    /**
     * Runs this object thread
     */
    @Override
    public void run() {
        seatDown();
        
        while(!refereeSite.hasMatchEnded()) {
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
        bench.getContestant();
        
        if(!refereeSite.hasMatchEnded())
            playground.addContestant();
    }
    
    /**
     * Contestant gets ready.
     * Changes the Contestant state to DO_YOUR_BEST
     */
    private void getReady() {
        this.setState(ContestantState.DO_YOUR_BEST);
        GeneralInformationRepository.getInstance().printLineUpdate();
    }
 
    /**
     * Contestant pulls the rope
     */
    private void pullTheRope() {
        playground.pullRope();
    }

    /**
     * If contestant was playing moves to his bench and changes his 
     * state to SEAT_AT_THE_BENCH
     */
    private void seatDown() {
        playground.getContestant();
        bench.addContestant();
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
