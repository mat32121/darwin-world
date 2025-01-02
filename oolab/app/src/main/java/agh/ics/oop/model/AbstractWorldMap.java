package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractWorldMap implements WorldMap {
	private final UUID id;
	protected static final Vector2d ORIGIN = new Vector2d(0, 0);
	protected final Map<Vector2d, Animal> animals;
	protected final MapVisualizer visualizer;

	protected final List<MapChangeListener> listeners;

	protected AbstractWorldMap() {
		this.animals = new HashMap<>();
		this.visualizer = new MapVisualizer(this);
		this.listeners = new ArrayList<>();
		this.id = UUID.randomUUID();
	}

	@Override
	public boolean canMoveTo(Vector2d position) {
		return !this.isOccupied(position);
	}

	@Override
	public void place(Animal animal) throws IncorrectPositionException {
		if(this.canMoveTo(animal.getPosition()))
		{
			animals.put(animal.getPosition(), animal);
			this.notifyListeners("Placed animal " + animal);
		}
		else
			throw new IncorrectPositionException(animal.getPosition());
	}
	@Override
	public void move(Animal animal, Integer direction) {
		//*** do przeniesienia, gdzie indziej, funkcja usuwajaca martwe zwierzaki!!!
		this.animals.remove(animal.getPosition());
		animal.move(direction, this);
		this.animals.put(animal.getPosition(), animal);
		this.notifyListeners("Moved animal at " + animal.getPosition() + " in direction " + direction);

	}


	@Override
	public boolean isOccupied(Vector2d position) {
		return (this.objectAt(position) instanceof Animal);
	}

	@Override
	public WorldElement objectAt(Vector2d position) {
		return this.animals.get(position);
	}

	@Override
	public List<WorldElement> getElements() {
		List<WorldElement> elements = new ArrayList<>();

		for(Animal entry : this.animals.values()) {
			if (entry.getLiveStatus()){
				elements.add(entry);
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