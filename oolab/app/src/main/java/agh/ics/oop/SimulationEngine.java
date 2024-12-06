package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> threads;
    private final ExecutorService executorService;

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
        this.threads = new ArrayList<>(this.simulations.size());
        this.executorService = Executors.newFixedThreadPool(4);
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

    public void awaitSimulationsEnd() throws InterruptedException {
        for(Thread thread : this.threads)
            thread.join();
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    public void runAsyncInThreadPool() {
        for(Simulation simulation : this.simulations)
            this.executorService.submit(simulation);
    }
}
