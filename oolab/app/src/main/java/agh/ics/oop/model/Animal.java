package agh.ics.oop.model;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.MoveValidator;
import agh.ics.oop.model.Vector2d;

public class Animal {
	private MapDirection direction;
	private Vector2d position;

	public Animal() {
		this(new Vector2d(2, 2));
	}

	public Animal(Vector2d position) {
		this.position = position;
		this.direction = MapDirection.NORTH;
	}

	public Vector2d getPosition()
	{
		return new Vector2d(this.position.getX(), this.position.getY());
	}

	@Override
	public String toString() {
		return switch(this.direction) {
			case NORTH -> "N";
			case WEST  -> "W";
			case SOUTH -> "S";
			case EAST  -> "E";
		};
	}

	public boolean isAt(Vector2d position) {
		return this.position.equals(position);
	}

	public boolean isHeaded(MapDirection direction) {
		return this.direction == direction;
	}

	public void move(MoveDirection direction, MoveValidator validator) {
		Vector2d newPosition = new Vector2d(this.position.getX(), this.position.getY());
		switch(direction) {
			case LEFT -> this.direction = this.direction.next();
			case RIGHT -> this.direction = this.direction.previous();
			case FORWARD -> newPosition = this.position.add(this.direction.toUnitVector());
			case BACKWARD -> newPosition = this.position.subtract(this.direction.toUnitVector());
		};

		if(validator.canMoveTo(newPosition))
			this.position = newPosition;
	}
}
