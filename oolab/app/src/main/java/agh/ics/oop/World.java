package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Vector2d;

import java.util.List;

public class World {
	private static void run(List<MoveDirection> steps) {
		for(MoveDirection step : steps)
			switch (step) {
				case MoveDirection.FORWARD  -> System.out.println("Zwierzak idzie do przodu");
				case MoveDirection.LEFT     -> System.out.println("Zwierzak skręca w lewo");
				case MoveDirection.BACKWARD -> System.out.println("Zwierzak idzie do tyłu");
				case MoveDirection.RIGHT    -> System.out.println("Zwierzak skręca w prawo");
			};
	}

	public static void main(String[] args) {
		System.out.println("Start");
		List<MoveDirection> steps = OptionsParser.parse(args);
		run(steps);
		System.out.println("Stop");

		Vector2d position1 = new Vector2d(1,2);
		System.out.println(position1);
		Vector2d position2 = new Vector2d(-2,1);
		System.out.println(position2);
		System.out.println(position1.add(position2));

		MapDirection direction1 = MapDirection.NORTH;
		for(int i = 0; i < 4; ++i) {
			System.out.println(direction1.toString());
			System.out.println(direction1.toUnitVector());
			direction1 = direction1.next();
		}

		Animal animal = new Animal();
		System.out.println(animal.toString());

		List<MoveDirection> directions = OptionsParser.parse(args);
		List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
		RectangularMap map = new RectangularMap(5, 5);
		Simulation simulation = new Simulation(positions, directions, map);
		simulation.run();

		GrassField grassField = new GrassField(10);
		Simulation grassSim = new Simulation(positions, directions, grassField);
		grassSim.run();
	}
}
