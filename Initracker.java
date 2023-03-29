import java.util.function.UnaryOperator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.*;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TabPane.TabClosingPolicy;
//import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;
//import javafx.collections.ListChangeListener;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
//import javafx.scene.control.ListView;
//import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Initracker extends Application {

    public static final int MIN_WIDTH = 610;
    public static final int MIN_HEIGHT = 325;


    // Tab Pane for application
    TabPane tabPane = new TabPane();

    // Tab positions in list
    public static final int HOME = 0; 
    public static final int TRACKER = 1; 
    public static final int SETTINGS = 2; 


    // Table view to see the housemates and room numbers
    private final TableView<Creature> initList = new TableView<>();
    private final ObservableList<Creature> order = 
        FXCollections.observableArrayList();

    CheckBox randomizeInit = new CheckBox("Random Initiative");
    CheckBox showExample = new CheckBox("Show example creature");
    CheckBox clearFields = new CheckBox("Empty fields");
    CheckBox displayDice = new CheckBox("Display dice rolled");
    CheckBox warnOnRemove = new CheckBox("Warning for removing");

    Font inputF = new Font("Modesto Bold Condensed", 14);
    Font titleF = new Font("Modesto Bold Condensed", 26);



// ----------------------------- START FUNCTION --------------------------

public void start(Stage primaryStage) {
    //Tab home = new Tab("Home", new Label("Applications and how to use them"));
    Tab tracker = new Tab("Tracker"  , new Label("Initiative tracker application"));
    Tab settings = new Tab("Settings"  , new Label("Customize the Initiative Tracker"));
    Tab statBlock = new Tab("Stat Block"  , new Label("Reference creature stat block"));
    
    ScrollPane banditBox = new ScrollPane();
    
    Image banditImg = new Image("/bandit.png");

    ImageView imageView = new ImageView(banditImg); 

    banditBox.setContent(imageView);

    imageView.fitWidthProperty().bind(banditBox.widthProperty());
    imageView.fitHeightProperty().bind(banditBox.heightProperty());

    tabPane.getTabs().addAll(tracker, settings, statBlock);

    tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
   
    Scene scene = new Scene(tabPane, MIN_WIDTH, MIN_HEIGHT); // Set size of window as a scene

    scene.setOnKeyPressed(e -> {
        if (e.getCode() == KeyCode.ESCAPE) {
          Platform.exit();
        }
    });

    tracker.setContent(createTracker());

    //home.setContent(createHome());
    settings.setContent(createSettings());

    statBlock.setContent(banditBox);

    scene.getStylesheets().add(getClass().getResource("initracker.css").toExternalForm());

    // Display and window configurations
    primaryStage.setMinHeight(MIN_HEIGHT);
    primaryStage.setMinWidth(MIN_WIDTH);
    primaryStage.setTitle("D&D Initiative Tracker"); // Set title of window
    primaryStage.setResizable(true); // Make it non-resizable
    primaryStage.setScene(scene);   
    primaryStage.show();
}



// ----------------------------- HOME --------------------------

    public ScrollPane createHome(){
        ScrollPane homeScroll = new ScrollPane();

        homeScroll.getStyleClass().add("home-pane");

        VBox homeBox = new VBox();

        homeScroll.setContent(homeBox);

        homeBox.setPadding(new Insets(10));
        homeBox.setSpacing(5);
        homeBox.getStyleClass().add("home-pane");


        Text title = new Text("Dungeons & Dragons tools");
        title.setId("home-title");
        title.setUnderline(true);

        Text desc = new Text("Tools to help D&D and other Tabletop RPG Game Masters and players\n");
        //desc.setFont(inputF);

        Label subTracker = new Label("Initiative Tracker (GO)");

        Tooltip goToTracker = new Tooltip("Click to go");

        subTracker.setTooltip(goToTracker);

        subTracker.setStyle("-fx-font-size: 20");

        subTracker.setOnMouseClicked(e -> {
            tabPane.getSelectionModel().select(TRACKER);
        });

        Text trackerInfo = new Text("""
        - Add/Remove creatures to list with an iniatiative value, name and total hitpoints.
            - Creatures are listed top to bottom from highest to lowest initiative value.
        - Default values for empty fields (? for name, 0 for initiative, 1 for hitpoints).
            - At least one of the fields must have a value
        - Modify hitpoints of chosen creature from the list.
            - Clicking a creature on the list selects it.
            - Hitpoints can be increased (heal) or decreased (damage) using the text field.
        - Hitpoint field accepts dice rolling format: XdY+Z (e.g. 3d6+6)
            - (X = number of dice to roll (3dY is three dice)) 
            - (Y = value of a singular die (1d6 is one six-sided die)
            - (Z = value to add to the total rolled (3d6 + 6 will add 6 to the total of 3d6)).
        - Randomize initiative field calculates a pseudorandom number from 1-20 (1d20).
            - Field changed to accept a modifier (+ or -) to add to the total
        """);
        //trackerInfo.setFont(inputF);
        

        homeBox.getChildren().addAll(desc, subTracker, trackerInfo);

        return homeScroll;
    }








