package Others;

import java.io.Serializable;

/**
 * Generic Triple implementation
 * 
 * @author Eduardo Sousa - eduardosousa@ua.pt
 * @author Guilherme Cardoso - gjc@ua.pt
 * @version 2016-3
 * @param <X> 1st value
 * @param <Y> 2nd value
 * @param <Z> 3rd value
 * @param <W> 4th value
 */
public class Quadruple<X, Y, Z, W> implements Serializable {

    private static final long serialVersionUID = -1136253493056494680L;

    private final X first;
    private final Y second;
    private final Z third;
    private final W fourth;

    public Quadruple(X first, Y second, Z third, W fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public X getFirst() {
        return first;
    }

    public Y getSecond() {
        return second;
    }

    public Z getThird() {
        return third;
    }

    public W getFourth() {
        return fourth;
    }
}
