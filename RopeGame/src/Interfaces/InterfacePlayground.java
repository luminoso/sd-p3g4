package Interfaces;

import Others.InterfaceContestant;
import Others.Tuple;
import Others.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

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
     * The method adds a contestant to the playground
     * @param id
     * @param team
     * @param status
     * @param strength
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Integer> addContestant(int id, int team, int status, int strength, VectorTimestamp vt) throws RemoteException;

    /**
     * Checks if all contestants are ready to pull the rope
     *
     * @param vt
     * @return true if every Contestant is in place to pull the rope
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, Boolean> checkAllContestantsReady(VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronisation point for waiting for the teams to be ready
     * @param team
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp checkTeamPlacement(int team, VectorTimestamp vt) throws RemoteException;

    /**
     * The method removes the contestant from the playground
     * @param id
     * @param team
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp getContestant(int id, int team, VectorTimestamp vt) throws RemoteException;

    /**
     * The method returns the flag position in relation to the middle. Middle =
     * 0.
     *
     * @param vt
     * @return position of the flag
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
     * Gets the current teams in the playground
     *
     * @param vt
     * @return list containing both teams Contestants in the playground
     * @throws java.rmi.RemoteException
     */
    public Tuple<VectorTimestamp, List<InterfaceContestant>[]> getTeams(VectorTimestamp vt) throws RemoteException;

    /**
     * Checks if everyone pulled the rope
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp haveAllPulled(VectorTimestamp vt) throws RemoteException;

    /**
     * Contestant pulls the rope
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp pullRope(VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronisation point for signalling the result is asserted
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp resultAsserted(VectorTimestamp vt) throws RemoteException;

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
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp startPulling(VectorTimestamp vt) throws RemoteException;

    /**
     * Synchronisation point for watching the trial in progress
     * @param vt
     * @return 
     * @throws java.rmi.RemoteException
     */
    public VectorTimestamp watchTrial(VectorTimestamp vt) throws RemoteException;

    /**
     * Checks if the game should be shut down
     *
     * @return true if the game must be shut down
     * @throws java.rmi.RemoteException
     */
    public boolean shutdown() throws RemoteException;
}
