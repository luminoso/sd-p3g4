package Others;

/**
 * Enums of possible contestant states.
 * 
 * @author ed1000
 */
public enum ContestantState {
    SEAT_AT_THE_BENCH(1, "STB"),
    STAND_IN_POSITION(2, "SIP"),
    DO_YOUR_BEST(3, "DYB");

    private final int id;
    private final String state;

    /**
     * Create a contestant state enum
     *
     * @param id of the enum contestant state
     * @param state initials of the contestant state
     */
    ContestantState(int id, String state) {
        this.id = id;
        this.state = state;
    }

    /**
     * Converts current contestant state to String
     *
     * @return string describing contestant sate
     */
    @Override
    public String toString() {
        return state;
    }
    
    public static ContestantState getById(int id) {
        for(ContestantState state : values())
            if(state.id == id)
                return state;
        
        return null;
    }
}
