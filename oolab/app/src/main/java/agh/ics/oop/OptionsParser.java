package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

public class OptionsParser {
    public static MoveDirection[] parse(String[] steps_string) {
        int numMoves = 0;
        for(String step : steps_string)
            if(step.length() == 1) {
                char x = step.charAt(0);
				if(x == 'f' || x == 'l' || x == 'b' || x == 'r') {
					++numMoves;
				}
            }

        int counter = 0;
        MoveDirection result[] = new MoveDirection[numMoves];
        for(String step : steps_string)
            if(step.length() == 1)
				switch(step.charAt(0)) {
					case 'f' -> result[counter++] = MoveDirection.FORWARD;
					case 'l' -> result[counter++] = MoveDirection.LEFT;
					case 'b' -> result[counter++] = MoveDirection.BACKWARD;
					case 'r' -> result[counter++] = MoveDirection.RIGHT;
				};

        return result;
    }
}
