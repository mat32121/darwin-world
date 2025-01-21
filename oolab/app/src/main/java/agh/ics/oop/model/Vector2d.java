package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
	private final int x, y;

	public Vector2d(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {return this.x;}
	public int getY() {return this.y;}

	@Override
	public String toString() {
		return "(%d,%d)".formatted(this.x, this.y);
	}

	public boolean precedes(Vector2d other) {
		return (this.x <= other.x && this.y <= other.y);
	}

	public boolean follows(Vector2d other) {
		return (other.x <= this.x && other.y <= this.y);
	}

	public Vector2d add(Vector2d other) {
		return new Vector2d(this.x+other.x, this.y+other.y);
	}

	public Vector2d subtract(Vector2d other) {
		return new Vector2d(this.x-other.x, this.y-other.y);
	}

	public Vector2d upperRight(Vector2d other) {
		return new Vector2d((Math.max(this.x, other.x)),
							(Math.max(this.y, other.y)));
	}

	public Vector2d lowerLeft(Vector2d other) {
		return new Vector2d((Math.min(this.x, other.x)),
							(Math.min(this.y, other.y)));
	}

	public Vector2d opposite() {
		return new Vector2d(-this.x, -this.y);
	}

	public boolean isWithinBounds(Boundary boundary) {
		Vector2d lowerLeftCorner = boundary.lowerLeft();
		Vector2d upperRightCorner = boundary.upperRight();
		return this.x >= lowerLeftCorner.getX() && this.x <= upperRightCorner.getX()
				&& this.y >= lowerLeftCorner.getY() && this.y <= upperRightCorner.getY();
	}

	@Override
	public boolean equals(Object other) {
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(this.getClass() != other.getClass())
			return false;
		Vector2d that = (Vector2d)other;
		return Objects.equals(this.x, that.x)
			&& Objects.equals(this.y, that.y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.y);
	}
}
