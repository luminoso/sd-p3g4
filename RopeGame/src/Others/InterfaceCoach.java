package Others;

import ClientSide.Coach.CoachState;

/**
 *
 * @author luminoso
 */
public interface InterfaceCoach {
    
    /**
     * Get the current Coach state
     *
     * @return CoachState
     */
    CoachState getCoachState();

    /**
     * Gets the coach team number
     *
     * @return coach team number
     */
    int getTeam();

    /**
     * Gets the coach strategy
     *
     * @return coach strategy
     */
    CoachStrategy getStrategy();

    /**
     * Sets the current Coach state
     *
     * @param state CoachState
     */
    void setState(CoachState state);

    /**
     * Sets the current Coach state
     *
     * @param strategy of the coach
     */
    void setStrategy(CoachStrategy strategy);

    /**
     * Sets the current Coach team
     *
     * @param team of the coach
     */
    void setTeam(int team);

}
