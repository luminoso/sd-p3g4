package ClientSide;

import Others.CoachStrategy;
import Others.InterfaceCoach;
import Others.InterfaceContestantsBench;
import Others.InterfacePlayground;
import Others.InterfaceRefereeSite;
import RopeGame.Constants;
import java.util.Set;

/**
 * General Description: This is an active class implements the Coach and his
 * interactions in the passive classes
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Coach extends Thread implements Comparable<InterfaceCoach>, InterfaceCoach {

    /**
     *
     */
    private final InterfaceContestantsBench bench;

    /**
     *
     */
    private final InterfaceRefereeSite refereeSite;

    /**
     *
     */
    private final InterfacePlayground playground;

    /**
     *
     */
    private CoachState state;           // Coach state

    /**
     *
     */
    private int team;                   // Coach team

    /**
     *
     */
    private CoachStrategy strategy;     // Team picking strategy

    /**
     * Initialises a Coach instance
     *
     * @param name Name of the coach
     * @param team Team of the coach
     * @param strategy Coach strategy
     */
    public Coach(String name, int team, CoachStrategy strategy) {
        super(name);                    // Giving name to thread

        // Initial state
        state = CoachState.WAIT_FOR_REFEREE_COMMAND;

        this.team = team;               // Team assignement
        this.strategy = strategy;       // Team picking strategy

        bench = new ContestantsBenchStub(team);
        refereeSite = new RefereeSiteStub();
        playground = new PlaygroundStub();
    }

    /**
     * Get the current Coach state
     *
     * @return CoachState
     */
    @Override
    public CoachState getCoachState() {
        return state;
    }

    /**
     * Sets the current Coach state
     *
     * @param state CoachState
     */
    @Override
    public void setCoachState(CoachState state) {
        this.state = state;
    }

    /**
     * Gets the coach team number
     *
     * @return coach team number
     */
    @Override
    public int getCoachTeam() {
        return team;
    }

    /**
     * Sets the current Coach team
     *
     * @param team of the coach
     */
    @Override
    public void setCoachTeam(int team) {
        this.team = team;
    }

    /**
     * Gets the coach strategy
     *
     * @return coach strategy
     */
    @Override
    public CoachStrategy getCoachStrategy() {
        return strategy;
    }

    /**
     * Sets the current Coach state
     *
     * @param strategy of the coach
     */
    @Override
    public void setCoachStrategy(CoachStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Runs this object thread
     */
    @Override
    public void run() {
        bench.waitForNextTrial();

        while (!refereeSite.hasMatchEnded()) {
            switch (state) {
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
     * The coach decides which players are selected for next round and updates
     * selectedContestants array at the Bench
     */
    private void callContestants() {
        Set<Integer> pickedContestants = this.strategy.pickTeam(bench, refereeSite);
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
     * The coach updates his players which have played and game and updates
     * their strength
     */
    private void reviewNotes() {
        // Redo because can't pass active entities with messages like this
        // Sugestion: update message where if the ID isn't in the set
        //              update with +1, else update with -1
        Set<Integer> selectedContestants = bench.getSelectedContestants();

        for (int i = 1; i <= Constants.NUMBER_OF_PLAYERS_IN_THE_BENCH; i++) {
            if (selectedContestants.contains(i)) {
                // CB_Stub.updateStrength(i, -1);
            } else {
                // CB_Stub.updateStrength(i, +1);
            }
        }

        bench.waitForNextTrial();
    }

    /**
     * Compares this coach to another coach. Comparable implementation.
     *
     * @param coach another coach to compare to
     * @return difference between two coaches
     */
    @Override
    public int compareTo(InterfaceCoach coach) {
        return getCoachTeam() - coach.getCoachTeam();
    }

    /**
     * Enums of possible Coach states
     */
    public enum CoachState {
        WAIT_FOR_REFEREE_COMMAND(1, "WFRC"),
        ASSEMBLE_TEAM(2, "AETM"),
        WATCH_TRIAL(3, "WHTL");

        private final int id;
        private final String state;

        /**
         * Create a CoachState enum
         *
         * @param id of the enum Coach state
         * @param state Initials of the coach state
         */
        CoachState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        /**
         * Gets the ID of the CoachState enum
         *
         * @return id of the coach state
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the enum Coach state
         *
         * @return Coach state enum string
         */
        public String getState() {
            return state;
        }

        /**
         * Converts current Coach state to String
         *
         * @return String describing Contestant sate
         */
        @Override
        public String toString() {
            return state;
        }
    }
}
