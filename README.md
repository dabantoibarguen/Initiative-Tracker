# Initiative-Tracker
Project for WhiskerHacks Hackathon 2023

Demo: https://youtu.be/l3HQ9Rb3XJI?t=8

Inspired by Dungeons & Dragons 5th edition combat rules. This program is not to be used commercially, but rather as a free tool for 
Game Masters and players of other Tabletop RPG games. 

Current features:

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

Currently in concept stage, ideal features to be added (following dnd 5e rules) include:

  - Web/mobile browser compatibility
  - Column with conditions (charmed, stunned, poisoned, etc)
  - Visualization of enemy stat blocks (from a site like dndbeyond, free stat blocks)
  - On site dice roller for creature modifiers for specific attacks/abilities/saving throws
  - Custom creature stat block builder to include in combat (can be used for players)
  - Save/Load feature for sessions that end mid-combat
  - Settings tab
  - Instructions tab (for new or non-ttrpg players)
  - Dice roll display area (for hitpoints and on site dice roller)

Commands to run from files (javafx required):

export PATH_TO_FX=~(FOLDER LOCATION)/lib/

javac --module-path $PATH_TO_FX --add-modules=ALL-MODULE-PATH Initracker.java

java --module-path $PATH_TO_FX --add-modules=ALL-MODULE-PATH Initracker

<img width="606" alt="Preview Image" src="https://user-images.githubusercontent.com/78096139/223048646-f811bcd2-1381-4a22-96c2-9ca6cadd744f.png">
