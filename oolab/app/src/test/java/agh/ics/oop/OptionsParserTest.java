package agh.ics.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.MoveDirection;

import java.util.List;

public class OptionsParserTest {
	@Test
	public void testParse() {
		final String orderStringA[] = {"f","l","b","r"};
		final List<MoveDirection> realParsedOrdersA = List.of(
			MoveDirection.FORWARD,
			MoveDirection.LEFT,
			MoveDirection.BACKWARD,
			MoveDirection.RIGHT
		);

		final String orderStringB0[] = {"l","r","34","f","l","ff","f-b","Xb","l","b"};
		assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(orderStringB0));
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
		assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(orderStringD0));

		final List<MoveDirection> parsedOrdersA = OptionsParser.parse(orderStringA);
		final List<MoveDirection> parsedOrdersB = OptionsParser.parse(orderStringB);
		final List<MoveDirection> parsedOrdersC = OptionsParser.parse(orderStringC);

		assertEquals(realParsedOrdersA, parsedOrdersA);
		assertEquals(realParsedOrdersB, parsedOrdersB);
		assertEquals(realParsedOrdersC, parsedOrdersC);
		assertNotEquals(realParsedOrdersA, parsedOrdersB);
	}
}
