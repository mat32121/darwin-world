package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

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
    }
}
