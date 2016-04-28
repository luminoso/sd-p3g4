package Others;

import Others.InterfaceContestant.ContestantState;

/**
 * Interface that defines the operations available over the objects that
 * represent the contestant.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public interface InterfaceContestant {

    /**
     * Gets the Contestant id.
     *
     * @return contestant id number
     */
    int getContestantId();

    /**
     * Sets the current Contestant id.
     *
     * @param id of the contestant
     */
    void setContestantId(int id);

    /**
     * Get the current Contestant state.
     *
     * @return Contestant state
     */
    ContestantState getContestantState();

    /**
     * Sets the current Contestant state.
     *
     * @param state ContestantState
     */
    void setContestantState(ContestantState state);

    /**
     * Gets the Contestant strength.
     *
     * @return contestant strength
     */
    int getContestantStrength();

    /**
     * Sets the Contestant strength.
     *
     * @param strength contestant strength
     */
    void setContestantStrength(int strength);

    /**
     * Gets the Contestant team number.
     *
     * @return contestant team number
     */
    int getContestantTeam();

    /**
     * Sets the current Contestant team.
     *
     * @param team of the contestant
     */
    void setContestantTeam(int team);

    /**
     * Enums of possible Contestant states
     */
    public enum ContestantState {
        /**
         *
         */
        SEAT_AT_THE_BENCH(1, "STB"),
        /**
         *
         */
        STAND_IN_POSITION(2, "SIP"),
        /**
         *
         */
        DO_YOUR_BEST(3, "DYB");

        /**
         *
         */
        private final int id;

        /**
         *
         */
        private final String state;

        /**
         * Create a ContestantState enum
         *
         * @param id of the enum Contestant state
         * @param state Initials of the Contestant state
         */
        ContestantState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        /**
         * Gets the ID of the ContestantState enum
         *
         * @return id of the Contestant state
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the enum Contestant state
         *
         * @return Contestant state enum string
         */
        public String getState() {
            return state;
        }

        /**
         * Converts current Contestant state to String
         *
         * @return String describing Contestant sate
         */
        @Override
        public String toString() {
            return state;
        }
    }
}
