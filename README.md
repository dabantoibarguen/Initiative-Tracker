# Initiative-Tracker
Project for WhiskerHacks Hackathon 2023

Inspired by Dungeons & Dragons 5th edition combat rules. This program is not to be used commercially, but rather as a free tool for 
Game Masters for any Tabletop RPG games. 

Current features:

- Add/Remove creatures to list with Iniatiative value, name and total hitpoints
- Modify hitpoints of chosen creature from the list (either increase or decrease)
- Hitpoint field accepts dice rolling format: xdy+z (e.g. 3d6+6)
  - (x = number of dice to roll, y = value of a singular die (a d6 is a six-sided die), z = modifier)
- Randomize initiative field calculates a pseudorandom number from 1-20 (like rolling 1d20). 
  - Field changed to accept modifier rather than final value.
- Default values for empty fields (? for name, 0 for initiative, 1 for hitpoints).  

Currently in concept stage, ideal features to be added (following dnd 5e rules) include:

- Web/mobile browser compatibility
- Column with conditions (charmed, stunned, poisoned, etc)
- Visualization of enemy stat blocks (from a site like dndbeyond, free stat blocks)
- On site dice roller for creature modifiers for specific attacks/abilities/saving throws
- Custom creature stat block builder to include in combat (can be used for players)
- Save/Load feature for sessions that end mid-combat


Commands to run from files:
export PATH_TO_FX=~/Initiative_Tracker/Initiative-Tracker/lib/
javac --module-path $PATH_TO_FX --add-modules=ALL-MODULE-PATH Initracker.java
java --module-path $PATH_TO_FX --add-modules=ALL-MODULE-PATH Initracker
