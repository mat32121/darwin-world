package agh.ics.oop.presenter;

import java.util.List;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SimulationPresenter implements MapChangeListener {
    // private static final int CELL_WIDTH = 25, CELL_HEIGHT = 25;
    private WorldMap worldMap;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField movesField;
    @FXML
    private GridPane mapGrid;
    
    private SimulationEngine simulationEngine;
    
    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }

    private void clearGrid() {
        this.mapGrid.getChildren().retainAll(this.mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        this.mapGrid.getColumnConstraints().clear();
        this.mapGrid.getRowConstraints().clear();
    }

    private void drawMap() {
        this.clearGrid();
        // this.mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        // this.mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        Label yx = new Label("y\\x");
        GridPane.setHalignment(yx, HPos.CENTER);
        this.mapGrid.add(yx, 0, 0, 1, 1);
        Boundary mapBoundary = worldMap.getCurrentBounds();

        for (int i = mapBoundary.lowerLeft().getX(); i <= mapBoundary.upperRight().getX(); ++i) {
            // this.mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
            Label newLabel = new Label(Integer.toString(i));
            GridPane.setHalignment(newLabel, HPos.CENTER);
            this.mapGrid.add(newLabel, i-mapBoundary.lowerLeft().getX()+1, 0, 1, 1);
        }

        for (int i = mapBoundary.lowerLeft().getY(); i <= mapBoundary.upperRight().getY(); ++i) {
            // this.mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
            Label newLabel = new Label(Integer.toString(i));
            GridPane.setHalignment(newLabel, HPos.CENTER);
            this.mapGrid.add(newLabel, 0, i-mapBoundary.lowerLeft().getY()+1, 1, 1);
        }

        for(WorldElement elem : this.worldMap.getElements()) {
            Label newLabel = new Label(elem.toString());
            GridPane.setHalignment(newLabel, HPos.CENTER);
            this.mapGrid.add(newLabel,
                elem.getPosition().getX()-mapBoundary.lowerLeft().getX()+1,
                elem.getPosition().getY()-mapBoundary.lowerLeft().getY()+1);
        }
    }

    @FXML
    private void onSimulationStartClicked() {
        List<Vector2d> positions = List.of(new Vector2d(3,4), new Vector2d(5,4));
        Simulation simulation = new Simulation(positions, this.worldMap);
        this.simulationEngine = new SimulationEngine(List.of(simulation));
        this.simulationEngine.runAsync();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            this.infoLabel.setText(message);
            this.drawMap();
        });
    }

    public void endSimulation() throws InterruptedException {
        if(this.simulationEngine instanceof SimulationEngine) {
            this.simulationEngine.stopSimulations();
            this.simulationEngine.awaitSimulationsEnd();
        }
    }
}
