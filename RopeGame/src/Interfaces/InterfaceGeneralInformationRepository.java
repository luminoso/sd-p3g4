package Interfaces;

import Others.GameScore;
import Others.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that defines the operations available over the objects that
 * represent the general information repository.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public interface InterfaceGeneralInformationRepository extends Remote {

    /**
     * Adds a Coach to General Information Repository
     *
     * @param team
     * @param status
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void updateCoach(int team, int status, VectorTimestamp vt) throws RemoteException;

    /**
     * Adds a Referee to General Information Repository
     *
     * @param id
     * @param team
     * @param status
     * @param strength
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void updateContestant(int id, int team, int status, int strength, VectorTimestamp vt) throws RemoteException;

    /**
     * Updates the stored meta data about the strength of a contestant
     *
     * @param team of the contestant
     * @param id of the contestant
     * @param strength of the contestant
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void updateContestantStrength(int team, int id, int strength, VectorTimestamp vt) throws RemoteException;

    /**
     * Adds a Referee to General Information Repository
     *
     * @param status
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void updateReferee(int status, VectorTimestamp vt) throws RemoteException;

    /**
     * Prints an line with updated information about game state
     *
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void printLineUpdate(VectorTimestamp vt) throws RemoteException;

    /**
     * Closes log file
     *
     * @throws java.rmi.RemoteException
     */
    public void close() throws RemoteException;

    /**
     * Print game header
     *
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void printGameHeader(VectorTimestamp vt) throws RemoteException;

    /**
     * Fully prints the game result
     *
     * @param score to be printed
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void printGameResult(GameScore score, VectorTimestamp vt) throws RemoteException;

    /**
     * Print general information repository header
     *
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void printHeader(VectorTimestamp vt) throws RemoteException;

    /**
     * Prints game logger legend
     *
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void printLegend(VectorTimestamp vt) throws RemoteException;

    /**
     * Prints that was a draw
     *
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void printMatchDraw(VectorTimestamp vt) throws RemoteException;

    /**
     * Print Match winner
     *
     * @param team that won
     * @param score1 score team 1
     * @param score2 score team 2
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void printMatchWinner(int team, int score1, int score2, VectorTimestamp vt) throws RemoteException;

    /**
     * Resets team placement
     *
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void resetTeamPlacement(VectorTimestamp vt) throws RemoteException;

    /**
     * Sets flag position
     *
     * @param flagPosition to set
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void setFlagPosition(int flagPosition, VectorTimestamp vt) throws RemoteException;

    /**
     * Sets a game number
     *
     * @param gameNumber to set
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void setGameNumber(int gameNumber, VectorTimestamp vt) throws RemoteException;

    /**
     * Sets a team placement
     *
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void setTeamPlacement(VectorTimestamp vt) throws RemoteException;

    /**
     * Sets a trial score score
     *
     * @param trialNumber to set
     * @param vt
     * @throws java.rmi.RemoteException
     */
    public void setTrialNumber(int trialNumber, VectorTimestamp vt) throws RemoteException;

    /**
     * Checks if the game should be shut down
     *
     * @return true if the game must be shut down
     * @throws java.rmi.RemoteException
     */
    public boolean shutdown() throws RemoteException;
}
