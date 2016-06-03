/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package others;

/**
 * Enums of possible Coach states
 * 
 * @author ed1000
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
     * @param id of the enum coach state
     * @param state initials of the coach state
     */
    CoachState(int id, String state) {
        this.id = id;
        this.state = state;
    }

    public int getId() {
        return this.id;
    }

    public String getState() {
        return state;
    }

    public static CoachState getStateById(int id) {
        for (CoachState st : CoachState.values())
            if (st.getId() == id)
                return st;

        return null;
    }
}
