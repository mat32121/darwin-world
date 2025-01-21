package agh.ics.oop.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class MapDirectionTest {

    // Test metody next dla przesunięcia o 2 kroki
    @Test
    public void testNext() {
        assertEquals(MapDirection.WEST, MapDirection.NORTH.next(2));
        assertEquals(MapDirection.SOUTH, MapDirection.WEST.next(2));
        assertEquals(MapDirection.EAST, MapDirection.SOUTH.next(2));
        assertEquals(MapDirection.NORTH, MapDirection.EAST.next(2));
    }

    // Test metody next dla przesunięcia o więcej niż pełny obrót
    @Test
    public void testNextWithOverflow() {
        assertEquals(MapDirection.WEST, MapDirection.NORTH.next(10)); // 10 % 8 = 2
        assertEquals(MapDirection.SOUTH, MapDirection.WEST.next(18)); // 18 % 8 = 2
    }

    // Test metody next dla przesunięcia o 0 (brak zmiany)
    @Test
    public void testNextWithZeroSteps() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTH.next(0));
        assertEquals(MapDirection.EAST, MapDirection.EAST.next(0));
    }

    // Test metody next dla przesunięcia o wartość równą pełnemu obrotowi (8 kroków)
    @Test
    public void testNextWithFullCycle() {
        assertEquals(MapDirection.NORTH, MapDirection.NORTH.next(8));
        assertEquals(MapDirection.WEST, MapDirection.WEST.next(8));
        assertEquals(MapDirection.SOUTH, MapDirection.SOUTH.next(8));
        assertEquals(MapDirection.EAST, MapDirection.EAST.next(8));
    }

    // Test metody toUnitVector
    @Test
    public void testToUnitVector() {
        assertEquals(new Vector2d(0, 1), MapDirection.NORTH.toUnitVector());
        assertEquals(new Vector2d(-1, 1), MapDirection.NORTH_WEST.toUnitVector());
        assertEquals(new Vector2d(-1, 0), MapDirection.WEST.toUnitVector());
        assertEquals(new Vector2d(-1, -1), MapDirection.SOUTH_WEST.toUnitVector());
        assertEquals(new Vector2d(0, -1), MapDirection.SOUTH.toUnitVector());
        assertEquals(new Vector2d(1, -1), MapDirection.SOUTH_EAST.toUnitVector());
        assertEquals(new Vector2d(1, 0), MapDirection.EAST.toUnitVector());
        assertEquals(new Vector2d(1, 1), MapDirection.NORTH_EAST.toUnitVector());
    }

    // Test metody toString
    @Test
    public void testToString() {
        assertEquals("Północ", MapDirection.NORTH.toString());
        assertEquals("Północny Zachód", MapDirection.NORTH_WEST.toString());
        assertEquals("Zachód", MapDirection.WEST.toString());
        assertEquals("Południowy Zachód", MapDirection.SOUTH_WEST.toString());
        assertEquals("Południe", MapDirection.SOUTH.toString());
        assertEquals("Południowy Wschód", MapDirection.SOUTH_EAST.toString());
        assertEquals("Wschód", MapDirection.EAST.toString());
        assertEquals("Północny Wschód", MapDirection.NORTH_EAST.toString());
    }
}