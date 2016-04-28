package Others;

import java.io.Serializable;

/**
 * Generic Tuple implementation.
 *
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-2
 * @param <X> left value to be stored
 * @param <Y> right value to be stored
 */
public class Tuple<X, Y> implements Serializable {

    private final X left;
    private final Y right;

    public Tuple(X left, Y right) {
        this.left = left;
        this.right = right;
    }

    public X getLeft() {
        return left;
    }

    public Y getRight() {
        return right;
    }
}
