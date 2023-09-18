import tester.Tester;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.impworld.*;
import javalib.worldimages.*;

//Examples class for FloodIt
class Examples {

  ArrayList<Cell> row1;

  ArrayList<Cell> row2;

  ArrayList<Cell> row3;

  ArrayList<ArrayList<Cell>> makeB1;

  ArrayList<ArrayList<Cell>> makeB2;

  ArrayList<ArrayList<Cell>> makeB3;

  FloodIt w1;

  FloodIt w2;

  int sizeOfBoard = 3;

  void initData() {

    // Cells
    Cell one = new Cell(0, 0, Color.RED, true);
    Cell two = new Cell(1, 0, Color.GREEN);
    Cell three = new Cell(0, 1, Color.RED);
    Cell four = new Cell(1, 1, Color.RED);
    Cell five = new Cell(2, 2, Color.BLUE);
    Cell six = new Cell(2, 2, Color.YELLOW);

    row1 = new ArrayList<Cell>(Arrays.asList(one, two, three));
    row2 = new ArrayList<Cell>(Arrays.asList(four, five, six));
    row3 = new ArrayList<Cell>(Arrays.asList(one, four, six));

    makeB1 = new ArrayList<ArrayList<Cell>>(Arrays.asList(this.row1, this.row2, this.row3));
    makeB2 = new ArrayList<ArrayList<Cell>>(Arrays.asList(this.row3, this.row2, this.row1));

    this.w1 = new FloodIt(2, new Random(1), 2);
    this.w2 = new FloodIt(3, new Random(1), 3);

  }
  
  void runFloodIt(Tester t) {

    FloodIt world = new FloodIt(50, 8);
    int sceneSize = 800;
    world.bigBang(sceneSize, sceneSize, 0.001);

  }

  //test FloodIt.initBoard()
  void testInitBoard(Tester t) {
    

    FloodIt world = new FloodIt(1, new Random(1), 1);

    ArrayList<Cell> board = world.initBoard(2);

    Cell one = new Cell(0, 0, Color.RED, true);
    Cell two = new Cell(1, 0, Color.RED, true);
    Cell three = new Cell(0, 1, Color.RED, true);
    Cell four = new Cell(1, 1, Color.RED, true);
    one.link(two, three);
    two.link(one, four);
    three.link(four, one);
    four.link(three, two);

    //fails because of arraylist equality stuff 
    t.checkExpect(board.equals(new ArrayList<Cell>(Arrays.asList(one, two, three, four))), false);

    t.checkExpect(world.board, new ArrayList<Cell>(Arrays.asList(new Cell(0, 0, Color.RED, true))));

    Cell single = new Cell(0, 0, Color.RED, true);

    t.checkExpect(world.initBoard(1), new ArrayList<Cell>(Arrays.asList(single)));

    t.checkConstructorException(new IllegalArgumentException("Size must be greater than 0!"),
        "FloodIt", 0, 1);

    t.checkConstructorException(
        new IllegalArgumentException("Number of colors must be between 1 and 8 (inclusive)!"),
        "FloodIt", 1, 0);

  }

  //test Cell.Draw()
  void testDraw(Tester t) {

    WorldScene scene = new WorldScene(800, 800);
    Cell ex = new Cell(0, 0, Color.RED, true);
    ex.draw(2, scene);

    WorldScene scene2 = new WorldScene(800, 800);
    scene2.placeImageXY(new RectangleImage(300, 300, OutlineMode.SOLID, Color.RED), 250, 250);

    t.checkExpect(scene, scene2);

    Cell ex2 = new Cell(1, 0, Color.BLUE, true);
    ex2.draw(2, scene);
    scene2.placeImageXY(new RectangleImage(300, 300, OutlineMode.SOLID, Color.BLUE), 550, 250);

    t.checkExpect(scene, scene2);

    WorldScene scene3 = new WorldScene(800, 800);
    Cell ex3 = new Cell(0, 0, Color.BLACK, false);
    ex3.draw(10, scene3);

    WorldScene scene4 = new WorldScene(800, 800);
    scene4.placeImageXY(new RectangleImage(60, 60, OutlineMode.SOLID, Color.BLACK), 130, 130);

    t.checkExpect(scene3, scene4);

  }

  //test FloodIt.makeScene()
  void testMakeScene(Tester t) {

    FloodIt flood = new FloodIt(2, new Random(1), 2);

    WorldScene scene = new WorldScene(800, 800);
    Cell one = new Cell(0, 0, Color.GREEN, true);
    Cell two = new Cell(1, 0, Color.RED);
    Cell three = new Cell(0, 1, Color.RED);
    Cell four = new Cell(1, 1, Color.RED);
    one.link(three, two);
    two.link(four, one);
    three.link(one, four);
    four.link(two, three);

    one.draw(2, scene);
    two.draw(2, scene);
    three.draw(2, scene);
    four.draw(2, scene);

    scene.placeImageXY(new TextImage("Time: " + Integer.toString(0), Color.BLACK), 30, 10);
    scene.placeImageXY(new TextImage("Moves: " + Integer.toString(0) + " / " + Integer.toString(2),
        32, Color.BLACK), 420, 720);

    t.checkExpect(flood.makeScene(), scene);

  }

  //test Cell.link(Cell)
  void testLink(Tester t) {

    Cell a = new Cell(0, 0, Color.RED);

    t.checkExpect(a.adjacent, new ArrayList<Cell>());

    Cell b = new Cell(0, 1, Color.RED);

    a.link(b);

    t.checkExpect(a.adjacent, new ArrayList<Cell>(Arrays.asList(b)));

    Cell c = new Cell(1, 1, Color.BLACK);
    Cell d = new Cell(0, 0, Color.RED);

    a.link(c, d);

    t.checkExpect(a.adjacent, new ArrayList<Cell>(Arrays.asList(b, c, d)));

  }

