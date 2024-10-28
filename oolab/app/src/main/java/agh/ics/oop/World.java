package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.MapDirection;

public class World {
    private static void run(MoveDirection[] steps) {
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
        MoveDirection steps[] = OptionsParser.parse(args);
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
    }
}
