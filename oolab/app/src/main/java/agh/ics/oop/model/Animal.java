package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement, Comparable<Animal> {
	//private int plantsEaten = 0;
	private MapDirection direction;
	private Vector2d position;
	private int energy;
	private boolean liveStatus;
	private int daysAfterDeath;
	private int genIndex;
	private final int[] genotype;
	private List<Animal> children;
	private int age;
	private Random rng;


	//private int genotypeIndex;

	public Animal(Vector2d position, int energy, int[] genotype) {
		this.position = position;
		this.energy=energy;
        this.direction = MapDirection.NORTH; //zmienic na losowy
		this.liveStatus=true;
		this.genotype=genotype;
		this.daysAfterDeath=0;
		this.rng = new Random();
		this.genIndex=(int) (Math.random() * genotype.length); //mozna poprawic na randomnext
		//this.genIndex=0; //**** do usuniecia!!! poprawna wersja wyzej /\
		//***zakladam, ze zawsze tworzymy zywego zwierzaka
		this.children = new ArrayList<>();
		this.age = 0;

	}

	@Override
	public int compareTo(Animal other) {
		if(other instanceof Animal) {
			int res = other.getEnergy()-this.energy;
			if(res == 0)
				res = other.getAge()-this.age;
			if(res == 0)
				res = other.getNumChildren()-this.getNumChildren();
			if(res == 0)
				res = this.rng.ints(0, 2).findFirst().getAsInt()*2-1;
			return res;
		}
		else
			throw new IllegalArgumentException("Cannot compare Animal to different class!");
	}



	public boolean getLiveStatus() {
		return this.liveStatus;
	}

	public int getEnergy() {
		return this.energy;
	}

	public Vector2d getPosition() {
		return this.position;
	}

	public int getDaysAfterDeath() {
		return this.daysAfterDeath;
	}

	public int[] getGenotype() {
		return genotype;
	}

	public int getGenIndex() {
		return genIndex;
	}

	public void setLiveStatus(boolean status) {
		this.liveStatus=status;
	}

	//*** przydalby sie moze ladniejszy mechanizm

	public void incrementDaysAfterDeath() {
		this.daysAfterDeath += 1;
	}

	public void incrementAge() {
		this.age += 1;
	}

	public void changeEnergy(int n) {
		this.energy += n;
	}

	public int getAge() {
		return age;
	}

	public int getNumChildren() {
		return this.children.size();
	}

	public void addChildren(Animal offspring) {
		this.children.add(offspring);
	}

	//public Animal breeding(Animal animal) {
		//*** trzeba dodac, ze wyrzua blad kiedy zwierzeta nie sa na tej samej pozycji.
	//	int newEnergy = (this.energy^2 + animal.energy^2)/(this.energy+animal.energy);
	//	this.changeEnergy(-newEnergy);
	//	animal.changeEnergy(-newEnergy);

	//	int[] dominantGenotype = this.genotype;
	//	int[] recessiveGenotype = animal.genotype;



	//	return new Animal(this.getPosition(), newEnergy, );
	//}

	@Override
	public String toString() {
		return switch(this.direction) {
			//***mapa jest odwrocona, dlatego NORTH -> "↓", do poprawy!
			case NORTH -> "↑";
			case NORTH_WEST->"↖";
			case WEST  -> "←";
            case SOUTH_WEST -> "↙";
            case SOUTH -> "↓";
            case SOUTH_EAST -> "↘";
            case EAST  -> "→";
            case NORTH_EAST -> "↗";
        };
	}

	public boolean isAt(Vector2d position) {
		return this.position.equals(position);
	}

	public boolean isHeaded(MapDirection direction) {
		return this.direction == direction;
	}

	public HashSet<Animal> getAllDescendants(HashSet<Animal> countedDescendants) {
		if (this.children.isEmpty()) {
			return countedDescendants;
		}
		for (Animal child : this.children) {
			if (!countedDescendants.contains(child)) {
				countedDescendants.add(child);
				child.getAllDescendants(countedDescendants);
			}
		}
		return countedDescendants;
	}
	
	public void move(Boundary boundary) {
		//*** do poprawy skladni!!!
		this.direction=this.direction.next(genotype[genIndex]);
		genIndex=(genIndex+1)%genotype.length;

		Vector2d newPosition = new Vector2d(this.position.getX(), this.position.getY());
		newPosition=newPosition.add(this.direction.toUnitVector());
		this.position = newPosition;
		this.placeWithinBounds(boundary);
		this.changeEnergy(-1);
		//dodac liczbe zalezna od wspolrzednej y

		//uwaga, zwierze wpierw rusza sie o jeden, a dopiero potem zmienia kierunek na ten w kodzie genetycznym.
		//***prawdopodobnie da sie szybciej zrobic funkcje, ktora zapetla mape, niz ta ponizej


	}

	public void placeWithinBounds(Boundary boundary) {
		int yMax = boundary.upperRight().getY();
		int yMin = boundary.lowerLeft().getY();
		int xMax = boundary.upperRight().getX();
		int xMin = boundary.lowerLeft().getX();

		int xNew = this.getPosition().getX();
		int yNew = this.getPosition().getY();

		if (xNew < xMin) {
			xNew = xMax;
		} else if (xNew > xMax) {
			xNew = xMin;
		}

		if (yNew < yMin) {
			yNew = yMin;
			this.direction=this.direction.next(4);
		} else if (yNew > yMax) {
			yNew = yMax;
			this.direction=this.direction.next(4);
		}

		this.position = new Vector2d(xNew, yNew);

	}

	public Animal copulate(Animal partner, int reproductionEnergyThreshold, int maxChildMutations, int minChildMutations) {
		// Pobranie energii rodziców
		int parentEnergy1 = this.getEnergy();
		int parentEnergy2 = partner.getEnergy();
		int totalEnergy = parentEnergy1 + parentEnergy2;

		// Sprawdzenie, czy oboje rodziców mają wystarczającą energię
		if (parentEnergy1 >= reproductionEnergyThreshold && parentEnergy2 >= reproductionEnergyThreshold) {
			// Obliczenie energii potomka
			int offspringEnergy = calculateOffspringEnergy(parentEnergy1, parentEnergy2, totalEnergy);

			// Aktualizacja energii rodziców
			this.changeEnergy(-calculateEnergyReduction(parentEnergy1, totalEnergy));
			partner.changeEnergy(-calculateEnergyReduction(parentEnergy2, totalEnergy));

			// Krzyżowanie genotypów
			int[] offspringGenotype = crossoverGenotypes(this.getGenotype(), partner.getGenotype(), parentEnergy1, parentEnergy2);

			// Mutacje genotypu potomka
			mutateGenotype(offspringGenotype, maxChildMutations, minChildMutations);

			// Tworzenie nowego zwierzęcia
			Animal offspring = new Animal(this.getPosition(), offspringEnergy, offspringGenotype);

			// Dodanie potomka do rodziców
			this.addChildren(offspring);
			partner.addChildren(offspring);

			return offspring;
		}

		// Zwraca null, jeśli warunki rozmnażania nie są spełnione
		return null;
	}

	private int calculateOffspringEnergy(int energy1, int energy2, int totalEnergy) {
		return (int) ((Math.pow(energy1, 2) + Math.pow(energy2, 2)) / (double) totalEnergy);
	}

	private int calculateEnergyReduction(int parentEnergy, int totalEnergy) {
		return (int) (Math.pow(parentEnergy, 2) / (double) totalEnergy);
	}

	private int[] crossoverGenotypes(int[] genotype1, int[] genotype2, int energy1, int energy2) {
		int length = genotype1.length;
		int[] offspringGenotype = new int[length];
		Random random = new Random();

		// Wyznaczenie punktu podziału
		double ratio1 = energy1 / (double) (energy1 + energy2);
		int splitPoint = (int) (length * ratio1);

		// Losowanie strony podziału
		boolean strongerParentRightSide = random.nextBoolean();

		if (strongerParentRightSide) {
			// Silniejszy rodzic daje prawą stronę
			System.arraycopy(genotype1, 0, offspringGenotype, 0, splitPoint);
			System.arraycopy(genotype2, splitPoint, offspringGenotype, splitPoint, length - splitPoint);
		} else {
			// Silniejszy rodzic daje lewą stronę
			System.arraycopy(genotype2, 0, offspringGenotype, 0, splitPoint);
			System.arraycopy(genotype1, splitPoint, offspringGenotype, splitPoint, length - splitPoint);
		}

		return offspringGenotype;
	}

	private void mutateGenotype(int[] genotype, int maxChildMutations, int minChildMutations) {
		Random random = new Random();

		// Upewnienie się, że minChildMutations nie przekracza maxChildMutations
		if (minChildMutations > maxChildMutations) {
			minChildMutations = maxChildMutations;
		}

		// Liczba mutacji
		int numberOfMutations = random.nextInt((maxChildMutations - minChildMutations) + 1) + minChildMutations;

		// Wprowadzanie mutacji
		for (int i = 0; i < numberOfMutations; i++) {
			int index = random.nextInt(genotype.length); // Losowy indeks w genotypie
			genotype[index] = random.nextInt(8); // Nowa wartość genu (od 0 do 7)
		}
	}

}
