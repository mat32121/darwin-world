package agh.ics.oop.presenter;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SimulationPresenter implements MapChangeListener {
    private static final double ENERGY_COLOR_COEFF = 1.25;
    private WorldMap worldMap;
    private Stage simulationStage;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Label infoLabel;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button startButton;
    @FXML
    private VBox statisticsBox;
    @FXML
    private VBox animalTrackBox;
    
    private SimulationEngine simulationEngine;
    private Simulation simulation;
    private boolean simulationStarted = false;
    private boolean simulationPaused = false;

    private PrintWriter statisticsWriter;
    private int lastStatisticDayWritten = -2;
    private Animal animalTracked;

    public Stage createStage() {
        if(this.simulationStage == null) {
            this.simulationStage = new Stage();
            this.simulationStage.setOnCloseRequest(actionEvent -> {this.simulationStage.close();
                if(this.simulation instanceof Simulation)
                    try {
                        this.simulation.stop();
                        this.simulationEngine.joinAsync(this.worldMap.getId());
                    } catch (InterruptedException e) {
                        System.err.println("Could not join simulation thread!");
                        e.printStackTrace();
                    }});
        }
        return this.simulationStage;
    }

    public void setSimulationEngine(SimulationEngine simulationEngine) {
        this.simulationEngine = simulationEngine;
    }
    
    public void setWorldMap(WorldMap map) {
        this.worldMap = map;
    }

    public void setStatisticsWriter(PrintWriter statisticsWriter) {
        this.statisticsWriter = statisticsWriter;
    }

    private void clearGrid() {
        this.mapGrid.getChildren().retainAll(this.mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        this.mapGrid.getColumnConstraints().clear();
        this.mapGrid.getRowConstraints().clear();
    }

    // Energy could be of any range, so a sigmoid function is used
    // Higher energy -> darker animal
    private static double energyCurve(double energy) {
        // System.out.println("Energy: " + Integer.toString(energy));
        // System.out.println(1.0-0.8*(2.0/(1.0+Math.pow(SimulationPresenter.ENERGY_COLOR_COEFF, (double)(-energy)))-1));
        return 1.0-0.8*(2.0/(1.0+Math.pow(SimulationPresenter.ENERGY_COLOR_COEFF, -energy))-1);
    }

    private void printStatistics() {
        List<String> stats = this.getStatistics("\n");
        this.statisticsBox.getChildren().clear();
        this.statisticsBox.getChildren().add(new Label("Number of animals: "+stats.get(0)));
        this.statisticsBox.getChildren().add(new Label("Number of grass pieces: "+stats.get(1)));
        this.statisticsBox.getChildren().add(new Label("Number of free squares: "+stats.get(2)));
        this.statisticsBox.getChildren().add(new Label("Most popular genotypes:\n"+stats.get(3)));
        this.statisticsBox.getChildren().add(new Label("Average animal energy: "+stats.get(4)));
        this.statisticsBox.getChildren().add(new Label("Average lifespan of currently dead animals: "+stats.get(5)));
        this.statisticsBox.getChildren().add(new Label("Average number of children of living animals: "+stats.get(6)));
    }

    private void printAnimalTrack() {
        this.animalTrackBox.getChildren().clear();
        if(this.animalTracked == null)
            this.animalTrackBox.getChildren().add(new Label("No animal is currently being tracked"));
        else {
            this.animalTrackBox.getChildren().add(new Label("Genome: " + Arrays.stream(this.animalTracked.getGenotype()).boxed().map(a -> Integer.toString(a)).reduce((a, b) -> a + ", " + b).get()));
            this.animalTrackBox.getChildren().add(new Label("Active gene index: " + Integer.toString(this.animalTracked.getGenIndex())));
            this.animalTrackBox.getChildren().add(new Label("Energy: " + Integer.toString(this.animalTracked.getEnergy())));
            this.animalTrackBox.getChildren().add(new Label("Grass eaten: " + Integer.toString(this.animalTracked.getNumGrassEaten())));
            this.animalTrackBox.getChildren().add(new Label("Number of descendants: " + this.animalTracked.getAllDescendants(new HashSet<>()).size()));
            if(this.animalTracked.getLiveStatus())
                this.animalTrackBox.getChildren().add(new Label("Days alive: " + Integer.toString(this.animalTracked.getAge())));
            else
                this.animalTrackBox.getChildren().add(new Label("Day of death: " + Integer.toString(this.animalTracked.getDayOfDeath())));
        }
    }

    private List<String> getStatistics(String delim) {
        return List.of(
            Integer.toString(this.simulation.getNumAnimals()),
            Integer.toString(this.simulation.getNumGrass()),
            Integer.toString(this.simulation.getNumFreeSquares()),
            String.join(delim, this.simulation.getGenotypeList()),
            Double.toString(this.simulation.getAverageEnergy()),
            Double.toString(this.simulation.getAverageLifespan()),
            Double.toString(this.simulation.getAverageNumChildren()));
    }

    private void drawMap() {
        this.clearGrid();

        Boundary mapBoundary = worldMap.getCurrentBounds();
        Bounds gridBounds = this.mainBorderPane.getCenter().getLayoutBounds();
        int cell_width = (int)gridBounds.getWidth()/(mapBoundary.upperRight().getX()-mapBoundary.lowerLeft().getX()+2), cell_height = (int)gridBounds.getHeight()/(mapBoundary.upperRight().getY()-mapBoundary.lowerLeft().getY()+2);
        cell_width = cell_height = Math.min(cell_width, cell_height);

        this.mapGrid.getColumnConstraints().add(new ColumnConstraints(cell_width));
        this.mapGrid.getRowConstraints().add(new RowConstraints(cell_height));
        Label yx = new Label("y\\x");
        GridPane.setHalignment(yx, HPos.CENTER);
        this.mapGrid.add(yx, 0, 0, 1, 1);

        for(int i = mapBoundary.lowerLeft().getX(); i <= mapBoundary.upperRight().getX(); ++i) {
            this.mapGrid.getColumnConstraints().add(new ColumnConstraints(cell_width));
            Label newLabel = new Label(Integer.toString(i));
            GridPane.setHalignment(newLabel, HPos.CENTER);
            this.mapGrid.add(newLabel, i-mapBoundary.lowerLeft().getX()+1, 0, 1, 1);
        }

        for(int i = 0; i <= mapBoundary.upperRight().getY()-mapBoundary.lowerLeft().getY(); ++i) {
            this.mapGrid.getRowConstraints().add(new RowConstraints(cell_height));
            Label newLabel = new Label(Integer.toString(mapBoundary.upperRight().getY()-i));
            GridPane.setHalignment(newLabel, HPos.CENTER);
            this.mapGrid.add(newLabel, 0, i+1, 1, 1);
        }

        for(WorldElement elem : this.worldMap.getElements()) {
            if(elem instanceof Grass) {
                Color grassColor = (this.simulationPaused ? (this.worldMap.isJungle(elem.getPosition()) ? Color.DARKGREEN : Color.LIGHTGREEN) : Color.GREEN);
                Rectangle newRectangle = new Rectangle(cell_width, cell_height, grassColor);
                GridPane.setHalignment(newRectangle, HPos.CENTER);
                this.mapGrid.add(newRectangle,
                    elem.getPosition().getX()-mapBoundary.lowerLeft().getX()+1,
                    mapBoundary.upperRight().getY()-elem.getPosition().getY()+1);
            }
            else if(elem instanceof Object && !(elem instanceof Animal)) {
                Label newLabel = new Label(elem.toString());
                GridPane.setHalignment(newLabel, HPos.CENTER);
                this.mapGrid.add(newLabel,
                    elem.getPosition().getX()-mapBoundary.lowerLeft().getX()+1,
                    mapBoundary.upperRight().getY()-elem.getPosition().getY()+1);
            }
        }
        int[] maxGenome = this.simulation.getMostPopularGenome();
        Map<Vector2d, Animal> animalDrawMap = new HashMap<>();
        for(Vector2d animalPos : this.worldMap.getAnimalPositions())
            animalDrawMap.put(animalPos, this.worldMap.getFittestAnimalOnPosition(animalPos));
        for(Animal animal : animalDrawMap.values())
            if(animal != null)
                if(animal.getLiveStatus()) {
                    boolean hasMaxGenome = false;
                    if(maxGenome != null)
                        if(animal.getGenotype().length == maxGenome.length) {
                            hasMaxGenome = true;
                            for(int i = 0; i < maxGenome.length; ++i)
                                if(animal.getGenotype()[i] != maxGenome[i])
                                    hasMaxGenome = false;
                        }
                    Circle newCircle = new Circle();
                    newCircle.setRadius(Math.min(cell_width, cell_height)/2);
                    Color fillColor = (Color.hsb(0.1, 0.5, SimulationPresenter.energyCurve(animal.getEnergy())));
                    newCircle.setFill(fillColor);
                    if(hasMaxGenome && this.simulationPaused) {
                        newCircle.setStroke(Color.RED);
                        newCircle.setStrokeWidth(cell_height/10);
                    }
                    GridPane.setHalignment(newCircle, HPos.CENTER);
                    this.mapGrid.add(newCircle,
                        animal.getPosition().getX()-mapBoundary.lowerLeft().getX()+1,
                        mapBoundary.upperRight().getY()-animal.getPosition().getY()+1);
                }
        this.printStatistics();
        this.printAnimalTrack();
        if(this.lastStatisticDayWritten < this.simulation.getNumDays()) {
            this.pushStatisticsToFile(this.simulation.getNumDays(), this.getStatistics(", "));
            this.lastStatisticDayWritten = this.simulation.getNumDays();
        }
    }

    private void pushStatisticsToFile(final int day, final List<String> statistics) {
        if(this.statisticsWriter != null) {
            this.statisticsWriter.print(Integer.toString(day) + ";");
            for(String stat : statistics)
                this.statisticsWriter.print(stat + ";");
            this.statisticsWriter.println();
            this.statisticsWriter.flush();
        }
    }

    @FXML
    private void onSimulationStartClicked() {
        if(!this.simulationStarted) {
            this.simulation = new Simulation(this.worldMap);
            this.simulationEngine.addAsync(this.simulation, this.worldMap.getId());
            this.simulationStarted = true;
            this.startButton.setText("Pauza");
        }
        else if(this.simulation.togglePause()) {
            this.startButton.setText("Start");
            this.simulationPaused = true;
            this.drawMap();
        }
        else {
            this.startButton.setText("Pauza");
            this.simulationPaused = false;
            this.drawMap();
        }
    }

    @FXML
    private void onGridClicked(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if(clickedNode != this.mainBorderPane) {
            Integer col = GridPane.getColumnIndex(clickedNode);
            Integer row = GridPane.getRowIndex(clickedNode);
            // System.out.println("Mouse clicked at: " + col + ", " + row);
            if(col != null && row != null) {
                Boundary mapBoundary = this.worldMap.getCurrentBounds();
                col += mapBoundary.lowerLeft().getX()-1;
                row = mapBoundary.upperRight().getY()-row+1;
                this.animalTracked = this.worldMap.getFittestAnimalOnPosition(new Vector2d(col, row));
                // System.out.println("Map coordinates: " + col + ", " + row + " -> " + this.animalTracked);
            }
        }
        this.drawMap();
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            this.infoLabel.setText(message);
            this.drawMap();
        });
    }
}
