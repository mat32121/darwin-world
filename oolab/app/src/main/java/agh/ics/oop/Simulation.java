package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
		//*** do poprawy
		for(int i = 0; i<this.worldMap.getNumInitialAnimals(); i++) {
			Random random = new Random();

			// Generowanie pozycji dla nowego zwierzęcia
			int x = random.nextInt(this.worldMap.getWidth());
			int y = random.nextInt(this.worldMap.getHeight());
			Vector2d newAnimalPosition = new Vector2d(x, y);

			// Generowanie genów dla nowego zwierzęcia
			int numGenes = this.worldMap.getNumGenes();
			int[] newAnimalGene = random.ints(numGenes, 0, 8).toArray(); // Generuje tablicę losowych liczb od 0 do 7

			// Tworzenie i umieszczanie zwierzęcia na mapie
			Animal animal = new Animal(newAnimalPosition, this.worldMap.getInitialEnergy(), newAnimalGene);
			//TODO usunac wyjatek
			try {
				this.worldMap.place(animal);
				this.animals.add(animal);
			} catch (IncorrectPositionException e) {
				// Pozycja zajęta - ignorujemy wyjątek
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
			//*** for zaminiec na while, ktory w warunku ma ilosc jeszcze zjacych zwierzat
			//*** do poprawy, usuniecie redundancji, plus dodanie genomu
			//*** dodac zatrzymanie zwierzaka na czas jedzenia i kopulacji?
			//***kazdy element przeniesc do osobnej funkcji w simulation

			ArrayList<Vector2d> eatingPositions = new ArrayList<>();
			ArrayList<Vector2d> breedingPositions = new ArrayList<>();
			for(numDays = 0; this.isRunning;) {
				Thread.sleep(Simulation.MILLIS_INTERVAL);
				if(!this.isPaused) {
					for (Animal animal : animals) {
						//wpierw usuwamy martwe zwierzaki
						if (animal.getEnergy() <= 0) {
							animal.setLiveStatus(false);
						}
						//potem ruszamy zwierzakami
						if (animal.getLiveStatus()) {
							this.worldMap.move(animal);
							animal.incrementAge();
							int positionAnimalCount = this.worldMap.getAnimalsOnPosition(animal.getPosition()).size();
							if (this.worldMap.grassAt(animal.getPosition()) && positionAnimalCount==1) {
								eatingPositions.add(animal.getPosition());
							}
							// zmienic eatingQueue na eatingPositions, wtedy bedzie jeden mechanizm porownywania

							if(positionAnimalCount==2){
								breedingPositions.add(animal.getPosition());
							}


						} else {
							animal.incrementDaysAfterDeath();
							System.out.println(animal.getDaysAfterDeath());
						}
					}
					for(Vector2d position : eatingPositions) {
						Animal eatingAnimal = this.worldMap.getFittestAnimalOnPosition(position);
						this.worldMap.animalEatsGrass(eatingAnimal);
						this.worldMap.addFreePosition(position);
						// zwierzeta jedza, potrzebne petle na na kazdy etap
					}

					for (Vector2d position : breedingPositions) {
						try {
							// Pobranie pierwszego zwierzęcia
							Animal animal1 = this.worldMap.getFittestAnimalOnPosition(position);
							if (animal1 == null) {
								continue; // Przejdź do kolejnej pozycji, jeśli brak zwierząt
							}

							this.worldMap.removeAnimal(animal1);

							// Pobranie drugiego zwierzęcia
							Animal animal2 = this.worldMap.getFittestAnimalOnPosition(position);
							this.worldMap.place(animal1); // Przywrócenie pierwszego zwierzęcia

							// Sprawdzenie, czy oboje rodzice są dostępni
							if (animal2 == null) {
								continue; // Przejdź do kolejnej pozycji, jeśli brak drugiego zwierzęcia
							}

							// Próba rozmnażania
							Animal offspring = animal1.copulate(animal2, this.worldMap.getMinCopulateEnergy(), this.worldMap.getMaxChildMutations(),this.worldMap.getMinChildMutations() );
							if (offspring != null) {
								System.out.println("Dodaję potomka z energią: " + offspring.getEnergy());
								this.worldMap.place(offspring); // Dodanie potomka na mapę
								this.animals.add(offspring); // Dodanie potomka do listy zwierząt
							}

						} catch (IncorrectPositionException e) {
							// Obsługa wyjątku - logowanie błędu
							System.err.println("Nie można umieścić zwierzęcia na pozycji: " + e.getMessage());
						} catch (Exception e) {
							// Obsługa innych nieoczekiwanych błędów
							System.err.println("Wystąpił nieoczekiwany błąd: " + e.getMessage());
						}
					}


					breedingPositions.clear();
					eatingPositions.clear();
					this.worldMap.grassGrows(this.worldMap.getNumGrassPerDay(), false);
					//*** to tez mozna poprawic, chodzi o mechanike zwiazana z aktualizacja mapy.
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
		// System.out.println("BEGIN COUNTING");
		for(WorldElement elem : this.worldMap.getElements())
			if(elem instanceof Animal){
				// System.out.println(animal.getPosition());
				++result;
			}
		// System.out.println("FOUND: " + Integer.toString(result));
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
			if(elem instanceof Animal animal)
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

		// Zliczanie wystąpień genotypów
		for (WorldElement elem : this.worldMap.getElements()) {
			if (elem instanceof Animal && ((Animal) elem).getLiveStatus()) {
				int[] genotype = ((Animal) elem).getGenotype();

				// Zliczanie wystąpień w mapie
				countingGenoms.put(genotype, countingGenoms.getOrDefault(genotype, 0) + 1);
			}
		}

		// Sortowanie genotypów malejąco według liczby wystąpień i przekształcanie na listę
		int[] maxGenome = countingGenoms.entrySet()
				.stream()
				.max((e1, e2) -> e2.getValue().compareTo(e1.getValue())).get().getKey();
		return maxGenome;
	}

	public List<String> getGenotypeList() {
		HashMap<int[], Integer> countingGenoms = new HashMap<>();

		// Zliczanie wystąpień genotypów
		for (WorldElement elem : this.worldMap.getElements()) {
			if (elem instanceof Animal && ((Animal) elem).getLiveStatus()) {
				int[] genotype = ((Animal) elem).getGenotype();

				// Zliczanie wystąpień w mapie
				countingGenoms.put(genotype, countingGenoms.getOrDefault(genotype, 0) + 1);
			}
		}

		// Sortowanie genotypów malejąco według liczby wystąpień i przekształcanie na listę
		List<String> sortedGenotypes = countingGenoms.entrySet()
				.stream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sortuj malejąco po wartości
				.map(entry -> Arrays.toString(entry.getKey()) + " (Number of animals: " + entry.getValue() + ")")
				.limit(5)
				.toList();

		return sortedGenotypes;
		// return new ArrayList<>();
	}

	public double getAverageLifespan() {
		//sredni czas zycia ZMARLYCH zwierzakow
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
			if(elem instanceof Animal){
				result+=((Animal) elem).getNumChildren();
			}
		return Math.round(result * 100.0/this.getNumAnimals())/ 100.0;

	}

	public UUID getMapId() {
		return this.worldMap.getId();
	}
}
