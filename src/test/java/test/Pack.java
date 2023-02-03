package test;

import java.util.Arrays;
import java.util.Objects;

public class Pack {

    public long id;
    public String name;
    public Card[][] cards;

    public Pack() {
    }

    public Pack(long id, String name, Card[][] cards) {
        this.id = id;
        this.name = name;
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cards=" + Arrays.deepToString(cards) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pack pack = (Pack) o;
        return id == pack.id && Objects.equals(name, pack.name) && Arrays.deepEquals(cards, pack.cards);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name);
        result = 31 * result + Arrays.hashCode(cards);
        return result;
    }
}
