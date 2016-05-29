package Others;

/**
 * Enums that describe the trial score.
 * 
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public enum TrialScore {
    DRAW(0, "D"),
    VICTORY_TEAM_1(1, "VT1"),
    VICTORY_TEAM_2(2, "VT2");

    private final int id;
    private final String status;

    /**
     * Initializes the trial score enum
     *
     * @param id of the trial
     * @param status of the trial
     */
    TrialScore(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    } 

    public String getStatus() {
        return status;
    }
    
    public static TrialScore getById(int id) {
        for(TrialScore score : values())
            if(score.id == id)
                return score;
        
        return null;
    }
}
