package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

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
	private static final long MILLIS_INTERVAL = 1000;

	private int numDays = 0;

	private boolean isRunning = true;
	private boolean isPaused = false;

	public Simulation(WorldMap worldMap) {
		this.animals = new ArrayList<>();
		this.worldMap = worldMap;
		for(int i = 0; i<this.worldMap.getNumInitialAnimals(); i++) {
			Random random = new Random();

			int x = random.nextInt(this.worldMap.getWidth());
			int y = random.nextInt(this.worldMap.getHeight());
			Vector2d newAnimalPosition = new Vector2d(x, y);

			int numGenes = this.worldMap.getNumGenes();
			int[] newAnimalGene = random.ints(numGenes, 0, 8).toArray();

			Animal animal = new Animal(newAnimalPosition, this.worldMap.getInitialEnergy(), newAnimalGene);
			try {
				this.worldMap.place(animal);
				this.animals.add(animal);
			} catch (IncorrectPositionException e) {
				// Position occupied - ignoring exception
			}
		}
	}

	public int getNumDays() {
		return this.numDays;
	}

	@Override
	public void run() {
		if(this.animals.isEmpty())
			return;
		try {
			ArrayList<Vector2d> eatingPositions = new ArrayList<>();
			ArrayList<Vector2d> breedingPositions = new ArrayList<>();
			for(numDays = 0; this.isRunning;) {
				Thread.sleep(Simulation.MILLIS_INTERVAL);
				if(!this.isPaused) {
					for (Animal animal : animals) {
						if (animal.getLiveStatus() && animal.getEnergy() <= 0) {
							animal.setLiveStatus(false);
							animal.setDayOfDeath(numDays);
						}
						if (animal.getLiveStatus()) {
							this.worldMap.move(animal);
							animal.incrementAge();
							int positionAnimalCount = this.worldMap.getAnimalsOnPosition(animal.getPosition()).size();
							if (this.worldMap.grassAt(animal.getPosition()) && positionAnimalCount==1)
								eatingPositions.add(animal.getPosition());
							if(positionAnimalCount==2)
								breedingPositions.add(animal.getPosition());
						} else {
							animal.incrementDaysAfterDeath();
						}
					}
					for(Vector2d position : eatingPositions) {
						Animal eatingAnimal = this.worldMap.getFittestAnimalOnPosition(position);
						this.worldMap.animalEatsGrass(eatingAnimal);
						this.worldMap.addFreePosition(position);
						eatingAnimal.incrementEatenGrass();
					}

					for (Vector2d position : breedingPositions) {
						try {
							Animal animal1 = this.worldMap.getFittestAnimalOnPosition(position);
							if (animal1 == null) {
								continue;
							}

							this.worldMap.removeAnimal(animal1);

							Animal animal2 = this.worldMap.getFittestAnimalOnPosition(position);
							this.worldMap.place(animal1);
							if (animal2 == null) {
								continue;
							}

							Animal offspring = animal1.copulate(animal2, this.worldMap.getMinCopulateEnergy(), this.worldMap.getMaxChildMutations(),this.worldMap.getMinChildMutations());
							if (offspring != null) {
								this.worldMap.place(offspring);
								this.animals.add(offspring);
							}

						} catch (IncorrectPositionException e) {
							System.err.println("Can't place animal on position: " + e.getMessage());
						} catch (Exception e) {
							System.err.println("Unexpected exception occurred: " + e.getMessage());
						}
					}


					breedingPositions.clear();
					eatingPositions.clear();
					this.worldMap.grassGrows(this.worldMap.getNumGrassPerDay(), false);
					this.worldMap.mapTicks(numDays + (numDays == 1 ? " dzień" : " dni") + " od rozpoczęcia symulacji");
					++numDays;
				}
			}
		} catch (InterruptedException e) {
			// Sleep was interrupted. Leaving catch empty.
		}
	}

	public void stop() {this.isRunning = false;}

	public List<Animal> getAnimals() {
		return Collections.unmodifiableList(this.animals);
	}

	public boolean togglePause() {
		this.isPaused = !this.isPaused;
		return this.isPaused;
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
		int freeSpace = (bounds.upperRight().getX()-bounds.lowerLeft().getX()+1)*
			   (bounds.upperRight().getY()-bounds.lowerLeft().getY()+1);
		HashSet<Vector2d> occupiedSpace = new HashSet<>();
		for(WorldElement elem : this.worldMap.getElements())
			if(elem instanceof Animal)
				occupiedSpace.add(elem.getPosition());

		return freeSpace-occupiedSpace.size();
	}

	public double getAverageEnergy() {
		double energySum = 0.0;
		for(WorldElement elem : this.worldMap.getElements())
			if(elem instanceof Animal animal)
				energySum += animal.getEnergy();
		return Math.round(energySum * 100.0/this.getNumAnimals())/ 100.0;
	}

	public int[] getMostPopularGenome() {
		HashMap<int[], Integer> countingGenoms = new HashMap<>();

		for (WorldElement elem : this.worldMap.getElements()) {
			if (elem instanceof Animal && ((Animal) elem).getLiveStatus()) {
				int[] genotype = ((Animal) elem).getGenotype();
				countingGenoms.put(genotype, countingGenoms.getOrDefault(genotype, 0) + 1);
			}
		}

		Optional<Entry<int[], Integer>> maxGenome = countingGenoms.entrySet()
				.stream()
				.max((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
		if(maxGenome.isPresent())
			return maxGenome.get().getKey();
		else
			return null;
	}

	public List<String> getGenotypeList() {
		HashMap<int[], Integer> countingGenoms = new HashMap<>();

		for (WorldElement elem : this.worldMap.getElements()) {
			if (elem instanceof Animal && ((Animal) elem).getLiveStatus()) {
				int[] genotype = ((Animal) elem).getGenotype();
				countingGenoms.put(genotype, countingGenoms.getOrDefault(genotype, 0) + 1);
			}
		}

		List<String> sortedGenotypes = countingGenoms.entrySet()
				.stream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sortuj malejąco po wartości
				.map(entry -> Arrays.toString(entry.getKey()) + " (Number of animals: " + entry.getValue() + ")")
				.limit(5)
				.toList();

		return sortedGenotypes;
	}

	public double getAverageLifespan() {
		int result = 0;
		for(WorldElement elem : this.worldMap.getElements())
			if(elem instanceof Animal && !((Animal) elem).getLiveStatus()){
				result+=((Animal) elem).getAge();
			}
		return Math.round(result * 100.0/this.getNumAnimals())/ 100.0;
	}

	public double getAverageNumChildren() {
		int result = 0;
		for(WorldElement elem : this.worldMap.getElements())
			if(elem instanceof Animal animal)
				result += animal.getNumChildren();
		return Math.round(result * 100.0/this.getNumAnimals())/ 100.0;

	}

	public UUID getMapId() {
		return this.worldMap.getId();
	}
}
