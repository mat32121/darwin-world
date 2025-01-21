package agh.ics.oop.model;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AnimalTest {

    @Test
    public void testInitialization() {
        int[] genotype = {0, 1, 2, 3};
        Animal animal = new Animal(new Vector2d(2, 2), 50, genotype);

        assertEquals(new Vector2d(2, 2), animal.getPosition());
        assertEquals(50, animal.getEnergy());
        assertTrue(animal.getLiveStatus());
        assertArrayEquals(genotype, animal.getGenotype());
        assertEquals(0, animal.getNumChildren());
    }

    @Test
    public void testEnergyChange() {
        Animal animal = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        animal.changeEnergy(-10);
        assertEquals(40, animal.getEnergy());

        animal.changeEnergy(20);
        assertEquals(60, animal.getEnergy());
    }

    @Test
    public void testMove() {
        Animal animal = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        Boundary boundary = new Boundary(new Vector2d(0, 0), new Vector2d(5, 5));

        animal.move(boundary);
        assertNotEquals(new Vector2d(2, 2), animal.getPosition());
        assertTrue(animal.getPosition().follows(boundary.lowerLeft()));
        assertTrue(animal.getPosition().precedes(boundary.upperRight()));
        assertTrue(animal.getEnergy() < 50); // Energia powinna spaść po ruchu
    }


    @Test
    public void testAddChildren() {
        Animal parent = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        Animal child = new Animal(new Vector2d(2, 2), 30, new int[]{0, 1, 2, 3});

        parent.addChildren(child);

        assertEquals(1, parent.getNumChildren());
    }

    @Test
    public void testGetAllDescendants() {
        Animal parent = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        Animal child1 = new Animal(new Vector2d(2, 2), 30, new int[]{0, 1, 2, 3});
        Animal child2 = new Animal(new Vector2d(2, 2), 20, new int[]{0, 1, 2, 3});
        parent.addChildren(child1);
        parent.addChildren(child2);

        HashSet<Animal> descendants = parent.getAllDescendants(new HashSet<>());

        assertTrue(descendants.contains(child1));
        assertTrue(descendants.contains(child2));
        assertEquals(2, descendants.size());
    }

    @Test
    public void testCopulate() {
        Animal parent1 = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        Animal parent2 = new Animal(new Vector2d(2, 2), 50, new int[]{4, 5, 6, 7});

        Animal child = parent1.copulate(parent2, 40, 2, 1);

        assertNotNull(child);
        assertEquals(parent1.getPosition(), child.getPosition());
        assertEquals(1, parent1.getNumChildren());
        assertEquals(1, parent2.getNumChildren());
        assertEquals(4, child.getGenotype().length);
    }

    @Test
    public void testCopulateEnergyThreshold() {
        Animal parent1 = new Animal(new Vector2d(2, 2), 30, new int[]{0, 1, 2, 3});
        Animal parent2 = new Animal(new Vector2d(2, 2), 30, new int[]{4, 5, 6, 7});

        Animal child = parent1.copulate(parent2, 50, 2, 1);

        assertNull(child); // Rodzice nie mają wystarczająco dużo energii, by się rozmnożyć
    }

    @Test
    public void testCompareToDeterministic() {
        // Zwierzęta z różną energią
        Animal animal1 = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        Animal animal2 = new Animal(new Vector2d(2, 2), 40, new int[]{0, 1, 2, 3});

        // Zwierzęta z równą energią, różnym wiekiem
        Animal animal3 = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        animal3.incrementAge();

        // Zwierzęta z równą energią i wiekiem, różną liczbą dzieci
        Animal animal4 = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        animal4.addChildren(new Animal(new Vector2d(2, 2), 30, new int[]{0, 1, 2, 3}));

        // Energia ma pierwszeństwo
        assertTrue(animal1.compareTo(animal2) < 0, "Animal1 powinien być uznany za 'lepszego' (większa energia)");

        // Wiek ma pierwszeństwo przy równej energii
        assertTrue(animal3.compareTo(animal1) < 0, "Animal3 powinien być uznany za 'lepszego' (starszy)");

        // Liczba dzieci ma pierwszeństwo przy równej energii i wieku
        assertTrue(animal4.compareTo(animal1) < 0, "Animal4 powinien być uznany za 'lepszego' (więcej dzieci)");
    }

    @Test
    public void testGetNumChildren() {
        Animal parent = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        Animal child1 = new Animal(new Vector2d(2, 2), 30, new int[]{0, 1, 2, 3});
        Animal child2 = new Animal(new Vector2d(2, 2), 20, new int[]{0, 1, 2, 3});

        parent.addChildren(child1);
        parent.addChildren(child2);

        assertEquals(2, parent.getNumChildren(), "Parent powinien mieć 2 dzieci.");
    }


    @Test
    public void testDaysAfterDeath() {
        Animal animal = new Animal(new Vector2d(2, 2), 50, new int[]{0, 1, 2, 3});
        animal.setLiveStatus(false);

        animal.incrementDaysAfterDeath();
        animal.incrementDaysAfterDeath();

        assertEquals(2, animal.getDaysAfterDeath());
    }
}