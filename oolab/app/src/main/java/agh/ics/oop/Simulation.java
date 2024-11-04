package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
	private List<Animal> animals;
	private List<MoveDirection> moves;

	public Simulation(List<Vector2d> initialPositions, List<MoveDirection> moves) {
		this.animals = new ArrayList<Animal>();
		for(Vector2d x : initialPositions)
			this.animals.add(new Animal(x));
		this.moves = moves;
	}

	public void run() {
		for(int i = 0; i < moves.size(); ++i) {
			this.animals.get(i%animals.size()).move(moves.get(i));
			System.out.println("Zwierzę %d: %s".formatted(i%animals.size()+1, this.animals.get(i%animals.size()).toString()));
		}
	}

	public List<Animal> getAnimals() {
		return this.animals;
	}
}
