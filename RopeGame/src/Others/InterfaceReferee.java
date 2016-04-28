package Others;


/**
 * Interface that defines the operations available over the objects that
 * represent the referee.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public interface InterfaceReferee {

    /**
     * Get the current Referee state
     *
     * @return Referee state
     */
    RefereeState getRefereeState();

    /**
     * Sets the current Referee state
     *
     * @param state RefereeState
     */
    void setRefereeState(RefereeState state);
    
    /**
     * Enums of possible Referee states
     */
    public enum RefereeState {
        /**
         * 
         */
        START_OF_THE_MATCH(1, "SOM"),
        
        /**
         * 
         */
        START_OF_A_GAME(2, "SOG"),
        
        /**
         * 
         */
        TEAMS_READY(3, "TRD"),
        
        /**
         * 
         */
        WAIT_FOR_TRIAL_CONCLUSION(4, "WTC"),
        
        /**
         * 
         */
        END_OF_A_GAME(5, "EOG"),
        
        /**
         * 
         */
        END_OF_THE_MATCH(6, "EOM");

        /**
         * 
         */
        private final int id;
        
        /**
         * 
         */
        private final String state;

        /**
         * Create a RefereeState enum
         *
         * @param id of the enum Referee state
         * @param state Initials of the Referee state
         */
        RefereeState(int id, String state) {
            this.id = id;
            this.state = state;
        }

        /**
         * Gets the ID of the RefereeState enum
         *
         * @return id of the Referee state
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the enum Referee state
         *
         * @return Referee state enum string
         */
        public String getState() {
            return state;
        }

        /**
         * Converts current Referee state to String
         *
         * @return String describing Referee sate
         */
        @Override
        public String toString() {
            return state;
        }
    }
}
