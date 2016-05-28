package Interfaces;

import Interfaces.InterfaceRefereeSite.GameScore;
import Interfaces.InterfaceRefereeSite.TrialScore;
import Others.Tuple;
import Others.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface that defines the operations available over the objects that
 * represent the referee site
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public interface InterfaceRefereeSite extends Remote {
    /**
     * The method allows to set the game points for both team
     *
     * @param score game points of both teams
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp addGamePoint(GameScore score, VectorTimestamp vt) throws RemoteException;

    /**
     * The method allows to set the trial points for both team
     *
     * @param score trial points of both teams
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp addTrialPoint(TrialScore score, VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronization point where the Referee waits for both teams to be ready
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp bothTeamsReady(VectorTimestamp vt) throws RemoteException;

    /**
     * The method returns the game points in the form of an array
     *
     * @param vt
     * @return game points
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, List<GameScore>> getGamePoints(VectorTimestamp vt) throws RemoteException;

    /**
     * Gets how many games are remaining to play
     *
     * @param vt
     * @return number of remaining games left
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> getRemainingGames(VectorTimestamp vt) throws RemoteException;

    /**
     * Gets how many trials are remaining to play
     *
     * @param vt
     * @return number of remaining trials left
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> getRemainingTrials(VectorTimestamp vt) throws RemoteException;

    /**
     * The method returns the trial points in the form of an array
     *
     * @param vt
     * @return trial points.
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, List<TrialScore>> getTrialPoints(VectorTimestamp vt) throws RemoteException;

    /**
     * Checks if the match has ended
     *
     * @param vt
     * @return true if no more matches to play. False if otherwise
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Boolean> hasMatchEnded(VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronisation point where the Coaches inform the Referee that they're
     * ready
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp informReferee(VectorTimestamp vt) throws RemoteException;

    /**
     * Resets the trial points
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp resetTrialPoints(VectorTimestamp vt) throws RemoteException;

    /**
     * Changes the information at RefereeSite if the match as ended
     *
     * @param hasMatchEnded true if match ended
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp setHasMatchEnded(boolean hasMatchEnded, VectorTimestamp vt) throws RemoteException;

    /**
     * Checks if the game should be shut down
     *
     * @return true if the game must be shut down
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Boolean> shutdown() throws RemoteException;

    /**
     * Enums that describe the trial score
     */
    enum TrialScore {
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
    }

    /**
     * Enums that describe the game score
     */
    enum GameScore {
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
    }
}
