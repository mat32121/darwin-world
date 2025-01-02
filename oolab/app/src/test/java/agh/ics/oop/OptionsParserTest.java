package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class OptionsParserTest {

	@Test
	public void testParse() {
        ArrayList<String> orderStringA = new ArrayList<>(Arrays.asList("0","1","2","4","5","6","7"));
        List<Integer> orderIntigersA = List.of(0, 1, 2, 4, 5, 6, 7);
        List<Integer> parserIntigersA = OptionsParser.parse(orderStringA);

        assertEquals(orderIntigersA,  parserIntigersA);

        ArrayList<String> orderStringB = new ArrayList<>(Arrays.asList("1","2","3","4","0","1"));
        List<Integer> orderIntigersB = List.of(1, 2, 3, 4, 0, 1);
        List<Integer> parserIntigersB = OptionsParser.parse(orderStringB);

        System.out.println(parserIntigersB);

        //assertEquals(orderIntigersB,  parserIntigersB);
        // Tworzenie dwóch list


        /*
		final String orderStringB[] = {"l","r","f","l","l","b"};
		final List<MoveDirection> realParsedOrdersB = List.of(
			MoveDirection.LEFT,
			MoveDirection.RIGHT,
			MoveDirection.FORWARD,
			MoveDirection.LEFT,
			MoveDirection.LEFT,
			MoveDirection.BACKWARD
		);

		final String orderStringC[] = {};
		final List<MoveDirection> realParsedOrdersC = List.of();

		final String orderStringD0[] = {"k", "o"};
		assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(List.of(orderStringD0)));

		final List<MoveDirection> parsedOrdersA = OptionsParser.parse(List.of(orderStringA));
		final List<MoveDirection> parsedOrdersB = OptionsParser.parse(List.of(orderStringB));
		final List<MoveDirection> parsedOrdersC = OptionsParser.parse(List.of(orderStringC));

		assertEquals(realParsedOrdersA, parsedOrdersA);
		assertEquals(realParsedOrdersB, parsedOrdersB);
		assertEquals(realParsedOrdersC, parsedOrdersC);
		assertNotEquals(realParsedOrdersA, parsedOrdersB);
         */
	}
}
