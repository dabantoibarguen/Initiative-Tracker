import javax.swing.event.ChangeListener;
import javax.swing.text.TabableView;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


public class Initracker extends Application {

    public static final int MIN_WIDTH = 285;
    public static final int MIN_HEIGHT = 200;

    public void start(Stage primaryStage) {
        Pane  aPane = new Pane();


        // Create and position the "new item" TextField
        TextField newItemField = new TextField();
        newItemField.relocate(10, 10);
        newItemField.setPrefSize(150, 25);

        // Create and position the "fruit" ListView with some fruits in it
        ListView<String> fruitList = new ListView<String>();

        ObservableList<String> fruits = 
              FXCollections.observableArrayList("Apples", "Oranges", "Bananas", "Cherries");
        fruitList.setItems(fruits);

        fruitList.relocate(10, 45);
        fruitList.setPrefSize(150, 150);

        // Create and position the "Add" Button
        Button addButton = new Button("Add");
        addButton.relocate(175, 10);
        addButton.setPrefSize(100, 25);

        // Handler needed
        addButton.setOnAction(evt -> {
            if (!newItemField.getText().isEmpty() && !fruits.contains(newItemField.getText())) {
                fruits.add(newItemField.getText());  
                newItemField.setText(null);
            }
        });

        newItemField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
            if(newItemField.getText() != null && !fruits.contains(newItemField.getText())) {
                fruits.add(newItemField.getText());  
                newItemField.setText(null);
            }}
        });

        // Create and position the "Remove" Button
        Button removeButton = new Button("Remove");
        removeButton.relocate(175, 45);
        removeButton.setPrefSize(100, 25);

        removeButton.setOnAction(evt -> {
            if (newItemField.getText() != null && fruits.contains(newItemField.getText())) {
                fruits.remove(newItemField.getText());  
                newItemField.setText(null);
            }
        });

        // TO COMPLETE for Monday
        // Handler needed

        // Add all the components to the window
        aPane.getChildren().addAll(newItemField, addButton, removeButton, fruitList);


        Scene scene = new Scene(aPane, MIN_WIDTH, MIN_HEIGHT); // Set size of window as a scene

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
              Platform.exit();
            }
        });

        primaryStage.setTitle("D&D Initiative Tracker"); // Set title of window
        primaryStage.setResizable(false); // Make it non-resizable
        primaryStage.setScene(scene);   
        primaryStage.show();
    }

    public static void main(String[] args){
        launch();
    }
}