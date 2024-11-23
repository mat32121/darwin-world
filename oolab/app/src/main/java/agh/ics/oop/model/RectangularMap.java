package agh.ics.oop.model;

public class RectangularMap extends AbstractWorldMap {
	private final int width, height;
	private final Vector2d boundary;

	public RectangularMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.boundary = new Vector2d(width-1, height-1);
	}

	@Override
	public boolean canMoveTo(Vector2d position) {
		return position.follows(AbstractWorldMap.ORIGIN)
		    && position.precedes(this.boundary)
		    && super.canMoveTo(position);
	}
	@Override
	public String toString() {
		return super.visualizer.draw(AbstractWorldMap.ORIGIN, this.boundary);
	}
	@Override
	public Animal objectAt(Vector2d position) {
		return (Animal)super.objectAt(position);
	}
};
