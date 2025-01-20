package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> threads;
    private final Map<UUID, Thread> simulationIDs;
    private final ExecutorService executorService;

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
        this.threads = new ArrayList<>(this.simulations.size());
        this.executorService = Executors.newFixedThreadPool(4);
        this.simulationIDs = new HashMap<>();
    }

    public void runSync() {
        for(Simulation simulation : this.simulations) {
            simulation.run();
        }
    }

    public void runAsync() {
        for(Simulation simulation : this.simulations)
            this.threads.add(new Thread(simulation));
        for(Thread thread : this.threads)
            thread.start();
    }

    public void addAsync(Simulation simulation, UUID mapId) {
        this.threads.add(new Thread(simulation));
        this.threads.getLast().start();
        this.simulationIDs.put(mapId, this.threads.getLast());
    }

    public void joinAsync(UUID mapId) throws InterruptedException {
        this.simulationIDs.get(mapId).join();
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for(Thread thread : this.threads)
            thread.join();
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    public void stopSimulations() {
        for(Simulation simulation : this.simulations)
            simulation.stop();
    }

    public void runAsyncInThreadPool() {
        for(Simulation simulation : this.simulations)
            this.executorService.submit(simulation);
    }

	// public boolean togglePause(UUID mapId) {
    //     boolean result = false;
    //     for(Simulation simulation : this.simulations)
    //         result |= simulation.togglePause(); // All simulations have the same isPaused value
    //     return result;
	// }
}
