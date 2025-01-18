package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import agh.ics.oop.model.util.MapVisualizer;

public abstract class AbstractWorldMap implements WorldMap {
	private final UUID id;
	protected static final Vector2d ORIGIN = new Vector2d(0, 0);
	protected final Map<Vector2d, Set<Animal>> animals;
	protected final MapVisualizer visualizer;

	protected final List<MapChangeListener> listeners;

	protected AbstractWorldMap() {
		this.animals = new HashMap<>();
		this.visualizer = new MapVisualizer(this);
		this.listeners = new ArrayList<>();
		this.id = UUID.randomUUID();
	}

	@Override
	public void mapTicks(String message){
		this.notifyListeners(message);
	}

	/*
	@Override
	public boolean canMoveTo(Vector2d position) {
		return !this.isOccupied(position);
	}
	 */

	// TODO: Implement
	@Override
	public boolean isJungle(Vector2d position) {
		return (position.getX()%2 == 0 ? true : false);
	}

	@Override
	public void place(Animal animal) throws IncorrectPositionException {
		if(animal.getPosition().isWithinBounds(this.getCurrentBounds()))
		{
			if(animals.containsKey(animal.getPosition())){
				animals.get(animal.getPosition()).add(animal);
			} else {
				Set<Animal> animalSet = new HashSet<>();
				animalSet.add(animal);
				animals.put(animal.getPosition(), animalSet);
			}
			this.notifyListeners("Placed animal " + animal);
		}
		else
			throw new IncorrectPositionException(animal.getPosition());
	}
	@Override
	public void move(Animal animal) {
		// int currentNumAnimals = 0;
		// System.out.println("BEFORE:");
		// for(Set<Animal> animalSet : this.animals.values()) {
		// 	currentNumAnimals += animalSet.size();
		// 	for(Animal anim : animalSet) {
		// 		System.out.println(anim.hashCode());
		// 	}
		// }
		if(this.animals.get(animal.getPosition()).remove(animal)) {
			animal.move(this.getCurrentBounds());
			//wiele zwierzat na jednej pozycji
			if(this.animals.containsKey(animal.getPosition())) {
				this.animals.get(animal.getPosition()).add(animal);
			}
			else {
				Set<Animal> newAnimalSet=new HashSet<>();
				newAnimalSet.add(animal);
				this.animals.put(animal.getPosition(), newAnimalSet);
			}
		}
		else
			System.out.println("Animal " + animal.hashCode() + " did not exist!"); // DEBUG, optional
		// System.out.println(animal.equals(animal));
		// System.out.println("AFTER:");
		// for(Set<Animal> animalSet : this.animals.values()) {
		// 	currentNumAnimals -= animalSet.size();
		// 	for(Animal anim : animalSet)
		// 		System.out.println(anim.hashCode());
		// }
		// if(currentNumAnimals != 0)
		// 	System.out.println("NO ANIMAL BALANCE");
	}

	@Override
	public Set<Animal> getAnimalsOnPosition(Vector2d position) {
		return this.animals.get(position);
	}


	//@Override
	//public boolean isOccupied(Vector2d position) {
	//	return (this.objectAt(position) instanceof Animal);
	//}
	//***do poprawy jutro!!!!
	//*** czy da sie zrobic tak, by rysowalo wszystkie zwierzaki w jednym miejscu?
	//*** aktualnie wybiera 'losowego' zwierzaka i jego rysuje.
	@Override
	public WorldElement objectAt(Vector2d position) {
		return this.animals.get(position).iterator().next();
	}

	@Override
	public List<WorldElement> getElements() {
		List<WorldElement> elements = new ArrayList<>();

		for (Set<Animal> animalList : this.animals.values()){
			for(Animal entry : animalList) {
				if (entry.getLiveStatus()){
					elements.add(entry);
				}
			}
		}


		return elements;
	}
	@Override
	public abstract Boundary getCurrentBounds();

	@Override
	public String toString() {
		Boundary bounds = this.getCurrentBounds();
		return this.visualizer.draw(bounds.lowerLeft(), bounds.upperRight());
	}
	public void addListener(MapChangeListener newListener) {
		this.listeners.add(newListener);
	}
	public void removeListener(MapChangeListener newListener) {
		this.listeners.remove(newListener);
	}

	private void notifyListeners(String message) {
		for(MapChangeListener listener : this.listeners)
			listener.mapChanged(this, message);
	}

	@Override
	public UUID getId() {
		return this.id;
	}
}