package test;

import com.branow.serialization.FileText;
import com.branow.serialization.Serialization;
import com.branow.serialization.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SerializationTest {

    private final List<Card> cards = getCards();
    private final String dir = "src/test/java/test", name = "data";


    @Test
    public void testSerialization() throws SerializationException {
        Serialization<Pack[]> xml = Serialization.serializationXML(dir, name);
        Pack[] expected = getPacks();
        xml.serializate(expected);
        Pack[] actual = xml.deserializate();
        for (int i=0; i<expected.length; i++) {
            Assert.assertEquals(expected[i], actual[i]);
        }

    }

    @Test
    public void testSerializationIntegerMatrix() throws SerializationException {
        Integer[][] expected = {{6546, 132, 0},
                                {54, 5},
                                {87, 554, 879, 78 ,55},
                                {}};

        Serialization<Integer[][]> xml = Serialization.serializationXML(dir, name);
        xml.serializate(expected);
        Integer[][] actual = xml.deserializate();
        Assert.assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    public void testSerializationObjectMatrix() throws SerializationException {
        Card[][] expected = getCardsMat(0);
        Serialization<Card[][]> xml = Serialization.serializationXML(dir, name);
        xml.serializate(expected);
        Card[][] actual = xml.deserializate();
        Assert.assertTrue(Arrays.deepEquals(expected, actual));
        Assert.assertEquals(XML_TEXT_MATRIX_CARDS_0, new FileText(dir + File.separator + name + ".xml").read());
    }

    @Test
    public void testSerializationCard() throws SerializationException {
        Card expected = getCard(0);
        Serialization<Card> xml = Serialization.serializationXML(dir, name);
        xml.serializate(expected);
        Card actual = xml.deserializate();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(XML_TEXT_CARD_0, new FileText(dir + File.separator + name + ".xml").read());
    }


    private Pack[] getPacks() {
        return new Pack[] { new Pack(45, "pack1 r", getCardsMat(0)),
                            new Pack(54325, "3241das ", getCardsMat(1)),
                            new Pack(5432534, "   675", getCardsMat(2)),
                            new Pack(44, "p|\n", getCardsMat(3))};
    }

    private Card[][] getCardsMat(int index) {
        return switch (index) {
            case 0 -> new Card[][] {getCards(0)};
            case 1 -> new Card[][] {getCards(0), getCards(1)};
            case 2 -> new Card[][] {getCards(3), getCards(2), getCards(0)};
            default -> new Card[][] {};
        };
    }

    private Card[] getCards(int index) {
        return switch (index) {
            case 0 -> new Card[] {getCard(0)};
            case 1 -> new Card[] {getCard(0), getCard(1)};
            case 2 -> new Card[] {getCard(3), getCard(2), getCard(4)};
            default -> new Card[] {};
        };
    }

    private Card getCard(int index) {
        if (index < 0 || index > cards.size()) index = 0;
        return cards.get(index);
    }

    private List<Card> getCards() {
        return List.of( new Card(5, "card1", new Pack(421, "pack card1", new Card[][]{}), new Long[]{}, new Card()),

                        new Card(78, "card2", new Pack(12, "pack card2", new Card[][]{{new Card(5, "card1",
                                new Pack(421, "pack card1", new Card[][]{}), new Long[]{}, new Card())}}), new Long[]{56412L},
                                new Card(), new Card(5, "card1", new Pack(421, "pack card1", new Card[][]{}), new Long[]{}, new Card())),

                        new Card(0, "card3", new Pack(33, "pack card13 )", null), null),

                        new Card(1, "card4", new Pack(341, "pack card- ---{}]][*4", new Card[][]{{
                                new Card(0, "card3", new Pack(33, "pack card13 )", null), null),
                                new Card(0, "card3", new Pack(33, "pack card13 )", null), null),
                        }}), new Long[]{9841654L, 561154L}),

                        new Card(-89, "card5", new Pack(43, "pack card1     ", null), new Long[]{894651L},
                                new Card(0, "card3", new Pack(33, "pack card13 )", null), null),
                                new Card(0, "card3", new Pack(33, "pack card13 )", null), null))
                );
    }

    private final String XML_TEXT_MATRIX_CARDS_0 = "<array name=\"parent\" class=\"array:array:test.Card\">\n" +
            "\t<array name=\"item\" class=\"array:test.Card\">\n" +
            "\t\t<object name=\"item\" class=\"test.Card\">\n" +
            "\t\t\t<Integer name=\"id\" value=\"5\"/>\n" +
            "\t\t\t<String name=\"name\" value=\"card1\"/>\n" +
            "\t\t\t<array name=\"time\" class=\"array:Long\">\n" +
            "\t\t\t</array>\n" +
            "\t\t\t<array name=\"neighbours\" class=\"array:test.Card\">\n" +
            "\t\t\t\t<object name=\"item\" class=\"test.Card\">\n" +
            "\t\t\t\t</object>\n" +
            "\t\t\t</array>\n" +
            "\t\t\t<object name=\"parent\" class=\"test.Pack\">\n" +
            "\t\t\t\t<Long name=\"id\" value=\"421\"/>\n" +
            "\t\t\t\t<String name=\"name\" value=\"pack card1\"/>\n" +
            "\t\t\t\t<array name=\"cards\" class=\"array:array:test.Card\">\n" +
            "\t\t\t\t</array>\n" +
            "\t\t\t</object>\n" +
            "\t\t</object>\n" +
            "\t</array>\n" +
            "</array>\n";

    private final String XML_TEXT_CARD_0 = "<object name=\"parent\" class=\"test.Card\">\n" +
            "\t<Integer name=\"id\" value=\"5\"/>\n" +
            "\t<String name=\"name\" value=\"card1\"/>\n" +
            "\t<array name=\"time\" class=\"array:Long\">\n" +
            "\t</array>\n" +
            "\t<array name=\"neighbours\" class=\"array:test.Card\">\n" +
            "\t\t<object name=\"item\" class=\"test.Card\">\n" +
            "\t\t</object>\n" +
            "\t</array>\n" +
            "\t<object name=\"parent\" class=\"test.Pack\">\n" +
            "\t\t<Long name=\"id\" value=\"421\"/>\n" +
            "\t\t<String name=\"name\" value=\"pack card1\"/>\n" +
            "\t\t<array name=\"cards\" class=\"array:array:test.Card\">\n" +
            "\t\t</array>\n" +
            "\t</object>\n" +
            "</object>\n";

}
