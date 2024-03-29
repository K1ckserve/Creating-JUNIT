package unittest.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TestGUI extends Application {

    @Override
    public void start(Stage applicationStage) {
        applicationStage.setTitle("Test Framework GUI");

        // GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10); // Horizontal gap between cells
        gridPane.setVgap(10); // Vertical gap between cells
        gridPane.setPadding(new Insets(25, 25, 25, 25)); // Padding around the grid (top, right, bottom, left)

        // Test selection label
        Label testSelectionLabel = new Label("Select Tests to Run:");
        gridPane.add(testSelectionLabel, 0, 0, 2, 1); // Add to column=0, row=0, colspan=2, rowspan=1

        // Run button
        Button runTestsButton = new Button("Run Selected Tests");
        runTestsButton.setOnAction(e -> {
            // Logic to run tests goes here
            System.out.println("Running tests...");
        });
        gridPane.add(runTestsButton, 0, 1, 2, 1); // Add to column=0, row=1, colspan=2, rowspan=1

        // Test results label - placeholder
        Label testResultsLabel = new Label("Test Results Will Appear Here");
        gridPane.add(testResultsLabel, 0, 2, 2, 1); // Add to column=0, row=2, colspan=2, rowspan=1

        // Setting the scene
        Scene scene = new Scene(gridPane, 500, 275); // Width: 500px, Height: 275px
        applicationStage.setScene(scene);

        // Show the stage
        applicationStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Launch application
    }
}