// ----------------------------- TRACKER --------------------------

    public GridPane createTracker(){
        GridPane aPane = new GridPane();

        //Label title = new Label("Initiative Tracker");
        //title.setFont(titleF);

        //aPane.add(title, 0, 0, 5, 1);


        // Link order array to initiative container
        initList.setItems(order);
        initList.setEditable(false);

        // Create the table with its columns
        TableColumn<Creature, String> initCol = new TableColumn<Creature, String>("Initiative");
        initCol.setCellValueFactory(new PropertyValueFactory<Creature, String>("initiative"));
        initCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //initCol.setPrefWidth(50);
        initCol.setSortable(false);

        TableColumn<Creature, String> nameCol = new TableColumn<Creature, String>("Creature");
        nameCol.setCellValueFactory(new PropertyValueFactory<Creature, String>("cName"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        //nameCol.setPrefWidth(150);
        nameCol.setSortable(false);

        TableColumn<Creature, String> hpCol = new TableColumn<Creature, String>("HP");
        hpCol.setCellValueFactory(new PropertyValueFactory<Creature, String>("hp"));
        hpCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //hpCol.setPrefWidth(50);
        hpCol.setSortable(false);

        initList.getColumns().addAll(initCol, nameCol, hpCol);
        initList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initList.setPrefHeight(Integer.MAX_VALUE);

        // Add list to the window
        aPane.add(initList, 0, 2, 4, 3);


        // Create the panel for the dice rolled
        VBox dicePanel = new VBox();

        dicePanel.setVisible(false);
        aPane.add(dicePanel, 4, 0, 1, 3);

        // Create and position the 3 TextFields with the appropriate label
        Label nameL = new Label("Creature Name:");
        //nameL.setFont(inputF);
        nameL.getStyleClass().add("red-labels");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter a name");
        //nameField.setPrefSize(100, 25);

        VBox nameB = new VBox();
        nameB.getChildren().addAll(nameL, nameField);

        aPane.add(nameB, 0, 0, 1, 2);


        Label initL = new Label("Initiative:");
        //initL.setFont(inputF);
        initL.getStyleClass().add("red-labels");

        Tooltip initTooltip = new Tooltip("Defines position in list (descending order)");
        
        TextField initField = new TextField();
        initField.setPromptText("Total");
        initField.setTooltip(initTooltip);

        VBox initB = new VBox();
        initB.getChildren().addAll(initL, initField);

        aPane.add(initB, 1, 0, 1, 2);


        Label hpL = new Label("Hit Points:");
        hpL.getStyleClass().add("red-labels");
        //hpL.setFont(inputF);

        TextField hpField = new TextField();
        hpField.setPromptText("Total / Dice");

        Tooltip rollDice = new Tooltip("Example with dice: 1d8+2 = (random value from 1-8) + 2");

        hpField.setTooltip(rollDice);

        VBox hpB = new VBox();
        hpB.getChildren().addAll(hpL, hpField);

        aPane.add(hpB, 2, 0, 1, 2);

        VBox addBox = new VBox();
        // Create and position the "Add" Button
        Button addButton = new Button("+");

        Tooltip addToList = new Tooltip("Adds the creature to the list");

        addButton.setTooltip(addToList);

        //addButton.setMinSize(30, 4);

        addButton.setAlignment(Pos.BOTTOM_LEFT);

        addBox.getChildren().addAll(addButton);
        addBox.setAlignment(Pos.BOTTOM_LEFT);

        aPane.add(addBox, 3, 0, 1, 2);

        // Handler needed
        addButton.setOnAction(evt -> {
            assignCreature(nameField, initField, hpField, dicePanel);
            initList.getSortOrder().add(initCol);
        });

        addButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                assignCreature(nameField, initField, hpField, dicePanel);
            }
        });

        nameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                assignCreature(nameField, initField, hpField, dicePanel);
            }
        });

        initField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                assignCreature(nameField, initField, hpField, dicePanel);
            }
        });

        hpField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                assignCreature(nameField, initField, hpField, dicePanel);
            }
        });


        // Create the panel to affect a creature's hit points

        VBox hpPanel = new VBox();

        hpPanel.setId("influence");

        hpPanel.setVisible(false);

        hpPanel.setSpacing(5);
        
        hpPanel.setPadding(new Insets(5));

        hpPanel.setMaxWidth(Integer.MAX_VALUE);

        HBox closePanel = new HBox();

        Tooltip closePTt = new Tooltip("Close the creature info panel");

        Button closeP = new Button("x");

        closeP.setTooltip(closePTt);

        closeP.setVisible(false);

        closeP.setOnAction(e -> {
            hpPanel.setVisible(false);
            closeP.setVisible(false);
            initList.getSelectionModel().setSelectionMode(null);
        });

        closePanel.setAlignment(Pos.TOP_RIGHT);

        closePanel.getChildren().addAll(closeP);


        Tooltip closeTt = new Tooltip("Close the creature interaction area"); 

        closeP.setTooltip(closeTt);


        Label cb = new Label("No Chosen Creature");
        cb.getStyleClass().add("red-labels");
        cb.setId("selected");

        HBox hpChange = new HBox();


        VBox curHp = new VBox();

        Label currentHp = new Label("Current HP: ");

        Tooltip modHp = new Tooltip("Enter a number to increase/decrease the creature's hit points"); 

        TextField modify = new TextField();
        modify.setPromptText("Enter a value");

        modify.setTooltip(modHp);

        curHp.getChildren().addAll(currentHp, modify);

        curHp.setSpacing(5);

        HBox damageHeal = new HBox();

        Button dmg = new Button("-");

        Button heal = new Button("+");

        damageHeal.setPadding(new Insets(0, 0, 0, 10));

        damageHeal.setSpacing(3);

        damageHeal.setAlignment(Pos.BOTTOM_RIGHT);


        damageHeal.getChildren().addAll(dmg, heal);

        hpChange.getChildren().addAll(curHp, damageHeal);

        Button remove = new Button("Remove from combat");

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remove creature");
        alert.setHeaderText("This creature has hit points remaining");
        alert.setContentText("Would you like to remove it?");

        // Removing, damaging and healing

        remove.setOnAction(e -> {removeCreature(hpPanel, closeP, hpField,alert);});

        // Checkbox actions

        randomizeInit.setOnAction(e -> {
            if(randomizeInit.isSelected()){
                initL.setText("Modifier:");
                if(!showExample.isSelected()){
                    initField.setPromptText("+ or -");
                }
                else{
                    initField.setPromptText("+1");
                }
            }
            else if(!randomizeInit.isSelected()){
                //if(!initField.getText().isEmpty() && initField.getText().charAt(0) == '+' || initField.getText().charAt(0) == '-'){
                    //  initField.setText(initField.getText().substring(1));
                //}
                initL.setText("Initiative:");
                initField.setPromptText("Total");
            }
        });
        
        showExample.setOnAction(e -> {
            if(showExample.isSelected()){
                nameField.setPromptText("Bandit");
                initField.setPromptText("11");
                hpField.setPromptText("2d8+2");
                if(randomizeInit.isSelected()){
                    initField.setPromptText("+1");
                }
            }
            else if(!showExample.isSelected()){
                nameField.setPromptText("Enter a name");
                if(randomizeInit.isSelected()){
                    initField.setPromptText("+ or -");
                }
                else{
                    initField.setPromptText("Total");
                }
                hpField.setPromptText("Total / Dice");

            }
        });

        displayDice.setOnAction(e -> {
            if(!displayDice.isSelected()){
                dicePanel.setVisible(false);
            }
        });

        warnOnRemove.setOnAction(e -> {
            if(!warnOnRemove.isSelected()){
                remove.setStyle("-fx-opacity: 1.0");
            }
        });


        // Display the panel

        initList.setOnMouseClicked(e -> {
            if(initList.getSelectionModel().getSelectedItem() != null){
                cb.setText(initList.getSelectionModel().getSelectedItem().getCName());
                currentHp.setText("Current Hit points: " + initList.getSelectionModel().getSelectedItem().getHp());
                if(!initList.getSelectionModel().getSelectedItem().getHp().equals("0") && warnOnRemove.isSelected()){
                    remove.setStyle("-fx-opacity: 0.3");
                }
                else{
                    remove.setStyle("-fx-opacity: 1.0");
                }
                modify.setText("");
                hpPanel.setVisible(true);  
                closeP.setVisible(true);              
            }
        });

        // Button functionality for health panel

        dmg.setOnAction(e -> { // Fix on empty input field
            if(initList.getSelectionModel().getSelectedItem() != null && !modify.getText().isEmpty()){

                int idx = initList.getSelectionModel().getSelectedIndex();
                Creature c = order.get(idx);
                c.modifyHP(-Integer.parseInt(modify.getText()));
                currentHp.setText("Current Hit points: " + initList.getSelectionModel().getSelectedItem().getHp());
                order.set(idx, c);
                if(!initList.getSelectionModel().getSelectedItem().getHp().equals("0") && warnOnRemove.isSelected()){
                    System.out.println(initList.getSelectionModel().getSelectedItem().getHp());
                    remove.setStyle("-fx-opacity: 0.3");
                }
                else{
                    remove.setStyle("-fx-opacity: 1.0");
                }
            }
                
        });
        
        heal.setOnAction(e -> {
            if(initList.getSelectionModel().getSelectedItem() != null && !modify.getText().isEmpty()){    
                int idx = initList.getSelectionModel().getSelectedIndex();
                Creature c = order.get(idx);
                c.modifyHP(Integer.parseInt(modify.getText()));
                currentHp.setText("Current Hit points: " + initList.getSelectionModel().getSelectedItem().getHp());
                order.set(idx, c);
                if(!initList.getSelectionModel().getSelectedItem().getHp().equals("0") && warnOnRemove.isSelected()){
                    remove.setStyle("-fx-opacity: 0.3");
                }
                else{
                    remove.setStyle("-fx-opacity: 1.0");
                }
            }
        });

        hpPanel.getChildren().addAll(cb, hpChange, remove);

        aPane.add(closePanel, 4, 3);

        aPane.add(hpPanel, 4, 4, 2, 1);

        aPane.setPadding(new Insets(10));

        aPane.setHgap(5);
        aPane.setVgap(10);

        // Set numeric text fields

        UnaryOperator<Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[-+]?([0-9]*)?")) { 
                return change;
            }
            return null;
        };
        


        modify.setTextFormatter(
            new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));

        initField.setTextFormatter(
            new TextFormatter<Integer>(new IntegerStringConverter(), null, integerFilter));


        // Grid pane constraints

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(11);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(15);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(6);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPercentWidth(40);
        
        aPane.getColumnConstraints().addAll(col1,col2,col3, col4, col5);


        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        aPane.getRowConstraints().addAll(row2);

        return aPane;
    }




