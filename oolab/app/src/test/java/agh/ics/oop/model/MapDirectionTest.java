package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {
	@Test
	public void testNext() {
		assertEquals(MapDirection.WEST,  MapDirection.NORTH.next());
		assertEquals(MapDirection.SOUTH, MapDirection.WEST.next());
		assertEquals(MapDirection.EAST,  MapDirection.SOUTH.next());
		assertEquals(MapDirection.NORTH, MapDirection.EAST.next());
	}

	@Test
	public void testPrevious() {
		assertEquals(MapDirection.EAST,  MapDirection.NORTH.previous());
		assertEquals(MapDirection.SOUTH, MapDirection.EAST.previous());
		assertEquals(MapDirection.WEST,  MapDirection.SOUTH.previous());
		assertEquals(MapDirection.NORTH, MapDirection.WEST.previous());
	}
}
