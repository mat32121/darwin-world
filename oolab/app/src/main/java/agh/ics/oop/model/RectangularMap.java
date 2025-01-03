package agh.ics.oop.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RectangularMap extends AbstractWorldMap {
	private final int width, height;
	private final Vector2d boundary;
	private final int startingGrass;
	private Map<Vector2d, Grass> grassPatches;

	public RectangularMap(int width, int height, int startingGrass) {
		this.width = width;
		this.height = height;
        this.startingGrass = startingGrass;
		this.grassPatches = new HashMap<>();
        this.boundary = new Vector2d(width-1, height-1);

		if(this.startingGrass > 0) {
			RandomPositionGenerator randPosGen = new RandomPositionGenerator(this.height, this.width, this.startingGrass);
			for(Vector2d grassPosition : randPosGen) {
				this.grassPatches.put(grassPosition, new Grass(grassPosition));
			}
			//*** do usuniecia, testy!!!
			Vector2d testVector=new Vector2d(5,5);
			this.grassPatches.put(testVector, new Grass(testVector));
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
