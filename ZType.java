import tester.*;
import javalib.worldimages.*;  
import javalib.funworld.*;

import java.awt.Color;
import java.util.Random;

//represent a ZTypeWorld game
class ZTypeWorld extends World {
  ILoWord word;
  Random rand;
  // CONSTANTS
  final int SCREEN_WIDTH = 400;
  final int SCREEN_HEIGHT = 600;
  int counter;
  int score;

  // the constructor 
  ZTypeWorld(ILoWord word, int count, int score) {
    this.word = word; 
    this.rand = new Random();
    this.counter = count;
    this.score = score;
  }

  /* TEMPLATE:
   * Fields:
     ... this.word ...                  -- ILoWord
     ... this.rand ...                  -- Random
     ... this.counter ...               -- int

     Method:
     ... this.makeScene() ...                       -- WorldScene
     ... this.onTick() ...                          -- ZTypeWorld
     ... this.onTickForTesting(Random r) ...        -- ZTypeWorld
     ... this.worldEnds() ...                       -- WorldEnd
     ... this.makeAFinalScene() ...                 -- WorldScene
     ... this.onKeyEvent(String str) ...            -- ZTypeWorld
     ... this.onMouseClicked(Posn pos) ...          -- ZTypeWorld
     ... this.restartButtonClick(Posn pos) ...      -- boolean
     ... this.drawRestartButton(WorldScene ws) ...  -- WorldScene

     Method of fields:
     ... this.word.checkAndReduce(String str) ...   -- ILoWord
     ... this.word.filterOutEmpties() ...           -- ILoWord
     ... this.word.draw(WorldScene ws) ...          -- WorldScene
     ... this.word.move() ...                       -- ILoWord
     ... this.word.activate(String str) ...         -- ILoWord
     ... this.word.anyActive() ...                  -- boolean
     ... this.word.sort() ...                       -- ILoWord
     ... this.word.insert(IWord word) ...           -- ILoWord
     ... this.word.activateFirst(String str) ...    -- ILoWord
     ... this.word.activateRest(String str) ...     -- ILoWord
     ... this.word.check(String str) ...            -- boolean
     ... this.word.gameIsOver() ...                 -- boolean
   */

  // images of a rocket :D!
  final OverlayOffsetImage ROCKET1 = new OverlayOffsetImage(new TriangleImage(
      new Posn(25, 0), new Posn(0, 25), new Posn(50, 25), "solid", Color.RED),
      0, 30,
      (new EllipseImage(30, 60, OutlineMode.SOLID, Color.LIGHT_GRAY)));

  final OverlayOffsetImage ROCKET2 = new OverlayOffsetImage(
      new CircleImage(10, "solid", Color.BLUE), 0, 0,
      ROCKET1);

  final OverlayOffsetImage ROCKET3 = new OverlayOffsetImage(new TriangleImage(
      new Posn(15, 0), new Posn(0, 15), new Posn(15, 15),
      "solid", Color.RED), 15, -30, ROCKET2);

  final OverlayOffsetImage ROCKET4 = new OverlayOffsetImage(new TriangleImage(
      new Posn(-15, 0), new Posn(-15, 15), new Posn(0, 15),
      "solid", Color.RED), -15, -30, ROCKET3);


  // draw a WorldScene
  public WorldScene makeScene() {
    // Template: everything in the class template
    return this.word.draw(
        new WorldScene(400, 600)
        .placeImageXY(
            new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, OutlineMode.SOLID, Color.BLACK), 
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)
        .placeImageXY(new StarImage(15, OutlineMode.SOLID, Color.YELLOW), 30, 30)
        .placeImageXY(new CircleImage(20, OutlineMode.SOLID, Color.YELLOW), 360, 30)
        .placeImageXY(
            new TextImage(
                "Score: " + Integer.toString(this.score), 20.0, 
                FontStyle.BOLD, Color.WHITE), SCREEN_WIDTH / 2, 30)
        .placeImageXY(new RectangleImage(SCREEN_WIDTH, 100, OutlineMode.SOLID, Color.BLUE), 
            SCREEN_WIDTH / 2, SCREEN_HEIGHT))
        .placeImageXY(ROCKET4, 200, 550);
  }


  // move the words on the scene. Add a new word at a random location at every 10 ticks of the clock
  public World onTick() {   
    // Template: Everything in the class template
    ILoWord add = 
        new ConsLoWord(
            new InactiveWord(
                new Utils().randomGenerator(new Random()), 
                new Utils().randomRestrict(new Random()), 0), this.word);

    // game will add a new word after every 10 clicks 
    if (this.counter % 10 == 0 && this.counter < 200) {
      return new ZTypeWorld(add.move(), this.counter + 1, this.score);
      // game will NOT add a new word if 10 clicks have not passed yet
    } else if (this.counter % 10 != 0 && this.counter < 200) {
      return new ZTypeWorld(this.word.move(), this.counter + 1, this.score);
    } else {
      // stops adding new word after 20 words using this.counter < 200
      return new ZTypeWorld(this.word.move(), this.counter + 1, this.score);
    }
  }


  // move the words on the scene. Add a new word at a random location at every tick of the clock
  // uses a seeded random object for making the new IWord to make it easier for testing
  public ZTypeWorld onTickForTesting(Random r) {
    // Template: everything in the class template
    ILoWord add = 
        new ConsLoWord(
            new InactiveWord(
                new Utils().randomGenerator(r), 
                new Utils().randomRestrict(r), 0), this.word);

    if (this.counter % 10 == 0 && this.counter < 200) {
      return new ZTypeWorld(add.move(), this.counter + 1, this.score);
    } else if (this.counter % 10 != 0 && this.counter < 200) {
      return new ZTypeWorld(this.word.move(), this.counter + 1, this.score);
    } else {
      // stops adding new word after 10 words using this.counter < 200
      return new ZTypeWorld(this.word.move(), this.counter + 1, this.score);
    }
  }

  // add a key event to check and reduce an active word or turn an inactive word 
  // into an active word
  public ZTypeWorld onKeyEvent(String key) {  
    // Template: Everything in the class template
    if (this.word.anyActive()) {
      return new ZTypeWorld(this.word.checkAndReduce(key).filterOutEmpties(), 
          this.counter + 1, this.score + 10);
    } else {
      return new ZTypeWorld(this.word.activate(key), this.counter + 1, this.score);
    }
  }

  // indicate when the world ends
  public WorldEnd worldEnds() {
    // Template: Everything in the class template  
    if (this.word.gameIsOver()) {
      return new WorldEnd(true, this.makeAFinalScene());
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  } 

  // Make the final scene to end the world 
  public WorldScene makeAFinalScene() {
    // Template: everything in the class template
    WorldScene finalScene = new WorldScene(400, 600)
        .placeImageXY(
            new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, OutlineMode.SOLID, Color.BLACK), 
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)
        .placeImageXY(new StarImage(25, OutlineMode.SOLID, Color.YELLOW), SCREEN_WIDTH / 2, 240)
        .placeImageXY(new TextImage("Try again next time!", 22.0, FontStyle.BOLD, Color.GREEN), 
            SCREEN_WIDTH / 2, 340)
        .placeImageXY(new TextImage("Game Over", 30.0, FontStyle.BOLD, Color.RED), 
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)
        .placeImageXY(
            new TextImage(
                "Score: " + Integer.toString(this.score), 20.0, 
                FontStyle.BOLD, Color.WHITE), SCREEN_WIDTH / 2, 380);

    // Draw the restart button only if the game has ended
    return finalScene;
  } 
}

// -----------------
// utility class
class Utils {
  Utils() {}

  // produce a string of 6 random letters
  String randomGenerator(Random r) {
    /* TEMPLATE: Everything in the class template
     */
    return stringProducer(6, r); 
  }

  // helper method for randomGenerator()
  // acts as an accumulator for randomGenerator()
  String stringProducer(int x, Random r) {
    // TEMPLATE: Everything in the class template
    int randomNum = r.nextInt(123);

    if (x == 0) {
      return "";
    } else if (x <= 6) {
      if (randomNum < 97) {
        return stringProducer(x, r);
      } else {
        return String.valueOf((char)randomNum) + stringProducer(x - 1, r);
      }
    } else {
      return "";
    }
  }

  // restrict the x-coordinate of every new InactiveWord 
  // so that it doesn't fall out of the frame
  public int randomRestrict(Random r) {
    // TEMPLATE: Everything in the class template
    int randomNum = r.nextInt(355);

    if (randomNum < 50) {
      return randomRestrict(r);
    } else {
      return randomNum;
    }
  }
}

//------------------------------
//represents a list of words
interface ILoWord {

  // takes in a String of length 1 and produces an ILoWord where any active words
  // in this ILoWord are reduced by removing the first letter only 
  // if the given string matches the first letter.
  ILoWord checkAndReduce(String str);

  //produces an ILoWord with any IWords that have an empty string are filtered out.
  ILoWord filterOutEmpties();

  // move the list of words 
  ILoWord move(); 

  // is there any active words in the list?
  boolean anyActive();

  // turn a IWord active
  ILoWord activate(String str);

  // produces a new list, with words sorted in alphabetical order, 
  // treating all Strings as if they were given in all lower case (case-insensitive) 
  ILoWord sort();

  // helper function for sort() - insert a word into a list
  ILoWord insert(IWord word);

  //check the first word in the list matches the string
  boolean check(String str);

