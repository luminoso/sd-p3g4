package Others;

import java.io.Serializable;

/**
 * Generic Tuple implementation
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 * @param <X> left value to be stored
 * @param <Y> right value to be stored
 */
public class Tuple<X, Y> implements Serializable {
    private final X first;
    private final Y second;

    /**
     * Tuple constructor
     *
     * @param left value to be stored
     * @param right value to be stored
     */
    public Tuple(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get left value
     *
     * @return left value
     */
    public X getFirst() {
        return first;
    }

    /**
     * Get right value
     *
     * @return right value
     */
    public Y getSecond() {
        return second;
    }
}
