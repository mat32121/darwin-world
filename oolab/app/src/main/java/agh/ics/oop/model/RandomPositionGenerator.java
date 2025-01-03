package agh.ics.oop.model;

import java.lang.Iterable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class RandomPositionGenerator implements Iterable<Vector2d>, Iterator<Vector2d> {
	private List<Vector2d> positions;
	private int counter;

	@Override
	public Iterator<Vector2d> iterator() {
		return this;
	}

	public RandomPositionGenerator(ArrayList<Vector2d> wholeSpace, int grassCount) {
		this.positions = wholeSpace;
		this.counter = grassCount;
		Collections.shuffle(this.positions);
	}

	@Override
	public boolean hasNext() {
		return this.counter > 0;
	}

	@Override
	public Vector2d next() {
		return this.positions.get(--this.counter);
	}
}
