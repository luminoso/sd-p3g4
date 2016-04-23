package Others;

import ClientSide.Contestant.ContestantState;

/**
 * Interface that defines the operations available over the objects that represent 
 * the contestants.
 * 
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public interface InterfaceContestant {
    /**
     * Gets the Contestant id.
     *
     * @return contestant id number
     */
    public int getContestantId();

    /**
     * Sets the current Contestant id.
     *
     * @param id of the contestant
     */
    public void setContestantId(int id);
    
    /**
     * Get the current Contestant state.
     *
     * @return Contestant state
     */
    public ContestantState getContestantState();

    /**
     * Sets the current Contestant state.
     *
     * @param state ContestantState
     */
    public void setContestantState(ContestantState state);

    /**
     * Gets the Contestant strength.
     *
     * @return contestant strength
     */
    public int getContestantStrength();

    /**
     * Sets the Contestant strength.
     *
     * @param strength contestant strength
     */
    public void setContestantStrength(int strength);

    /**
     * Gets the Contestant team number.
     *
     * @return contestant team number
     */
    public int getContestantTeam();

    /**
     * Sets the current Contestant team.
     *
     * @param team of the contestant
     */
    public void setContestantTeam(int team);
}
