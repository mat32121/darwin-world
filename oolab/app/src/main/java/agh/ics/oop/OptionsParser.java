package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    //***do usuniecia
    public static List<Integer> parse(List<String> steps_string) {
        ArrayList<Integer> result = new ArrayList<>();
        for(String step : steps_string) {
            if(step.length() == 1) {
				switch(step.charAt(0)) {
					case '0' -> result.add(0);
					case '1' -> result.add(1);
					case '2' -> result.add(2);
					case '3' -> result.add(3);
                    case '4' -> result.add(4);
                    case '5' -> result.add(5);
                    case '6' -> result.add(6);
                    case '7' -> result.add(7);
                    default  -> throw new IllegalArgumentException(step + " is not a legal move specification");
				};
            }
            else
                throw new IllegalArgumentException(step + " is not a legal move specification");
        }

        return result;
    }
}
