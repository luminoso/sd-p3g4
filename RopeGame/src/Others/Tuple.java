package Others;

/**
 * Generic Tuple implementation
 * @author Eduardo Sousa
 * @author Guilherme Cardoso
 */
public class Tuple<X, Y> {
    private X left;
    private Y right;
    
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
