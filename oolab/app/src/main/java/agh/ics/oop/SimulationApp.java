package agh.ics.oop;

import java.io.IOException;
import java.util.UUID;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    private SimulationEngine simulationEngine;

    @Override
    public void start(Stage primaryStage) throws IOException {
        SimulationConfig conf = new SimulationConfig(primaryStage, this);
    }

    public void addSimulation(AbstractWorldMap map) throws IOException {
        Stage mapStage = new Stage();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        //trzeba zrobic, by mapa dopasowywala sie do ekranu!!!
        presenter.setSimulationEngine(this.simulationEngine);
        presenter.setWorldMap(map);
        map.addListener(presenter);

        this.configureStage(mapStage, viewRoot, map.getId());
        mapStage.show();
    }

    private void configureStage(Stage mapStage, BorderPane viewRoot, UUID mapId) {
        Scene scene = new Scene(viewRoot);
        mapStage.setScene(scene);
        mapStage.setTitle("Simulation app");
        mapStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        mapStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        mapStage.setOnCloseRequest(actionEvent -> {mapStage.close(); try {
            this.simulationEngine.joinAsync(mapId);
        } catch (InterruptedException e) {
            System.err.println("Could not join simulation thread!");
            e.printStackTrace();
        }});
    }
}