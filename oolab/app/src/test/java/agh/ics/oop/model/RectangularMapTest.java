package agh.ics.oop.model;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class RectangularMapTest {

    @Test
    public void testMove() {
        final RectangularMap map = new RectangularMap(3, 5, 0, 0, 0, 0, 10, 0, 0, 0, 0);
        final Animal animal = new Animal(new Vector2d(1, 1), 10, new int[]{0, 1, 2, 3});

        assertDoesNotThrow(() -> map.place(animal));

        // Ruch w granicach mapy
        map.move(animal);
        assertNotNull(map.objectAt(animal.getPosition()));

        // Zwierzę pozostaje w granicach mapy
        for (int i = 0; i < 10; i++) {
            map.move(animal);
        }
        Vector2d position = animal.getPosition();
        assertTrue(position.follows(map.getCurrentBounds().lowerLeft()));
        assertTrue(position.precedes(map.getCurrentBounds().upperRight()));
    }

    @Test
    public void testGetFittestAnimalOnEmptyPosition() {
        final RectangularMap map = new RectangularMap(3, 5, 0, 0, 0, 0, 10, 0, 0, 0, 0);

        // Pobierz najzdrowsze zwierzę na pustej pozycji
        Animal fittest = map.getFittestAnimalOnPosition(new Vector2d(1, 1));
        assertNull(fittest, "Na pustej pozycji powinno zwrócić null");
    }

    @Test
    public void testGrassGrowth() {
        final RectangularMap map = new RectangularMap(3, 5, 5, 10, 0, 0, 10, 0, 0, 0, 0);

        // Początkowa liczba trawy
        int initialGrassCount = map.getElements().stream()
                .filter(element -> element instanceof Grass)
                .toList()
                .size();
        assertEquals(5, initialGrassCount);

        // Dodanie nowej trawy
        map.grassGrows(3, true);
        int updatedGrassCount = map.getElements().stream()
                .filter(element -> element instanceof Grass)
                .toList()
                .size();
        assertEquals(8, updatedGrassCount); // Łącznie 5 początkowych + 3 dodane
    }

    @Test
    public void testDescendants() {
        final Animal parent = new Animal(new Vector2d(1, 1), 50, new int[]{0, 1, 2, 3});
        final Animal child1 = new Animal(new Vector2d(1, 1), 20, new int[]{0, 1, 2, 3});
        final Animal child2 = new Animal(new Vector2d(1, 1), 30, new int[]{0, 1, 2, 3});
        parent.addChildren(child1);
        parent.addChildren(child2);

        HashSet<Animal> descendants = parent.getAllDescendants(new HashSet<>());
        assertTrue(descendants.contains(child1));
        assertTrue(descendants.contains(child2));
        assertEquals(2, descendants.size());
    }
}