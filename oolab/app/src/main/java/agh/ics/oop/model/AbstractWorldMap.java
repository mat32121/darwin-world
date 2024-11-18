package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap {
	protected static final Vector2d ORIGIN = new Vector2d(0, 0);
	protected final Map<Vector2d, Animal> animals;
	protected final MapVisualizer visualizer;

	protected AbstractWorldMap() {
		this.animals = new HashMap<>();
		this.visualizer = new MapVisualizer(this);
	}

	@Override
	public abstract boolean canMoveTo(Vector2d position);
	@Override
	public boolean place(Animal animal) {
		if(this.canMoveTo(animal.getPosition())) {
			animals.put(animal.getPosition(), animal);
			return true;
		}
		return false;
	}
	@Override
	public void move(Animal animal, MoveDirection direction) {
		this.animals.remove(animal.getPosition());
		animal.move(direction, this);
		this.animals.put(animal.getPosition(), animal);
	}
	@Override
	public boolean isOccupied(Vector2d position) {
		return this.objectAt(position) != null;
	}
	@Override
	public WorldElement objectAt(Vector2d position) {
		return this.animals.get(position);
	}
	@Override
	public List<WorldElement> getElements() {
		List<WorldElement> elements = new ArrayList<>();
		for(Map.Entry<Vector2d, Animal> entry : this.animals.entrySet())
			elements.add(entry.getValue());
		return elements;
	}
}