  // helper method for activate() - activate the first word in the list
  public ILoWord activateFirst(String str);

  //helper method for activate() - activate the rest of the list
  public ILoWord activateRest(String str);

  // takes in a WorldScene and draws all of the words in this ILoWord onto the given WorldScene. 
  WorldScene draw(WorldScene ws);

  // determine if the game is over
  boolean gameIsOver();

}


//------------------------------
//represents an empty list of words
class MtLoWord implements ILoWord {

  // the constructor
  MtLoWord() {
    // nothing to do!
  }

  /* TEMPLATE:
     Method:
     ... this.checkAndReduce(String str) ...   -- ILoWord
     ... this.filterOutEmpties() ...           -- ILoWord
     ... this.draw(WorldScene ws) ...          -- WorldScene
     ... this.move() ...                       -- ILoWord
     ... this.activate(String str) ...         -- ILoWord
     ... this.anyActive() ...                  -- boolean
     ... this.sort() ...                       -- ILoWord
     ... this.insert(IWord word) ...           -- ILoWord
     ... this.activateFirst(String str) ...    -- ILoWord
     ... this.activateRest(String str) ...     -- ILoWord
     ... this.check(String str) ...            -- boolean
     ... this.gameIsOver() ...                 -- boolean
   */

  // produces a new list, with words sorted in alphabetical order, 
  // treating all Strings as if they were given in all lower case (case-insensitive)
  public ILoWord sort() {
    // Everything in the class template
    return this;
  }

  //helper function for sort() & merge() - insert a word into a list
  public ILoWord insert(IWord word) {
    /* TEMPLATE: everything in the class template plus
    Fields of parameters:
    ... word.word ...         -- String
    ... word.x ...            -- int
    ... word.y ...            -- int

    Method on parameters:
    ... word.comesFirst(IWord word) ...           -- int
    ... word.compareString() ...                  -- int 
    ... word.checkEmpty() ...                     -- boolean
    ... word.check(String str) ...                -- boolean
    ... word.cut() ...                            -- IWord 
    ... word.drawOnWS() ...                       -- WorldScene
     */
    return new ConsLoWord(word, this);
  }

  // takes in a String of length 1 and produces an ILoWord where any active words
  // in this ILoWord are reduced by removing the first letter only 
  // if the given string matches the first letter.
  public ILoWord checkAndReduce(String str) {
    // Everything in the class template 
    return this;
  }


  //produces an ILoWord with any IWords that have an empty string are filtered out.
  public ILoWord filterOutEmpties() {
    // everything in the class template
    return this;
  }

  // move the list of words down
  public ILoWord move() {
    // everything in the class template
    return this;
  }

  // activate a list of inactive & active words
  public ILoWord activate(String str) {
    // everything in the class template
    return this;
  }

  // is there any active word in the list?
  public boolean anyActive() {
    // everything in the class template
    return false;
  }

  // check the first word in the list matches the string
  public boolean check(String str) {
    return false;
  }

  //helper method for activate() - activate the first word in the list
  public ILoWord activateFirst(String str) {
    return this;
  }

  // helper method for activate() - activate the first word in the list
  public ILoWord activateRest(String str) {
    return this;
  }

  // takes in a WorldScene and draws all of the words in this ILoWord onto the given WorldScene. 
  public WorldScene draw(WorldScene ws) {
    /* TEMPLATE: everything in the class template plus:
     Fields of parameters:
     ... ws.height ...         -- int
     ... ws.width  ...         -- int
     */
    return ws;
  }

  // determine if the game is over
  public boolean gameIsOver() {
    // everything in the class template
    return false;
  }
}

//------------------------------
//to represent a non-empty list
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;

  // the constructor
  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
  }

  /* TEMPLATE:
    Fields:
    ... this.first ...                -- IWord
    ... this.rest ...                 -- ILoWord

    Method:
     ... this.checkAndReduce(String str) ...   -- ILoWord
     ... this.filterOutEmpties() ...           -- ILoWord
     ... this.draw(WorldScene ws) ...          -- WorldScene
     ... this.move() ...                       -- ILoWord
     ... this.activate(String str) ...         -- ILoWord
     ... this.anyActive() ...                  -- boolean
     ... this.sort() ...                       -- ILoWord
     ... this.insert(IWord word) ...           -- ILoWord
     ... this.activateFirst(String str) ...    -- ILoWord
     ... this.activateRest(String str) ...     -- ILoWord
     ... this.check(String str) ...            -- boolean
     ... this.gameIsOver() ...                 -- boolean

    Methods of field:
    ... this.first.comesFirst(IWord that) ...         -- boolean
    ... this.first.compareY(int that) ...             -- boolean
    ... this.first.check(String str) ...              -- boolean
    ... this.first.checkEmpty() ...                   -- boolean
    ... this.first.checkFirst(String str) ...         -- boolean
    ... this.first.isActive() ...                     -- boolean
    ... this.first.cut() ...                          -- IWord
    ... this.first.move() ...                         -- IWord
    ... this.first.activate(String str) ...           -- IWord
    ... this.first.drawOnWS(WorldScene ws) ...        -- WorldScene 
    ... this.first.gameIsOver() ...                   -- boolean

     ... this.rest.checkAndReduce(String str) ...   -- ILoWord
     ... this.rest.filterOutEmpties() ...           -- ILoWord
     ... this.rest.draw(WorldScene ws) ...          -- WorldScene
     ... this.rest.move() ...                       -- ILoWord
     ... this.rest.activate(String str) ...         -- ILoWord
     ... this.rest.anyActive() ...                  -- boolean
     ... this.rest.sort() ...                       -- ILoWord
     ... this.rest.insert(IWord word) ...           -- ILoWord
     ... this.rest.activateFirst(String str) ...    -- ILoWord
     ... this.rest.activateRest(String str) ...     -- ILoWord
     ... this.rest.check(String str) ...            -- boolean
     ... this.rest.gameIsOver() ...                 -- boolean
   */

  // sort a list of words based on their y-coordinate and produce a new list
  public ILoWord sort() {
    // Everything in the class template
    return this.rest.sort().insert(this.first); 
    // sort the rest of the list and insert the first element into that result
  }

  // helper method for sort() method - determine if a word comes
  // before another based on their y-coordinates
  public ILoWord insert(IWord word) {
    /* TEMPLATE: EVERYTHING IN THE CLASS TEMPLATE PLUS
    Fields of parameters:
    ... word.word ...         -- String
    ... word.x ...            -- int
    ... word.y ...            -- int

    Method on parameters:
    Methods of field:
    ... word.comesFirst(IWord that) ...         -- boolean
    ... word.compareY(int that) ...             -- boolean
    ... word.check(String str) ...              -- boolean
    ... word.checkEmpty() ...                   -- boolean
    ... word.checkFirst(String str) ...         -- boolean
    ... word.isActive() ...                     -- boolean
    ... word.cut() ...                          -- IWord
    ... word.move() ...                         -- IWord
    ... word.activate(String str) ...           -- IWord
    ... word.drawOnWS(WorldScene ws) ...        -- WorldScene 
    ... word.gameIsOver() ...                   -- boolean
     */
    if (this.first.comesFirst(word)) {
      return new ConsLoWord(word, this);
    } else {
      return new ConsLoWord(this.first, this.rest.insert(word));
    }
  }

  // takes in a String of length 1 and produces an ILoWord where any active words
  // in this ILoWord are reduced by removing the first letter only 
  // if the given string matches the first letter.
  public ILoWord checkAndReduce(String str) {
    // Everything in the class template
    if (this.first.check(str)) {
      return new ConsLoWord(this.first.cut(), this.rest.checkAndReduce(str));
    } else {
      return new ConsLoWord(this.first, this.rest.checkAndReduce(str));
    }
  }

  // produces an ILoWord with any IWords that have an empty string are filtered out.
  public ILoWord filterOutEmpties() {
    // Template: Everything in the class template
    if (this.first.checkEmpty()) {
      return this.rest.filterOutEmpties();
    } else {
      return new ConsLoWord(this.first, this.rest.filterOutEmpties());
    }
  }

  // move the list of words
  public ILoWord move() {
    // Template: Everything in the class template
    return new ConsLoWord(this.first.move(), this.rest.move());
  }

  // activate the list of words if the first letter of the word 
  // matches the string that passes in
  public ILoWord activate(String str) {  
    // Template: Everything in the class template
    ILoWord sortVersion = this.sort();

    if (sortVersion.check(str)) {
      return sortVersion.activateFirst(str);
    } else {
      return sortVersion.activateRest(str);
    }
  }

  //check the first word in the list matches the string
  public boolean check(String str) {
    // Template: everything in the class template
    return this.first.checkFirst(str);
  }

  //helper method for activate() - activate the first word in the list
  public ILoWord activateFirst(String str) {
    // Template: everything in the class template
    return new ConsLoWord(this.first.activate(str), this.rest);
  }

  //helper method for activate() - activate the rest of the list
  public ILoWord activateRest(String str) {
    // Template: everything in the class template
    return new ConsLoWord(this.first, this.rest.activate(str));
  }

  // is there any active word in the list?
  public boolean anyActive() {
    // Template: Everything in the class template
    return this.first.isActive() || this.rest.anyActive();
  }

  // takes in a WorldScene and draws all of the words in this ILoWord onto the given WorldScene. 
  public WorldScene draw(WorldScene ws) {
    /* TEMPLATE: everything in the template ConsLoWord plus
     Fields of parameters:
     ... ws.height ...         -- int
     ... ws.width  ...         -- int
     */
    WorldScene updatedWS = first.drawOnWS(ws);
    return rest.draw(updatedWS);
  }

  // determine if the game is over
  public boolean gameIsOver() {
    // Template: everything in the class template
    return this.first.gameIsOver() || this.rest.gameIsOver();
  }
}

