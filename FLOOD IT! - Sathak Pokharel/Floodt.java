import javalib.impworld.*;

import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

//A complete game class, to be used with ImpWorld.
//keeps track of game state and dispatches calls when necessary
class FloodIt extends World {

  int moves = 0;

  int maxmoves;

  int ncolors;

  ArrayList<Cell> board;

  int size;

  Random r;

  int tick = 0;

  ArrayList<ArrayList<Cell>> animate;

  Color aColor;

  FloodIt(int size, int numcolors) {

    this(size, new Random(), numcolors);

  }

  FloodIt(int size, Random r, int numcolors) {

    if (size <= 0) {

      throw new IllegalArgumentException("Size must be greater than 0!");

    }

    if (numcolors < 1 || numcolors > 8) {

      throw new IllegalArgumentException("Number of colors must be between 1 and 8 (inclusive)!");

    }

    this.maxmoves = numcolors * size / 2;
    this.ncolors = numcolors;
    this.r = r;
    this.size = size;
    this.animate = new ArrayList<ArrayList<Cell>>();
    this.board = this.initBoard(this.size);

    for (int i = 0; i < this.size * 2 + 1; i++) {

      this.animate.add(new ArrayList<Cell>());

    }

  }

  //generate a complete game board for a certain size and set of colors
  ArrayList<Cell> initBoard(int size) {

    ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE,
        Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.PINK, Color.WHITE));

    ArrayList<Cell> temp = new ArrayList<Cell>(size * size);

    //generate cells
    for (int y = 0; y < size; y++) {

      for (int x = 0; x < size; x++) {

        if (x == 0 && y == 0) {

          temp.add(new Cell(0, 0, colors.get(this.r.nextInt(this.ncolors % 9)), true));

        } else {

          temp.add(new Cell(x, y, colors.get(this.r.nextInt(this.ncolors % 9))));

        }

      }

    }

    //link cells
    for (int y = 0; y < size; y++) {

      for (int x = 0; x < size; x++) {

        Cell current = temp.get(y * size + x);

        //left
        if (x > 0) {

          current.link(temp.get(y * size + x - 1));

        }

        //right
        if (x < size - 1) {

          current.link(temp.get(y * size + x + 1));

        }

        //bottom
        if (y < size - 1) {

          current.link(temp.get((y + 1) * size + x));

        }

        //top
        if (y > 0) {

          current.link(temp.get((y - 1) * size + x));

        }

      }

    }

    this.aColor = temp.get(0).startFlood(temp.get(0), this.animate, this.size);

    return temp;

  }

  //renders the game state visually
  public WorldScene makeScene() {

    WorldScene scene = new WorldScene(800, 800);

    for (int y = 0; y < this.size; y++) {

      for (int x = 0; x < this.size; x++) {

        this.board.get(y * this.size + x).draw(this.size, scene);

      }

    }

    scene.placeImageXY(new TextImage("Time: " + Integer.toString(this.tick / 100), Color.BLACK), 30,
        10);
    scene.placeImageXY(new TextImage(
        "Moves: " + Integer.toString(this.moves) + " / " + Integer.toString(this.maxmoves), 32,
        Color.BLACK), 420, 720);

    return scene;

  }

  //handler for tick events in the game, responsible for animation
  public void onTick() {

    this.tick++;

    if (this.board.get(0).filled(new ArrayList<Cell>())) {

      this.endOfWorld(null);

    }

    if (this.animateSize() > 0) {

      for (ArrayList<Cell> a : this.animate) {

        for (int i = 0; i < a.size(); i++) {

          a.remove(0).animate(this.aColor);

        }

      }

    }

  }

  //handler for mouse events in the game
  public void onMouseClicked(Posn pos) {

    if (pos.x > 800 || pos.y > 800 || pos.x < 0 || pos.y < 0) {

      throw new IllegalArgumentException("Click must be within bounds of window!");

    }

    int y = (pos.y - 100) / (int) (300 / this.size) / 2;
    int x = (pos.x - 100) / (int) (300 / this.size) / 2;

    if (this.animateSize() == 0) {

      if (y * size + x < size * size) {

        Color newcolor = this.board.get(y * size + x).startFlood(this.board.get(0), this.animate,
            this.size);

        if (!this.aColor.equals(newcolor)) {

          this.aColor = newcolor;
          this.moves++;

        }

      }

    }

  }

  //handler for keypresses
  public void onKeyEvent(String evt) {

    if (evt.toLowerCase().equals("r")) {

      this.board = this.initBoard(this.size);
      this.moves = 0;

    }

  }

  //Returns the final scene with the given message displayed
  public WorldScene lastScene(String msg) {

    WorldScene background = new WorldScene(800, 800);

    WorldImage gameover = new TextImage("You Lose!", 64, Color.BLACK);

    if (this.moves < this.maxmoves) {

      gameover = new TextImage("You Win!", 64, Color.BLACK);

    }

    WorldImage time = new TextImage("Time: " + Integer.toString(this.tick / 100), 32, Color.BLACK);
    WorldImage score = new TextImage(
        Integer.toString(this.moves) + " / " + Integer.toString(this.maxmoves), 32, Color.BLACK);

    background.placeImageXY(gameover, 400, 300);
    background.placeImageXY(time, 400, 550);
    background.placeImageXY(score, 400, 600);
    return background;

  }

  //returns the size of this.animate
  int animateSize() {

    int r = 0;

    for (ArrayList<Cell> a : this.animate) {

      r = r + a.size();

    }

    return r;

  }

}