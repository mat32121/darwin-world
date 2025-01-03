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
	private WorldMap worldMap;
	private static final long MILLIS_INTERVAL = 1000;

	public Simulation(List<Vector2d> initialPositions, WorldMap worldMap) {
		this.animals = new ArrayList<>();
		this.worldMap = worldMap;
		for(Vector2d x : initialPositions) {
			Animal animal = new Animal(x, 5, new int[] {1,1,1,1,1,1,1,1});
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
			//*** for zaminiec na while, ktory w warunku ma ilosc jeszcze zjacych zwierzat
			//*** do poprawy, usuniecie redundancji, plus dodanie genomu
			//*** dodac zatrzymanie zwierzaka na czas jedzenia i kopulacji?
			ArrayList<Animal> eatingQueue;
			for(int i = 0; i < 100; ++i) {
				Thread.sleep(Simulation.MILLIS_INTERVAL);
                for (Animal animal : animals) {
                    //wpierw usuwamy martwe zwierzaki
                    if (animal.getEnergy() == 0) {
                        animal.setLiveStatus(false);
					}
                    //potem ruszamy zwierzakami
                    if (animal.getLiveStatus()) {
                        this.worldMap.move(animal);
						if (this.worldMap) {

						}
                    } else {
                        animal.incrementDaysAfterDeath();
						System.out.println(animal.getDaysAfterDeath());
					}
				}
				for(int j = 0; j<animals.size(); ++j) {
					// zwierzeta jedza, potrzebne petle na na kazdy etap

				}
				//*** to tez mozna poprawic, chodzi o mechanike zwiazana z aktualizacja mapy.
				this.worldMap.mapTicks(i + " dni od rozpoczecia symulacji");

			}
		} catch (InterruptedException e) {
			// Sleep was interrupted. Leaving catch empty.
		}
	}

	List<Animal> getAnimals() {
		return Collections.unmodifiableList(this.animals);
	}
}
