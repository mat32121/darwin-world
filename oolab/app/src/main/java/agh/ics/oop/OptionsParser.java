package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

import agh.ics.oop.model.MoveDirection;

public class OptionsParser {
    public static List<MoveDirection> parse(List<String> steps_string) {
        List<MoveDirection> result = new ArrayList<>();
        for(String step : steps_string) {
            if(step.length() == 1) {
				switch(step.charAt(0)) {
					case 'f' -> result.add(MoveDirection.FORWARD);
					case 'l' -> result.add(MoveDirection.LEFT);
					case 'b' -> result.add(MoveDirection.BACKWARD);
					case 'r' -> result.add(MoveDirection.RIGHT);
                    default  -> throw new IllegalArgumentException(step + " is not a legal move specification");
				};
            }
            else
                throw new IllegalArgumentException(step + " is not a legal move specification");
        }

        return result;
    }
}