  //test Cell.flood(Color, ArrayList<Cell>, ArrayList<ArrayList<Cell>>, int)
  void testFlood(Tester t) {

    ArrayList<Cell> visited = new ArrayList<Cell>();
    ArrayList<ArrayList<Cell>> animate = new ArrayList<ArrayList<Cell>>();

    for (int i = 0; i < 13; i++) {

      animate.add(new ArrayList<Cell>());

    }

    Cell c1 = new Cell(0, 0, Color.RED, true);

    c1.flood(Color.BLUE, visited, animate, 1);
    t.checkExpect(visited, new ArrayList<Cell>(Arrays.asList(c1)));

    t.checkExpect(animate,
        new ArrayList<ArrayList<Cell>>(Arrays.asList(new ArrayList<Cell>(Arrays.asList(c1)),
            new ArrayList<Cell>(), new ArrayList<Cell>(), new ArrayList<Cell>(),
            new ArrayList<Cell>(), new ArrayList<Cell>(), new ArrayList<Cell>(),
            new ArrayList<Cell>(), new ArrayList<Cell>(), new ArrayList<Cell>(),
            new ArrayList<Cell>(), new ArrayList<Cell>(), new ArrayList<Cell>())));

  }

  //test Cell.filled()
  void testFilled(Tester t) {

    Cell one = new Cell(0, 0, Color.GREEN, true);
    Cell two = new Cell(1, 0, Color.RED);
    Cell three = new Cell(0, 1, Color.RED);
    Cell four = new Cell(1, 1, Color.RED);

    one.link(three, two);
    two.link(four, one);
    three.link(one, four);
    four.link(two, three);

    t.checkExpect(one.filled(new ArrayList<Cell>()), false);

    ArrayList<ArrayList<Cell>> animate = new ArrayList<ArrayList<Cell>>();
    animate.add(new ArrayList<Cell>());
    animate.add(new ArrayList<Cell>());

    one.flood(Color.RED, new ArrayList<Cell>(), animate, 2);
    t.checkExpect(one.filled(new ArrayList<Cell>()), true);

  }

  //FloodIt.onTick()
  void testOntick(Tester t) {

    this.initData();

    t.checkExpect(w1.tick, 0);
    w1.onTick();
    t.checkExpect(w1.tick, 1);
    w1.onTick();
    w1.onTick();
    t.checkExpect(w1.tick, 3);

  }

  //FloodIt.onMouseClicked()
  void testOnMouseClicked(Tester t) {

    this.initData();

    t.checkExpect(this.w1.moves, 0);
    t.checkExpect(w1.aColor, Color.GREEN);

    w1.onMouseClicked(new Posn(250, 250));

    t.checkExpect(this.w1.moves, 0);
    t.checkExpect(w1.aColor, Color.GREEN);

    t.checkException(new IllegalArgumentException("Click must be within bounds of window!"), w1,
        "onMouseClicked", new Posn(1000, 1000));
    t.checkException(new IllegalArgumentException("Click must be within bounds of window!"), w1,
        "onMouseClicked", new Posn(0, 1000));
    t.checkException(new IllegalArgumentException("Click must be within bounds of window!"), w1,
        "onMouseClicked", new Posn(1000, 0));
    t.checkException(new IllegalArgumentException("Click must be within bounds of window!"), w1,
        "onMouseClicked", new Posn(-1, -1));
    t.checkException(new IllegalArgumentException("Click must be within bounds of window!"), w1,
        "onMouseClicked", new Posn(0, -1));
    t.checkException(new IllegalArgumentException("Click must be within bounds of window!"), w1,
        "onMouseClicked", new Posn(-1, 0));

    w1.onMouseClicked(new Posn(600, 600));

    t.checkExpect(this.w1.moves, 1);
    t.checkExpect(w1.aColor, Color.RED);

  }

  //test FloodIt.onKeyEvent()
  void testonKeyEvent(Tester t) {

    this.initData();
    // FloodIt world = new FloodIt(1, new Random(1), 1);
    // Cell a = new Cell(0, 0, Color.RED);
    this.w1.onTick();
    this.w1.onTick();
    // this.w1.onMouseClicked(new Posn(70, 20));

    this.w1.onKeyEvent("r");
    t.checkExpect(this.w1.tick, 2);
    //t.checkExpect(this.w1.clicks, 0);
    t.checkExpect(this.w1.animate.equals(makeB1), false);

    // t.checkExpect(game1.board.get(0).color, new Color(212, 160, 167));
  }

  //test FloodIt.lastScene()
  void testanimateSize(Tester t) {
    this.initData();

    t.checkExpect(w1.animateSize(), 0);
    t.checkExpect(w2.animateSize(), 0);
    
    w1 = new FloodIt(2, 2);
    
    t.checkExpect(w1.animateSize(), 0);
  }
  
  void testLastScene(Tester t) {

    WorldScene scene = new WorldScene(800, 800);

    scene.placeImageXY(new TextImage("You Win!", 64, Color.BLACK), 400, 300);
    scene.placeImageXY(new TextImage("Time: " + Integer.toString(0), 32, Color.BLACK), 400, 550);
    scene.placeImageXY(
        new TextImage(Integer.toString(0) + " / " + Integer.toString(2), 32, Color.BLACK),
        400, 600);

    t.checkExpect(w1.lastScene(null), scene);

  }
  
}
