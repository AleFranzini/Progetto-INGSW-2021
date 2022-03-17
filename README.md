# Master of Renaissance

<img src="https://cf.geekdo-images.com/-zdSgCFfOGAsgZ6M-Rjw1w__itemrep/img/i9N9_89f-1VaAkHtxBt80pYXvYE=/fit-in/246x300/filters:strip_icc()/pic4782992.jpg" width=240px height=300 px align="right" />

**Table of Contents**

- [Project Specifications](#project-specifications)
- [Implemented Functionalities](#implemented-functionalities)
- [Run the Game](#run-the-game)
    - [Online Game](#online-game)
    - [Offline Game](#offline-game)
- [Recommended Platforms for the Game](#recommended-platforms-for-the-game)
  - [CLI](#cli)
  - [GUI](#gui)
- [Documentation](#documentation)
  - [Javadoc](#javadoc)
  - [UML Diagrams](#uml-diagrams)
  - [Class Diagrams](#class-diagrams)
- [Software Used](#software-used)
- [The Team](#the-team)



## Project Specifications
The project is a digital version of the board game "*Master Of Renaissance*" made by Cranio Creations.

You can find the rules for the game at this [link](https://craniointernational.com/2021/wp-content/uploads/2021/05/Masters-of-Renaissance_small.pdf).

This project is the final test of "Software Engineering", course of "Computer Science Engineering" held at Politecnico di Milano (2020/2021).

The project includes:
- a single JAR of the game with every rule implementation;
- the source code of the game classes; 
- the source code of the game tests;
- the game resources (img, json, fonts);
- initial and final UML diagram;
- network sequence diagrams;

## Implemented Functionalities
| Functionality | Status |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](http://placehold.it/15/44bb44/44bb44)](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/src/main/java/it/polimi/ingsw/model) |
| Complete rules | [![GREEN](http://placehold.it/15/44bb44/44bb44)](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/src/main/java/it/polimi/ingsw/model) |
| Socket |[![GREEN](http://placehold.it/15/44bb44/44bb44)](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/src/main/java/it/polimi/ingsw/network) |
| GUI | [![GREEN](http://placehold.it/15/44bb44/44bb44)](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/src/main/java/it/polimi/ingsw/view/gui) |
| CLI |[![GREEN](http://placehold.it/15/44bb44/44bb44)](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/src/main/java/it/polimi/ingsw/view/cli) |
| Local Games | [![GREEN](http://placehold.it/15/44bb44/44bb44)](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/blob/master/src/main/java/it/polimi/ingsw/network/client/ClientOffline.java) |
| Multiple games | [![RED](http://placehold.it/15/f03c15/f03c15)]()|
| Persistence | [![GREEN](http://placehold.it/15/44bb44/44bb44)](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/blob/master/src/main/java/it/polimi/ingsw/network/server/Backup.java) |
| Disconnections resilience | [![GREEN](http://placehold.it/15/44bb44/44bb44)](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/src/main/java/it/polimi/ingsw/network/server) |
| Parameters Editor | [![RED](http://placehold.it/15/f03c15/f03c15)]() |

#### Legend
[![RED](http://placehold.it/15/f03c15/f03c15)]() Not Implemented &nbsp;&nbsp;&nbsp;&nbsp;[![GREEN](http://placehold.it/15/44bb44/44bb44)]() Implemented


## Run the Game
The JAR for the game can be found [here](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/deliverables/final/jar).
The JAR contains both server and client (online and offline) and each one can be run after being selected.

Start the JAR on the command line by writing the following line in the folder where the JAR is contained: 
` java -jar MasterOfRenaissance.jar `

### Debug Mode
The debug mode permits to start the game with a great amount of resources in order to proceed faster in the game.

This mode can be access by adding a string to the default JAR running line. The result is the following: ` java -jar -Ddebug=true MasterOfRenaissance.jar `.

### Online Game
To player an online game you first must run the Server to host the game by selecting the option '*Server*' after the JAR has started.
The server will ask you to provide a port with an activated port forwarding to be able to connect to the clients.

Once the Server is running, you must start a JAR for each new player and select the option '*Client Online*'.
Then you can choose if you want to play using the CLI (Command-line Interface), or the GUI (Graphical User Interface).
The client will ask you to provide the server IP and port to successfully connect.

You can now insert your username and play both in single player or multiplayer mode.

### Offline Game
To play a single player offline game you must select the option '*Client Offline*' after the JAR has started.
Then you can choose if you want to play using the CLI (Command-line Interface), or the GUI (Graphical User Interface).

You can now insert your username and play in single player.


## Recommended Platforms for the Game
The game has been optimized to run on Windows.

We recommend running the server on CMD or Powershell.

### CLI
The CLI uses UTF-8 and Unicode characters, therefore is better experienced on Unix based terminals. 
We recommend using WSL or Ubuntu terminals.

To be able to read characters properly you must use a font that supports them. Our recommendation is this [font](https://www.jetbrains.com/lp/mono/).

### GUI
The GUI uses javafx, so you must install it to make sure the interface works properly.
We recommend running the GUI on CMD or Powershell. 

It won't work on WSL because of graphic restrictions imposed by Windows.

## Documentation

### Javadoc
The Javadoc is available [here](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/deliverables/final/javadoc).

### UML Diagrams
There are two UML diagrams, the initial one and the final one. Both can be found [here](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/deliverables/final/uml)

We created the initial one based on the architecture we had in mind, the final one was the realization of this architecture and was automatically generated by PlantUML.

### Class Diagrams
There are three class diagrams, we made them as a homework during the development. 

The diagrams represent the network behaviour in these three situations:
- access to the game;
- take resources;
- play a leader card;

The network behaviour for each case has been explained in details in a document called 'Client Server Communication Protocol'.

All the resources can be found [here](https://github.com/fe097/ing-sw-2021-Franzini-Galetti-Grugni/tree/master/deliverables/final/uml/Network_Sequence_Diagrams) 

## Software used
**PlantUML** - Sequence and UML Diagrams

**Maven** - Dependencies Management

**Intellij IDEA** - IDE

**JavaFX** - Graphical Framework

## The Team
* [Alessandro Franzini](https://github.com/AlessandroFranzini)
* [Filippo Galetti](https://github.com/Mr-WoIf)
* [Federico Grugni](https://github.com/fe097)

