package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement, Comparable {
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
		//this.genIndex=(int) (Math.random() * genotype.length); //mozna poprawic na randomnext
		this.genIndex=0; //**** do usuniecia!!! poprawna wersja wyzej /\
		//***zakladam, ze zawsze tworzymy zywego zwierzaka
		this.children = new ArrayList<>();
		this.age = 0;
		this.rng = new Random();
	}

	public int compareTo(Object other) {
		if(other instanceof Animal animal) {
			int res = animal.getEnergy()-this.energy;
			if(res == 0)
				res = animal.getAge()-this.age;
			if(res == 0)
				res = animal.getNumChildren()-this.getNumChildren();
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

	public void setLiveStatus(boolean status) {
		this.liveStatus=status;
	}

	//*** przydalby sie moze ladniejszy mechanizm

	public void incrementDaysAfterDeath() {
		this.daysAfterDeath += 1;
	}

	public void changeEnergy(int n) {
		this.energy += n;
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

	public void move(Boundary boundary) {
		//*** do poprawy skladni!!!
		Vector2d newPosition = new Vector2d(this.position.getX(), this.position.getY());
		newPosition=newPosition.add(this.direction.toUnitVector());
		this.changeEnergy(-1);
		//dodac liczbe zalezna od wspolrzednej y
		this.direction=this.direction.next(genotype[genIndex]);
		genIndex=(genIndex+1)%genotype.length;
		//uwaga, zwierze wpierw rusza sie o jeden, a dopiero potem zmienia kierunek na ten w kodzie genetycznym.
		//***prawdopodobnie da sie szybciej zrobic funkcje, ktora zapetla mape, niz ta ponizej
		if (!newPosition.isWithinBounds(boundary)){
			newPosition=newPosition.placeWithinBounds(boundary);
		}
		this.position = newPosition;

		System.out.println(this.energy);
	}

	// TODO: Implement
	// public Animal copulate(Animal mate) {
		// return new Animal();
	// }

    public int getAge() {
        return age;
    }

	public int getNumChildren() {
		return this.children.size();
	}
}
