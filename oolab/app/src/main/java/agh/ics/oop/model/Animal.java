package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

public class Animal implements WorldElement {
	//private List<Animal> children = new ArrayList<>();
	//private int age = 0;
	//private int plantsEaten = 0;
	private MapDirection direction;
	private Vector2d position;
	private int energy;
	private boolean liveStatus;
	private int daysAfterDeath;
	private int genIndex;
	protected final int[] genotype;


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
		return daysAfterDeath;
	}

	public void setLiveStatus(boolean status) {
		this.liveStatus=status;
	}

	//*** przydalby sie moze ladniejszy mechanizm

	public void incrementDaysAfterDeath() {
		this.daysAfterDeath += 1;
	}

	public void incrementEnergy() {
		this.energy += 4;
	}



	@Override
	public String toString() {
		return switch(this.direction) {
			//***mapa jest odwrocona, dlatego NORTH -> "↓", do poprawy!
			case NORTH -> "↓";
			case NORTH_WEST->"↙";
			case WEST  -> "←";
            case SOUTH_WEST -> "↖";
            case SOUTH -> "↑";
            case SOUTH_EAST -> "↗";
            case EAST  -> "→";
            case NORTH_EAST -> "↘";
        };
	}

	public boolean isAt(Vector2d position) {
		return this.position.equals(position);
	}

	public boolean isHeaded(MapDirection direction) {
		return this.direction == direction;
	}

	public void move(MoveValidator validator) {
		//*** do poprawy skladni!!!
		Vector2d newPosition = new Vector2d(this.position.getX(), this.position.getY());
		newPosition=newPosition.add(this.direction.toUnitVector());
		this.energy-=1;
		//dodac liczbe zalezna od wspolrzednej y
		this.direction=this.direction.next(genotype[genIndex]);
		genIndex=(genIndex+1)%genotype.length;
		//uwaga, zwierze wpierw rusza sie o jeden, a dopiero potem zmienia kierunek na ten w kodzie genetycznym.
		//wymagana poprawa, ale do tego potrzeba jest przebudowa simulation.
		if(validator.canMoveTo(newPosition)) {
			this.position = newPosition;
		}
		System.out.println(this.energy);
	}
}
