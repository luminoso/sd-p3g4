package Others;

import ClientSide.Coach;
import ClientSide.Contestant;
import ClientSide.Referee;
import ServerSide.RefereeSite;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public interface InterfaceGeneralInformationRepository {

    /**
     * Adds a Coach to General Information Repository
     *
     * @param coach coach that will be added to the information repository
     */
    void addCoach(Coach coach);

    /**
     * Adds a Referee to General Information Repository
     *
     * @param contestant Contestant to add
     */
    void addContestant(Contestant contestant);

    /**
     * Adds a Referee to General Information Repository
     *
     * @param referee Referee to add
     */
    void addReferee(Referee referee);

    /**
     * Closes log file
     */
    void close();

    /**
     * Print game header
     */
    void printGameHeader();

    /**
     * Fully prints the game result
     */
    void printGameResult(RefereeSite.GameScore score);

    /**
     * Print General Information Repository header
     */
    void printHeader();

    /**
     * Prints game logger legend
     */
    void printLegend();

    /**
     * Fully prints a line with all the updates
     */
    void printLineUpdate();

    /**
     * Prints that was a draw
     */
    void printMatchDraw();

    /**
     * Print Match winner
     *
     * @param team that won
     * @param score1 score team 1
     * @param score2 score team 2
     */
    void printMatchWinner(int team, int score1, int score2);

    /**
     * Resets team placement
     */
    void resetTeamPlacement();

    /**
     * Sets flag position
     *
     * @param flagPosition to set
     */
    void setFlagPosition(int flagPosition);

    /**
     * Sets a game score
     *
     * @param gameNumber
     */
    void setGameNumber(int gameNumber);

    /**
     * Sets a team placement
     */
    void setTeamPlacement();

    /**
     * Sets a trial score score
     *
     * @param trialNumber
     */
    void setTrialNumber(int trialNumber);
}
