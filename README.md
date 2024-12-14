# ZType
This is my implementation of the game [ZType](https://zty.pe/) using entirely Java. 
This was a pair-programming assignment for my class CS2510. 

In this project, my partner and I made the game from scratch using lists and Java funworld library.

# How to play
- Words will appear on the screen. Type the words to destroy the enemy ships.
- Each correct letter you type will fire a shot at the ship.
- If a ship reaches you, the game is over.
  
Type fast and good luck! 🚀

P.S. you can change how fast the words are appearing in the game by following [this](#change-speed).

# How to Set Up
Since this project was developed using a library made by my university, they must be installed and set up correctly for the game to work. **Please** run the project in Eclipse since the project was written in Eclipse. (IntelliJ IDEA for Java always but we gotta respect the OG and setting up the run config and library dependencies in other IDE is a nightmare...) 

If you do not have Eclipse and don't want to download the IDE, here is a demo of the project:

https://github.com/user-attachments/assets/0ae2c820-9a26-4a16-b226-d9b708408e0b

## Prerequisites
Java Development Kit (JDK): Make sure JDK 11 is installed. If not, you can download it [here](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html).

External Library: This project is built on a custom library provided by CS2510 Fundies 2 team. You need to import the required JAR files to run the project. The library can be found in the lib folder.

### Eclipse
1. Clone this repository or download the ZIP file and extract it.
2. Open Eclipse and create a new project.
3. Browse to the directory where you cloned or extracted the project and import it.
4. Adding the external JARS:
   1) Right-click on the project and select Properties.
   2) Click on Java Build Path > Libraries and click Add External JARS
   3) Browse to the lib folder and select both of the JAR files to add it.
   4) Click Apply and Close
5. Navigate to the EclipseWorkspace folder and move the ZType.java file to the src file of the project you created.
6. Running the file:
   1) Click the arrow down symbol next to the first green play button in the bar near the top of the screen/
   2) Click Run Configurations > Java Application in the sidebar.
   3) Select tester.Main as the Main class.
   4) In the Arguments tab, for Program arguments, type **ExamplesZType**
   5) Click Apply then Run.
7. Next time you want to run the project again, click the arrow down button next to the first green play button and choose the config you set up.

<a id="change-speed"></a>
# Change Speed of Words
To change the speed of words falling down the screen, navigate to the class ExamplesZType and scroll to the last method testBigBang, which should be the last method in the file. 

In this line,

```java
double tickRate = 0.15;
```
The number 0.15 represents the speed of words falling in the game. Currently, it is set to 0.15. Lower values make the words fall faster, and higher values slow them down.