//------------------------------
// represents a word in the ZType game
interface IWord {

  // determine which word comes first based on their y-coordinates
  boolean comesFirst(IWord that);

  // compare 2 y-coordinates with each other
  boolean compareY(int that);

  // helper method for checkAndReduce() - check if a word 
  // starts with a String str
  boolean check(String str);

  // helper method for filterOutEmpties() - determine if a word
  // is empty
  boolean checkEmpty();

  // check of the first letter matches given string
  boolean checkFirst(String str);

  // is this word active?
  boolean isActive();

  // helper method for checkAndReduce() - cut the word
  IWord cut();

  // move the word down the y-axis
  IWord move(); 

  // turn a IWord active
  IWord activate(String str);

  // helper function to draw the words onto the WorldScene
  WorldScene drawOnWS(WorldScene ws);

  // determine if the game is over
  boolean gameIsOver();
}

//------------------------------
// represent an abstract class AWord
abstract class AWord implements IWord {
  String word;
  Random rand;
  int x;
  int y;

  // the constructor
  AWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  }

  // RANDOM constructor
  AWord(String word, Random r, int y) {
    this.word = word;
    this.x = rand.nextInt(400);
    this.y = 0;
  }

  /* Template:
     Fields:
     ... this.word ...              -- String
     ... this.x ...                 -- int
     ... this.y ...                 -- int

     Methods of field:
    ... this.comesFirst(IWord that) ...         -- boolean
    ... this.compareY(int that) ...             -- boolean
    ... this.check(String str) ...              -- boolean
    ... this.checkEmpty() ...                   -- boolean
    ... this.checkFirst(String str) ...         -- boolean
    ... this.isActive() ...                     -- boolean
    ... this.cut() ...                          -- IWord
    ... this.move() ...                         -- IWord
    ... this.activate(String str) ...           -- IWord
    ... this.drawOnWS(WorldScene ws) ...        -- WorldScene 
    ... this.gameIsOver() ...                   -- boolean
   */

  // helper method for many methods
  // determine which word comes first based on their y-coordinates
  public boolean comesFirst(IWord word) {
    /* TEMPLATE: everything in the MtLoWord template plus
    Fields of parameters:
    ... word.word ...         -- String
    ... word.x ...            -- int
    ... word.y ...            -- int

    Method on parameters:
    ... word.comesFirst(IWord that) ...         -- boolean
    ... word.compareY(int that) ...             -- boolean
    ... word.check(String str) ...              -- boolean
    ... word.checkEmpty() ...                   -- boolean
    ... word.checkFirst(String str) ...         -- boolean
    ... word.isActive() ...                     -- boolean
    ... word.cut() ...                          -- IWord
    ... word.move() ...                         -- IWord
    ... word.activate(String str) ...           -- IWord
    ... word.drawOnWS(WorldScene ws) ...        -- WorldScene 
    ... word.gameIsOver() ...                   -- boolean
     */
    return word.compareY(this.y);
  }

  // helper method for comesFirst - compare 2 integers (y-coordinates)
  public boolean compareY(int that) {
    // TEMPLATE: everything in the class template
    return this.y > that;
  }

  // helper method for checkAndReduce() - check if word starts
  // with a String str
  public boolean check(String str) {
    // Template: Everything in the class template
    return false;
  }

  // helper method for filterOutEmpties() - check if a word is empty
  public boolean checkEmpty() {
    // everything in the class template
    return this.word.isEmpty();
  }

  // check the first string of the word
  public boolean checkFirst(String str) {
    // Template: Everything in the class template
    return this.check(str);
  }

  // helper method for checkAndReduce(String str)
  // since inactive words are not getting reduce, return false
  public IWord cut() {
    // everything in the class template
    return this; 
  }

  // move the word down the y-axis
  abstract public IWord move();

  // is the word active?
  public boolean isActive() {
    // Template: Everything in the class template
    return false;
  }

  // activate a word
  public IWord activate(String str) {
    // Template: Everything in the class template
    return this;
  }

  // to draw the words onto the WorldScene
  abstract public WorldScene drawOnWS(WorldScene ws);

  // determine if the game is over by looking at the y-coordinate of the word
  public boolean gameIsOver() {
    // Template: Everything in the class template
    return this.y >= 600 & !this.word.isEmpty();
  }
}

//------------------------------
//represents an active word in the ZType game
class ActiveWord extends AWord {

  // the constructor
  ActiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  // RANDOM constructor
  ActiveWord(String word, Random r, int y) {
    super(word, r, y);
  }

  /* Template:
  Fields:
  ... this.word ...              -- String
  ... this.x ...                 -- int
  ... this.y ...                 -- int

  Methods of field:
  ... this.check(String str) ...              -- boolean
  ... this.checkEmpty() ...                   -- boolean
  ... this.checkFirst(String str) ...         -- boolean
  ... this.gameIsOver() ...                   -- boolean
  ... this.isActive() ...                     -- boolean
  ... this.move() ...                         -- IWord
  ... this.activate(String str) ...           -- IWord
  ... this.cut() ...                          -- IWord
  ... this.drawOnWS(WorldScene ws) ...        -- WorldScene 

   */


  @Override
  // helper method for checkAndReduce() - check if word starts
  // with a String str
  public boolean check(String str) {
    // Everything in the class template
    return !this.word.isEmpty() && !str.isEmpty() && this.word.startsWith(str);
  }

  @Override
  // helper method for checkAndReduce() - cut the first letter of a word
  public IWord cut() {
    // everything in the class template
    return new ActiveWord(this.word.substring(1), this.x, this.y);  
  }

  // move the word down the y-axis
  public IWord move() {
    // Template: Everything in the class template
    return new ActiveWord(this.word, this.x, this.y + 5);
  }

  @Override
  // is the word active?
  public boolean isActive() {
    // Template: Everything in the class template
    return true;
  }

  // to draw the words onto the WorldScene
  public WorldScene drawOnWS(WorldScene ws) {
    /* TEMPLATE: everything in the class template PLUS
   Fields of parameters:
   ... ws.height ...         -- int
   ... ws.width  ...         -- int
     */
    TextImage wordImage = new TextImage(this.word, 20, FontStyle.BOLD, Color.GREEN);
    return ws.placeImageXY(wordImage, this.x, this.y);
  }

}

//------------------------------
//represents an inactive word in the ZType game
class InactiveWord extends AWord {

  // the constructor
  InactiveWord(String word, int x, int y) {
    super(word, x, y);
  }

  // RANDOM constructor
  InactiveWord(String word, Random r, int y) {
    super(word, r, y);
  }

  /* Template:
  Fields:
  ... this.word ...              -- String
  ... this.x ...                 -- int
  ... this.y ...                 -- int

  Methods of field:
  ... this.check(String str) ...              -- boolean
  ... this.checkEmpty() ...                   -- boolean
  ... this.checkFirst(String str) ...         -- boolean
  ... this.gameIsOver() ...                   -- boolean
  ... this.isActive() ...                     -- boolean
  ... this.move() ...                         -- IWord
  ... this.activate(String str) ...           -- IWord
  ... this.cut() ...                          -- IWord
  ... this.drawOnWS(WorldScene ws) ...        -- WorldScene 

   */


  // move the word down the screen
  public IWord move() {
    // Template: Everything in the class template
    return new InactiveWord(this.word, this.x, this.y + 5);
  }

  @Override
  // activate a word
  public IWord activate(String str) {
    // Template: Everything in the class template
    if (this.word.startsWith(str)) {
      return new ActiveWord(this.word, this.x, this.y).cut();
    } else {
      return this;
    }  
  }

  @Override
  // check if the word starts with the given string
  public boolean checkFirst(String str) {
    // everything in the class template
    return this.word.startsWith(str);
  }

  // helper method for draw() - to draw IWord onto the WorldScene
  public WorldScene drawOnWS(WorldScene ws) {
    /* TEMPLATE: everything in the template ConsLoWord PLUS
     Fields of parameters:
     ... ws.height ...         -- int
     ... ws.width  ...         -- int
     */
    TextImage wordImage = new TextImage(this.word, 20, FontStyle.BOLD, Color.RED);
    return ws.placeImageXY(wordImage, this.x, this.y);
  }
}


// ----------------------------------------
// ----------------------------------------
// ----------------------------------------

// examples of ZType
class ExamplesZType {
  ExamplesZType() {}

  // empty examples
  ILoWord empty = new MtLoWord();
  IWord emptyAWord = new ActiveWord("", 0, 0);
  IWord emptyIWord = new InactiveWord("", 0, 0);

  // Examples of base inactive and active IWord & ILoWord
  IWord inactive = new InactiveWord("inactive", 0, 0);
  IWord active = new ActiveWord("active", 0, 0);
  ILoWord inactiveList = new ConsLoWord(this.inactive, this.empty);
  ILoWord activeList = new ConsLoWord(this.active, this.empty);

