/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Others;

import ClientSide.Referee.RefereeState;

/**
 *
 * @author luminoso
 */
public interface InterfaceReferee {

    /**
     * Get the current Referee state
     * @return Referee state
     */
    RefereeState getRefereeState();

    /**
     * Sets the current Referee state
     * @param state RefereeState
     */
    void setState(RefereeState state);
    
}
