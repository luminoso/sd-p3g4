package others;


/**
 * Interface that defines the operations available over the objects that
 * represent the coach
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public interface InterfaceCoach {

    /**
     * Get the current Coach state
     *
     * @return coach state
     */
    CoachState getCoachState();

    /**
     * Sets the current Coach state
     *
     * @param state to set
     */
    void setCoachState(CoachState state);

    /**
     * Gets the coach team number
     *
     * @return coach team number
     */
    int getCoachTeam();

    /**
     * Sets the current Coach team
     *
     * @param team to set
     */
    void setCoachTeam(int team);

    /**
     * Gets the coach strategy
     *
     * @return coach strategy
     */
    CoachStrategy getCoachStrategy();

    /**
     * Sets the current Coach state
     *
     * @param strategy to set
     */
    void setCoachStrategy(CoachStrategy strategy);
}
