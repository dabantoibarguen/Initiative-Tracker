# D&D Initiative Tracker

## Description

This standalone application is targetted at Dungeons & Dragons (and other TableTop RPGs) players, mainly Game Masters.
The initiative tracker aids the user to keep a list of creatures in a combat encounter, ordered by initiative, and to
interact with them by adding/removing creatures, and updating the remaining health of each creature. The application
has a settings section for further customization.

## Installation

Download the required files from https://github.com/dabantoibarguen/UILab04 or the zip folder if available.

#### How to run the code from Visual Studio Code (Javafx required)

Place the files in a folder, then use the following commands

```
export PATH_TO_FX=~(PATH TO CODE FOLDER)/lib/
```

```
javac --module-path $PATH_TO_FX --add-modules=ALL-MODULE-PATH *.java
```

This will compile all the java files on the folder. Use this command if only the main application needs to be compiled:

```
javac --module-path $PATH_TO_FX --add-modules=ALL-MODULE-PATH Initracker.java
```

Code to run the program:

```
java --module-path $PATH_TO_FX --add-modules=ALL-MODULE-PATH Initracker.java
```

This file contains the main function to launch the application

## Files

### Creature.java

This file contains the "Creature" object used in the initiative tracker. The object has three attributes:

initiative: An initiative value (numerical) to define its position in the list.
creatureName: The name to identify the specific instance of the Creature.
hitpoints: A numerical value that represents the Creature's health (obtained from stat blocks from the desired game).

It also has 5 methods:

Constructor method: 
- Creature(String init, String cName, String hp): Creates a Creature instance.

Getter methods:
- getCName(): returns the Creature's creatureName.
- getInitiative(): returns the Creature's initiative.
- getHp(): returns the Creature's hitpoints.

Updating method:
- modifyHP(int n): Takes a numerical value n; updates the Creature's hitpoints by adding n to its current value.

### Initracker.java

Main file to create, launch and interact with the application.

#### Layout:

The application is organized in three tabs; Tracker, Settings and Stat Block.

- Tracker
    The main application view. This is where Creature objects are listed, and they can be added to the
    list by clicking the + button, or pressing the enter key inside any related input field.

    If the user decides to use a dice rolling formula (XdY+Z, standard in D&D), a panel will display the
    result of each die rolled that make up the total. An example is provided in the Settings and Stat Block
    tabs (see respective sections below). This panel can be hidden with the x button on the top right.

    When the user clicks on a Creature in the list, a panel will be displayed to allow the user to modify
    the Creature's hitpoints, or remove them (see Settings > Warning for removing). This panel can be hidden with 
    the x button on the top right.

![Initiative Tracker image](/Tracker_Tab.png)

- Settings
    There are 5 settings, each with a different default value (in parenthesis).

    - Random Initiative (off): When off, the user can enter an exact value for initiative. When on, the
    user can enter a modifier - a numerical value that will be added to a random value between 1-20 (1d20).

    - Show example creature (off): When off, the prompt text for the input fields in the Tracker tab will
    contain the type of input (name, value, dice). When on, an example creature values will be displayed as
    prompt text. The prompt in initiative changes based on the Random Initiative setting. See Stat Block tab
    for more details. 

    - Empty Fields (on): When on, the three input fields for Creature will be cleared after submitting it
    to the list. When off, they will remain unchanged to allow for quick repetition.

    - Display dice rolled (on): When on, the panel that shows each individual dice will appear if the user
    enters a dice value for hitpoints. When off, this panel will not be displayed, and a previous panel will 
    be closed.

    - Warning for removing (on): When on, the user will see a pop up confirmation window when attempting to
    remove a creature with more than 0 hitpoints. When off, this window will not appear, and the remove button
    will not be opaque at any moment.

![Settings tab image](/Settings_Tab.png)    

- Stat Block
    This tab contains an image for a Dungeons & Dragons 5th Edition stat block for a bandit. The stat block
    has red squares and arrows that indicate the values used for the Show Example setting (see Settings). 

![Bandit Stat Block image](/Stat_Block_Tab.png)

#### Functionality and facts

- The colors used for the interface are obtained from D&D stat blocks (see Stat Block in Layout).

- Pressing the ESCAPE key closes the application immediately.

- The initiative and creature health modification fields use regular expressions to limit input to - or +, followed by 0-9 values.
- The hitpoints field uses a regular expression to limit its input to 0-9 values, or to follow the XdY+Z format.

- The tracker does not accept a Creature in the list if the three input fields are empty. However, filling either of the 3 
creates the Creature with the default values ? for name, 0 for initiative and 1 for hitpoints for the empty fields.

- The hitpoints and initiative (when Random initiative is on) fields use the Thread Local Random library to calculate the result
of each die rolled (1d20 for initiative).

- The columns in the Tracker tab list can be dragged to reorder, but sorting is disabled. Cell edition is also disabled.

- There are ToolTips added to input fields and buttons for additional clarification (for example the + button next to hitpoints).

- The application is organized in Grids which resize the content when the window is resized. There is a minimum size.
- For the Stat Block tab, the image resizes with the window.

- The font used (serif) is inspired by the fonts used in Dungeons & Dragons books.


#### Warning
When compiling the program, the following message will be displayed:
```
Note: Initracker.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
```

This is because of the type the table view expected type of data. Even though cells and columns are specified,
the table itself creates a warning for unchecked generic array creation.

#### Pitfalls and unintended interactions

- An input in the form of X+Z is not accepted by the hitpoints field
- A negative value can be added in the Creature interaction panel (when updating hit points).
- A Creature cannot be selected with the ENTER key when navegating the list with arrow keys.
- A Creature cannot be deleted from the list with the BACKSPACE key.
- It is not possible to change tabs without the use of a mouse, which generates challenges for tabulation-only interactions.
- There is no option to save a list for future use at the moment.
- The height of the descriptions for the Settings tab is hard-coded (magic numbers)
- The functions are not javadoc friendly with the current comments.

### Initracker.css

This file contains the styles used by Initracker.java. Ids, classes and layout elements names are used to 
standarize the style of the application.

### launcher.java

File to launch the application as a .jar from Visual Studio Code extension for Java Projects. It is recommended to run the
application with the instructions above istead.

### Initiative-Tracker.jar

Attempt at deploying the application. Relies on launcher.java and Visual Studio Code at the moment. It is not recommended to 
use this file.

### README2.md

Previous README.md file for github repository. Includes image of initial version and most up-to-date one. It includes
the initial features designed for the application, and additional features to be implemented in the future (some included
in this version, such as dice display area).

## Author

Diego Abanto Ibarguen

## Acknowledgments

Inspiration, code snippets, etc.
* [TableViewCSS] https://edencoding.com/style-tableview-javafx/
* [TableView] http://www.java2s.com/Tutorials/Java/JavaFX/0650__JavaFX_TableView.htm 
* [ThreadLocalRandom] https://www.javatpoint.com/how-to-generate-random-number-in-java
* [ImageViewResizing] https://stackoverflow.com/questions/22993550/how-to-resize-an-image-when-resizing-the-window-in-javafx
* [TextFormatter] https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
* [RegularExpressions] https://www.w3schools.com/java/java_regex.asp
* [ScrollPanes] https://jenkov.com/tutorials/javafx/scrollpane.html

Additional Inspiration
* https://dm.tools/tracker
* https://www.dndbeyond.com/encounter-builder
* https://wiki.roll20.net/Turn_Tracker 
* https://www.dnddiceroller.com/
* https://roll20.net/compendium/dnd5e/Bandit#content (for color palette)