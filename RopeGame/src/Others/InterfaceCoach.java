/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import ClientSide.Coach.CoachState;
import Others.CoachStrategy;

/**
 *
 * @author luminoso
 */
public interface InterfaceCoach {

    /**
     * Get the current Coach state
     * @return CoachState
     */
    CoachState getCoachState();

    /**
     * Gets the coach team number
     * @return coach team number
     */
    int getTeam();

    /**
     * Gets the coach strategy
     * @return coach strategy
     */
    CoachStrategy getStrategy();

    /**
     *  Sets the current Coach state
     * @param state CoachState
     */
    void setCoachState(CoachState state);

    /**
     *  Sets the current Coach state
     * @param strategy of the coach
     */
    void setStrategy(CoachStrategy strategy);

    /**
     *  Sets the current Coach team
     * @param team of the coach
     */
    void setTeam(int team);
    
}
