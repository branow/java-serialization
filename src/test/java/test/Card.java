package test;

import java.util.Arrays;
import java.util.Objects;

public class Card {

    public Integer id;
    public String name;
    public Long[] time;
    public Card[] neighbours;
    public Pack parent;

    public Card() {
    }

    public Card(Integer id, String name, Pack parent, Long[] time, Card... neighbours) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.neighbours = neighbours;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + Arrays.toString(time) +
                ", neighbours=" + Arrays.toString(neighbours) +
                ", parent=" + parent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        if (!(Objects.equals(id, card.id) && Objects.equals(name, card.name) && Arrays.equals(time, card.time)
                && Arrays.equals(neighbours, card.neighbours) && Objects.equals(parent, card.parent))) {
            System.out.println(this);
            System.out.println(card);
            System.out.println(Arrays.equals(time, card.time));
            System.out.println(Arrays.equals(neighbours, card.neighbours));
            System.out.println(Objects.equals(parent, card.parent));
        }

        return Objects.equals(id, card.id) && Objects.equals(name, card.name) && Arrays.equals(time, card.time) &&
                Arrays.equals(neighbours, card.neighbours) && Objects.equals(parent, card.parent);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, parent);
        result = 31 * result + Arrays.hashCode(time);
        result = 31 * result + Arrays.hashCode(neighbours);
        return result;
    }
}
