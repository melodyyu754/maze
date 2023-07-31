/*
 * THIS IS THE MAZE FILE.
 * It contains the bulk of the code that creates and searches the maze.
 * See ExamplesMazeWorld.java for instructions on how to run the maze.
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

// Comparator for edges based on weight
class EdgeSorter implements Comparator<Edge> {
  // compares edges based on weight
  public int compare(Edge e1, Edge e2) {
    return e1.weight - e2.weight;
  }
}

// Represents an edge or "connection" between vertices
class Edge {
  // the vertex the edge is coming from
  Vertex from;
  // the vertex the edge is going to
  Vertex to;
  // the weight of the vertex (randomly generated then sorted)
  int weight;

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
}

// Represents a vertex or "cell"
class Vertex {
  // the x coordinate
  int x;
  // the y coordinate
  int y;
  // edges from this node
  ArrayList<Edge> outEdges;
  // color of the vertex
  Color color;


  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.outEdges = new ArrayList<Edge>();
    this.color = Color.white;
  }


  // overriding hashCode for a vertex
  @Override
  public int hashCode() {
    return 
        //this.outEdges.hashCode() * 1000 * this.color.hashCode() 
        + this.x * 100 + this.y;
  }

  // overriding .equals for a vertex
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Vertex)) {
      return false;
    }
    Vertex that = (Vertex) o;
    return this.x == that.x
        && this.y == that.y;
  }
}

// Represents a Maze game
class MazeWorld extends World {

  // CONSTANT WINDOW SIZE (determines cell size)
  int windowWidth = 1000;
  int windowHeight = 600;

  String gameState;

  // random variable
  Random rand;

  // number of vertices on the x-axis
  int width;

  // number of vertices on the y-axis
  int height;

  // the width of each vertex
  int vertexWidth;

  // the height of each vertex
  int vertexHeight;

  //grid of vertices
  ArrayList<ArrayList<Vertex>> board;

  // map of representatives
  HashMap<Vertex, Vertex> hash; 

  // list of edges (holes in maze)
  ArrayList<Edge> edges;

  // determines if the maze has been created yet
  boolean mazeMade;

  // the scene to be drawn
  WorldScene scene;

  // timer
  double timer;

  // searched cells
  ArrayList<Vertex> searched;

  // cheapest path (to be displayed at the end)
  ArrayList<Vertex> cheapestPath;


  // constructor that takes in width and height, used in gameplay
  MazeWorld(int width, int height) {
    this.rand = new Random();
    this.width = width;
    this.height = height;
    this.vertexWidth = windowWidth / width;
    this.vertexHeight = windowHeight / height;
    this.reset();
  }

  // constructor that takes in width, height, and a random seed, used for testing
  MazeWorld(int width, int height, Random rand) {
    this.rand = rand;
    this.width = width;
    this.height = height;
    this.vertexWidth = windowWidth / width;
    this.vertexHeight = windowHeight / height;
    this.reset();
  }

  // handles ticking of the timer and what to do in each gameState

  // EFFECT: -- mutates gameState, timer, cheapestPath and searched
  //         -- mutates the color of items in searched and cheapestPath
  public void onTick() {
    // ticks the timer
    timer += .01;

    // displays the solution once searching is done
    if (gameState.equals("solution")) {
      for (int i = 0; i < cheapestPath.size(); i++) {
        cheapestPath.remove(0).color = Color.blue;
        drawMaze();
      }
    }

    // animates search path / checks if searching is over
    if (gameState.equals("searching")) {
      if (timer >= .02) {
        if (searched.isEmpty()) {
          gameState = "solution";
          timer = 0;
        }
        else {
          searched.remove(0).color = new Color(51, 204, 255);
          drawMaze();
          timer = 0;
        }
      }
    }
  }

  // determines what occurs with each key press
  // EFFECT: calls bfs/dfs/reset, mutates gameState based on key press
  @Override
  public void onKeyEvent(String s) {
    super.onKeyEvent(s);
    if (gameState.equals("still")) {
      if (s.equals("b")) {
        this.bfs(this.board.get(0).get(0), this.board.get(this.width - 1).get(this.height - 1));
        this.gameState = "searching";
      }
      else if (s.equals("d")) {
        this.dfs(this.board.get(0).get(0), this.board.get(this.width - 1).get(this.height - 1));
        this.gameState = "searching";
      }
    }
    if (s.equals("r")) {
      this.reset();
    }
  }


  // NEW SEARCH ALGORITHM STUFF BEGINS:
  // using bfs, is there a path between 2 vertices?
  boolean bfs(Vertex from, Vertex to) {
    return searchHelp(from, to, new Queue<Vertex>());
  }

  // using dfs, is there a path between 2 vertices?
  boolean dfs(Vertex from, Vertex to) {
    return searchHelp(from, to, new Stack<Vertex>());
  }

  // helper for search algorithms
  boolean searchHelp(Vertex from, Vertex to, ICollection<Vertex> worklist) {
    HashMap<Vertex, Vertex> cameFromEdge = new HashMap<Vertex, Vertex>();
    ArrayList<Vertex> alreadySeen = new ArrayList<Vertex>();

    // Initialize the worklist with the from vertex
    worklist.add(from);

    // As long as the worklist isn't empty...
    while (!worklist.isEmpty()) {

      // next is the first item on the worklist
      Vertex next = worklist.remove();

      // if we reach our destination,
      if (next.equals(to)) {
        this.makePath(cameFromEdge, next);     
        return true; // Success!
      }

      else if (!alreadySeen.contains(next)) {
        // add all the neighbors of next to the worklist for further processing
        for (Edge e : next.outEdges) {
          // if the next vertex on the worklist is the same as the from vertex of the edge,
          // and the vertex the edge is pointed to hasn't already been seen
          if (next.equals(e.from) && !alreadySeen.contains(e.to)) {
            worklist.add(e.to);
            cameFromEdge.put(e.to, next);
          }
          // if the next edge of the worklist is the same as the to vertex and the from vertex
          // hasn't already been seen, add that from vertex to the worklist and make the from's 
          // link be the next.
          else if (next.equals(e.to) && !alreadySeen.contains(e.from)) {
            worklist.add(e.from);
            cameFromEdge.put(e.from, next);
          }
        }
        // add next to alreadySeen, since we're done with it
        alreadySeen.add(next);
        searched.add(next);
      }
    }
    // We haven't found the to vertex, and there are no more to try
    return false;
  }


  // constructs cheapest path
  // EFFECT: mutates and builds cheapestPath by pulling from the HashMap
  public void makePath(HashMap<Vertex, Vertex> cameFromEdge, Vertex next) {
    while (this.board.get(0).get(0) != next) {
      this.cheapestPath.add(cameFromEdge.get(next));
      next = cameFromEdge.get(next);
    }
  }

  // initializes the board if it hasn't been generated yet
  public WorldScene makeScene() {
    // generates maze if it has not been generated yet
    if (!mazeMade) {
      this.initVerts();
      this.initEdge();
      this.initHash();
      this.kruskal();
      this.initOutedge();
      this.drawMaze();
      mazeMade = true;
    }
    // TEMP for TESTING
    return scene;
  }

  // EFFECT: mutates everything to initial states
  // generates new maze and clears old one
  void reset() {    
    this.gameState = "still";
    this.board = new ArrayList<ArrayList<Vertex>>();
    this.hash = new HashMap<Vertex, Vertex>();
    this.edges = new ArrayList<Edge>();
    this.mazeMade = false;
    this.searched = new ArrayList<Vertex>();
    this.cheapestPath = new ArrayList<Vertex>();
    this.timer = 0;
  }

  // draws the maze on the scene
  // EFFECT: mutates scene by placing images on it
  void drawMaze() {
    // width & height of maze in pixels
    int widthPixels = width * vertexWidth;
    int heightPixels = height * vertexHeight;
    scene = new WorldScene(widthPixels, heightPixels);
    // draws every cell and places it on board
    for (ArrayList<Vertex> r : board) {
      for (Vertex v : r) {
        scene.placeImageXY(drawCell(v), v.x + vertexWidth / 2, v.y + vertexHeight / 2);
      }
    }
    // places the red square at the bottom corner (end)
    scene.placeImageXY(new RectangleImage(vertexWidth, vertexHeight, OutlineMode.SOLID, Color.red), 
        this.board.get(width - 1).get(height - 1).x + vertexWidth / 2, 
        this.board.get(width - 1).get(height - 1).y + vertexHeight / 2);

    // places the green square at the top left corner (start)
    scene.placeImageXY(new RectangleImage(vertexWidth, vertexHeight, 
        OutlineMode.SOLID, Color.green), 
        this.board.get(0).get(0).x + vertexWidth / 2, 
        this.board.get(0).get(0).y + vertexHeight / 2);
  }

  // EFFECT: initializes every vertex to fill the board
  void initVerts() {
    for (int r = 0; r < width; r++) {
      board.add(new ArrayList<Vertex>());
      for (int c = 0; c < height; c++) {
        board.get(r).add(new Vertex(r * vertexWidth, c * vertexHeight));
      }
    }
  }

  // EFFECT: initializes every edge and gives it a random weight, then sorts it
  void initEdge() {
    // connecting edges horizontally 
    for (int r = 0; r < width; r++) {
      for (int c = 0; c < height - 1; c++) {
        Edge curEdge = new Edge(board.get(r).get(c), board.get(r).get(c + 1), 
            rand.nextInt(6000));
        edges.add(curEdge);
        // NEW ADDITION: giving each vertex its edges
        // board.get(r).get(c).outEdges.add(curEdge);
      }
    }
    // connecting edges vertically
    for (int r = 0; r < width - 1; r++) {
      for (int c = 0; c < height; c++) {
        Edge curEdge = new Edge(board.get(r).get(c), board.get(r + 1).get(c), 
            rand.nextInt(6000));
        edges.add(curEdge);
        // NEW ADDITION: giving each vertex its edges
        // board.get(r).get(c).outEdges.add(curEdge);
      }
    }
    // sorts edges by weight
    edges.sort(new EdgeSorter());
  }

  // EFFECT: initializes all the outedges of each vertex
  void initOutedge() {
    // for every vertex in the board
    for (ArrayList<Vertex> r : board) {
      for (Vertex v : r) {
        for (int edge = 0; edge < edges.size(); edge++) {
          // add any edge that has this vertex as its from to its outedges class
          if (v.equals(edges.get(edge).from)) {
            v.outEdges.add(edges.get(edge));
          }
          // add any edge that has this vertex as its to to its outedges class, but flip the edge.
          if (v.equals(edges.get(edge).to)) {
            v.outEdges.add(new Edge(edges.get(edge).to, 
                edges.get(edge).from, 
                edges.get(edge).weight));
          }
        }
      }
    }
  }

  // EFFECT: creates representatives for all vertices and sets value to themselves
  void initHash() {
    for (int r = 0; r < width; r++) {
      for (int c = 0; c < height; c++) {
        hash.put(board.get(r).get(c), board.get(r).get(c));
      }
    }
  }

  // finds a vertex in the hash-map of representatives
  Vertex find(Vertex cell) {
    if (cell.equals(hash.get(cell))) {
      return cell;
    }
    else {
      return this.find(hash.get(cell));
    }
  }

  // EFFECT: generates the walls in the maze to create a path
  //         mutates edges field to filter out non-edges
  //         puts representatives in hashmap
  void kruskal() {
    // "good" edges
    ArrayList<Edge> minSpanTree = new ArrayList<Edge>();
    while (edges.size() > 0) {
      Edge cheapest = edges.remove(0);
      if (find(cheapest.from) != find(cheapest.to)) {
        minSpanTree.add(cheapest);
        // union
        hash.put(find(cheapest.from), find(cheapest.to));
      }
    }
    this.edges = minSpanTree;
  }

  // draws the cell, making the walls white or black depending on if there's an edge there
  WorldImage drawCell(Vertex v) {
    // Color of a solid wall
    Color wallCol = Color.black;
    // Color of a empty wall
    Color gapCol = v.color; // change: to blue
    // Colors of each side of a cell
    Color topCol = wallCol;
    Color bottomCol = wallCol;
    Color rightCol = wallCol;
    Color leftCol = wallCol;

    // If vertex has an edge in the tree make corresponding side empty
    // Checking the edges' start vertices
    for (Edge e : edges) {
      if (e.from == v) {
        if (e.to.x == v.x && e.to.y > v.y) {
          bottomCol = gapCol;
        }
        if (e.to.x == v.x && e.to.y < v.y) {
          topCol = gapCol;
        }
        if (e.to.y == v.y && e.to.x > v.x) {
          rightCol = gapCol;
        }
        if (e.to.y == v.y && e.to.x < v.x) {
          leftCol = gapCol;
        }
      }
      // Checking the edges' end vertices
      if (e.to == v) {
        if (e.from.x == v.x && e.from.y > v.y) {
          bottomCol = gapCol;
        }
        if (e.from.x == v.x && e.from.y < v.y) {
          topCol = gapCol;
        }
        if (e.from.y == v.y && e.from.x > v.x) {
          rightCol = gapCol;
        }
        if (e.from.y == v.y && e.from.x < v.x) {
          leftCol = gapCol;
        }
      }
    }

    // length of edge to preserve solid corners
    int horLineSize = vertexWidth - 2;
    int vertLineSize = vertexHeight - 2;

    // . = corner, -- = edge, | = edge
    // Representation of cell (9 rectangle images):
    // .__.
    // |  |
    // .__.
    return
        new AboveImage(
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(horLineSize, 1, OutlineMode.SOLID, topCol),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
            new AboveImage(    
                new BesideImage(
                    // | [center] |
                    new RectangleImage(1, vertLineSize, OutlineMode.SOLID, leftCol),
                    new BesideImage(
                        new RectangleImage(horLineSize, vertLineSize, OutlineMode.SOLID, 
                            v.color),
                        new RectangleImage(1, vertLineSize, OutlineMode.SOLID, rightCol))),
                new BesideImage(
                    // .--.
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(horLineSize, 1, OutlineMode.SOLID, bottomCol),
                        new RectangleImage(1, 1, OutlineMode.SOLID, Color.black)))));
  }
}