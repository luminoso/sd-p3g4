package Others;

import java.util.Set;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public interface InterfaceContestantsBench {
    /**
     * The method adds a contestant to the bench.
     *
     */
    public void addContestant();

    /**
     * This method returns the bench which contains the Contestants
     *
     * @return List of the contestants in the bench
     */
    public Set<InterfaceContestant> getBench();

    /**
     * The method removes a contestant from the bench.
     */
    public void getContestant();

    /**
     * Gets the selected contestants to play
     *
     * @return Set with the selected contestants
     */
    public Set<Integer> getSelectedContestants();

    /**
     * Synchronisation point where the Referee waits for the Coaches to pick the
     * teams
     */
    public void pickYourTeam();

    /**
     * Set selected contestants array. This arrays should be filled with the IDs
     * of the players for the next round.
     *
     * @param selected identifiers for the selected players
     */
    public void setSelectedContestants(Set<Integer> selected);

    /**
     * Synchronisation point where Coaches wait for the next trial instructed by
     * the Referee
     */
    public void waitForNextTrial();

    /**
     * 
     * @param id
     * @param delta 
     */
    public void updateContestantStrength(int id, int delta);
}
