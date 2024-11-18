package agh.ics.oop.model;

import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.Animal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrassFieldTest {
	@Test
	public void testCanMoveTo() {
		final GrassField map = new GrassField(10);
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
		assertTrue(map.canMoveTo(new Vector2d(3, 4)));
		assertFalse(map.canMoveTo(new Vector2d(0, 0)));
		assertFalse(map.canMoveTo(new Vector2d(2, 2)));
		assertTrue(map.canMoveTo(new Vector2d(0, -2)));
	}
	@Test
	public void testPlace() {
		final GrassField map = new GrassField(7);
		final Animal animals[] = {
			new Animal(new Vector2d(2, 2)),
			new Animal(new Vector2d(0, 0)),
			new Animal(new Vector2d(3, 5)),
			new Animal(new Vector2d(-1, 0)),
			new Animal(new Vector2d(2, 2))
		};

		assertTrue(map.place(animals[0]));
		assertTrue(map.place(animals[1]));
		assertTrue(map.place(animals[2]));
		assertTrue(map.place(animals[3]));
		assertFalse(map.place(animals[4]));

		assertTrue(map.isOccupied(new Vector2d(2, 2)));
		assertTrue(map.isOccupied(new Vector2d(0, 0)));
		assertTrue(map.isOccupied(new Vector2d(3, 5)));
		assertTrue(map.isOccupied(new Vector2d(-1, 0)));

		assertSame(animals[0], map.objectAt(new Vector2d(2, 2)));
		assertSame(animals[1], map.objectAt(new Vector2d(0, 0)));
		assertNotNull(map.objectAt(new Vector2d(3, 5)));
		assertNotNull(map.objectAt(new Vector2d(-1, 0)));
	}
	@Test
	public void testMove() {
		final GrassField map = new GrassField(4);
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
		assertTrue(((Animal)map.objectAt(new Vector2d(2, 3))).isHeaded(MapDirection.NORTH));

		map.move(animals[2], MoveDirection.FORWARD);
		assertNotSame(animals[2], map.objectAt(new Vector2d(2, 3)));

		map.move(animals[2], MoveDirection.RIGHT);
		assertTrue(((Animal)map.objectAt(new Vector2d(2, 4))).isHeaded(MapDirection.EAST));

		map.move(animals[2], MoveDirection.FORWARD);
		assertSame(animals[2], map.objectAt(new Vector2d(3, 4)));

		map.move(animals[1], MoveDirection.LEFT);
		assertTrue(((Animal)map.objectAt(new Vector2d(0, 0))).isHeaded(MapDirection.WEST));

		map.move(animals[1], MoveDirection.FORWARD);
		assertSame(animals[1], map.objectAt(new Vector2d(-1, 0)));

		map.move(animals[1], MoveDirection.LEFT);
		assertTrue(((Animal)map.objectAt(new Vector2d(-1, 0))).isHeaded(MapDirection.SOUTH));

		map.move(animals[1], MoveDirection.FORWARD);
		assertSame(animals[1], map.objectAt(new Vector2d(-1, -1)));

		map.move(animals[1], MoveDirection.BACKWARD);
		assertSame(animals[1], map.objectAt(new Vector2d(-1, 0)));
	}
	@Test
	public void testIsOccupied() {
		final GrassField map = new GrassField(5);
		final Animal animals[] = {
			new Animal(new Vector2d(2, 2)),
			new Animal(new Vector2d(3, 5)),
			new Animal(new Vector2d(0, 0))
		};

		map.place(animals[0]);
		map.place(animals[1]);
		map.place(animals[2]);

		assertTrue(map.isOccupied(new Vector2d(2, 2)));
		assertTrue(map.isOccupied(new Vector2d(3, 5)));
		assertTrue(map.isOccupied(new Vector2d(0, 0)));
	}
	@Test
	public void testObjectAt() {
		final GrassField map = new GrassField(4);
		final Animal animals[] = {
			new Animal(new Vector2d(2, 2)),
			new Animal(new Vector2d(0, 0)),
			new Animal(new Vector2d(-1, 0))
		};

		for(Animal animal : animals)
			map.place(animal);

		assertSame(animals[0], map.objectAt(new Vector2d(2, 2)));
		assertSame(animals[1], map.objectAt(new Vector2d(0, 0)));
		assertNotNull(map.objectAt(new Vector2d(-1, 0)));
	}
}
