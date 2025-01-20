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

	public Animal copulate(Animal partner, int reproductionEnergyThreshold) {
		// Suma energii rodziców

		int s1 = this.getEnergy();
		int s2 = partner.getEnergy();
		int S = s1 + s2;

		// Sprawdzenie, czy oboje rodziców mają wystarczającą energię
		if (s1 >= reproductionEnergyThreshold && s2 >= reproductionEnergyThreshold) {
			// Obliczenie energii nowego zwierzęcia
			int offspringEnergy = (int) ((Math.pow(s1, 2) + Math.pow(s2, 2)) / (double) S);

			// Aktualizacja energii rodziców za pomocą changeEnergy
			this.changeEnergy(-(int) (Math.pow(s1, 2) / (double) S));
			partner.changeEnergy(-(int) (Math.pow(s2, 2) / (double) S));

			// Krzyżowanie genotypów rodziców
			int[] offspringGenotype = crossoverGenotypes(this.getGenotype(), partner.getGenotype(), s1, s2);

			// Tworzenie nowego zwierzęcia z genotypem i energią
			Animal offspring = new Animal(this.getPosition(),offspringEnergy, offspringGenotype);

			// Mutacje w genotypie potomka
			mutateGenotype(offspring.getGenotype());

			// Przypisanie potomka do rodziców
			this.addChildren(offspring);
			partner.addChildren(offspring);

			return offspring;
		}

		// Zwraca null, jeśli warunki rozmnażania nie są spełnione
		return null;
	}

	private int[] crossoverGenotypes(int[] genotype1, int[] genotype2, int energy1, int energy2) {
		int length = genotype1.length; // Długość genotypu (stała)
		int[] offspringGenotype = new int[length];
		Random random = new Random();

		// Wyznaczenie proporcji podziału genotypu
		double ratio1 = energy1 / (double) (energy1 + energy2);
		int splitPoint = (int) (length * ratio1);

		// Losowanie strony podziału (lewa czy prawa)
		boolean strongerParentRightSide = random.nextBoolean();

		if (strongerParentRightSide) {
			// Silniejszy rodzic daje prawą stronę genotypu
			System.arraycopy(genotype1, 0, offspringGenotype, 0, splitPoint);
			System.arraycopy(genotype2, splitPoint, offspringGenotype, splitPoint, length - splitPoint);
		} else {
			// Silniejszy rodzic daje lewą stronę genotypu
			System.arraycopy(genotype2, 0, offspringGenotype, 0, splitPoint);
			System.arraycopy(genotype1, splitPoint, offspringGenotype, splitPoint, length - splitPoint);
		}

		return offspringGenotype;
	}

	private void mutateGenotype(int[] genotype) {
		Random random = new Random();
		// TODO: przekazywac maksymalna ilosc zmiany pozycji
		int numberOfMutations = random.nextInt(genotype.length) + 1; // Liczba mutacji (co najmniej 1)

		for (int i = 0; i < numberOfMutations; i++) {
			int index = random.nextInt(genotype.length); // Losowy indeks w tablicy genotypu
			genotype[index] = random.nextInt(8); // Nowa losowa wartość genu (od 0 do 7)
		}
	}

}
