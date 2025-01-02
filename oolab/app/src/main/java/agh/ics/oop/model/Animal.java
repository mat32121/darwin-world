package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

public class Animal implements WorldElement {
	//private List<Animal> children = new ArrayList<>();
	//protected final List<Integer> genotype;
	//private int age = 0;
	//private int plantsEaten = 0;
	private MapDirection direction;
	private Vector2d position;
	//private int energy;
	//private int genotypeIndex;
	//private int dayOfDeath;

	public Animal() {
		this(new Vector2d(2, 2));
	}

	public Animal(Vector2d position) {
		this.position = position;
        this.direction = MapDirection.NORTH;
	}

	public Vector2d getPosition() {
		return this.position;
	}

	@Override
	public String toString() {
		return switch(this.direction) {
			//mapa jest odwrocona, dlatego NORTH -> "↓"
			case NORTH -> "↓";
			case NORTH_WEST->"↙";
			case WEST  -> "←";
            case SOUTH_WEST -> "↖";
            case SOUTH -> "↑";
            case SOUTH_EAST -> "↗";
            case EAST  -> "→";
            case NORTH_EAST -> "↘";
        };
	}

	public boolean isAt(Vector2d position) {
		return this.position.equals(position);
	}

	public boolean isHeaded(MapDirection direction) {
		return this.direction == direction;
	}

	public void move(int n, MoveValidator validator) {
		//*** do poprawy skladni!!!
		Vector2d newPosition = new Vector2d(this.position.getX(), this.position.getY());
		newPosition=newPosition.add(this.direction.toUnitVector());
		this.direction=this.direction.next(n);
		//uwaga, zwierze wpierw rusza sie o jeden, a dopiero potem zmienia kierunek na ten w kodzie genetycznym.
		//wymagana poprawa, ale do tego potrzeba jest przebudowa simulation.
		if(validator.canMoveTo(newPosition)) {
			this.position = newPosition;
		}
	}
}
