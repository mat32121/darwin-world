package agh.ics.oop;


public class World {
	/*
	private static void run(List<MoveDirection> steps) {
		for(MoveDirection step : steps)
			switch (step) {
				case MoveDirection.FORWARD  -> System.out.println("Zwierzak idzie do przodu");
				case MoveDirection.LEFT     -> System.out.println("Zwierzak skręca w lewo");
				case MoveDirection.BACKWARD -> System.out.println("Zwierzak idzie do tyłu");
				case MoveDirection.RIGHT    -> System.out.println("Zwierzak skręca w prawo");
			};
	}
	*/
	public static void main(String[] args) {


		System.out.println("Działam");


		/*
		System.out.println("Start");
		List<MoveDirection> steps = OptionsParser.parse(List.of(args));
		run(steps);
		System.out.println("Stop");

		Vector2d position1 = new Vector2d(1,2);
		System.out.println(position1);
		Vector2d position2 = new Vector2d(-2,1);
		System.out.println(position2);
		System.out.println(position1.add(position2));

		MapDirection direction1 = MapDirection.NORTH;
		for(int i = 0; i < 4; ++i) {
			System.out.println(direction1.toString());
			System.out.println(direction1.toUnitVector());
			direction1 = direction1.next();
		}

		Animal animal = new Animal();
		System.out.println(animal.toString());

		ConsoleMapDisplay mainConsoleMapDisplay = new ConsoleMapDisplay();

		List<MoveDirection> directions;
		try {
			directions = OptionsParser.parse(List.of(args));
		}
		catch (IllegalArgumentException e) {
			directions = List.of();
		}
	
		List<Simulation> simulations = new ArrayList<>();
		for(int i = 0; i < 1000; ++i) {
			List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
			RectangularMap map = new RectangularMap(5, 5);
			Simulation simulation = new Simulation(positions, directions, map);
			map.addListener(mainConsoleMapDisplay);

			GrassField grassField = new GrassField(10);
			Simulation grassSim = new Simulation(positions, directions, grassField);
			grassField.addListener(mainConsoleMapDisplay);

			simulations.add(simulation);
			simulations.add(grassSim);
		}

		SimulationEngine simEngine = new SimulationEngine(simulations);
		simEngine.runAsyncInThreadPool();
		try {
			simEngine.awaitSimulationsEnd();
		}
		catch (InterruptedException e) {
			// A thread got interrupted. Leaving catch empty.
		}

		// Application.launch(SimulationApp.class, args);	

		System.out.println("System zakończył działanie");

		 */
	}
}