// ----------------------------- SETTINGS --------------------------

    public GridPane createSettings(){
        GridPane settingPane = new GridPane();

        settingPane.setPadding(new Insets(10));



        // Randomize Initiative

        HBox firstCheck = new HBox();
        firstCheck.getChildren().addAll(randomizeInit);
        firstCheck.setAlignment(Pos.CENTER_LEFT);

        firstCheck.getStyleClass().add("lined");

        settingPane.add(firstCheck, 0, 0);

        TextArea randInit = new TextArea("The initiative field will now take a modifier instead of a total value. The final iniative will be a random value from 1-20 with the modifier added (or substracted if negative) to the total.");
        randInit.setEditable(false);
        randInit.setWrapText(true);

        randInit.getStyleClass().add("lined");

        randInit.setMaxHeight(65);
        
        settingPane.add(randInit, 1, 0);




        // Show example creature

        TextArea example = new TextArea("The text fields will now display the input for an example creature (a bandit). See Statblock tab.");
        example.setEditable(false);
        example.setWrapText(true);
        
        example.setMaxHeight(45);

        example.getStyleClass().add("lined");


        settingPane.add(example, 1, 1);

        HBox secondCheck = new HBox();
        secondCheck.getChildren().addAll(showExample);
        secondCheck.setAlignment(Pos.CENTER_LEFT);

        secondCheck.getStyleClass().add("lined");

        settingPane.add(secondCheck, 0, 1);




        // Clear fields after input
        
        TextArea clearF = new TextArea("Empties every input field after adding a new creature.");
        clearF.setEditable(false);
        clearF.setWrapText(true);

        clearF.setMaxHeight(20);

        clearF.getStyleClass().add("lined");

        settingPane.add(clearF, 1, 2);

        clearFields.setSelected(true);
        
        HBox thirdCheck = new HBox();
        thirdCheck.getChildren().addAll(clearFields);
        thirdCheck.setAlignment(Pos.CENTER_LEFT);

        thirdCheck.getStyleClass().add("lined");

        settingPane.add(thirdCheck, 0, 2);
        

        HBox fourthCheck = new HBox();
        fourthCheck.getChildren().addAll(displayDice);
        fourthCheck.setAlignment(Pos.CENTER_LEFT);
        
        fourthCheck.getStyleClass().add("lined");

        settingPane.add(fourthCheck, 0, 3);

        displayDice.setSelected(true);

        TextArea dispD = new TextArea("Displays each die rolled and the total when using the dice format for hitpoints.");
        dispD.setEditable(false);
        dispD.setWrapText(true);

        dispD.getStyleClass().add("lined");

        dispD.setMaxHeight(45);

        settingPane.add(dispD, 1, 3);




        // Warning before removing a creature with hit points remaining

        HBox fifthCheck = new HBox();
        fifthCheck.getChildren().addAll(warnOnRemove);
        fifthCheck.setAlignment(Pos.CENTER_LEFT);

        settingPane.add(fifthCheck, 0, 4);

        warnOnRemove.setSelected(true);
        
        TextArea warnOnR = new TextArea("Display a warning window when trying to remove a creature with hitpoints remaining.");
        warnOnR.setEditable(false);
        warnOnR.setWrapText(true);

        warnOnR.setMaxHeight(45);

        settingPane.add(warnOnR, 1, 4);


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(75);
        col2.setHgrow(Priority.ALWAYS);
        
        settingPane.getColumnConstraints().addAll(col1,col2);

        return settingPane;
    }


