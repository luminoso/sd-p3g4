package Interfaces;

import Others.Triple;
import Others.Tuple;
import Others.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * Interface that defines the operations available over the objects that
 * represent the contestants bench
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public interface InterfaceContestantsBench extends Remote {
    /**
     * The method adds a contestant to the bench
     * @param id
     * @param team
     * @param state
     * @param strength
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public Triple<VectorTimestamp, Integer, Integer> addContestant(int id, int team, int state, int strength, VectorTimestamp vt) throws RemoteException;

    /**
     * This method returns the bench which contains the contestants
     *
     * @param team
     * @param vt
     * @return list of the contestants in the bench
     * @throws java.rmi.RemoteException
     */
    public Set<Tuple<Integer, Integer>> getBench(int team) throws RemoteException;

    /**
     * The method removes a contestant from the bench.
     * @param id
     * @param team
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp getContestant(int id, int team, VectorTimestamp vt) throws RemoteException;

    /**
     * Gets the selected contestants to play
     *
     * @param team
     * @param vt
     * @return set with the selected contestants
     * @throws java.rmi.RemoteException
     */
    public Set<Integer> getSelectedContestants(int team) throws RemoteException;

    /**
     * Synchronisation point where the Referee waits for the Coaches to pick the
     * teams
     * @param team
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp pickYourTeam(int team, VectorTimestamp vt) throws RemoteException;

    /**
     * Set selected contestants array. This arrays should be filled with the IDs
     * of the players for the next round.
     *
     * @param team
     * @param selected identifiers for the selected players
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp setSelectedContestants(int team, Set<Integer> selected, VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronisation point where Coaches wait for the next trial instructed by
     * the Referee
     * @param team
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> waitForNextTrial(int team, VectorTimestamp vt) throws RemoteException;

    /**
     * Updates the contestant strength
     *
     * @param id of the contestants to be updated
     * @param team
     * @param delta difference to be applied to the contestant
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp updateContestantStrength(int id, int team, int delta, VectorTimestamp vt) throws RemoteException;

    /**
     * Sends an interrupt to shut down the game
     * @param team
     * @return 
     * @throws java.rmi.RemoteException
     */
    public void interrupt(int team) throws RemoteException;

    /**
     * Checks if the game should be shut down
     *
     * @return true if the game must be shut down
     * @throws java.rmi.RemoteException
     */
    public boolean shutdown() throws RemoteException;
    
    /**
     * The referee waits for everyone before starting first game
     * @param team
     * @throws java.rmi.RemoteException
     */
    public void waitForEveryoneToStart(int team) throws RemoteException;
}
