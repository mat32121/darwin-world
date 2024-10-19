package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

public class World {
    private static void run(String[] args) {
        MoveDirection steps[] = OptionsParser.parse(args);
        for(MoveDirection step : steps) {
            String message = switch (step) {
                case MoveDirection.FORWARD  -> "Zwierzak idzie do przodu";
                case MoveDirection.LEFT     -> "Zwierzak skręca w lewo";
                case MoveDirection.BACKWARD -> "Zwierzak idzie do tyłu";
                case MoveDirection.RIGHT    -> "Zwierzak skręca w prawo";
                default                     -> "Nieznana komenda";
            };
            System.out.println(message);
        }
    }
    public static void main(String[] args) {
        System.out.println("Start");
        run(args);
        System.out.println("Stop");
    }
}