package agh.ics.oop;

import java.io.IOException;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {
    private SimulationPresenter presenter = null;

    @Override
    public void start(Stage primaryStage) throws IOException {
        AbstractWorldMap map = (new SimulationConfig()).getWorldMap();
        if(map instanceof AbstractWorldMap) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane viewRoot = loader.load();
            this.presenter = loader.getController();
            //trzeba zrobic, by mapa dopasowywala sie do ekranu!!!
            presenter.setWorldMap(map);
            map.addListener(presenter);

            this.configureStage(primaryStage, viewRoot);
            primaryStage.show();
        }
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        Scene scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
        primaryStage.setOnCloseRequest(actionEvent -> {primaryStage.close();
            try {
                this.presenter.endSimulation();
            } catch (InterruptedException e) {
                // Simulation was forced to end. Leaving catch empty.
            }});
    }
}