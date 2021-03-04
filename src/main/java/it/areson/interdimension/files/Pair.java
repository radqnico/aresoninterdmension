package it.areson.interdimension.files;

/**
 * Just a pair of objects.
 * Use {@link Pair#of(Object, Object)} to instantiate a new pair.
 *
 * @param <A> First type.
 * @param <B> Second type.
 */
public class Pair<A, B> {

    private final A left;
    private final B right;

    private Pair(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public static <A, B> Pair<A, B> of(A left, B right) {
        return new Pair<>(left, right);
    }

    public A left() {
        return left;
    }

    public B right() {
        return right;
    }
}
