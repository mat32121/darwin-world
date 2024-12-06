package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int numUpdates;

    public ConsoleMapDisplay() {
        this.numUpdates = 0;
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            System.out.println(message);
            System.out.println("WorldMap UUID: " + worldMap.getId());
            System.out.println(worldMap);
            ++this.numUpdates;
            System.out.println("Number of updates thus far: " + this.numUpdates);
        }
    }
}