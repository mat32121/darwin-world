package agh.ics.oop;

public class World {
    private static void run(String[] args) {
        for(String step : args)
            if(step.length() == 1)
                switch (step.charAt(0)) {
                    case 'f' -> System.out.println("Zwierzak idzie do przodu");
                    case 'b' -> System.out.println("Zwierzak idzie do tyłu");
                    case 'r' -> System.out.println("Zwierzak skręca w prawo");
                    case 'l' -> System.out.println("Zwierzak skręca w lewo");
                };
    }
    public static void main(String[] args) {
        System.out.println("Start");
        run(args);
        System.out.println("Stop");
    }
}