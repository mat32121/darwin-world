package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapDirectionTest {

	//***testy do poprawy skladniowej
	@Test
	public void testNext() {
		assertEquals(MapDirection.WEST,  MapDirection.NORTH.next(2));
		assertEquals(MapDirection.SOUTH, MapDirection.WEST.next(2));
		assertEquals(MapDirection.EAST,  MapDirection.SOUTH.next(2));
		assertEquals(MapDirection.NORTH, MapDirection.EAST.next(2));
	}

	@Test
	public void testPrevious() {
		assertEquals(MapDirection.EAST,  MapDirection.NORTH.next(6));
		assertEquals(MapDirection.SOUTH, MapDirection.EAST.next(6));
		assertEquals(MapDirection.WEST,  MapDirection.SOUTH.next(6));
		assertEquals(MapDirection.NORTH, MapDirection.WEST.next(6));
	}
}
