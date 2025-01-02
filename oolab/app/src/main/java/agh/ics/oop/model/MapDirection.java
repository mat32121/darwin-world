package agh.ics.oop.model;

public enum MapDirection {
	NORTH("Północ", new Vector2d(0, 1)),
	NORTH_WEST("Północny Zachód", new Vector2d(-1, 1)),
	WEST("Zachód", new Vector2d(-1, 0)),
	SOUTH_WEST("Południowy Zachód", new Vector2d(-1, -1)),
	SOUTH("Południe", new Vector2d(0, -1)),
	SOUTH_EAST("Południowy Wschód", new Vector2d(1, -1)),
	EAST("Wschód", new Vector2d(1, 0)),
	NORTH_EAST("Północny Wschód", new Vector2d(1, 1));

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

	public MapDirection next(int n) {
		//zakladamy, ze n<8, ale jakby bylo wieksze(mniejsze), to nic nie zmineia.
		return VAL_ARRAY[(this.ordinal()+n)%VAL_ARRAY.length];
	}

	public Vector2d toUnitVector() {
		return this.unitVector;
	}
}
