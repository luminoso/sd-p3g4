package Active;

import Others.CoachStrategy;
import Passive.ContestantsBench;
import Passive.GeneralInformationRepository;
import Passive.Playground;
import Passive.RefereeSite;
import java.util.Set;

/**
 * General Description:
 * This is an active class implements the Coach and his interactions in the passive classes
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Coach extends Thread implements Comparable<Coach>{
    private CoachState state;           // Coach state
    private final int team;                   // Coach team
    private final CoachStrategy strategy;     // Team picking strategy
    
    /**
     * Initializes a Coach instance
     * @param name Name of the coach
     * @param team Team of the coach
     * @param strategy Coach strategy
     */
    public Coach(String name, int team, CoachStrategy strategy) {
        super(name);                    // Giving name to thread
        // Initial state
        this.state = CoachState.WAIT_FOR_REFEREE_COMMAND;
        this.team = team;               // Team assignement
        this.strategy = strategy;       // Team picking strategy
    }

    /**
     * Get the current Coach state
     * @return CoachState
     */
    public CoachState getCoachState() {
        return state;
    }

    /**
     *  Sets the current Coach state
     * @param state CoachState
     */
    public void setCoachState(CoachState state) {
        this.state = state;
    }
    
    /**
     * Gets the coach team number
     * @return coach team number
     */
    public int getCoachTeam() {
        return team;
    }
    
    /**
     * Runs this object thread
     */
    @Override
    public void run() {
        ContestantsBench.getInstance().waitForNextTrial();
        
        while(true) {
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
        // Contestants bench
        ContestantsBench bench = ContestantsBench.getInstance();
        
        // Referee site
        RefereeSite site = RefereeSite.getInstance();
        
        // Picking team
        Set<Integer> pickedContestants = this.strategy.pickTeam(bench, site);
        
        // Setting the selected team
        bench.setSelectedContestants(pickedContestants);
        
        Playground.getInstance().checkTeamPlacement();
    }

    /**
     * Informs the Referee and watches the trial
     */
    private void informReferee() {
        RefereeSite.getInstance().informReferee();
        Playground.getInstance().watchTrial();
    }

    /**
     * The coach updates his players which have played and game and
     * updates their strength
     */
    private void reviewNotes() {
        ContestantsBench bench = ContestantsBench.getInstance();
        Set<Integer> selectedContestants = bench.getSelectedContestants();
        Set<Contestant> allContestants = bench.getBench();
        
        for(Contestant contestant : allContestants) {
            if(selectedContestants.contains(contestant.getContestantId())) {
                contestant.setContestantStrength(contestant.getContestantStrength() - 1);
            } else {
                contestant.setContestantStrength(contestant.getContestantStrength() + 1);
            }
        }
        
        ContestantsBench.getInstance().waitForNextTrial();
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
