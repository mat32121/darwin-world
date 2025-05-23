package agh.ics.oop.model;

import java.util.List;
import java.util.Set;
import java.util.UUID;

//całe do usuniecia;
/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place a animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     * @throws IncorrectPositionException 
     */
    void place(Animal animal) throws IncorrectPositionException;
    void grassGrows(int grassPerDay, boolean initialization);
    void addFreePosition(Vector2d position);
    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal);
    void mapTicks(String message);
    boolean grassAt(Vector2d position);
    void animalEatsGrass(Animal animal);
    void removeGrass(Vector2d position);
    Set<Animal> getAnimalsOnPosition(Vector2d position);
    boolean isJungle(Vector2d position);
    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    //boolean isOccupied(Vector2d position);

    Animal getFittestAnimalOnPosition(Vector2d position);
    List<Vector2d> getAnimalPositions();

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);

    void removeAnimal(Animal animal);

    int getMinCopulateEnergy();
    int getInitialEnergy();
    int getNumInitialAnimals();
    int getMinChildMutations();
    int getMaxChildMutations();
    int getNumGenes();
    int getWidth();
    int getHeight();
    int getNumGrassPerDay();

    List<WorldElement> getElements();
	Boundary getCurrentBounds();
    UUID getId();
}