  // Examples of active IWord
  IWord anh = new ActiveWord("anh", 2, 3);
  IWord anhCut = new ActiveWord("nh", 2, 3);
  IWord backBay = new ActiveWord("BACK BAY", 6, 0);
  IWord backBayCut = new ActiveWord("ACK BAY", 6, 0);
  IWord boston = new ActiveWord("boston", 4, 8);
  IWord cafe = new ActiveWord("cafe", 5, 5);
  IWord chin = new ActiveWord("chin", 6, 4);
  IWord decor = new ActiveWord("decor", 6, 2);
  IWord hello = new ActiveWord("hello", 1, 1);

  // Examples of inactive IWord
  IWord annie = new InactiveWord("annie", 3, 2);
  IWord bean = new InactiveWord("bean", 4, 5);
  IWord bin = new InactiveWord("BIN", 0, 6);
  IWord cap = new InactiveWord("cap", 1, 4);
  IWord khoury = new InactiveWord("khoury", 3, 3);

  // Examples of unsorted active ILoWord
  ILoWord lista = new ConsLoWord(this.anh, this.activeList); 
  // Examples of unsorted inactive ILoWord
  ILoWord listb = new ConsLoWord(this.bean, this.inactiveList);
  // Examples of unsorted ILoWord consist of active & inactive elements
  ILoWord listd = new ConsLoWord(this.annie,
      new ConsLoWord(this.chin, this.listb));
  // Examples of unsorted and sorted active ILoWord
  ILoWord listc = new ConsLoWord(this.cafe, new ConsLoWord(this.chin, this.lista));
  // Examples of unsorted and sorted ILoWord consist of active & inactive elements
  ILoWord liste = new ConsLoWord(this.boston, 
      new ConsLoWord(this.backBay, new ConsLoWord(this.anh, 
          new ConsLoWord(this.decor, new ConsLoWord(this.khoury, this.empty)))));
  // Examples of unsorted and sorted ILoWord consist of active & inactive elements
  ILoWord listf = new ConsLoWord(this.khoury, 
      new ConsLoWord(this.hello, new ConsLoWord(this.decor, 
          new ConsLoWord(this.cap, new ConsLoWord(this.bin,
              new ConsLoWord(this.chin, new ConsLoWord(this.anh, this.empty)))))));
  // Examples of unsorted and sorted ILoWord consist of active & inactive elements
  // and 1 empty active string
  ILoWord listg = new ConsLoWord(this.bean, new ConsLoWord(this.cafe,
      new ConsLoWord(this.emptyAWord, new ConsLoWord(this.hello, this.empty))));

  // Examples of unsorted and sorted ILoWord consist of active & inactive elements
  // with duplicates and many empty active & inactive strings
  ILoWord listh = new ConsLoWord(this.decor, new ConsLoWord(this.khoury, 
      new ConsLoWord(this.anh, new ConsLoWord(this.emptyAWord,
          new ConsLoWord(this.cafe, new ConsLoWord(this.emptyIWord,
              new ConsLoWord(this.boston, new ConsLoWord(this.emptyAWord,
                  new ConsLoWord(this.anh, this.empty)))))))));

  // Examples of unsorted and sorted ILoWord consist of active & inactive elements
  ILoWord listi = new ConsLoWord(this.bin, new ConsLoWord(this.backBay, 
      new ConsLoWord(this.bean, new ConsLoWord(this.boston, this.empty))));

  ILoWord listj = new ConsLoWord(this.anh, new ConsLoWord(this.anh, this.empty));


  // examples of ZTypeWorld
  ZTypeWorld emptyWorld = new ZTypeWorld(empty, 0, 0);
  ZTypeWorld activeWorld = new ZTypeWorld(lista, 0, 0);
  ZTypeWorld inactiveWorld = new ZTypeWorld(listb, 0, 0);
  ZTypeWorld mixWorld = new ZTypeWorld(listd, 0, 0);
  
  IWord phiet = new InactiveWord("phiet", 30, 0);
  ILoWord phietList = new ConsLoWord(this.phiet, 
      new ConsLoWord(new InactiveWord("anh", 230, 0), this.empty));
  ZTypeWorld phietWorld = new ZTypeWorld(phietList, 0, 0);


  // examples of WorldScene
  WorldScene s1 = new WorldScene(400, 600)
      .placeImageXY(new RectangleImage(400, 600, "solid", Color.BLACK), 250, 250);
  WorldScene s2 = new WorldScene(400, 600)
      .placeImageXY(new RectangleImage(400, 600, "solid", Color.DARK_GRAY), 250, 250);
  WorldScene s3 = new WorldScene(400, 600)
      .placeImageXY(new RectangleImage(400, 600, "solid", Color.WHITE), 250, 250);



  // --------------------
  // ------ TESTS -------
  // -------------------- 

  //---------------------------------------------
  //--- TESTS for methods in ZTypeWorld class ---


  // METHOD makeScene()
  // MAIN METHOD & HELPER METHODS INCLUDED 

  // test makeScene() method
  boolean testMakeScene(Tester t) {

    // constants
    int SCREEN_WIDTH = 400;
    int SCREEN_HEIGHT = 600;
    // image of a rocket :D!
    OverlayOffsetImage ROCKET1 = new OverlayOffsetImage(new TriangleImage(
        new Posn(25, 0), new Posn(0, 25), new Posn(50, 25), "solid", Color.RED),
        0, 30,
        (new EllipseImage(30, 60, OutlineMode.SOLID, Color.LIGHT_GRAY)));
    OverlayOffsetImage ROCKET2 = new OverlayOffsetImage(
        new CircleImage(10, "solid", Color.BLUE), 0, 0,
        ROCKET1);
    OverlayOffsetImage ROCKET3 = new OverlayOffsetImage(new TriangleImage(
        new Posn(15, 0), new Posn(0, 15), new Posn(15, 15),
        "solid", Color.RED), 15, -30, ROCKET2);
    // final rocket image
    OverlayOffsetImage ROCKET4 = new OverlayOffsetImage(new TriangleImage(
        new Posn(-15, 0), new Posn(-15, 15), new Posn(0, 15),
        "solid", Color.RED), -15, -30, ROCKET3);

    WorldScene expectedScene = 
        new WorldScene(400, 600)
        .placeImageXY(
            new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, OutlineMode.SOLID, Color.BLACK), 
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)
        .placeImageXY(new StarImage(15, OutlineMode.SOLID, Color.YELLOW), 30, 30)
        .placeImageXY(new CircleImage(20, OutlineMode.SOLID, Color.YELLOW), 360, 30)
        .placeImageXY(
            new TextImage(
                "Score: " + Integer.toString(0), 20.0, 
                FontStyle.BOLD, Color.WHITE), SCREEN_WIDTH / 2, 30)
        .placeImageXY(new RectangleImage(SCREEN_WIDTH, 100, OutlineMode.SOLID, Color.BLUE), 
            SCREEN_WIDTH / 2, SCREEN_HEIGHT)
        .placeImageXY(ROCKET4, 200, 550);

