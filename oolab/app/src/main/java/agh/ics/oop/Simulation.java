package agh.ics.oop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.IncorrectPositionException;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;

public class Simulation implements Runnable {
	private List<Animal> animals;
	private WorldMap worldMap;
	private static final long MILLIS_INTERVAL = 100;

	private boolean isRunning = true;

	public Simulation(List<Vector2d> initialPositions, WorldMap worldMap) {
		this.animals = new ArrayList<>();
		this.worldMap = worldMap;
		//*** do poprawy
		for(Vector2d x : initialPositions) {
			Animal animal = new Animal(x, 10, new int[] {6,0,0,0,0,0,0,0,0,0,0,5,1,1,1,1});

			if (x.equals(new Vector2d(5,4))){
				animal = new Animal(x, 10, new int[] {2,0,0,0,0,0,0,0,0,0,0,5,1,1,1,1});
			}
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
			//***kazdy element przeniesc do osobnej funkcji w simulation

			ArrayList<Animal> eatingQueue = new ArrayList<>();
			ArrayList<Vector2d> breedingPositions = new ArrayList<>();
			for(int i = 0; this.isRunning; ++i) {
				Thread.sleep(Simulation.MILLIS_INTERVAL);
                for (Animal animal : animals) {
                    //wpierw usuwamy martwe zwierzaki
                    if (animal.getEnergy() <= 0) {
                        animal.setLiveStatus(false);
					}
                    //potem ruszamy zwierzakami
                    if (animal.getLiveStatus()) {
                        this.worldMap.move(animal);
						if (this.worldMap.grassAt(animal.getPosition())) {
							eatingQueue.add(animal);
						}
						// zmienic eatingQueue na eatingPositions, wtedy bedzie jeden mechanizm porownywania
						int positionAnimalCount = this.worldMap.getAnimalsOnPosition(animal.getPosition()).size();
						if(positionAnimalCount>=2){
							breedingPositions.add(animal.getPosition());
						}


                    } else {
                        animal.incrementDaysAfterDeath();
						System.out.println(animal.getDaysAfterDeath());
					}
				}
				for(Animal animal : eatingQueue) {
					if (this.worldMap.grassAt(animal.getPosition())) {
						this.worldMap.animalEatsGrass(animal);
						this.worldMap.addFreePosition(animal.getPosition());
					}
					// zwierzeta jedza, potrzebne petle na na kazdy etap
				}

				for(Vector2d positions : breedingPositions) {

				}

				breedingPositions.clear();
				eatingQueue.clear();
				this.worldMap.grassGrows();
				//*** to tez mozna poprawic, chodzi o mechanike zwiazana z aktualizacja mapy.
				this.worldMap.mapTicks(i + (i == 1 ? " dzień" : " dni") + " od rozpoczęcia symulacji");

			}
		} catch (InterruptedException e) {
			// Sleep was interrupted. Leaving catch empty.
		}
	}

	public void stop() {this.isRunning = false;}

	public List<Animal> getAnimals() {
		return Collections.unmodifiableList(this.animals);
	}

	public int getNumAnimals() {
		int result = 0;
		for(WorldElement elem : this.worldMap.getElements())
			if(elem instanceof Animal)
				++result;
		return result;
	}

	public int getNumGrass() {
		int result = 0;
		for(WorldElement elem : this.worldMap.getElements())
			if(elem instanceof Grass)
				++result;
		return result;
	}

	public int getNumFreeSquares() {
		Boundary bounds = this.worldMap.getCurrentBounds();
		return (bounds.upperRight().getX()-bounds.lowerLeft().getX()+1)*
		       (bounds.upperRight().getY()-bounds.lowerLeft().getY()+1)-
			   this.getNumAnimals();
	}

	public double getAverageEnergy() {
		double energySum = 0.0;
		for(WorldElement elem : this.worldMap.getElements())
			if(elem instanceof Animal animal)
				energySum += animal.getEnergy();
		return energySum/this.getNumAnimals();
	}
}
