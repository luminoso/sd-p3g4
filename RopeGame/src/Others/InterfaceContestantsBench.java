/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import ClientSide.Contestant;
import java.util.List;
import java.util.Set;

/**
 *
 * @author luminoso
 * @param <T>
 */
public interface InterfaceContestantsBench {

    //FIXME: duvida a tirar com eduardo
    //public List<T> getBenches();
    List getBenches();

    /**
     * The method adds a contestant to the bench.
     *
     */
    void addContestant();

    /**
     * This method returns the bench which contains the Contestants
     *
     * @return List of the contestants in the bench
     */
    Set<Contestant> getBench();

    /**
     * The method removes a contestant from the bench.
     */
    void getContestant();

    /**
     * Gets the selected contestants to play
     *
     * @return Set with the selected contestants
     */
    Set<Integer> getSelectedContestants();

    /**
     * Synchronisation point where the Referee waits for the Coaches to pick the
     * teams
     */
    void pickYourTeam();

    /**
     * Set selected contestants array. This arrays should be filled with the IDs
     * of the players for the next round.
     *
     * @param selected identifiers for the selected players
     */
    void setSelectedContestants(Set<Integer> selected);

    /**
     * Synchronisation point where Coaches wait for the next trial instructed by
     * the Referee
     */
    void waitForNextTrial();

}