    return
        // test in an empty world state
        t.checkExpect(emptyWorld.makeScene(), expectedScene)
        &&
        // test makeScene() method in a world with an active words list
        t.checkExpect(activeWorld.makeScene(), 
            expectedScene
            .placeImageXY(new TextImage("anh", 20.0, FontStyle.BOLD, Color.GREEN), 2, 3)
            .placeImageXY(new TextImage("active", 20.0, FontStyle.BOLD, Color.GREEN), 0, 0))
        &&
        // test in a world with inactive words list
        t.checkExpect(inactiveWorld.makeScene(), expectedScene
            .placeImageXY(new TextImage("bean", 20.0, FontStyle.BOLD, Color.RED), 4, 5)
            .placeImageXY(new TextImage("inactive", 20.0, FontStyle.BOLD, Color.RED), 0, 0))
        &&
        // test in a world with active and inactive words list
        t.checkExpect(mixWorld.makeScene(), expectedScene
            .placeImageXY(new TextImage("annie", 20.0, FontStyle.BOLD, Color.RED), 3, 2)
            .placeImageXY(new TextImage("chin", 20.0, FontStyle.BOLD, Color.GREEN), 6, 4)
            .placeImageXY(new TextImage("bean", 20.0, FontStyle.BOLD, Color.RED), 4, 5)
            .placeImageXY(new TextImage("inactive", 20.0, FontStyle.BOLD, Color.RED), 0, 0));   
  }

  //--------------------
  // METHOD randomGenerator()
  // MAIN METHOD & HELPER METHODS INCLUDED 

  // test for randomGenerator() method
  // pass in a random seed to test the method
  boolean testRandomGenerator(Tester t) {
    Utils u = new Utils();
    Random randomSeed1 = new Random(12345);
    String randomWord1 = u.randomGenerator(randomSeed1);
    String expectedWord1 = "ayfhqf";

    Random randomSeed2 = new Random(230305);
    String randomWord2 = u.randomGenerator(randomSeed2);
    String expectedWord2 = "qntkxj";

    Random randomSeed3 = new Random(160224);
    String randomWord3 = u.randomGenerator(randomSeed3);
    String expectedWord3 = "kuqjbz";

    return 
        // test if randomGenerator() produces string of length 6 of random letters
        t.checkExpect(randomWord1, expectedWord1)
        &&
        // test if randomGenerator() produces string of length 6 of random letters
        t.checkExpect(randomWord2, expectedWord2)
        && 
        // test if randomGenerator() produces string of length 6 of random letters
        t.checkExpect(randomWord3, expectedWord3);
  }

  // test for stringProducer() 
  // helper method for randomGenerator()
  boolean testStringProducer(Tester t) {
    Utils u = new Utils();
    Random randomSeed4 = new Random(12345);
    String randomWord4 = u.stringProducer(5, randomSeed4);
    String expectedWord4 = "ayfhq";

    Random randomSeed5 = new Random(160224);
    String randomWord5 = u.stringProducer(3, randomSeed5);
    String expectedWord5 = "kuq";

    Random randomSeed6 = new Random(230305);
    String randomWord6 = u.stringProducer(1, randomSeed6);
    String expectedWord6 = "q";

    Random randomSeed7 = new Random(22222);
    String randomWord7 = u.stringProducer(2, randomSeed7);
    String expectedWord7 = "iu";

    Random randomSeed8 = new Random(444444);
    String randomWord8 = u.stringProducer(4, randomSeed8);
    String expectedWord8 = "qujb";

    Random randomSeed9 = new Random(666666);
    String randomWord9 = u.stringProducer(6, randomSeed9);
    String expectedWord9 = "xnypjq";

    return 
        // test if produce string of length 1 of random letters
        t.checkExpect(randomWord6, expectedWord6)
        &&
        // test if produce string of length 2 of random letters
        t.checkExpect(randomWord7, expectedWord7)
        &&
        // test if produce string of length 3 of random letters
        t.checkExpect(randomWord5, expectedWord5)
        && 
        // test if produce string of length 4 of random letters
        t.checkExpect(randomWord8, expectedWord8)
        &&
        // test if produce string of length 5 of random letters
        t.checkExpect(randomWord4, expectedWord4)
        &&
        // test if produce string of length 6 of random letters
        t.checkExpect(randomWord9, expectedWord9);
  }

  // test if randomRestrict() generate a random number within restricted range
  boolean testRandomRestrict(Tester t) {
    Utils u = new Utils();

    Random randomSeed1 = new Random(12345);
    int test1 = u.randomRestrict(randomSeed1);
    int result1 = 116;

    Random randomSeed2 = new Random(230305);
    int test2 = u.randomRestrict(randomSeed2);
    int result2 = 259;

    Random randomSeed3 = new Random(220224);
    int test3 = u.randomRestrict(randomSeed3);
    int result3 = 164;

    Random randomSeed4 = new Random(99999);
    int test4 = u.randomRestrict(randomSeed4);
    int result4 = 211;

    return 
        // return 116, which is within the range
        t.checkExpect(test1, result1)
        &&
        // return 259, which is within the range
        t.checkExpect(test2, result2)
        &&
        // return 164, which is within the range
        t.checkExpect(test3, result3)
        &&
        // return 211, which is within the range
        t.checkExpect(test4, result4);      
  }

  //--------------------
  // METHOD makeAFinalScene()
  // MAIN METHOD & HELPER METHODS INCLUDED 

  // make a final scene to display when the world ends
  boolean testMakeAFinalScene(Tester t) {
    // constants
    int SCREEN_WIDTH = 400;
    int SCREEN_HEIGHT = 600;
    // expected final scene
    WorldScene finalScene = new WorldScene(400, 600)
        .placeImageXY(new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, 
            OutlineMode.SOLID, Color.BLACK), SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)
        .placeImageXY(new StarImage(25, OutlineMode.SOLID, Color.YELLOW), SCREEN_WIDTH / 2, 240)
        .placeImageXY(new TextImage("Try again next time!", 22.0, FontStyle.BOLD, Color.GREEN), 
            SCREEN_WIDTH / 2, 340)
        .placeImageXY(new TextImage("Game Over", 30.0, FontStyle.BOLD, Color.RED), 
            SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)
        .placeImageXY(
            new TextImage(
                "Score: " + Integer.toString(0), 20.0, 
                FontStyle.BOLD, Color.WHITE), SCREEN_WIDTH / 2, 380);

    return
        // make a final scene in an empty world
        t.checkExpect(emptyWorld.makeAFinalScene(), finalScene)
        &&
        // make a final scene in a world with only active words
        t.checkExpect(activeWorld.makeAFinalScene(), finalScene)
        &&
        // make a final scene in a world with only inactive words
        t.checkExpect(inactiveWorld.makeAFinalScene(), finalScene)
        &&
        // make a final scene in a world with both inactive and active words
        t.checkExpect(mixWorld.makeAFinalScene(), finalScene);
  }

  //--------------------
  // METHOD onTickForTesting()
  // MAIN METHOD & HELPER METHODS INCLUDED

  // check onTick() with random seed
  boolean testOnTickWithSeededRandom(Tester t) {
    // example expected world to test onTick() method
    Random randomSeed1 = new Random(12345);
    ZTypeWorld testOnTickWorld1 = 
        new ZTypeWorld(
            new ConsLoWord(new InactiveWord("ayfhqf", 267, 5), new MtLoWord()), 1, 0);

    // example expected world to test onTick() method
    ZTypeWorld testOnTickWorld2 =         
        new ZTypeWorld(
            new ConsLoWord(new InactiveWord("ayfhqf", 267, 10), new MtLoWord()), 2, 0); 

    // example expected world to test onTick() method
    ZTypeWorld testOnTickWorld9 =         
        new ZTypeWorld(
            new ConsLoWord(new InactiveWord("ayfhqf", 267, 45), new MtLoWord()), 9, 0);

    // example expected world to test onTick() method
    Random randomSeed10 = new Random(123456789);
    ZTypeWorld testOnTickWorld10 =         
        new ZTypeWorld(
            new ConsLoWord(new InactiveWord("ayfhqf", 267, 50), new MtLoWord()), 10, 0);

    return 
        // test onTick() method on an empty world
        t.checkExpect(emptyWorld.onTickForTesting(randomSeed1), testOnTickWorld1)
        &&
        // test onTick() method on a world 
        // you can see the counter going up with y-coordinate being incremented by 5
        t.checkExpect(testOnTickWorld1.onTickForTesting(randomSeed1), testOnTickWorld2)
        &&
        // test onTick() method on a world
        // you can see the counter going up with y-coordinate being incremented by 5
        t.checkExpect(testOnTickWorld9.onTickForTesting(randomSeed10), testOnTickWorld10);
  }

  // test helper method move() - move the words down the y-axis
  // both ILoWord & IWord interfaces
  boolean testMove(Tester t) {
    return 
        // test if method move() move an empty active word
        t.checkExpect(emptyAWord.move(), new ActiveWord("", 0, 5))
        &&        
        // test if method move() move an empty inactive word
        t.checkExpect(emptyIWord.move(), new InactiveWord("", 0, 5))
        &&
        // test if method move() move an active word
        t.checkExpect(anh.move(), new ActiveWord("anh", 2, 8))
        &&        
        // test if method move() move an inactive word
        t.checkExpect(annie.move(), new InactiveWord("annie", 3, 7))
        &&   
        // test if method move() move an empty list of word
        t.checkExpect(this.empty.move(), this.empty)
        &&
        // test if method move() move a list of only active words
        t.checkExpect(this.lista.move(), 
            new ConsLoWord(new ActiveWord("anh", 2, 8), 
                new ConsLoWord(new ActiveWord("active", 0, 5), empty)))
        &&
        // test if method move() move a list of only inactive words
        t.checkExpect(listb.move(),
            new ConsLoWord(new InactiveWord("bean", 4, 10), 
                new ConsLoWord(new InactiveWord("inactive", 0, 5), empty)))
        &&
        // test if method move() move a list of both active and inactive words
        t.checkExpect(listh.move(), 
            new ConsLoWord(new ActiveWord("decor", 6, 7), 
                new ConsLoWord(new InactiveWord("khoury", 3, 8),
                    new ConsLoWord(new ActiveWord("anh", 2, 8),
                        new ConsLoWord(new ActiveWord("", 0, 5), 
                            new ConsLoWord(new ActiveWord("cafe", 5, 10), 
                                new ConsLoWord(new InactiveWord("", 0, 5), 
                                    new ConsLoWord(new ActiveWord("boston", 4, 13),
                                        new ConsLoWord(new ActiveWord("", 0, 5),
                                            new ConsLoWord(new ActiveWord("anh", 2, 8), 
                                                empty))))))))));
  }

  //--------------------
  // METHOD onKeyEvent()
  // MAIN METHOD & HELPER METHODS INCLUDED 

  // test for onKeyEvent() method
  // add a key event to check and reduce an active word or turn an inactive word 
  // into an active word
  boolean testOnKeyEvent(Tester t) {
    ZTypeWorld world1 = 
        new ZTypeWorld(new ConsLoWord(new ActiveWord("test", 0, 0), empty), 0, 0);
    ZTypeWorld result1 = 
        new ZTypeWorld(new ConsLoWord(new ActiveWord("est", 0, 0), empty), 1, 10);

    ZTypeWorld world2 = 
        new ZTypeWorld(new ConsLoWord(new ActiveWord("test", 0, 0), empty), 0, 0);
    ZTypeWorld result2 = 
        new ZTypeWorld(new ConsLoWord(new ActiveWord("test", 0, 0), empty), 1, 10);

    ZTypeWorld world3 = new ZTypeWorld(new MtLoWord(), 0, 0);
    ZTypeWorld result3 = new ZTypeWorld(new MtLoWord(), 1, 0);

    ZTypeWorld world4 = 
        new ZTypeWorld(new ConsLoWord(new InactiveWord("test", 0, 0), empty), 0, 0);
    ZTypeWorld result4 = 
        new ZTypeWorld(new ConsLoWord(new InactiveWord("test", 0, 0), empty), 1, 0);

    ZTypeWorld world5 = 
        new ZTypeWorld(new ConsLoWord(new InactiveWord("annie", 3, 2), empty), 0, 0);
    ZTypeWorld result5 = 
        new ZTypeWorld(new ConsLoWord(new ActiveWord("nnie", 3, 2), empty), 1, 0);

    ZTypeWorld world6 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("test", 0, 0),
                new ConsLoWord(new InactiveWord("example", 0, 0), empty)), 0, 0);
    ZTypeWorld result6 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("est", 0, 0),
                new ConsLoWord(new InactiveWord("example", 0, 0), empty)), 1, 10);

    ZTypeWorld world7 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("test", 0, 0),
                new ConsLoWord(new ActiveWord("example", 0, 0), empty)), 0, 0);
    ZTypeWorld result7 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("est", 0, 0),
                new ConsLoWord(new ActiveWord("example", 0, 0), empty)), 1, 10);

    ZTypeWorld world8 = 
        new ZTypeWorld(
            new ConsLoWord(new InactiveWord("test", 0, 0),
                new ConsLoWord(new ActiveWord("example", 0, 0), empty)), 0, 0);
    ZTypeWorld result8 = 
        new ZTypeWorld(
            new ConsLoWord(new InactiveWord("test", 0, 0),
                new ConsLoWord(new ActiveWord("xample", 0, 0), empty)), 1, 10);

    ZTypeWorld world9 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("anh", 0, 0),
                new ConsLoWord(new InactiveWord("annie", 0, 0),
                    new ConsLoWord(new ActiveWord("abc", 0, 0), empty))), 0, 0);
    ZTypeWorld result9 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("nh", 0, 0),
                new ConsLoWord(new InactiveWord("annie", 0, 0),
                    new ConsLoWord(new ActiveWord("bc", 0, 0), empty))), 1, 10);


    return 
        // test onKeyEvent() on a world with an active word that matches the string given
        t.checkExpect(world1.onKeyEvent("t"), result1)
        &&
        // test onKeyEvent() on a world with an active word that doesn't match the string given
        t.checkExpect(world2.onKeyEvent("x"), result2)
        &&
        // test onKeyEvent() on a world with an active word given an empty string
        t.checkExpect(world2.onKeyEvent(" "), result2)
        &&
        // test onKeyEvent() on an empty world that doesn't match the string given
        t.checkExpect(world3.onKeyEvent("a"), result3)
        &&
        // test onKeyEvent() on a world with an inactive word that doesn't match the given string
        t.checkExpect(world4.onKeyEvent("a"), result4)
        &&
        // test onKeyEvent() on a world with an inactive word given an empty string
        t.checkExpect(world4.onKeyEvent(" "), result4)
        &&
        // test onKeyEvent() - case-sensitivity 
        t.checkExpect(world4.onKeyEvent("A"), result4)
        &&
        // test onKeyEvent() with special characters
        t.checkExpect(world4.onKeyEvent("!"), result4)
        &&
        // test onKeyEvent() with string of not length 1
        t.checkExpect(world4.onKeyEvent("apple"), result4)
        &&
        // test onKeyEvent() on a world with an inactive word that matches the given string
        t.checkExpect(world5.onKeyEvent("a"), result5)
        &&
        // test onKeyEvent() on a world with mixed of active & inactive word 
        // that matches the given string
        t.checkExpect(world6.onKeyEvent("t"), result6)
        &&
        // test onKeyEvent() on a world with multiple active words 
        // but only 1 matches the given string
        t.checkExpect(world7.onKeyEvent("t"), result7)
        &&
        // test onKeyEvent() on a world with an inactive word followed by an active words 
        // but only the active word matches the given string
        t.checkExpect(world8.onKeyEvent("e"), result8)
        &&
        // test onKeyEvent() on a world with mixed of active and inactive words with the same
        // starting letter
        t.checkExpect(world9.onKeyEvent("a"), result9);
  }

  //--------------------
  // METHOD worldEnds()
  // MAIN METHOD & HELPER METHODS INCLUDED 

  // test for worldEnds() method
  // add a key event to check and reduce an active word or turn an inactive word 
  // into an active word

  boolean testWorldEnds(Tester t) {

    ZTypeWorld world1 = new ZTypeWorld(new MtLoWord(), 0, 0); // Empty world
    WorldEnd expectedEnd = 
        new WorldEnd(false, world1.makeScene());

    ZTypeWorld world2 = new ZTypeWorld(lista, 230, 0); 
    WorldEnd expectedEnd2 = 
        new WorldEnd(false, world2.makeScene());

    ZTypeWorld world3 = 
        new ZTypeWorld(new ConsLoWord(new InactiveWord("test", 0, 600), empty), 100, 0); 
    WorldEnd expectedEnd3 = 
        new WorldEnd(true, world3.makeAFinalScene());

    ZTypeWorld world4 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("test", 0, 599),
                new ConsLoWord(new InactiveWord("example", 0, 599), empty)), 0, 0);
    WorldEnd expectedEnd4 = 
        new WorldEnd(false, world4.makeScene());

    ZTypeWorld world5 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("test", 0, 600),
                new ConsLoWord(new InactiveWord("example", 0, 599), empty)), 0, 0);
    WorldEnd expectedEnd5 = 
        new WorldEnd(true, world5.makeAFinalScene());

    ZTypeWorld world6 = 
        new ZTypeWorld(
            new ConsLoWord(new ActiveWord("test", 0, 700),
                new ConsLoWord(new InactiveWord("example", 0, 599), empty)), 0, 0);
    WorldEnd expectedEnd6 = 
        new WorldEnd(true, world6.makeAFinalScene());

    return
        // test if the world has ended with an empty list
        t.checkExpect(world1.worldEnds(), expectedEnd)
        &&
        // test if the world has ended with a list but 
        // no word has touched the bottom yet
        t.checkExpect(world2.worldEnds(), expectedEnd2)
        &&
        // test if the world has ended with a list with 1 word touches the bottom
        t.checkExpect(world3.worldEnds(), expectedEnd3)
        &&
        // test if the world has ended with a list with no word touches the bottom
        t.checkExpect(world4.worldEnds(), expectedEnd4)
        &&
        // test if the world has ended with a list with no word touches the bottom
        t.checkExpect(world5.worldEnds(), expectedEnd5)
        &&
        // test if the world has ended with a list with no word touches the bottom
        t.checkExpect(world6.worldEnds(), expectedEnd6);  
  }

  //----------------------------------------------
  //--- TESTS for methods in ILoWord interface ---

  //--------------------
  // METHOD checkAndReduce(String str)
  // MAIN METHOD & HELPER METHODS INCLUDED

  // takes in a String of length 1 and produces an ILoWord where any active words
  // in this ILoWord are reduced by removing the first letter only 
  // if the given string matches the first letter.
  boolean testCheckAndReduce(Tester t) { 
    return
        // test if checkAndReduce(String str) works on empty list
        t.checkExpect(this.empty.checkAndReduce(""), this.empty)
        &&
        // test if checkAndReduce(String str) works on empty list
        t.checkExpect(this.empty.checkAndReduce("a"), this.empty)
        &&
        // test if checkAndReduce(String str) works on a list with 2 active components
        t.checkExpect(this.lista.checkAndReduce("a"), 
            new ConsLoWord(new ActiveWord("nh", 2, 3), 
                new ConsLoWord(new ActiveWord("ctive", 0, 0), this.empty)))
        &&
        // test if checkAndReduce(String str) works on a list with 2 inactive components
        t.checkExpect(this.listb.checkAndReduce("b"), this.listb)
        &&
        // test if checkAndReduce(String str) works on a list with multiple active components
        t.checkExpect(this.listc.checkAndReduce("c"), 
            new ConsLoWord(new ActiveWord("afe", 5, 5), new ConsLoWord(new ActiveWord("hin", 6, 4), 
                new ConsLoWord(this.anh, new ConsLoWord(this.active, this.empty)))))
        &&
        // test if checkAndReduce(String str) works on a list with multiple inactive components
        t.checkExpect(this.listd.checkAndReduce("a"), this.listd)
        && 
        // test if checkAndReduce(String str) works on a list with 1 empty word
        t.checkExpect(this.listg.checkAndReduce("h"), 
            new ConsLoWord(this.bean, new ConsLoWord(this.cafe, 
                new ConsLoWord(this.emptyAWord, new ConsLoWord(new ActiveWord("ello", 1, 1), 
                    this.empty)))))
        &&
        // test if checkAndReduce(String str) works on a list with mix of inactive
        // and active components
        t.checkExpect(this.listi.checkAndReduce("b"),
            new ConsLoWord(this.bin, new ConsLoWord(this.backBay,
                new ConsLoWord(this.bean, new ConsLoWord(new ActiveWord("oston", 4, 8), 
                    this.empty)))))
        &&
        // test if checkAndReduce(String str) works on a list with mix of inactive
        // and active components
        t.checkExpect(this.liste.checkAndReduce("z"), this.liste)
        &&
        // test if checkAndReduce(String str) works on a list with mix of inactive 
        // and active component by searching an upper-case string of length 1
        t.checkExpect(this.listi.checkAndReduce("B"),
            new ConsLoWord(this.bin, new ConsLoWord(new ActiveWord("ACK BAY", 6, 0),
                new ConsLoWord(this.bean, new ConsLoWord(this.boston, 
                    this.empty)))));     
  }

  // helper method check() for checkAndReduce() - check if the first letter of an IWord matches
  // the string of length 1 being searched
  // in ILoWord & IWord interface
  boolean testCheck(Tester t) {
    return
        // ILoWord interface
        // check() in an empty list with a string
        t.checkExpect(empty.check("a"), false)
        &&
        // check() in an empty list with a string
        t.checkExpect(empty.check(""), false)
        &&
        // check() in an active list with a string
        t.checkExpect(lista.check("a"), true)
        &&
        // check() in an active list with a string - case sensitivity
        t.checkExpect(lista.check("A"), false)
        &&
        // check() in an active list with a string
        t.checkExpect(lista.check("z"), false)
        &&
        // check() in an inactive list with a string
        t.checkExpect(listb.check("a"), false)
        &&
        // check() in an inactive list with a string
        t.checkExpect(listb.check("b"), true)
        &&
        // check() in a mixed list with a string
        t.checkExpect(listh.check("k"), false)
        &&

        // IWord 
        // test check() in IWord for an active word with a string that matches
        t.checkExpect(this.anh.check("a"), true)
        &&
        // test check() in IWord for an active word with a string that doens't match
        t.checkExpect(this.anh.check("A"), false)
        &&
        // check() in a mixed list with a string
        t.checkExpect(annie.check("a"), false)
        &&
        // check() in a mixed list with a string
        t.checkExpect(annie.check("A"), false)
        &&
        // check() in a mixed list with a string
        t.checkExpect(anh.check(""), false)
        &&
        // check() in a mixed list with a string
        t.checkExpect(annie.check(""), false)
        &&
        // check() in a mixed list with a string
        t.checkExpect(emptyAWord.check(""), false)
        &&
        // check() in a mixed list with a string
        t.checkExpect(emptyIWord.check(""), false)
        &&
        // check() in a mixed list with a string
        t.checkExpect(emptyAWord.check("s"), false)
        &&
        // check() in a mixed list with a string
        t.checkExpect(emptyIWord.check("s"), false);
  }

  // helper method cut() for checkAndReduce - cut the string of an IWord correctly
  boolean testCut(Tester t) { 
    return
        // test if helper method cut the string of an active word correctly
        t.checkExpect(this.anh.cut(), this.anhCut)
        &&
        // test if helper method cut the string of an inactive word correctly
        t.checkExpect(this.annie.cut(), this.annie)
        &&
        // test if helper method cut the string of an active word correctly
        t.checkExpect(this.backBay.cut(), this.backBayCut);    
  }

  //--------------------
  // METHOD filterOutEmpties()
  // MAIN METHOD & HELPER METHODS INCLUDED

  // produces an ILoWord with any IWords that have an empty string are filtered out.
  boolean testFilterOutEmpties(Tester t) { 
    return
        // test if filterOutEmpties() works on an empty list
        t.checkExpect(this.empty.filterOutEmpties(), this.empty)
        &&
        // test if filterOutEmpties() works on a list with only active elements
        // with no empty string
        t.checkExpect(this.lista.filterOutEmpties(), this.lista)
        &&
        // test if filterOutEmpties() works on a list with only inactive elements
        // with no empty string
        t.checkExpect(this.listd.filterOutEmpties(), this.listd)
        &&
        // test if filterOutEmpties() works on a list with both active 
        // and inactive elements with 1 empty string
        t.checkExpect(this.listg.filterOutEmpties(), 
            new ConsLoWord(this.bean, new ConsLoWord(this.cafe,
                new ConsLoWord(this.hello, this.empty))))
        &&
        // test if filterOutEmpties() works on a list with both active 
        // and inactive elements with many empty strings
        t.checkExpect(this.listh.filterOutEmpties(), 
            new ConsLoWord(this.decor, new ConsLoWord(this.khoury,
                new ConsLoWord(this.anh, new ConsLoWord(this.cafe,
                    new ConsLoWord(this.boston, new ConsLoWord(this.anh, this.empty)))))));
  }

  // test helper method checkEmpty() for filterOutEmpties() - check if an IWord is empty
  boolean testCheckEmpty(Tester t) {
    return
        // test if an empty active word is empty
        t.checkExpect(this.emptyAWord.checkEmpty(), true)
        &&
        // test if an empty inactive word is empty
        t.checkExpect(this.emptyIWord.checkEmpty(), true)
        &&
        // test if an non-empty active word is empty
        t.checkExpect(this.anh.checkEmpty(), false)
        &&
        // test if an non-empty inactive word is empty
        t.checkExpect(this.chin.checkEmpty(), false);
  }

  // test to check the first string of the word
  boolean testCheckFirst(Tester t) {
    return 
        // check the first word of an empty inactive word
        t.checkExpect(emptyIWord.checkFirst(""), true)
        &&
        // check the first word of an empty active word
        t.checkExpect(emptyAWord.check(""), false)
        &&
        // check the first of an empty inactive word with string of length 1
        t.checkExpect(emptyIWord.check("a"), false)
        &&
        // check the first word of an inactive word
        t.checkExpect(this.inactive.checkFirst(""), true)
        &&
        // check the first word of an inactive word
        t.checkExpect(this.inactive.checkFirst("i"), true)
        && 
        // check the first word of an active word
        t.checkExpect(this.active.checkFirst("c"), false)
        &&
        // check the first word of an active word
        t.checkExpect(this.anh.checkFirst("a"), true)
        &&
        // check the first word of an active word
        t.checkExpect(this.anh.checkFirst("A"), false)
        &&
        // check the first word of an active word
        t.checkExpect(this.anh.checkFirst("b"), false);
  }

  // test the helper method compareY();
  boolean testCompareY(Tester t) {
    return
        // check if y-coordinate of an active word is larger than 3
        t.checkExpect(this.cafe.compareY(3), true)
        && 
        // check if y-coordinate of an inactive word is larger than 6
        t.checkExpect(this.chin.compareY(6), false)
        && 
        // check if y-coordinate of an active word is larger than 10
        t.checkExpect(this.anh.compareY(10), false);
  }

  //--------------------
  // METHOD draw()
  // MAIN METHOD & HELPER METHODS INCLUDED

  // takes in a WorldScene and draws all of the words in this ILoWord onto the given WorldScene. 
  boolean testDraw(Tester t) {
    WorldScene s1 = new WorldScene(500, 500)
        .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250);
    return 
        // c1.drawScene(aListOfWords.draw(s1)) && c1.show();
        // &&
        // test if draw() method draws an empty list
        t.checkExpect(empty.draw(s1), empty.draw(s1))
        &&
        // test if draw() method draws a list with 1 active word 
        t.checkExpect(activeList.draw(s1), activeList.draw(s1))
        &&
        // test if draw() method draws a list with 1 inactive word
        t.checkExpect(inactiveList.draw(s1), inactiveList.draw(s1))
        &&
        // test if draw() method draws a list with all active words
        t.checkExpect(listc.draw(s1), listc.draw(s1))
        &&
        // test if draw() method draws a list with all inactive words
        t.checkExpect(listd.draw(s1), listd.draw(s1))
        &&
        // test if draw() method draws a list with mix of active & inactive words
        t.checkExpect(listi.draw(s1), listi.draw(s1));
  }

  // HELPER METHOD for draw() method - turns an IWord into a WorldScene
  boolean testDrawOnWS(Tester t) {
    WorldScene s1 = new WorldScene(500, 500)
        .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250);
    return 
        // test if drawOnWS(ws) draw an empty active word
        t.checkExpect(
            this.emptyAWord.drawOnWS(s1), 
            new WorldScene(500, 500)
            .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250)
            .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250)
            .placeImageXY(new TextImage("", 20.0, FontStyle.BOLD, Color.GREEN), 0, 0))
        &&
        // test if drawOnWS(ws) draw an empty inactive word
        t.checkExpect(
            this.emptyIWord.drawOnWS(s1), 
            new WorldScene(500, 500)
            .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250)
            .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250)
            .placeImageXY(new TextImage("", 20.0, FontStyle.BOLD, Color.RED), 0, 0))
        &&
        // check if drawOnWS(ws) draw an active word
        t.checkExpect(
            this.anh.drawOnWS(s1), 
            new WorldScene(500, 500)
            .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250)
            .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250)
            .placeImageXY(new TextImage("anh", 20.0, FontStyle.BOLD, Color.GREEN), 2, 3))
        &&
        // check if drawOnWS(ws) draw an active word
        t.checkExpect(this.annie.drawOnWS(s1), 
            new WorldScene(500, 500)
            .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250)
            .placeImageXY(new RectangleImage(500, 500, "solid", Color.BLACK), 250, 250)
            .placeImageXY(new TextImage("annie", 20.0, FontStyle.BOLD, Color.RED), 3, 2));
  }


  //--------------------
  // METHOD anyActive()
  // HELPER METHODS

  // test the helper method isActive()
  boolean testAnyActive(Tester t) {
    return
        // test if an empty list has an active word
        t.checkExpect(new MtLoWord().anyActive(), false)
        &&
        // test if an inactive list has an active word
        t.checkExpect(inactiveList.anyActive(), false)
        &&
        // test if an active list has an active word
        t.checkExpect(activeList.anyActive(), true)
        &&
        // test if an active list has an active word
        t.checkExpect(lista.anyActive(), true)
        &&
        // test if an inactive list has an active word
        t.checkExpect(listb.anyActive(), false)
        &&
        // test if a mixed list has an active word
        t.checkExpect(listc.anyActive(), true)
        &&
        // test if a mixed list has an active word
        t.checkExpect(listd.anyActive(), true)
        &&
        // test if a mixed list has an active word
        t.checkExpect(liste.anyActive(), true)
        &&
        // test if a mixed list has an active word
        t.checkExpect(listf.anyActive(), true)
        &&
        // test if a mixed list has an active word
        t.checkExpect(listg.anyActive(), true)
        &&
        // test if a mixed list has an active word
        t.checkExpect(listh.anyActive(), true)
        &&
        // test if a mixed list has an active word
        t.checkExpect(listi.anyActive(), true)
        &&
        // test if a mixed list has an active word
        t.checkExpect(listj.anyActive(), true);      
  }

  //--------------------
  // METHOD activate()
  // HELPER METHODS

  // test the helper method activate();
  boolean testActivate(Tester t) {
    return
        // in IWord interface
        t.checkExpect(this.anh.activate("a"), this.anh)
        && 
        t.checkExpect(this.chin.activate("h"), this.chin)
        && 
        t.checkExpect(this.annie.activate("a"), new ActiveWord("nnie", 3, 2))
        &&

        // in ILoWord interface 
        // activate an active list with string "a"
        t.checkExpect(activeList.activate("a"), activeList)
        &&
        // try to activate an active list with string "b"
        t.checkExpect(activeList.activate("b"), activeList)
        &&
        // try to activate an active list with string "A"
        t.checkExpect(activeList.activate("A"), activeList)
        &&
        // try to activate an inactive list with string "A"
        t.checkExpect(inactiveList.activate("A"), inactiveList)
        &&
        // try to activate an active list with string "A"
        t.checkExpect(inactiveList.activate("i"), 
            new ConsLoWord(new ActiveWord("nactive", 0, 0), this.empty))
        &&
        // try to activate an active list with string "A"
        t.checkExpect(lista.activate("a"), lista)
        &&
        // try to activate an active list with string "A"
        t.checkExpect(listb.activate("b"),
            new ConsLoWord(new ActiveWord("ean", 4, 5), this.inactiveList))
        &&
        // activate a mixed list with letter "b"
        t.checkExpect(listi.activate("b"), 
            new ConsLoWord(new ActiveWord("boston", 4, 8), 
                new ConsLoWord(new InactiveWord("BIN", 0, 6), 
                    new ConsLoWord(new InactiveWord("bean", 4, 5), 
                        new ConsLoWord(new ActiveWord("BACK BAY", 6, 0), this.empty)))));
  }

  //--------------------
  // METHOD isActive()
  // HELPER METHODS

  // test the helper method isActive()
  boolean testIsActive(Tester t) {
    return
        // is an active word active?
        t.checkExpect(this.anh.isActive(), true)
        && 
        // is an active word active?
        t.checkExpect(this.chin.isActive(), true)
        && 
        // is an inactive word active?
        t.checkExpect(this.annie.isActive(), false);
  }

  //--------------------
  // METHOD activateFirst()
  // HELPER METHODS

  // test the helper method activateFirst
  boolean testActivateFirst(Tester t) {
    return
        // activate an inactive list with letter i
        t.checkExpect(inactiveList.activateFirst("i"), 
            new ConsLoWord(new ActiveWord("nactive", 0, 0), this.empty))
        && 
        // activate an inactive list with letter "b"
        t.checkExpect(listb.activate("b"), 
            new ConsLoWord(new ActiveWord("ean", 4, 5), this.inactiveList))
        &&
        // activate an active list with letter a
        t.checkExpect(activeList.activateFirst("a"), activeList)
        &&
        // activate an empty list
        t.checkExpect(empty.activateFirst("a"), empty);
  }

  //--------------------
  // METHOD insert()
  // HELPER METHODS

  // test helper method insert
  boolean testInsert(Tester t) {
    return 
        t.checkExpect(this.activeList.insert(active), 
            new ConsLoWord(new ActiveWord("active", 0, 0), this.activeList))
        && 
        t.checkExpect(this.inactiveList.insert(inactive), 
            new ConsLoWord(new InactiveWord("inactive", 0, 0), this.inactiveList));
  }

  //--------------------
  // METHOD sort()
  // HELPER METHODS

  // test the helper method sort
  boolean testSort(Tester t) {
    return 
        // test sort() on active list
        // test if the sort() method sort the empty list
        t.checkExpect(this.empty.sort(), this.empty)
        &&
        // test if sort() method sort a list with 1 active component
        t.checkExpect(this.activeList.sort(), this.activeList)
        &&
        // test if sort() method sort a list with 1 inactive component
        t.checkExpect(this.inactiveList.sort(), this.inactiveList)
        &&
        // test if sort() method sort a list with only active components
        t.checkExpect(this.lista.sort(), new ConsLoWord(this.anh, 
            new ConsLoWord(this.active, this.empty)))
        &&
        // test if sort() method sort a list with only inactive components 
        // (already sorted list)
        t.checkExpect(this.listb.sort(), this.listb)
        &&
        // test if sort() method sort a list with many active components
        t.checkExpect(this.listc.sort(), 
            new ConsLoWord(this.cafe, new ConsLoWord(this.chin, 
                new ConsLoWord(this.anh, new ConsLoWord(this.active, this.empty)))))
        && 
        // test if sort() method sort a list with many inactive components
        t.checkExpect(this.listd.sort(), 
            new ConsLoWord(this.bean, new ConsLoWord(this.chin,
                new ConsLoWord(this.annie, new ConsLoWord(this.inactive, this.empty)))))
        &&
        // test if sort() method sort a list with active & inactive components
        t.checkExpect(this.liste.sort(), 
            new ConsLoWord(this.boston, new ConsLoWord(this.khoury,
                new ConsLoWord(this.anh, new ConsLoWord(this.decor,
                    new ConsLoWord(this.backBay, this.empty))))))
        &&
        // test if sort() method sort a list with active & inactive components
        t.checkExpect(this.listf.sort(), new ConsLoWord(this.bin, 
            new ConsLoWord(this.chin, new ConsLoWord(this.cap,
                new ConsLoWord(this.anh, new ConsLoWord(this.khoury, 
                    new ConsLoWord(this.decor, new ConsLoWord(this.hello, this.empty))))))))
        &&
        // test if sort() method sort a list with an empty word
        t.checkExpect(this.listg.sort(), 
            new ConsLoWord(this.cafe, new ConsLoWord(this.bean, 
                new ConsLoWord(this.hello, new ConsLoWord(this.emptyAWord, this.empty)))));
  }

  // test the helper method activateSecond
  boolean testActivateRest(Tester t) {
    return
        // test activateRest() on inactive list with string i
        t.checkExpect(this.inactiveList.activateRest("i"), 
            this.inactiveList) 
        && 
        // test activateRest() on inactive list with string b
        t.checkExpect(this.listb.activateRest("b"), 
            this.listb)
        && 
        // test activateRest() on an inactive list with string i
        t.checkExpect(this.listb.activateRest("i"), 
            new ConsLoWord(this.bean, new ConsLoWord(new ActiveWord("nactive", 0, 0), this.empty)));
  }

  //--------------------
  // METHOD gameIsOver()
  // MAIN METHOD & HELPER METHODS INCLUDED

  // test the method gameIsOver() in ILoWord
  boolean testGameIsOver(Tester t) {
    return 
        t.checkExpect(this.anh.gameIsOver(), false)
        && 
        t.checkExpect(this.chin.gameIsOver(), false)
        && 
        t.checkExpect(new ActiveWord("anh", 2, 700).gameIsOver(), true)
        &&
        t.checkExpect(this.listc.gameIsOver(), false)
        && 
        t.checkExpect(new ConsLoWord(new ActiveWord("active", 0, 700), this.empty).gameIsOver(), 
            true)
        && 
        t.checkExpect(this.lista.gameIsOver(), false);
  }

  //--------------------
  // METHOD comesFirst()
  // HELPER METHOD

  boolean testComesFirst(Tester t) {
    IWord word1 = new ActiveWord("apple", 0, 100);
    IWord word2 = new InactiveWord("banana", 0, 200);

    IWord word3 = new ActiveWord("apple", 0, 200);
    IWord word4 = new InactiveWord("banana", 0, 100);

    IWord word5 = new ActiveWord("apple", 0, 150);
    IWord word6 = new InactiveWord("banana", 0, 150);

    IWord word7 = new ActiveWord("apple", 0, -100);
    IWord word8 = new InactiveWord("banana", 0, 0);

    IWord word9 = new ActiveWord("apple", 0, 0);
    IWord word10 = new InactiveWord("banana", 0, -100);

    return
        // test if this word comes before that word
        t.checkExpect(word1.comesFirst(word2), true)
        &&
        // test if this word comes before that word
        t.checkExpect(word3.comesFirst(word4), false)
        &&
        // test if this word comes before that word
        t.checkExpect(word5.comesFirst(word6), false)
        &&
        // test if this word comes before that word
        t.checkExpect(word7.comesFirst(word8), true)
        &&
        // test if this word comes before that word
        t.checkExpect(word9.comesFirst(word10), false);  
  }


  //--------------------
  // test bigBang() method
  boolean testBigBang(Tester t) {
    ZTypeWorld world = emptyWorld;
    int worldWidth = 400;
    int worldHeight = 600;
    double tickRate = 0.15;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }
}

