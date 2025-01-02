package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
	/*
	@Test
	public void testToString() {
		assertTrue((new Vector2d(1,-2)).toString().equals("(1,-2)"));
		assertTrue((new Vector2d(-40,1)).toString().equals("(-40,1)"));
	}

	@Test
	public void testPrecedes() {
		final Vector2d first  = new Vector2d(1, 2);
		final Vector2d second = new Vector2d(1, 1);
		final Vector2d third  = new Vector2d(2, 1);

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
		final Vector2d first  = new Vector2d(1, 2);
		final Vector2d second = new Vector2d(1, 1);
		final Vector2d third  = new Vector2d(2, 1);

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
		final Vector2d first  = new Vector2d(3, 7);
		final Vector2d second = new Vector2d(1, 4);
		final Vector2d sum    = new Vector2d(4, 11);

		assertTrue(first.add(second).equals(sum));
		assertTrue(second.add(first).equals(sum));
	}

	@Test
	public void testSubtract() {
		final Vector2d first  = new Vector2d(3, 7);
		final Vector2d second = new Vector2d(1, 4);
		final Vector2d diffA  = new Vector2d(2, 3);
		final Vector2d diffB  = new Vector2d(-2, -3);

		assertTrue(first.subtract(second).equals(diffA));
		assertTrue(second.subtract(first).equals(diffB));
	}

	@Test
	public void testUpperRight() {
		final Vector2d first  = new Vector2d(5, 11);
		final Vector2d second = new Vector2d(7, 6);
		final Vector2d uRight = new Vector2d(7, 11);

		assertTrue(first.upperRight(second).equals(uRight));
		assertTrue(second.upperRight(first).equals(uRight));
	}

	@Test
	public void testLowerLeft() {
		final Vector2d first  = new Vector2d(5, 11);
		final Vector2d second = new Vector2d(7, 6);
		final Vector2d lLeft  = new Vector2d(5, 6);

		assertTrue(first.lowerLeft(second).equals(lLeft));
		assertTrue(second.lowerLeft(first).equals(lLeft));
	}

	@Test
	public void testOpposite() {
		final Vector2d vecA = new Vector2d(2, 3);
		final Vector2d vecB = new Vector2d(-2, -3);
		final Vector2d vecC = new Vector2d(2, -3);
		final Vector2d vecD = new Vector2d(-2, 3);

		assertTrue(vecA.opposite().equals(vecB));
		assertTrue(vecC.opposite().equals(vecD));
	}

	@Test
	public void testEquals() {
		final Vector2d vecA = new Vector2d(2, 3);
		final Vector2d vecB = new Vector2d(2, 3);
		final Vector2d vecC = new Vector2d(2, -3);

		assertTrue(vecA.equals(vecA));

		assertTrue(vecA.equals(vecB));
		assertTrue(vecB.equals(vecA));

		assertFalse(vecA.equals(vecC));
		assertFalse(vecC.equals(vecA));

		assertEquals(vecA, vecB);
		assertNotEquals(vecA, vecC);
	}

	 */
}
