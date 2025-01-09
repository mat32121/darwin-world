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

    public void stopSimulations() {
        for(Simulation simulation : this.simulations)
            simulation.stop();
    }

    public void runAsyncInThreadPool() {
        for(Simulation simulation : this.simulations)
            this.executorService.submit(simulation);
    }

	public int getNumAnimals() {
		int result = 0;
		for(Simulation sim : this.simulations)
            result += sim.getNumAnimals();
		return result;
	}

	public int getNumGrass() {
		int result = 0;
		for(Simulation sim : this.simulations)
            result += sim.getNumGrass();
		return result;
	}

	public int getNumFreeSquares() {
		int result = 0;
		for(Simulation sim : this.simulations)
            result += sim.getNumFreeSquares();
		return result;
	}

	public double getAverageEnergy() {
		double result = 0.0;
        double weight_sum = 0.0;
		for(Simulation sim : this.simulations) {
            result += sim.getAverageEnergy()*sim.getNumAnimals();
            weight_sum += sim.getNumAnimals();
        }
        if(!this.simulations.isEmpty())
            result /= weight_sum;
		return result;
	}
}
