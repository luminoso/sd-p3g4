/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import ServerSide.RefereeSite.GameScore;
import ServerSide.RefereeSite.TrialScore;
import java.util.List;

/**
 *
 * @author luminoso
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

}
