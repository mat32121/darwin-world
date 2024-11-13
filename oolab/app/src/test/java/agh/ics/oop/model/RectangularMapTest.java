package agh.ics.oop.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.RectangularMap;

import java.util.Collections;
import java.util.List;

public class RectangularMapTest {
	@Test
	public void testCanMoveTo() {
		final RectangularMap map = new RectangularMap(3, 5);
		final Animal animals[] = {
			new Animal(new Vector2d(2, 2)),
			new Animal(new Vector2d(0, 0)),
			new Animal(new Vector2d(3, 5)),
			new Animal(new Vector2d(-1, 0))
		};

		for(Animal animal : animals)
			map.place(animal);

		assertTrue(map.canMoveTo(new Vector2d(1, 0)));
		assertTrue(map.canMoveTo(new Vector2d(2, 4)));
		assertFalse(map.canMoveTo(new Vector2d(3, 4)));
		assertFalse(map.canMoveTo(new Vector2d(0, 0)));
		assertFalse(map.canMoveTo(new Vector2d(2, 2)));
		assertFalse(map.canMoveTo(new Vector2d(0, -2)));
	}
	@Test
	public void testPlace() {
		final RectangularMap map = new RectangularMap(3, 5);
		final Animal animals[] = {
			new Animal(new Vector2d(2, 2)),
			new Animal(new Vector2d(0, 0)),
			new Animal(new Vector2d(3, 5)),
			new Animal(new Vector2d(-1, 0)),
			new Animal(new Vector2d(2, 2))
		};

		assertTrue(map.place(animals[0]));
		assertTrue(map.place(animals[1]));
		assertFalse(map.place(animals[2]));
		assertFalse(map.place(animals[3]));
		assertFalse(map.place(animals[4]));

		assertTrue(map.isOccupied(new Vector2d(2, 2)));
		assertTrue(map.isOccupied(new Vector2d(0, 0)));
		assertFalse(map.isOccupied(new Vector2d(3, 5)));
		assertFalse(map.isOccupied(new Vector2d(-1, 0)));

		assertSame(animals[0], map.objectAt(new Vector2d(2, 2)));
		assertSame(animals[1], map.objectAt(new Vector2d(0, 0)));
		assertNull(map.objectAt(new Vector2d(3, 5)));
		assertNull(map.objectAt(new Vector2d(-1, 0)));
	}
	@Test
	public void testMove() {
		final RectangularMap map = new RectangularMap(3, 4);
		final Animal animals[] = {
			new Animal(new Vector2d(2, 2)),
			new Animal(new Vector2d(0, 0)),
			new Animal(new Vector2d(2, 3))
		};

		for(Animal animal : animals)
			map.place(animal);

		map.move(animals[2], MoveDirection.BACKWARD);
		assertSame(animals[2], map.objectAt(new Vector2d(2, 3)));
		assertNotSame(animals[2], map.objectAt(new Vector2d(2, 2)));
		assertNotSame(animals[2], map.objectAt(new Vector2d(1, 0)));
		assertNotSame(animals[2], map.objectAt(new Vector2d(0, 0)));
		assertTrue(map.objectAt(new Vector2d(2, 3)).isHeaded(MapDirection.NORTH));

		map.move(animals[2], MoveDirection.FORWARD);
		assertSame(animals[2], map.objectAt(new Vector2d(2, 3)));

		map.move(animals[2], MoveDirection.RIGHT);
		assertTrue(map.objectAt(new Vector2d(2, 3)).isHeaded(MapDirection.EAST));

		map.move(animals[2], MoveDirection.FORWARD);
		assertSame(animals[2], map.objectAt(new Vector2d(2, 3)));

		map.move(animals[1], MoveDirection.LEFT);
		assertTrue(map.objectAt(new Vector2d(0, 0)).isHeaded(MapDirection.WEST));

		map.move(animals[1], MoveDirection.FORWARD);
		assertSame(animals[1], map.objectAt(new Vector2d(0, 0)));

		map.move(animals[1], MoveDirection.LEFT);
		assertTrue(map.objectAt(new Vector2d(0, 0)).isHeaded(MapDirection.SOUTH));

		map.move(animals[1], MoveDirection.FORWARD);
		assertSame(animals[1], map.objectAt(new Vector2d(0, 0)));

		map.move(animals[1], MoveDirection.BACKWARD);
		assertSame(animals[1], map.objectAt(new Vector2d(0, 1)));
	}
	@Test
	public void testIsOccupied() {
		final RectangularMap map = new RectangularMap(3, 5);
		final Animal animals[] = {
			new Animal(new Vector2d(2, 2)),
			new Animal(new Vector2d(3, 5)),
			new Animal(new Vector2d(0, 0))
		};

		map.place(animals[0]);
		map.place(animals[1]);

		assertTrue(map.isOccupied(new Vector2d(2, 2)));
		assertFalse(map.isOccupied(new Vector2d(3, 5)));
		assertFalse(map.isOccupied(new Vector2d(0, 0)));
	}
	@Test
	public void testObjectAt() {
		final RectangularMap map = new RectangularMap(3, 5);
		final Animal animals[] = {
			new Animal(new Vector2d(2, 2)),
			new Animal(new Vector2d(0, 0)),
			new Animal(new Vector2d(-1, 0))
		};

		for(Animal animal : animals)
			map.place(animal);

		assertSame(animals[0], map.objectAt(new Vector2d(2, 2)));
		assertSame(animals[1], map.objectAt(new Vector2d(0, 0)));
		assertNull(map.objectAt(new Vector2d(-1, 0)));
	}
}
