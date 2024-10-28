package agh.ics.oop.model;

public enum MapDirection {
	NORTH("Północ", new Vector2d(0, 1)),
	EAST("Wschód", new Vector2d(1, 0)),
	SOUTH("Południe", new Vector2d(0, -1)),
	WEST("Zachód", new Vector2d(-1, 0));

	private static final MapDirection[] VAL_ARRAY = values();
	private final String directionName;
	private final Vector2d unitVector;

	MapDirection(String directionName, Vector2d unitVector) {
		this.directionName = directionName;
		this.unitVector = unitVector;
	}

	@Override
	public String toString() {
		return this.directionName;
	}

	public MapDirection next() {
		return VAL_ARRAY[(this.ordinal()+1)%VAL_ARRAY.length];
	}

	public MapDirection previous() {
		return VAL_ARRAY[(this.ordinal()+VAL_ARRAY.length-1)%VAL_ARRAY.length];
	}

	public Vector2d toUnitVector() {
		return this.unitVector;
	}
}
