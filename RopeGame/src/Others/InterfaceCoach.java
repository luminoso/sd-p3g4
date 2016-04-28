package Others;

import Others.InterfaceCoach.CoachState;

/**
 * Interface that defines the operations available over the objects that
 * represent the coach.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public interface InterfaceCoach {

    /**
     * Get the current Coach state
     *
     * @return CoachState
     */
    CoachState getCoachState();

    /**
     * Sets the current Coach state
     *
     * @param state CoachState
     */
    void setCoachState(CoachState state);

    /**
     * Gets the coach team number
     *
     * @return coach team number
     */
    int getCoachTeam();

    /**
     * Sets the current Coach team
     *
     * @param team of the coach
     */
    void setCoachTeam(int team);

    /**
     * Gets the coach strategy
     *
     * @return coach strategy
     */
    CoachStrategy getCoachStrategy();

    /**
     * Sets the current Coach state
     *
     * @param strategy of the coach
     */
    void setCoachStrategy(CoachStrategy strategy);

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
