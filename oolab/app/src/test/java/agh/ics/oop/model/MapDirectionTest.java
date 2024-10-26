package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {
	@Test public void testNext() {
		assertTrue(MapDirection.NORTH.next() == MapDirection.EAST);
		assertTrue(MapDirection.EAST.next()  == MapDirection.SOUTH);
		assertTrue(MapDirection.SOUTH.next() == MapDirection.WEST);
		assertTrue(MapDirection.WEST.next()  == MapDirection.NORTH);
	}

	@Test public void testPrevious() {
		assertTrue(MapDirection.NORTH.previous() == MapDirection.WEST);
		assertTrue(MapDirection.WEST.previous()  == MapDirection.SOUTH);
		assertTrue(MapDirection.SOUTH.previous() == MapDirection.EAST);
		assertTrue(MapDirection.EAST.previous()  == MapDirection.NORTH);
	}
}
