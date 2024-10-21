package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

public class OptionsParser {
    public static MoveDirection[] parse(String[] args) {
        final char VALID_MOVES[] = {'f', 'l', 'b', 'r'};
        final MoveDirection MOVES[] = {
            MoveDirection.FORWARD,
            MoveDirection.LEFT,
            MoveDirection.BACKWARD,
            MoveDirection.RIGHT
        };

        int numMoves = 0;
        for(String step : args)
            if(step.length() == 1) {
                char x = step.charAt(0);
                for(int i = 0; i < VALID_MOVES.length; ++i)
                    if(x == VALID_MOVES[i]) {
                        ++numMoves;
                        break;
                    }
            }

        int counter = 0;
        MoveDirection result[] = new MoveDirection[numMoves];
        for(String step : args)
            if(step.length() == 1) {
                char x = step.charAt(0);
                for(int i = 0; i < VALID_MOVES.length; ++i)
                    if(x == VALID_MOVES[i]) {
                        result[counter++] = MOVES[i];
                        break;
                    }
            }

        return result;
    }
}