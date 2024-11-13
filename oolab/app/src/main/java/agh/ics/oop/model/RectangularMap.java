package agh.ics.oop.model;

import java.util.Map;
import java.util.HashMap;

import agh.ics.oop.model.util.MapVisualizer;

public class RectangularMap implements WorldMap {
	private final int width, height;
	private Map<Vector2d, Animal> animals;
	private final MapVisualizer visualizer;
	private static final Vector2d ORIGIN = new Vector2d(0, 0);
	private final Vector2d boundary;

	public RectangularMap(int width, int height) {
		this.animals = new HashMap<>();
		this.width = width;
		this.height = height;
		this.visualizer = new MapVisualizer(this);
		this.boundary = new Vector2d(width-1, height-1);
	}

	@Override
	public boolean canMoveTo(Vector2d position) {
		return position.follows(ORIGIN)
		    && position.precedes(boundary)
		    && !this.isOccupied(position);
	}
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
		return this.animals.containsKey(position);
	}
	@Override
	public Animal objectAt(Vector2d position) {
		return this.animals.get(position);
	}
	@Override
	public String toString() {
		return this.visualizer.draw(ORIGIN, boundary);
	}
};
