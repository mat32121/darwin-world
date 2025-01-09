package agh.ics.oop.presenter;

import java.util.List;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Grass;
import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldElement;
import agh.ics.oop.model.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SimulationPresenter implements MapChangeListener {
    private static final int CELL_WIDTH = 20, CELL_HEIGHT = 20;
    private static final double ENERGY_COLOR_COEFF = 1.25;
    private WorldMap worldMap;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField movesField;
    @FXML
    private GridPane mapGrid;
    
    private SimulationEngine simulationEngine;
    private boolean simulationStarted = false;
    
    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }

    private void clearGrid() {
        this.mapGrid.getChildren().retainAll(this.mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        this.mapGrid.getColumnConstraints().clear();
        this.mapGrid.getRowConstraints().clear();
    }

    // Energy could be of any range, so a sigmoid function is used
    // Higher energy -> darker animal
    private static double energyCurve(int energy) {
        // System.out.println("Energy: " + Integer.toString(energy));
        // System.out.println(1.0-0.8*(2.0/(1.0+Math.pow(SimulationPresenter.ENERGY_COLOR_COEFF, (double)(-energy)))-1));
        return 1.0-0.8*(2.0/(1.0+Math.pow(SimulationPresenter.ENERGY_COLOR_COEFF, (double)(-energy)))-1);
    }

    private void drawMap() {
        this.clearGrid();
        this.mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        this.mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        Label yx = new Label("y\\x");
        GridPane.setHalignment(yx, HPos.CENTER);
        this.mapGrid.add(yx, 0, 0, 1, 1);
        Boundary mapBoundary = worldMap.getCurrentBounds();

        for(int i = mapBoundary.lowerLeft().getX(); i <= mapBoundary.upperRight().getX(); ++i) {
            this.mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
            Label newLabel = new Label(Integer.toString(i));
            GridPane.setHalignment(newLabel, HPos.CENTER);
            this.mapGrid.add(newLabel, i-mapBoundary.lowerLeft().getX()+1, 0, 1, 1);
        }

        for(int i = 0; i <= mapBoundary.upperRight().getY()-mapBoundary.lowerLeft().getY(); ++i) {
            this.mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
            Label newLabel = new Label(Integer.toString(mapBoundary.upperRight().getY()-i));
            GridPane.setHalignment(newLabel, HPos.CENTER);
            this.mapGrid.add(newLabel, 0, i+1, 1, 1);
        }

        for(WorldElement elem : this.worldMap.getElements()) {
            if(elem instanceof Animal animal) {
                Circle newCircle = new Circle();
                newCircle.setRadius(Math.min(SimulationPresenter.CELL_WIDTH*0.75, SimulationPresenter.CELL_HEIGHT*0.75)/2);
                Color fillColor = (Color.hsb(0.25, 1.0, SimulationPresenter.energyCurve(animal.getEnergy())));
                newCircle.setFill(fillColor);
                GridPane.setHalignment(newCircle, HPos.CENTER);
                this.mapGrid.add(newCircle,
                    elem.getPosition().getX()-mapBoundary.lowerLeft().getX()+1,
                    mapBoundary.upperRight().getY()-elem.getPosition().getY()+1);
            }
            else if(elem instanceof Grass) {
                Rectangle newRectangle = new Rectangle(CELL_WIDTH*0.75, CELL_HEIGHT*0.75, Color.GREEN);
                GridPane.setHalignment(newRectangle, HPos.CENTER);
                this.mapGrid.add(newRectangle,
                    elem.getPosition().getX()-mapBoundary.lowerLeft().getX()+1,
                    mapBoundary.upperRight().getY()-elem.getPosition().getY()+1);
            }
            else
            {
                Label newLabel = new Label(elem.toString());
                GridPane.setHalignment(newLabel, HPos.CENTER);
                this.mapGrid.add(newLabel,
                    elem.getPosition().getX()-mapBoundary.lowerLeft().getX()+1,
                    mapBoundary.upperRight().getY()-elem.getPosition().getY()+1);
            }
        }
    }

    @FXML
    private void onSimulationStartClicked() {
        if(!this.simulationStarted) {
            List<Vector2d> positions = List.of(new Vector2d(3,4), new Vector2d(5,4));
            Simulation simulation = new Simulation(positions, this.worldMap);
            this.simulationEngine = new SimulationEngine(List.of(simulation));
            this.simulationEngine.runAsync();
            this.simulationStarted = true;
        }
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
