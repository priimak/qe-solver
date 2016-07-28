package net.priimak.numeric;

import java.util.Objects;

/**
 * Immutable class that represents a specific numeric value and associated error.
 *
 * @param <T> type of numeric value.
 */
public final class Value<T extends Number & Comparable<T>> {
    private final T value;
    private final T error;

    public Value(T value, T error) {
        this.value = value;
        this.error = error;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        } else if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }

        Value<?> other = (Value<?>) otherObject;
        return Objects.equals(value, other.value)
            && Objects.equals(error, other.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, error);
    }

    @Override
    public String toString() {
        return String.format("%s ± %s", value, error);
    }

    public T getValue() {
        return value;
    }

    public T getError() {
        return error;
    }
}
