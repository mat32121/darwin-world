package agh.ics.oop;

import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.RectangularMap;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
	@Test
	public void testRun() {
		final String orderStringA[] = {"r", "l", "f", "bb", "l", "l", "b", "f"};
		final List<MoveDirection> directionsA = OptionsParser.parse(orderStringA);
		final List<Vector2d> positionsA = new ArrayList<Vector2d>();
		positionsA.add(new Vector2d(4, 4));
		positionsA.add(new Vector2d(0, 1));
		Simulation simulationA = new Simulation(positionsA, directionsA, new RectangularMap(5, 5));

		final String orderStringB[] = {"l", "l", "l", "l", "l", "f", "l", "f", "r"};
		final List<MoveDirection> directionsB = OptionsParser.parse(orderStringB);
		final List<Vector2d> positionsB = new ArrayList<Vector2d>();
		positionsB.add(new Vector2d(0, 0));
		Simulation simulationB = new Simulation(positionsB, directionsB, new RectangularMap(5, 5));

		simulationA.run();
		simulationB.run();

		assertTrue(simulationA.getAnimals().get(0).isAt(new Vector2d(4, 4)));
		assertTrue(simulationA.getAnimals().get(0).isHeaded(MapDirection.NORTH));
		assertTrue(simulationA.getAnimals().get(1).isAt(new Vector2d(0, 2)));
		assertTrue(simulationA.getAnimals().get(1).isHeaded(MapDirection.SOUTH));

		assertTrue(simulationB.getAnimals().get(0).isAt(new Vector2d(0, 0)));
		assertTrue(simulationB.getAnimals().get(0).isHeaded(MapDirection.WEST));
	}
}
