package agh.ics.oop.model;

public enum MapDirection {
	NORTH("Północ", new Vector2d(0, 1)),
	EAST("Wschód", new Vector2d(1, 0)),
	SOUTH("Południe", new Vector2d(0, -1)),
	WEST("Zachód", new Vector2d(-1, 0));

	private static final MapDirection[] valArray = values();
	private final String directionName;
	private final Vector2d unitVector;

	MapDirection(String directionName, Vector2d unitVector) {
		this.directionName = directionName;
		this.unitVector = unitVector;
	}

	@Override public String toString() {
		return this.directionName;
	}

	public MapDirection next() {
		return valArray[(this.ordinal()+1)%valArray.length];
	}

	public MapDirection previous() {
		return valArray[(this.ordinal()+valArray.length-1)%valArray.length];
	}

	public Vector2d toUnitVector() {
		return this.unitVector;
	}
}
