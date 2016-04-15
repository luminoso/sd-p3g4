/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import ClientSide.Contestant.ContestantState;

/**
 *
 * @author luminoso
 */
public interface InterfaceContestant {

    /**
     * Gets the Contestant id
     *
     * @return contestant id number
     */
    int getContestatId();

    /**
     * Sets the current Contestant id
     *
     * @param id of the contestant
     */
    void setContestantId(int id);

    /**
     * Get the current Contestant state
     *
     * @return Contestant state
     */
    ContestantState getContestantState();

    /**
     * Sets the current Contestant state
     *
     * @param state ContestantState
     */
    void setState(ContestantState state);

    /**
     * Gets the Contestant strength
     *
     * @return contestant strength
     */
    int getStrength();

    /**
     * Sets the Contestant strength
     *
     * @param strength contestant strength
     */
    void setStrength(int strength);

    /**
     * Gets the Contestant team number
     *
     * @return contestant team number
     */
    int getTeam();

    /**
     * Sets the current Contestant team
     *
     * @param team of the contestant
     */
    void setTeam(int team);

}
