package Others;

import ClientSide.Referee.RefereeState;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public interface InterfaceReferee {
    /**
     * Get the current Referee state
     *
     * @return Referee state
     */
    RefereeState getRefereeState();

    /**
     * Sets the current Referee state
     *
     * @param state RefereeState
     */
    void setRefereeState(RefereeState state);
}
