package Others;

import ClientSide.Coach.CoachState;

/**
 *
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public interface InterfaceCoach {
    /**
     * Get the current Coach state
     *
     * @return CoachState
     */
    public CoachState getCoachState();

    /**
     * Sets the current Coach state
     *
     * @param state CoachState
     */
    public void setCoachState(CoachState state);
    
    /**
     * Gets the coach team number
     *
     * @return coach team number
     */
    public int getCoachTeam();

    /**
     * Sets the current Coach team
     *
     * @param team of the coach
     */
    public void setCoachTeam(int team);
    
    /**
     * Gets the coach strategy
     *
     * @return coach strategy
     */
    public CoachStrategy getCoachStrategy();

    /**
     * Sets the current Coach state
     *
     * @param strategy of the coach
     */
    public void setCoachStrategy(CoachStrategy strategy);
}
