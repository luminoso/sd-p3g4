package ClientSide;

import Others.Bench;
import Others.InterfaceCoach;
import Others.CoachStrategy;
import Others.Ground;
import Others.Site;
import ServerSide.ContestantsBench;
import ServerSide.Playground;
import ServerSide.RefereeSite;
import java.util.Set;

/**
 * General Description:
 * This is an active class implements the Coach and his interactions in the passive classes
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Coach extends Thread implements Comparable<Coach>, InterfaceCoach {
    private CoachState state;           // Coach state
    private int team;                   // Coach team
    private CoachStrategy strategy;     // Team picking strategy
    private final Bench bench;
    private final Site refereeSite;
    private final Ground playground;
 
    public Coach(String name, int team, CoachStrategy strategy) {
        this(name, team, strategy, true);
    }

    /**
     * Initialises a Coach instance
     * @param name Name of the coach
     * @param team Team of the coach
     * @param strategy Coach strategy
     * @param runlocal
     */
    public Coach(String name, int team, CoachStrategy strategy, boolean runlocal) {
        super(name);                    // Giving name to thread
        // Initial state
        this.state = CoachState.WAIT_FOR_REFEREE_COMMAND;
        this.team = team;               // Team assignement
        this.strategy = strategy;       // Team picking strategy

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
     * Get the current Coach state
     * @return CoachState
     */
    @Override
    public CoachState getCoachState() {
        return state;
    }

    /**
     *  Sets the current Coach state
     * @param state CoachState
     */
    @Override
    public void setState(CoachState state) {
        this.state = state;
    }
    
    /**
     * Gets the coach team number
     * @return coach team number
     */
    @Override
    public int getTeam() {
        return team;
    }

    /**
     *  Sets the current Coach team
     * @param team of the coach
     */
    @Override
    public void setTeam(int team) {
        this.team = team;
    }

    /**
     * Gets the coach strategy
     * @return coach strategy
     */
    @Override
    public CoachStrategy getStrategy() {
        return strategy;
    }

    /**
     *  Sets the current Coach state
     * @param strategy of the coach
     */
    @Override
    public void setStrategy(CoachStrategy strategy) {
        this.strategy = strategy;
    }
    
    
    /**
     * Runs this object thread
     */
    @Override
    public void run() {
        this.bench.waitForNextTrial();
        
        while(!refereeSite.hasMatchEnded()) {
            switch(state) {
                case WAIT_FOR_REFEREE_COMMAND:
                    callContestants();
                    break;
                case ASSEMBLE_TEAM:
                    informReferee();
                    break;
                case WATCH_TRIAL:
                    reviewNotes();
                    break;
            }
        }
    }

    /**
     * The coach decides which players are selected for next round
     * and updates selectedContestants array at the Bench
     */
    private void callContestants() {
        // Referee site
        RefereeSite site = RefereeSite.getInstance();
        
        // Picking team
        Set<Integer> pickedContestants = this.strategy.pickTeam(bench, site);
        
        // Setting the selected team
        bench.setSelectedContestants(pickedContestants);
        
        playground.checkTeamPlacement();
    }

    /**
     * Informs the Referee and watches the trial
     */
    private void informReferee() {
        refereeSite.informReferee();
        playground.watchTrial();
    }

    /**
     * The coach updates his players which have played and game and
     * updates their strength
     */
    private void reviewNotes() {
        Set<Integer> selectedContestants = bench.getSelectedContestants();
        Set<Contestant> allContestants = bench.getBench();
        
        if(allContestants != null) {
            for(Contestant contestant : allContestants) {
                if(selectedContestants.contains(contestant.getContestatId())) {
                    contestant.setStrength(contestant.getStrength() - 1);
                } else {
                    contestant.setStrength(contestant.getStrength() + 1);
                }
            }
        }
        bench.waitForNextTrial();
    }

    /**
     * Compares this coach to another coach.
     * Comparable implementation.
     * @param coach another coach to compare to
     * @return difference between two coaches
     */
    @Override
    public int compareTo(Coach coach) {
        return this.team - coach.team;
    }
    
    /**
     * Enums of possible Coach states
     */
    public enum CoachState {
        WAIT_FOR_REFEREE_COMMAND (1, "WFRC"),
        ASSEMBLE_TEAM (2, "AETM"),
        WATCH_TRIAL (3, "WHTL");
        
        private final int id;
        private final String state;
        
        /**
         * Create a CoachState enum
         * @param id of the enum Coach state
         * @param state Initials of the coach state
         */
        CoachState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        /**
         * Gets the ID of the CoachState enum
         * @return id of the coach state
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the enum Coach state
         * @return Coach state enum string
         */
        public String getState() {
            return state;
        }

        /**
         * Converts current Coach state to String
         * @return String describing Contestant sate
         */
        @Override
        public String toString() {
            return state;
        }
    }
}
