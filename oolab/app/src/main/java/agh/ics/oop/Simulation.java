package agh.ics.oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.IncorrectPositionException;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

public class Simulation implements Runnable {
	private List<Animal> animals;
	private List<Integer> moves;
	private WorldMap worldMap;
	private static final long MILLIS_INTERVAL = 1000;

	public Simulation(List<Vector2d> initialPositions, List<Integer> moves, WorldMap worldMap) {
		this.animals = new ArrayList<>();
		this.moves = moves;
		this.worldMap = worldMap;
		for(Vector2d x : initialPositions) {
			Animal animal = new Animal(x);
			try {
				this.worldMap.place(animal);
				this.animals.add(animal);
			}
			catch (IncorrectPositionException e) {
				// Ignoring position. Catch is empty
			}
		}
	}

	@Override
	public void run() {
		if(this.animals.isEmpty())
			return;
		try {
			for(int i = 0; i < moves.size(); ++i) {
				Thread.sleep(Simulation.MILLIS_INTERVAL);
				this.worldMap.move(this.animals.get(i%this.animals.size()), this.moves.get(i));
			}
		} catch (InterruptedException e) {
			// Sleep was interrupted. Leaving catch empty.
		}
	}

	List<Animal> getAnimals() {
		return Collections.unmodifiableList(this.animals);
	}
}
