package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import others.InterfaceContestant;
import others.Tuple;
import others.VectorTimestamp;

/**
 * Interface that defines the operations available over the objects that
 * represent the playground
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public interface InterfacePlayground extends Remote {

    /**
     * Adds a contestant to the playground
     *
     * @param id of the contestant
     * @param team of the contestant
     * @param status of the contestant
     * @param strength of the contestant
     * @param vt clock of the contestant
     * @return clock and new contestant state id
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> addContestant(int id, int team, int status, int strength, VectorTimestamp vt) throws RemoteException;

    /**
     * Checks if all contestants are ready to pull the rope
     *
     * @return true if every Contestant is in place to pull the rope
     */
    public boolean checkAllContestantsReady() throws RemoteException;

    /**
     * Synchronisation point for waiting for the teams to be ready
     *
     * @param team number
     * @param vt clock
     * @return tuple with updated clock and new coach state id
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> checkTeamPlacement(int team, VectorTimestamp vt) throws RemoteException;

    /**
     * Removes the contestant from the playground
     *
     * @param id of the contestant
     * @param team of the contestant
     * @param vt clock
     * @return updated clock
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp getContestant(int id, int team, VectorTimestamp vt) throws RemoteException;

    /**
     * The method returns the flag position in relation to the middle. Middle =
     * 0
     *
     * @param vt clock
     * @return updated clock and flag position
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> getFlagPosition(VectorTimestamp vt) throws RemoteException;

    /**
     * Gets the last flag position
     *
     * @param vt
     * @return flag position before the current position
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> getLastFlagPosition(VectorTimestamp vt) throws RemoteException;

    /**
     * Contestant pulls the rope
     *
     * @throws java.rmi.RemoteException
     */
    public void pullRope() throws RemoteException;

    /**
     * Synchronisation point for signalling the result is asserted
     *
     * @throws java.rmi.RemoteException
     */
    public void resultAsserted() throws RemoteException;

    /**
     * Sets the flag position
     *
     * @param flagPosition position of the flag
     * @param vt
     * @return
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp setFlagPosition(int flagPosition, VectorTimestamp vt) throws RemoteException;

    /**
     * Referee instructs the Contestants to start pulling the rope
     *
     * @param vt
     * @return updated clock and new referee state id
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> startPulling(VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronisation point for watching the trial in progress
     *
     * @param vt clock
     * @return with updated clock and updated coach state id
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> watchTrial(int team, VectorTimestamp vt) throws RemoteException;

    /**
     * Checks if the game should be shut down
     *
     * @return true if the game must be shut down
     * @throws java.rmi.RemoteException
     */
    public boolean shutdown() throws RemoteException;
}
