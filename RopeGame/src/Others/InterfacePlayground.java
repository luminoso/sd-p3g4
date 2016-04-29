package Others;

import java.util.List;

/**
 * Interface that defines the operations available over the objects that
 * represent the playground.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 */
public interface InterfacePlayground {

    /**
     * The method adds a contestant to the playground.
     *
     */
    void addContestant();

    /**
     * Checks if all contestants are ready to pull the rope
     *
     * @return true if every Contestant is in place to pull the rope
     */
    boolean checkAllContestantsReady();

    /**
     * Synchronisation point for waiting for the teams to be ready
     */
    void checkTeamPlacement();

    /**
     * The method removes the contestant from the playground.
     *
     */
    void getContestant();

    /**
     * The method returns the flag position in relation to the middle. Middle =
     * 0.
     *
     * @return position of the flag.
     */
    int getFlagPosition();

    /**
     * Gets the last flag position
     *
     * @return flag position before the current position
     */
    int getLastFlagPosition();

    /**
     * Gets the current teams in the playground
     *
     * @return List containing both teams Contestants in the playground
     */
    List<InterfaceContestant>[] getTeams();

    /**
     * Checks if everyone pulled the rope
     */
    void haveAllPulled();

    /**
     * Contestant pulls the rope
     */
    void pullRope();

    /**
     * Synchronisation point for signalling the result is asserted
     */
    void resultAsserted();

    /**
     * Sets the flag position
     *
     * @param flagPosition position of the flag
     */
    void setFlagPosition(int flagPosition);

    /**
     * Referee instructs the Contestants to start pulling the rope
     */
    void startPulling();

    /**
     * Synchronisation point for watching the trial in progress
     */
    void watchTrial();
    
    /**
     * 
     */
    boolean shutdown();
}
