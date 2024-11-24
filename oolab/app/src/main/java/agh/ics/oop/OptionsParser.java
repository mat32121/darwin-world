package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.List;
import java.util.ArrayList;

public class OptionsParser {
    public static List<MoveDirection> parse(String[] steps_string) {
        List<MoveDirection> result = new ArrayList<MoveDirection>();
        for(String step : steps_string) {
            boolean correctArg = true;
            if(step.length() == 1) {
				switch(step.charAt(0)) {
					case 'f' -> result.add(MoveDirection.FORWARD);
					case 'l' -> result.add(MoveDirection.LEFT);
					case 'b' -> result.add(MoveDirection.BACKWARD);
					case 'r' -> result.add(MoveDirection.RIGHT);
                    default  -> correctArg = false;
				};
            }
            else
                correctArg = false;
            if(!correctArg)
                throw new IllegalArgumentException(step + " is not a legal move specification");
        }

        return result;
    }
}
