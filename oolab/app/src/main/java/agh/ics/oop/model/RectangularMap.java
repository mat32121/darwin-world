package agh.ics.oop.model;

import java.util.*;

public class RectangularMap extends AbstractWorldMap {
	private final int width, height;
	private final Vector2d boundary;
	private final int jungleLowerBound;
	private final int jungleUpperBound;
	private final int minCopulateEnergy;
	private Map<Vector2d, Grass> grassPatches;
	private ArrayList<Vector2d> freePositions;

	//*** czy wystepuje startowa ilosc trawy?
	public RectangularMap(int width, int height, int numInitialGrass, int energyPerGrass, int numGrassPerDay, int numInitialAnimals, int initialEnergy, int minCopulateEnergy, int copulateEnergyUsed, int minChildMutations, int maxChildMutations, int numGenes) {
		this.width = width;
		this.height = height;
		this.grassPatches = new HashMap<>();
        this.boundary = new Vector2d(width-1, height-1);
		this.freePositions = new ArrayList<>(width * height);
		this.minCopulateEnergy = minCopulateEnergy;

		int p = 0;
		if (this.height%2==0) {
			p+=1;
		}
		this.jungleLowerBound = (this.height / 2) - (int) Math.ceil(this.height * 0.1);
		this.jungleUpperBound = (this.height / 2) + (int) Math.ceil(this.height * 0.1) - p;

		for(int i = 0; i < width; ++i)
			for(int j = 0; j < height; ++j)
				this.freePositions.add(new Vector2d(i, j));
	}

	@Override
	public int getMinCopulateEnergy() {
		return minCopulateEnergy;
	}

	@Override
	public boolean isJungle(Vector2d position) {
		return position.getY() >= this.jungleLowerBound && position.getY() <= this.jungleUpperBound;
	}

	@Override
	public void addFreePosition(Vector2d position) {
		this.freePositions.add(position);
	}
	//*** do poprawy, czas O(n)!! ~ chociaz ilosc elementow to max 3000, ale i tak warto by bylo
	//przeniesc mechanizm losowania do innej klasy.

	@Override
	public void grassGrows() {
		int i = 0;
		while (i < this.freePositions.size()) {
			if (Math.random() < 0.01) {
				Vector2d position = this.freePositions.get(i);
				this.grassPatches.put(position, new Grass(position));
				this.freePositions.remove(i); // Usunięcie elementu bez zwiększania indeksu
			} else {
				i++; // Przejście do następnego elementu tylko, gdy nie usuwamy
			}
		}
	}

	@Override
	public boolean grassAt(Vector2d position) {
		return grassPatches.containsKey(position);
	}

	@Override
	public void animalEatsGrass(Animal animal) {
		animal.changeEnergy(4);
		//*** mozliwosc ustawienia wartosci energetycznej rosliny do dodania!
		this.removeGrass(animal.getPosition());
	}

	@Override
	public void removeGrass(Vector2d position) {
		grassPatches.remove(position);
	}




	//***do poprawy, na jednym polu moze byc wiele zwierzat, poza tym zwierze moze poruszyc sie na dowolne pole
	/*
	@Override
	public boolean canMoveTo(Vector2d position) {
		return position.follows(AbstractWorldMap.ORIGIN)
		    && position.precedes(this.boundary)
		    && super.canMoveTo(position);
	}
	 */

	@Override
	public List<WorldElement> getElements() {
		List<WorldElement> elements = super.getElements();
        elements.addAll(this.grassPatches.values());
		return elements;
	}

	@Override
	public WorldElement objectAt(Vector2d position) {
		WorldElement foundAnimal = super.objectAt(position);
		if(foundAnimal == null)
			return this.grassPatches.get(position);
		return foundAnimal;
	}

	@Override
	public Boundary getCurrentBounds() {
		return new Boundary(AbstractWorldMap.ORIGIN, this.boundary);
	}
};
