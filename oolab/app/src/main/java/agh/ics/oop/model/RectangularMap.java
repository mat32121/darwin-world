package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RectangularMap extends AbstractWorldMap {
	private final int width, height;
	private final Vector2d boundary;
	private final int jungleLowerBound;
	private final int jungleUpperBound;
	private final int minCopulateEnergy;
	private final int initialEnergy;
	private final int numInitialGrass;
	private final int energyPerGrass;
	private final int numGrassPerDay;
	private final int numInitialAnimals;
	private final int minChildMutations;
	private final int maxChildMutations;
	private final int numGenes;
	private final Map<Vector2d, Grass> grassPatches;
	private final ArrayList<Vector2d> freeSteppePositions;
	private final ArrayList<Vector2d> freeJunglePositions;
	private final Random random;

	public RectangularMap(int width, int height, int numInitialGrass, int energyPerGrass, int numGrassPerDay, int numInitialAnimals, int initialEnergy, int minCopulateEnergy, int minChildMutations, int maxChildMutations, int numGenes) {
		this.width = width;
		this.height = height;
		this.initialEnergy = initialEnergy;
		this.grassPatches = new HashMap<>();
        this.boundary = new Vector2d(width-1, height-1);
		this.freeSteppePositions = new ArrayList<>();
		this.freeJunglePositions = new ArrayList<>();
		this.minCopulateEnergy = minCopulateEnergy;
		this.numInitialGrass = numInitialGrass;
		this.energyPerGrass = energyPerGrass;
		this.numGrassPerDay = numGrassPerDay;
		this.numInitialAnimals = numInitialAnimals;
		this.minChildMutations = minChildMutations;
		this.maxChildMutations = maxChildMutations;
		this.numGenes = numGenes;

		int p = 0;
		if (this.height%2==0) {
			p+=1;
		}
		this.jungleLowerBound = Math.max((this.height / 2) - (int) Math.ceil(this.height * 0.1) + (1 - p),0);
		this.jungleUpperBound = Math.max((this.height / 2) + (int) Math.ceil(this.height * 0.1) -1,0);
		this.random = new Random();

		for(int i = 0; i < width; ++i)
			for(int j = 0; j < height; ++j) {
				Vector2d newPosition = new Vector2d(i, j);
				if (this.isJungle(newPosition))
					this.freeJunglePositions.add(newPosition);
				else
					this.freeSteppePositions.add(newPosition);
			}

		this.grassGrows(numInitialGrass, true);
	}

	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getNumInitialAnimals() {
		return numInitialAnimals;
	}

	@Override
	public int getMinChildMutations() {
		return minChildMutations;
	}

	@Override
	public int getMaxChildMutations() {
		return maxChildMutations;
	}

	@Override
	public int getNumGenes() {
		return numGenes;
	}

	@Override
	public int getNumGrassPerDay() {
		return numGrassPerDay;
	}

	@Override
	public int getInitialEnergy() {
		return initialEnergy;
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
		if (this.isJungle(position))
			this.freeJunglePositions.add(position);
		else
			this.freeSteppePositions.add(position);
	}

	@Override
	public void grassGrows(int grassNumber, boolean inicialization) {
		for (int i = 0; i < grassNumber; i++) {
			boolean growInSteppe = this.random.nextDouble(1.0) < 0.2;
			List<Vector2d> targetList = growInSteppe ? this.freeSteppePositions : this.freeJunglePositions;
			List<Vector2d> backupList = growInSteppe ?  this.freeJunglePositions : this.freeSteppePositions;

			if (!targetList.isEmpty()) {
				addGrassAtRandomPosition(targetList);
			} else if(inicialization && !backupList.isEmpty()){
				addGrassAtRandomPosition(backupList);
			}
		}
	}

	private void addGrassAtRandomPosition(List<Vector2d> positions) {
		if (!positions.isEmpty()) {
			int randomIndex = this.random.nextInt(positions.size());
			Vector2d position = positions.get(randomIndex);
			this.grassPatches.put(position, new Grass(position));
			positions.remove(randomIndex);
		}
	}
	@Override
	public boolean grassAt(Vector2d position) {
		return grassPatches.containsKey(position);
	}

	@Override
	public void animalEatsGrass(Animal animal) {
		animal.changeEnergy(energyPerGrass);
		this.removeGrass(animal.getPosition());
	}

	@Override
	public void removeGrass(Vector2d position) {
		grassPatches.remove(position);
	}

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
