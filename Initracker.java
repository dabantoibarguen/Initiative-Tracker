import java.security.Guard;

import javax.swing.SortOrder;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.control.CheckBox;


public class Initracker extends Application {

    public static final int MIN_WIDTH = 450;
    public static final int MIN_HEIGHT = 250;

    // Table view to see the housemates and room numbers
    private final TableView<Creature> initList = new TableView<>();
    private final ObservableList<Creature> order = 
        FXCollections.observableArrayList();
    final HBox hb = new HBox();

    CheckBox randomizeInit = new CheckBox("Random Initiative");

    public void assignCreature(TextField n, TextField in, TextField h){
        String nm = "?";
        String it = "0";
        String hitP = "0";
        if(!n.getText().isEmpty()){
            nm = n.getText();
        }
        if(!in.getText().isEmpty()){
            it = in.getText();
        }
        if(!h.getText().isEmpty()){
            hitP = h.getText();
        }
        if(nm == "?" && it == "0" && hitP == "0"){
            return;
        }
        boolean added = false;
        for(int i = 0; i< order.size(); i++){   
            if(Integer.parseInt(it) < Integer.parseInt(order.get(i).getInitiative())){
                order.add(i, new Creature(it, nm, hitP));
                added = true;
                break;
            }
        }
        if(!added){
            order.add(new Creature(it, nm, hitP));
        }
        n.setText("");
        in.setText("");
        h.setText("");

    }

    public void start(Stage primaryStage) {
        GridPane aPane = new GridPane();

        Font plus = new Font("Komoda", 18);
        Font inputF = new Font("Modesto Bold Condensed", 14);
        Font titleF = new Font("Modesto Bold Condensed", 26);

        Label title = new Label("Initiative Tracker");
        title.setFont(titleF);

        aPane.add(title, 0, 0, 4, 1);


        // Link order array to initiative container
        initList.setItems(order);
        initList.setEditable(true);

        // Create the table with its columns
        TableColumn initCol = new TableColumn("Initiative");
        initCol.setCellValueFactory(new PropertyValueFactory("initiative"));
        initCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //initCol.setPrefWidth(50);
        initCol.setSortable(false);

        TableColumn nameCol = new TableColumn("Creature");
        nameCol.setCellValueFactory(new PropertyValueFactory("cName"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //nameCol.setPrefWidth(150);
        nameCol.setSortable(false);

        TableColumn hpCol = new TableColumn("HP");
        hpCol.setCellValueFactory(new PropertyValueFactory("hp"));
        hpCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //hpCol.setPrefWidth(50);
        hpCol.setSortable(false);

        initList.getColumns().addAll(initCol, nameCol, hpCol);
        initList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initList.setPrefHeight(Integer.MAX_VALUE);

        // Add list to the window
        aPane.add(initList, 0, 2, 3, 1);

        // Create and position the 3 TextFields with the appropriate label
        Label nameL = new Label("Name:");
        nameL.setFont(inputF);

        TextField nameField = new TextField();
        //nameField.setPrefSize(100, 25);

        VBox nameB = new VBox();
        nameB.getChildren().addAll(nameL, nameField);

        aPane.add(nameB, 0, 1);


        Label initL = new Label("Initiative:");
        initL.setFont(inputF);
        
        TextField initField = new TextField();

        VBox initB = new VBox();
        initB.getChildren().addAll(initL, initField);

        aPane.add(initB, 1, 1);


        Label hpL = new Label("Hit Points:");
        hpL.setFont(inputF);

        TextField hpField = new TextField();
        VBox hpB = new VBox();
        hpB.getChildren().addAll(hpL, hpField);

        aPane.add(hpB, 2, 1);



        // Create and position the "Add" Button
        Button addButton = new Button("+");
        addButton.setFont(plus);

        aPane.add(addButton, 3, 1);

        // Handler needed
        addButton.setOnAction(evt -> {
            assignCreature(nameField, initField, hpField);
            initList.getSortOrder().add(initCol);
        });

        addButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                assignCreature(nameField, initField, hpField);
            }
        });

        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                assignCreature(nameField, initField, hpField);
            }
        });

        initField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                assignCreature(nameField, initField, hpField);
            }
        });

        hpField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                assignCreature(nameField, initField, hpField);
            }
        });



        /*
        // Create and position the "Remove" Button
        Button removeButton = new Button("Remove");
        removeButton.relocate(175, 45);
        removeButton.setPrefSize(100, 25);

        removeButton.setOnAction(evt -> {
            if (newItemField.getText() != null && order.contains(newItemField.getText())) {
                order.remove(newItemField.getText());  
                newItemField.setText(null);
            }
        }); */

        // Setting options
        aPane.add(randomizeInit, 3, 2, 2, 1);


        //Set padding
        aPane.setPadding(new Insets(10));

        aPane.setHgap(5);
        aPane.setVgap(10);

        Scene scene = new Scene(aPane, MIN_WIDTH, MIN_HEIGHT); // Set size of window as a scene

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
              Platform.exit();
            }
        });


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        col1.setHgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(23);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(10);
        aPane.getColumnConstraints().addAll(col1,col2,col3, col4);
        aPane.getRowConstraints().addAll(row2);

        primaryStage.setTitle("D&D Initiative Tracker"); // Set title of window
        primaryStage.setResizable(true); // Make it non-resizable
        primaryStage.setScene(scene);   
        primaryStage.show();
    }

    public static void main(String[] args){
        launch();
    }

    public static class Creature {
        private String initiative;
        private String creatureName;
        private String hitpoints;
     
        private Creature(String init, String cName, String hp) {
            this.initiative = init;
            this.creatureName = cName;
            this.hitpoints = hp;
        }
     
        public String getCName() {
            return creatureName;
        }
            
        public String getInitiative() {
            return initiative;
        }

        public String getHp() {
            return hitpoints;
        }
    }
}