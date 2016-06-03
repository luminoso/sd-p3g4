package interfaces;

import others.Triple;
import others.Tuple;
import others.VectorTimestamp;
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
     * Adds a contestant to the bench
     *
     * @param id of the contestant
     * @param team of the contestant
     * @param state Id of the contestant
     * @param strength of the contestant
     * @param vt clock
     * @return Triple with VT, ContestantState Id, Strength
     * @throws RemoteException
     */
    public Triple<VectorTimestamp, Integer, Integer> addContestant(int id, int team, int state, int strength, VectorTimestamp vt) throws RemoteException;

    /**
     * This method returns the bench which contains the contestants
     *
     * @param team bench to get
     * @param vt clock
     * @return tuple with clock and the Set with contestants iD, strength. State
     * is SEAT_AT_THE_BENCH
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Set<Tuple<Integer, Integer>>> getBench(int team, VectorTimestamp vt) throws RemoteException;

    /**
     * Removes a contestant from the bench.
     *
     * @param id of the contestant
     * @param team of the contestant
     * @throws java.rmi.RemoteException
     */
    public void getContestant(int id, int team) throws RemoteException;

    /**
     * Gets the selected contestants to play
     *
     * @param team of the selected contestants
     * @param vt clock
     * @return clock and an set with the selected contestants iDs
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Set<Integer>> getSelectedContestants(int team, VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronisation point where the Referee waits for the Coaches to pick the
     * teams
     *
     * @param team of the coach waiting
     * @throws java.rmi.RemoteException
     */
    public void pickYourTeam(int team) throws RemoteException;

    /**
     * Set selected contestants array. This arrays should be filled with the IDs
     * of the players for the next round.
     *
     * @param team of the selected contestants
     * @param selected iDs for the selected players
     * @param vt clock
     * @return updated clock
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp setSelectedContestants(int team, Set<Integer> selected, VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronisation point where Coaches wait for the next trial instructed by
     * the Referee
     *
     * @param team of the coach waiting
     * @param vt current clock
     * @return updated clock and coach state iD
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> waitForNextTrial(int team, int status, VectorTimestamp vt) throws RemoteException;

    /**
     * Updates the contestant strength
     *
     * @param id of the contestants to be updated
     * @param team of the contestant to be updated
     * @param delta difference to be applied to the contestant
     * @param vt clock
     * @return updated clock
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp updateContestantStrength(int id, int team, int delta, VectorTimestamp vt) throws RemoteException;

    /**
     * Sends an interrupt to shut down the game
     *
     * @param team to be interrupted
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
     *
     * @param team
     * @param vt clock
     * @return updated clock
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp waitForEveryoneToStart(int team, VectorTimestamp vt) throws RemoteException;

}
