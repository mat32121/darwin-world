package agh.ics.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.MoveDirection;

public class OptionsParserTest {
	@Test
	public void testParse() {
		final String orderStringA[] = {"f","l","b","r"};
		final MoveDirection realParsedOrdersA[] = {
			MoveDirection.FORWARD,
			MoveDirection.LEFT,
			MoveDirection.BACKWARD,
			MoveDirection.RIGHT
		};

		final String orderStringB[] = {"l","r","34","f","l","ff","f-b","Xb","l","b"};
		final MoveDirection realParsedOrdersB[] = {
			MoveDirection.LEFT,
			MoveDirection.RIGHT,
			MoveDirection.FORWARD,
			MoveDirection.LEFT,
			MoveDirection.LEFT,
			MoveDirection.BACKWARD
		};

		final String orderStringC[] = {};
		final MoveDirection realParsedOrdersC[] = {};

		final String orderStringD[] = {"k", "o"};
		final MoveDirection realParsedOrdersD[] = {};

		final MoveDirection parsedOrdersA[] = OptionsParser.parse(orderStringA);
		final MoveDirection parsedOrdersB[] = OptionsParser.parse(orderStringB);
		final MoveDirection parsedOrdersC[] = OptionsParser.parse(orderStringC);
		final MoveDirection parsedOrdersD[] = OptionsParser.parse(orderStringD);

		assertArrayEquals(realParsedOrdersA, parsedOrdersA);
		assertArrayEquals(realParsedOrdersB, parsedOrdersB);
		assertArrayEquals(realParsedOrdersC, parsedOrdersC);
		assertArrayEquals(realParsedOrdersD, parsedOrdersD);
	}
}
