package agh.ics.oop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class Vector2dTest {

    @Test
    public void testToString() {
        assertEquals("(1,-2)", new Vector2d(1, -2).toString());
        assertEquals("(-40,1)", new Vector2d(-40, 1).toString());
    }

    @Test
    public void testPrecedes() {
        Vector2d first = new Vector2d(1, 2);
        Vector2d second = new Vector2d(1, 1);
        Vector2d third = new Vector2d(2, 1);

        assertTrue(first.precedes(first));
        assertFalse(first.precedes(second));
        assertFalse(first.precedes(third));
        assertTrue(second.precedes(first));
        assertTrue(second.precedes(third));
        assertFalse(third.precedes(first));
        assertFalse(third.precedes(second));
    }

    @Test
    public void testFollows() {
        Vector2d first = new Vector2d(1, 2);
        Vector2d second = new Vector2d(1, 1);
        Vector2d third = new Vector2d(2, 1);

        assertTrue(first.follows(first));
        assertTrue(first.follows(second));
        assertFalse(first.follows(third));
        assertFalse(second.follows(first));
        assertFalse(second.follows(third));
        assertFalse(third.follows(first));
        assertTrue(third.follows(second));
    }

    @Test
    public void testAdd() {
        Vector2d first = new Vector2d(3, 7);
        Vector2d second = new Vector2d(1, 4);
        Vector2d sum = new Vector2d(4, 11);

        assertEquals(sum, first.add(second));
        assertEquals(sum, second.add(first));
    }

    @Test
    public void testSubtract() {
        Vector2d first = new Vector2d(3, 7);
        Vector2d second = new Vector2d(1, 4);
        Vector2d diffA = new Vector2d(2, 3);
        Vector2d diffB = new Vector2d(-2, -3);

        assertEquals(diffA, first.subtract(second));
        assertEquals(diffB, second.subtract(first));
    }

    @Test
    public void testUpperRight() {
        Vector2d first = new Vector2d(5, 11);
        Vector2d second = new Vector2d(7, 6);
        Vector2d uRight = new Vector2d(7, 11);

        assertEquals(uRight, first.upperRight(second));
        assertEquals(uRight, second.upperRight(first));
    }

    @Test
    public void testLowerLeft() {
        Vector2d first = new Vector2d(5, 11);
        Vector2d second = new Vector2d(7, 6);
        Vector2d lLeft = new Vector2d(5, 6);

        assertEquals(lLeft, first.lowerLeft(second));
        assertEquals(lLeft, second.lowerLeft(first));
    }

    @Test
    public void testIsWithinBounds() {
        Boundary boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
        Vector2d inside = new Vector2d(5, 5);
        Vector2d outsideX = new Vector2d(11, 5);
        Vector2d outsideY = new Vector2d(5, 11);

        assertTrue(inside.isWithinBounds(boundary));
        assertFalse(outsideX.isWithinBounds(boundary));
        assertFalse(outsideY.isWithinBounds(boundary));
    }

    @Test
    public void testEqualsAndHashCode() {
        Vector2d vecA = new Vector2d(2, 3);
        Vector2d vecB = new Vector2d(2, 3);
        Vector2d vecC = new Vector2d(2, -3);

        assertEquals(vecA, vecB);
        assertEquals(vecA.hashCode(), vecB.hashCode());
        assertNotEquals(vecA, vecC);
        assertNotEquals(vecA.hashCode(), vecC.hashCode());
    }
}