// ----------------------- DICE ROLLER ---------------------------

    public String diceRoller(String diceStr, VBox diceRolled){
        Pattern dice = Pattern.compile("^([0-9]*)([dD])?([0-9]*)([+-])?([0-9]*$)");
        Matcher match = dice.matcher(diceStr);

        diceRolled.getChildren().clear();

        HBox closeD = new HBox();

        Tooltip closeDTt = new Tooltip("Close the dice panel");

        Button closeDice = new Button("x");

        closeDice.setTooltip(closeDTt);

        closeDice.setOnAction(e -> {
            diceRolled.setVisible(false);
            closeDice.setVisible(false);
            initList.getSelectionModel().setSelectionMode(null);
        });

        closeD.setAlignment(Pos.BOTTOM_RIGHT);

        closeD.setPadding(new Insets(0, 0, 5, 0));

        closeD.getChildren().addAll(closeDice);



        diceRolled.getChildren().add(closeD);

        TextArea hpDice = new TextArea(); 

        hpDice.setStyle("-fx-background-color: #E69B27");
        hpDice.setWrapText(true);

        if(!match.find()){
            return "";
        }
        if(match.group(2) != null){
            hpDice.appendText("Hit points (" + diceStr + "): ");
            int result = 0;
            int gOne = 1;
            int gThree = 1;
            if(match.group(1) != null){
                gOne = Integer.parseInt(match.group(1));
            }
            if(match.group(3) != ""){
                gThree = Integer.parseInt(match.group(3));
            }
            int rollGThree = 0; // Total amount rolled in dice
            int curRoll;
            for(int r = 0; r<gOne; r++){
                curRoll = 0;
                if(gThree != 1){
                    curRoll = ThreadLocalRandom.current().nextInt(1, gThree+1);
                    hpDice.appendText(curRoll + "");
                    if(r != gOne-1){
                        hpDice.appendText(", ");
                    }
                    rollGThree += curRoll;
                }
                else{
                    rollGThree += 1;
                }
            }
            result = rollGThree;
            // Add hit points dice to panel
            diceRolled.getChildren().addAll(hpDice);
            diceRolled.setVisible(true);
            if(match.group(4) != null && match.group(5) != ""){
                hpDice.appendText(" " + match.group(4) + " " + match.group(5));
                if(match.group(4).equals("+")){
                    result += Integer.parseInt(match.group(5));
                }
                else if(match.group(4).equals("-")){
                    result -= Integer.parseInt(match.group(5));
                }
                if(result < 1){
                    result = 1;
                }
            }
            hpDice.appendText("\nTotal = " + result);

            if(!displayDice.isSelected()){
                diceRolled.getChildren().clear();
            }

            return Integer.toString(result); 
        }
        if(!displayDice.isSelected()){
            diceRolled.getChildren().clear();
        }
        return diceStr;
    }


