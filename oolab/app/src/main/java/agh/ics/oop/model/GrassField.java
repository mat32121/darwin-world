package agh.ics.oop.model;

import java.lang.Math;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GrassField extends AbstractWorldMap {
	private static final Vector2d LOWER_LEFT_INF = new Vector2d(Integer.MIN_VALUE+1, Integer.MIN_VALUE+1);
	private static final Vector2d UPPER_RIGHT_INF = new Vector2d(Integer.MAX_VALUE-1, Integer.MAX_VALUE-1);

	private final int numGrass;
	private final int maxSide;
	private final Map<Vector2d, Grass> grassPatches;
	private Vector2d lowerLeftGrass;
	private Vector2d upperRightGrass;

	public GrassField(int numGrass) {
		this.numGrass = numGrass;
		this.maxSide = (int)Math.sqrt(10*numGrass)+1;
		this.grassPatches = new HashMap<>();
		this.lowerLeftGrass = null;
		this.upperRightGrass = null;

		if(this.numGrass > 0) {
			this.lowerLeftGrass = new Vector2d(this.maxSide, this.maxSide);
			this.upperRightGrass = AbstractWorldMap.ORIGIN;
			RandomPositionGenerator randPosGen = new RandomPositionGenerator(this.maxSide, this.maxSide, this.numGrass);
			for(Vector2d grassPosition : randPosGen) {
				this.grassPatches.put(grassPosition, new Grass(grassPosition));
				this.lowerLeftGrass = this.lowerLeftGrass.lowerLeft(grassPosition);
				this.upperRightGrass = this.upperRightGrass.upperRight(grassPosition);
			}
		}
	}

	@Override
	public boolean canMoveTo(Vector2d position) {
		return position.follows(GrassField.LOWER_LEFT_INF)
		    && position.precedes(GrassField.UPPER_RIGHT_INF)
		    && super.canMoveTo(position);
	}

	@Override
	public WorldElement objectAt(Vector2d position) {
		WorldElement foundAnimal = super.objectAt(position);
		if(foundAnimal == null)
			return this.grassPatches.get(position);
		return foundAnimal;
	}

	@Override
	public String toString() {
		Vector2d lowerLeft = null;
		Vector2d upperRight = null;

		if(this.lowerLeftGrass != null) {
			lowerLeft = lowerLeftGrass;
			upperRight = upperRightGrass;
		}
		else if(!this.animals.isEmpty()) {
			lowerLeft = this.animals.entrySet().iterator().next().getKey();
			upperRight = lowerLeft;
		}
		for(Map.Entry<Vector2d, Animal> animal : this.animals.entrySet()) {
			lowerLeft = lowerLeft.lowerLeft(animal.getKey());
			upperRight = upperRight.upperRight(animal.getKey());
		}

		if(lowerLeft == null)
			return this.visualizer.draw(GrassField.LOWER_LEFT_INF, GrassField.LOWER_LEFT_INF);
		return this.visualizer.draw(lowerLeft, upperRight);
	}

	@Override
	public List<WorldElement> getElements() {
		List<WorldElement> elements = super.getElements();
		for(Map.Entry<Vector2d, Grass> grass : this.grassPatches.entrySet())
			elements.add(grass.getValue());
		return elements;
	}
}
