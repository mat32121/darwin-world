package agh.ics.oop.model;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

public class Animal {
	private MapDirection direction;
	private Vector2d position;

	public Animal() {
		this.position = new Vector2d(2, 2);
		this.direction = MapDirection.NORTH;
	}

	public Animal(Vector2d position) {
		this.position = position;
		this.direction = MapDirection.NORTH;
	}

	@Override
	public String toString() {
		return "POS: %s\tDIR: %s".formatted(this.position.toString(), this.direction.toString());
	}

	public boolean isAt(Vector2d position) {
		return this.position.equals(position);
	}

	public boolean isHeaded(MapDirection direction) {
		return this.direction == direction;
	}

	public void move(MoveDirection direction) {
		final Vector2d mapBoundaryUpperRight = new Vector2d(4, 4);
		final Vector2d mapBoundaryLowerLeft  = new Vector2d(0, 0);

		switch(direction) {
			case LEFT  -> this.direction = this.direction.next();
			case RIGHT -> this.direction = this.direction.previous();
			case FORWARD  -> this.position =
				this.position.add(this.direction.toUnitVector());
			case BACKWARD -> this.position =
				this.position.subtract(this.direction.toUnitVector());
		};

		this.position = this.position.upperRight(mapBoundaryLowerLeft);
		this.position = this.position.lowerLeft(mapBoundaryUpperRight);
	}
}
