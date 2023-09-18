import java.awt.Color;
import java.util.ArrayList;

import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

//A cell in the game
class Cell {

  int x;

  int y;

  Color color;

  boolean flooded;

  ArrayList<Cell> adjacent;

  Cell(int x, int y, Color color) {

    this(x, y, color, false);

  }

  Cell(int x, int y, Color color, boolean flooded) {

    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;
    this.adjacent = new ArrayList<Cell>();

  }

  //draw this cell onto the given worldscene
  //EFFECT: worldscene is mutated to have this Cell rendered on it.
  void draw(int size, WorldScene scene) {

    int cellsize = 300 / size;

    int padding = 100;

    WorldImage rect = new RectangleImage(2 * cellsize, 2 * cellsize, OutlineMode.SOLID, this.color);

    /*WorldImage debug = new OverlayImage(
        new TextImage(Integer.toString(this.x) + ", " + Integer.toString(this.y), Color.BLACK),
        rect);*/

    scene.placeImageXY(rect, cellsize * (2 * this.x + 1) + padding,
        cellsize * (2 * this.y + 1) + padding);

    //return rect;

  }

  //changes the cell color to the animation color
  public void animate(Color color) {

    this.color = color;

  }

  //floods the cell and any neighbors
  //EFFECT: mutates the given AnimateQueue to contain any cells which need to be animated by color
  public void flood(Color color, ArrayList<Cell> visited, ArrayList<ArrayList<Cell>> animateQueue,
      int size) {

    if (visited.indexOf(this) < 0) {

      visited.add(this);

      if (this.flooded || this.color.equals(color)) {

        this.flooded = true;

        if (!this.color.equals(color)) {

          animateQueue.get(size - 1 - this.x + this.y).add(this);

        }

        for (Cell cell : this.adjacent) {

          cell.flood(color, visited, animateQueue, size);

        }

      }

    }

  }

  //starts a flood using the current cell's color
  Color startFlood(Cell cell, ArrayList<ArrayList<Cell>> animate, int size) {

    cell.flood(this.color, new ArrayList<Cell>(), animate, size);

    return this.color;

  }

  //adds cell to the list of adjacent cells
  public void link(Cell c) {

    this.adjacent.add(c);

  }

  //for testing purposes
  public void link(Cell... cells) {

    for (Cell c : cells) {

      this.link(c);

    }

  }

  //is the board is filled completely?
  public boolean filled(ArrayList<Cell> visited) {

    if (!this.flooded) {

      return false;

    }

    if (visited.indexOf(this) < 0 && this.flooded) {

      visited.add(this);

      for (Cell c : this.adjacent) {

        if (!c.filled(visited)) {

          return false;

        }

      }

    }

    return true;

  }

}