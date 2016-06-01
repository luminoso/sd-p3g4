package Others;

/**
 * Interface that defines the operations available over the objects that
 * represent the referee
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 */
public interface InterfaceReferee {

    /**
     * Get the current referee state
     *
     * @return referee state
     */
    RefereeState getRefereeState();

    /**
     * Sets the current referee state
     *
     * @param state to set
     */
    void setRefereeState(RefereeState state);

    static RefereeState getState(int id) {

        for (RefereeState st : RefereeState.values()) {
            if (st.getId() == id) {
                return st;
            }
        }

        return null;
    }
}
