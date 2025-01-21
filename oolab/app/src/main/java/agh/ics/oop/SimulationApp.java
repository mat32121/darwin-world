package agh.ics.oop;

import java.io.IOException;
import java.util.LinkedList;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    private SimulationEngine simulationEngine;
    private SimulationConfig conf;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.simulationEngine = new SimulationEngine(new LinkedList<>());
        this.conf = new SimulationConfig(primaryStage, this);
    }

    public void addSimulation(AbstractWorldMap map) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();
        presenter.setSimulationEngine(this.simulationEngine);
        presenter.setWorldMap(map);
        presenter.setStatisticsWriter(this.conf.getStatisticsWriter());
        map.addListener(presenter);

        Stage mapStage = presenter.createStage();

        this.configureStage(mapStage, viewRoot);
        mapStage.show();
    }

    private void configureStage(Stage mapStage, BorderPane viewRoot) {
        Scene scene = new Scene(viewRoot);
        mapStage.setScene(scene);
        mapStage.setTitle("Simulation app");
        mapStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        mapStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        viewRoot.setPadding(new Insets(5, 5, 5, 5));
    }
}