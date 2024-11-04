package agh.ics.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.MoveDirection;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class OptionsParserTest {
	@Test
	public void testParse() {
		final String orderStringA[] = {"f","l","b","r"};
		List<MoveDirection> realParsedOrdersA = new ArrayList<MoveDirection>();
		realParsedOrdersA.add(MoveDirection.FORWARD);
		realParsedOrdersA.add(MoveDirection.LEFT);
		realParsedOrdersA.add(MoveDirection.BACKWARD);
		realParsedOrdersA.add(MoveDirection.RIGHT);

		final String orderStringB[] = {"l","r","34","f","l","ff","f-b","Xb","l","b"};
		List<MoveDirection> realParsedOrdersB = new ArrayList<MoveDirection>();
		realParsedOrdersB.add(MoveDirection.LEFT);
		realParsedOrdersB.add(MoveDirection.RIGHT);
		realParsedOrdersB.add(MoveDirection.FORWARD);
		realParsedOrdersB.add(MoveDirection.LEFT);
		realParsedOrdersB.add(MoveDirection.LEFT);
		realParsedOrdersB.add(MoveDirection.BACKWARD);

		final String orderStringC[] = {};
		List<MoveDirection> realParsedOrdersC = new ArrayList<MoveDirection>();

		final String orderStringD[] = {"k", "o"};
		List<MoveDirection> realParsedOrdersD = new ArrayList<MoveDirection>();

		final List<MoveDirection> parsedOrdersA = OptionsParser.parse(orderStringA);
		final List<MoveDirection> parsedOrdersB = OptionsParser.parse(orderStringB);
		final List<MoveDirection> parsedOrdersC = OptionsParser.parse(orderStringC);
		final List<MoveDirection> parsedOrdersD = OptionsParser.parse(orderStringD);

		assertEquals(realParsedOrdersA, parsedOrdersA);
		assertEquals(realParsedOrdersB, parsedOrdersB);
		assertEquals(realParsedOrdersC, parsedOrdersC);
		assertEquals(realParsedOrdersD, parsedOrdersD);
		assertNotEquals(realParsedOrdersA, parsedOrdersB);
	}
}
