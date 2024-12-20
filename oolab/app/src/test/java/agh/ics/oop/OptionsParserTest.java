package agh.ics.oop;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import agh.ics.oop.model.MoveDirection;

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
		assertThrows(IllegalArgumentException.class, () -> OptionsParser.parse(List.of(orderStringB0)));
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
	}
}
