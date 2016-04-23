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
    public void addCoach(InterfaceCoach coach);

    /**
     * Adds a Referee to General Information Repository
     *
     * @param contestant Contestant to add
     */
    public void addContestant(InterfaceContestant contestant);

    /**
     * Adds a Referee to General Information Repository
     *
     * @param referee Referee to add
     */
    public void addReferee(InterfaceReferee referee);

    /**
     * Closes log file
     */
    public void close();

    /**
     * Print game header
     */
    public void printGameHeader();

    /**
     * Fully prints the game result
     */
    public void printGameResult(RefereeSite.GameScore score);

    /**
     * Print General Information Repository header
     */
    public void printHeader();

    /**
     * Prints game logger legend
     */
    public void printLegend();

    /**
     * Fully prints a line with all the updates
     */
    public void printLineUpdate();

    /**
     * Prints that was a draw
     */
    public void printMatchDraw();

    /**
     * Print Match winner
     *
     * @param team that won
     * @param score1 score team 1
     * @param score2 score team 2
     */
    public void printMatchWinner(int team, int score1, int score2);

    /**
     * Resets team placement
     */
    public void resetTeamPlacement();

    /**
     * Sets flag position
     *
     * @param flagPosition to set
     */
    public void setFlagPosition(int flagPosition);

    /**
     * Sets a game score
     *
     * @param gameNumber
     */
    public void setGameNumber(int gameNumber);

    /**
     * Sets a team placement
     */
    public void setTeamPlacement();

    /**
     * Sets a trial score score
     *
     * @param trialNumber
     */
    public void setTrialNumber(int trialNumber);
}
