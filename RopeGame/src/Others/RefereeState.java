/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

/**
 * Enums of possible Referee states.
 * 
 * @author ed1000
 */
public enum RefereeState {
    START_OF_THE_MATCH(1, "SOM"),
    START_OF_A_GAME(2, "SOG"),
    TEAMS_READY(3, "TRD"),
    WAIT_FOR_TRIAL_CONCLUSION(4, "WTC"),
    END_OF_A_GAME(5, "EOG"),
    END_OF_THE_MATCH(6, "EOM");

    private final int id;
    private final String state;

    /**
     * Create a referee state enum
     *
     * @param id of the enum referee state
     * @param state initials of the referee state
     */
    RefereeState(int id, String state) {
        this.id = id;
        this.state = state;
    }

    /**
     * Converts current referee state to String
     *
     * @return string describing referee sate
     */
    @Override
    public String toString() {
        return state;
    }

    public int getId() {
        return this.id;
    }
    
    public static RefereeState getStateById(int id) {
        for(RefereeState state : values())
            if(state.getId() == id)
                return state;
        
        return null;
    }
}
