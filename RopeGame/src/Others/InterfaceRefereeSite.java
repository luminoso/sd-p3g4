package Others;

import Others.InterfaceRefereeSite.GameScore;
import Others.InterfaceRefereeSite.TrialScore;
import java.util.List;

/**
 * Interface that defines the operations available over the objects that
 * represent the referee site.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public interface InterfaceRefereeSite {

    /**
     * The method allows to set the game points for both team.
     *
     * @param score Game points of both teams.
     */
    void addGamePoint(GameScore score);

    /**
     * The method allows to set the trial points for both team.
     *
     * @param score Trial points of both teams.
     */
    void addTrialPoint(TrialScore score);

    /**
     * Synchronization point where the Referee waits for both teams to be ready
     */
    void bothTeamsReady();

    /**
     * The method returns the game points in the form of an array.
     *
     * @return Game points.
     */
    List<GameScore> getGamePoints();

    /**
     * Gets how many games are remaining to play
     *
     * @return number of remaining games left
     */
    int getRemainingGames();

    /**
     * Gets how many trials are remaining to play
     *
     * @return number of remaining trials left
     */
    int getRemainingTrials();

    /**
     * The method returns the trial points in the form of an array.
     *
     * @return Trial points.
     */
    List<TrialScore> getTrialPoints();

    /**
     * Checks if the match has ended
     *
     * @return True if no more matches to play. False if otherwise.
     */
    boolean hasMatchEnded();

    /**
     * Synchronisation point where the Coaches inform the Referee that they're
     * ready
     */
    void informReferee();

    /**
     * Resets the trial points
     */
    void resetTrialPoints();

    /**
     * Changes the information at RefereeSite if the match as ended
     *
     * @param hasMatchEnded true if match ended
     */
    void setHasMatchEnded(boolean hasMatchEnded);

    /**
     * 
     */
    boolean shutdown();
    
    /**
     *
     */
    public enum TrialScore {

        DRAW(0, "D"),
        VICTORY_TEAM_1(1, "VT1"),
        VICTORY_TEAM_2(2, "VT2");

        private final int id;
        private final String status;

        /**
         * Initializes the TrialScore
         *
         * @param id of the trial
         * @param status of the trial
         */
        private TrialScore(int id, String status) {
            this.id = id;
            this.status = status;
        }

        /**
         *
         * @return
         */
        public int getId() {
            return this.id;
        }

        /**
         * Returns the
         *
         * @return
         */
        public String getStatus() {
            return this.status;
        }
    }

    /**
     * Enums that describe the GameScore
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
         * Initializes the GameScore
         *
         * @param id of the score
         * @param status of the score
         */
        private GameScore(int id, String status) {
            this.id = id;
            this.status = status;
        }

    }
}
