package agh.ics.oop.model;

import java.util.*;

public class RectangularMap extends AbstractWorldMap {
	private final int width, height;
	private final Vector2d boundary;
	private Map<Vector2d, Grass> grassPatches;
	private ArrayList<Vector2d> freePositions;

	//*** czy wystepuje startowa ilosc trawy?
	public RectangularMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.grassPatches = new HashMap<>();
        this.boundary = new Vector2d(width-1, height-1);
		this.freePositions = new ArrayList<>(width * height);

		//przygotowywujemy tablice wolnych miejsc.
		for(int i = 0; i < width; ++i)
			for(int j = 0; j < height; ++j)
				this.freePositions.add(new Vector2d(i, j));
	}

	@Override
	public void addFreePosition(Vector2d position) {
		this.freePositions.add(position);
	}
	//*** do poprawy, czas O(n)!! ~ chociaz ilosc elementow to max 3000
	@Override
	public void grassGrows() {
		int i = 0;
		while (i < this.freePositions.size()) {
			if (Math.random() < 0.02) {
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
		//wartosc energetyczna rosliny do ustawienia
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
		for(Grass grass : this.grassPatches.values())
			elements.add(grass);
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
