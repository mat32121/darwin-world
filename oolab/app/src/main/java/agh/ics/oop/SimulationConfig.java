package agh.ics.oop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.model.RectangularMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SimulationConfig {
    // Simulation parameters
    private int mapWidth = 50, mapHeight = 30;
    private int numInitialGrass = 5;
    private int energyPerGrass = 1;
    private int numGrassPerDay = 5;
    private int numInitialAnimals = 5;
    private int initialEnergy = 10;
    private int minCopulateEnergy = 6;
    private int copulateEnergyUsed = 5;
    private int minChildMutations = 1, maxChildMutations = 2;
    private int numGenes = 7;

    private boolean simulationStarts = true;

    private final AbstractWorldMap worldMap;

    @FXML
    private TextField mapWidthField, mapHeightField,
        numInitialGrassField,
        energyPerGrassField,
        numGrassPerDayField,
        numInitialAnimalsField,
        initialEnergyField,
        minCopulateEnergyField,
        copulateEnergyUsedField,
        minChildMutationsField, maxChildMutationsField,
        numGenesField;
    
    public SimulationConfig() throws IOException {
        this.setSimulationParams();
        if(this.simulationStarts)
            this.worldMap = new RectangularMap(this.mapWidth, this.mapHeight);
        else
            this.worldMap = null;
    }

    public AbstractWorldMap getWorldMap() {
        return this.worldMap;
    }
    
    private void setSimulationParams() throws IOException {
        Stage setSimulationParamsStage = new Stage();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simconfig.fxml"));
        VBox vbox = loader.load();

        this.mapWidthField = (TextField)vbox.lookup("#mapWidthField");
        this.mapHeightField = (TextField)vbox.lookup("#mapHeightField");
        this.numInitialGrassField = (TextField)vbox.lookup("#numInitialGrassField");
        this.energyPerGrassField = (TextField)vbox.lookup("#energyPerGrassField");
        this.numGrassPerDayField = (TextField)vbox.lookup("#numGrassPerDayField");
        this.numInitialAnimalsField = (TextField)vbox.lookup("#numInitialAnimalsField");
        this.initialEnergyField = (TextField)vbox.lookup("#initialEnergyField");
        this.minCopulateEnergyField = (TextField)vbox.lookup("#minCopulateEnergyField");
        this.copulateEnergyUsedField = (TextField)vbox.lookup("#copulateEnergyUsedField");
        this.minChildMutationsField = (TextField)vbox.lookup("#minChildMutationsField");
        this.maxChildMutationsField = (TextField)vbox.lookup("#maxChildMutationsField");
        this.numGenesField = (TextField)vbox.lookup("#numGenesField");
    
        this.updateFieldsText();

        ((Button)vbox.lookup("#readButton")).setOnAction((actionEvent) -> {this.readFromCSV();});
        ((Button)vbox.lookup("#writeButton")).setOnAction((actionEvent) -> {this.writeToCSV();});
        ((Button)vbox.lookup("#startButton")).setOnAction((actionEvent) -> {this.setParamsFromFields(); setSimulationParamsStage.close();});

        Scene scene = new Scene(vbox);

        setSimulationParamsStage.setScene(scene);
        setSimulationParamsStage.setOnCloseRequest((actionEvent) -> {this.simulationStarts = false; setSimulationParamsStage.close();});
        setSimulationParamsStage.showAndWait();
    }

    private void setParamsFromFields() {
        this.mapWidth = Integer.parseInt(this.mapWidthField.getText());
        this.mapHeight = Integer.parseInt(this.mapHeightField.getText());
        this.numInitialGrass = Integer.parseInt(this.numInitialGrassField.getText());
        this.energyPerGrass = Integer.parseInt(this.energyPerGrassField.getText());
        this.numGrassPerDay = Integer.parseInt(this.numGrassPerDayField.getText());
        this.numInitialAnimals = Integer.parseInt(this.numInitialAnimalsField.getText());
        this.initialEnergy = Integer.parseInt(this.initialEnergyField.getText());
        this.minCopulateEnergy = Integer.parseInt(this.minCopulateEnergyField.getText());
        this.copulateEnergyUsed = Integer.parseInt(this.copulateEnergyUsedField.getText());
        this.minChildMutations = Integer.parseInt(this.minChildMutationsField.getText());
        this.maxChildMutations = Integer.parseInt(this.maxChildMutationsField.getText());
        this.numGenes = Integer.parseInt(this.numGenesField.getText());
    }

    private void updateFieldsText() {
        this.mapWidthField.setText(Integer.toString(this.mapWidth));
        this.mapHeightField.setText(Integer.toString(this.mapHeight));
        this.numInitialGrassField.setText(Integer.toString(this.numInitialGrass));
        this.energyPerGrassField.setText(Integer.toString(this.energyPerGrass));
        this.numGrassPerDayField.setText(Integer.toString(this.numGrassPerDay));
        this.numInitialAnimalsField.setText(Integer.toString(this.numInitialAnimals));
        this.initialEnergyField.setText(Integer.toString(this.initialEnergy));
        this.minCopulateEnergyField.setText(Integer.toString(this.minCopulateEnergy));
        this.copulateEnergyUsedField.setText(Integer.toString(this.copulateEnergyUsed));
        this.minChildMutationsField.setText(Integer.toString(this.minChildMutations));
        this.maxChildMutationsField.setText(Integer.toString(this.maxChildMutations));
        this.numGenesField.setText(Integer.toString(this.numGenes));
    }

    private void readFromCSV() {
        Stage fileOpenStage = new Stage();
        fileOpenStage.setTitle("Choose a file to read parameters from");
        FileChooser fileChooser = new FileChooser();
        File readFile = fileChooser.showOpenDialog(fileOpenStage);
        if(readFile != null) {
            try (Scanner scanner = new Scanner(readFile)) {
                this.mapWidth = scanner.nextInt();
                this.mapHeight = scanner.nextInt();
                this.numInitialGrass = scanner.nextInt();
                this.energyPerGrass = scanner.nextInt();
                this.numGrassPerDay = scanner.nextInt();
                this.numInitialAnimals = scanner.nextInt();
                this.initialEnergy = scanner.nextInt();
                this.minCopulateEnergy = scanner.nextInt();
                this.copulateEnergyUsed = scanner.nextInt();
                this.minChildMutations = scanner.nextInt();
                this.maxChildMutations = scanner.nextInt();
                this.numGenes = scanner.nextInt();
            } catch (FileNotFoundException e) {
                System.err.println("File " + readFile.getName() + " not found!");
            } catch (RuntimeException e) {
                System.err.println("Error while reading file " + readFile.getName());
            }
            this.updateFieldsText();
        }
    }

    private void writeToCSV() {
        Stage fileOpenStage = new Stage();
        fileOpenStage.setTitle("Choose a file to write parameters to");
        FileChooser fileChooser = new FileChooser();
        File writeFile = fileChooser.showOpenDialog(fileOpenStage);
        if(writeFile != null) {
            try (PrintWriter writer = new PrintWriter(writeFile)) {
                writer.println(this.mapWidth);
                writer.println(this.mapHeight);
                writer.println(this.numInitialGrass);
                writer.println(this.energyPerGrass);
                writer.println(this.numGrassPerDay);
                writer.println(this.numInitialAnimals);
                writer.println(this.initialEnergy);
                writer.println(this.minCopulateEnergy);
                writer.println(this.copulateEnergyUsed);
                writer.println(this.minChildMutations);
                writer.println(this.maxChildMutations);
                writer.println(this.numGenes);
            } catch (FileNotFoundException e) {
                System.err.println("File " + writeFile.getName() + " not found!");
            }
        }
    }
}
