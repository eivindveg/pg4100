package no.westerdals.student.vegeiv13.pg4100.assignment2.models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    public static final String TEST_PLAYER = "TestPlayer";
    public static final int SCORE = 200;
    private Player player;

    @Before
    public void setup() {
        player = new Player();
        player.setName(TEST_PLAYER);
        player.setScore(SCORE);
        player.setId(Long.MAX_VALUE);
    }

    @Test
    public void testValidate() {
        boolean validate = Player.validate(player);
        assertTrue("Player with name validates correctly", validate);

        validate = Player.validate(new Player());
        assertFalse("Player with no name validates false", validate);

        validate = Player.validate(new Player(" "));
        assertFalse("Player with \" \" as name validates false", validate);
    }

    @Test
    public void testStandardGetters() {
        Long expectedLong = Long.MAX_VALUE;
        Long actualLong = player.getId();
        assertEquals(expectedLong, actualLong);

        String expected = TEST_PLAYER;
        String actual = player.getName();
        assertEquals(expected, actual);

        Integer expectedInt = SCORE;
        Integer actualInt = player.getScore();
        assertEquals(expectedInt, actualInt);
    }

    @Test
    public void testToString() {
        String expected = ToStringBuilder.reflectionToString(player);
        String actual = player.toString();

        assertEquals("Player conforms to toString style", expected, actual);
    }
}