// ----------------------------- ASSIGN CREATURE (TRACKER) --------------------------

    public void assignCreature(TextField n, TextField in, TextField h, VBox diceRolled){
        String nm = "?";
        String it = "0";
        String hitP = "1";
        if(!n.getText().isEmpty()){
            nm = n.getText();
        }
        if(!in.getText().isEmpty()){
            it = in.getText();
        }
        if(randomizeInit.isSelected()){
            int inN = ThreadLocalRandom.current().nextInt(1, 20);
            String initVal = in.getText();
            if(!initVal.isEmpty()){
                it = Integer.toString(inN);
                inN += Integer.parseInt(initVal.substring(0, initVal.length()));
                it = Integer.toString(inN);
                //it += sign + " (" + it + ")";
            }
        }
        if(!h.getText().isEmpty()){
            hitP = h.getText();
            hitP = diceRoller(hitP, diceRolled);
            if(hitP == ""){
                return;
            }
            Pattern p = Pattern.compile("(^[0-9]*$)");
            Matcher m = p.matcher(hitP);
            if(!m.find()){
                return;
            }
            
        }
        if(nm == "?" && it == "0" && hitP == "1"){
            return;
        }
        boolean added = false;
        String compareInit1 = "";
        for(int c = 0; c < it.length(); c++){
            if(Character.isDigit(it.charAt(c))){
                compareInit1 += it.charAt(c);
            }
            else if(c == 0 && (it.charAt(c) == '+' || it.charAt(c) == '-')){
                compareInit1 += it.charAt(c);
            }
            else{
                return;
            }
            /*else if(it.charAt(c) != '+' || it.charAt(c) == '-' || it.charAt(c) == '(' || it.charAt(c) == ')' || it.charAt(c) == ' '){
                return;
            }*/
        }
        int toCompare = Integer.parseInt(compareInit1);
        int i;
        for(i = 0; i< order.size(); i++){   
            if(toCompare > Integer.parseInt(order.get(i).getInitiative())){
                order.add(i, new Creature(it, nm, hitP));
                added = true;
                break;
            }
        }
        if(!added){
            order.add(new Creature(it, nm, hitP));
            
        }
        if(clearFields.isSelected()){
            n.setText("");
            in.setText("");
            h.setText("");
        }

    }

// ------------------------------REMOVE CREATURE -----------------------
    public void removeCreature(VBox hpPanel, Button closeP, TextField modify, Alert alert){
        if(initList.getSelectionModel().getSelectedItem() != null){
            if(!initList.getSelectionModel().getSelectedItem().getHp().equals("0") && warnOnRemove.isSelected()){
                alert.setHeaderText(initList.getSelectionModel().getSelectedItem().getCName() + " has hit points remaining");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.CANCEL){        
                    return;
                }
            }
        }
        int idx = initList.getSelectionModel().getSelectedIndex();
                    order.remove(idx);
                    hpPanel.setVisible(false);
                    closeP.setVisible(false);
                    modify.setText("");
    
    }

// ----------------------------- MAIN --------------------------

    public static void main(String[] args){
        launch();
    }

// ----------------------------- CREATURE CLASS --------------------------

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
        
        public void modifyHP(int n){
            int tot = (Integer.parseInt(this.hitpoints) + n);
            this.hitpoints = Integer.toString(Math.max(tot, 0));
        }
    }
}

/* TO - DO
 * 
 * Make creatures erasable with the backspace key
 * Allow for enter key when choosing a creature
 * Create window with example stat block
 */
