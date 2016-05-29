package Others;

/**
 * Enums that describe the game score.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public enum GameScore {
    DRAW(0, "D"),
    VICTORY_TEAM_1_BY_POINTS(1, "VT1PT"),
    VICTORY_TEAM_1_BY_KNOCKOUT(2, "VT1KO"),
    VICTORY_TEAM_2_BY_POINTS(3, "VT2PT"),
    VICTORY_TEAM_2_BY_KNOCKOUT(4, "VT2KO");

    private final int id;
    private final String status;

    /**
     * Initializes the game score
     *
     * @param id of the score
     * @param status of the score
     */
    GameScore(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public static GameScore getById(int id) {
        for (GameScore score : values()) {
            if (score.id == id) {
                return score;
            }
        }

        return null;
    }
}
