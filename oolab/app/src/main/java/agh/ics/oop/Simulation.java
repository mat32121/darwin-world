package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Simulation {
	private List<Animal> animals;
	private List<MoveDirection> moves;
	private WorldMap worldMap;

	public Simulation(List<Vector2d> initialPositions, List<MoveDirection> moves, WorldMap worldMap) {
		this.animals = new ArrayList<Animal>();
		for(Vector2d x : initialPositions)
			this.animals.add(new Animal(x));
		this.moves = moves;
		this.worldMap = worldMap;
		for(Animal animal : this.animals)
			this.worldMap.place(animal);
	}

	public void run() {
		for(int i = 0; i < moves.size(); ++i) {
			this.worldMap.move(animals.get(i%animals.size()), moves.get(i));
			System.out.println(this.worldMap);
		}
	}

	List<Animal> getAnimals() {
		return Collections.unmodifiableList(this.animals);
	}
}
