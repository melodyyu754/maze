/*
 * THIS IS THE EXAMPLES MAZEWORLD FILE. 
 * It contains the ExamplesMazeWorld class, which has example vertices,
 * edges, and MazeWorlds. We tested the majority of our algorithmic and 
 * functional testing with a seeded 3x3 grid, and most of our draw
 * methods with a seeded 2x2 grid.
 * 
 * INSTRUCTIONS FOR THE MAZE:
 * 
 * TO BEGIN THE MAZE:
 * It also has the bigbang test to start the game:
 * 
 *  void testMazeWorld(Tester t) {
    MazeWorld m = new MazeWorld(20, 12);
    this.initData();
    m.bigBang(1000, 600, 0.0001);
  }

 * To change the size of the maze, you can change the number of cells across and down
 * by changing the numbers within MazeWorld m.
 * To change the speed, you can change the "0.0001" to whatever you speed you would 
 * like. The larger the number, the slower the speed.
 * 
 * TO PLAY:
 * PRESS "b": for breadth first search. The maze will start at the top left corner (green)
 * and end at the bottom right corner (red), and traverse through using bfs. It will display
 * the final path after it reaches the destination.
 * 
 * PRESS "d": for depth first search. The maze will start at the top left corner (green)
 * and end at the bottom right corner (red), and traverse through using dfs. It will display
 * the final path after it reaches the destination.
 * 
 * PRESS "r": for reset. This resets the maze.
 * 
 * NOTE: b cannot be switched from d or vice versa while it is actively searching.
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import tester.Tester;

// examples and testing
class ExamplesMazeWorld {
  //example Vertices
  Vertex v1 = new Vertex(0, 0);
  Vertex v1Dupl = new Vertex(0, 0);
  Vertex v2 = new Vertex(0, 1);
  Vertex v3 = new Vertex(1, 0);
  Vertex v4 = new Vertex(1, 1);

  // example Edges
  //  _ _
  // |1|2|
  //  _ _
  // |3|4|
  //  _ _

  // between 1 and 2 (vertical)
  Edge e1 = new Edge(v1, v2, 10);
  // between 1 and 3
  Edge e2 = new Edge(v1, v3, 20);
  // between 3 and 4 (vertical)
  Edge e3 = new Edge(v3, v4, 30);
  // between 2 and 4
  Edge e4 = new Edge(v2, v4, 40);
  // list of the 4 edges
  ArrayList<Edge> edges = new ArrayList<Edge>(Arrays.asList(e2, e4, e3, e1));

  // edgeSorter
  EdgeSorter sortEdgesByWeight = new EdgeSorter();  

  //FOR THE 3x3 EXAMPLE
  // example hashMap with all vertices in 3x3 map linked to themselves
  HashMap<Vertex, Vertex> initHash;

  // 3x3 maze
  MazeWorld m1;
  Vertex ver1;
  Vertex ver2;
  Vertex ver3;
  Vertex ver4;
  Vertex ver5;
  Vertex ver6;
  Vertex ver7;
  Vertex ver8;
  Vertex ver9;

  // 2x2 maze
  MazeWorld m2;
  Vertex m2v1;
  Vertex m2v2;
  Vertex m2v3;
  Vertex m2v4;

  // initializes MazeWorld and individual vertices
  void initData() {
    m1 = new MazeWorld(3, 3, new Random(10));

    // vertices in a 3x3 maze on a 1000x600 canvas
    ver1 = new Vertex(0,0);
    ver2 = new Vertex(0,200);
    ver3 = new Vertex(0,400);
    ver4 = new Vertex(333,0);
    ver5 = new Vertex(333,200);
    ver6 = new Vertex(333,400);
    ver7 = new Vertex(666,0);
    ver8 = new Vertex(666,200);
    ver9 = new Vertex(666,400);

    m2 = new MazeWorld(2, 2, new Random(10));

    // vertices in a 2x2 maze on a 400 x 400 canvas
    m2v1 = new Vertex(0,0);
    m2v2 = new Vertex(0,200);
    m2v3 = new Vertex(200,0);
    m2v4 = new Vertex(200,200);
  }

  // initializes the board of a MazeWorld
  void initBoard() {
    // adds vertices to board of m2 to test
    m2.board.add(new ArrayList<Vertex>(Arrays.asList(m2v1, m2v3)));
    m2.board.add(new ArrayList<Vertex>(Arrays.asList(m2v2, m2v4)));
  }

  // tests hashCode (override)
  void testHashCode(Tester t) {
    t.checkExpect(v1.hashCode() == v1.hashCode(), true);
    t.checkExpect(v1.hashCode() == v1Dupl.hashCode(), true);
    t.checkExpect(v1Dupl.hashCode() == v1.hashCode(), true);
    t.checkExpect(v1.hashCode() != v2.hashCode(), true);
    t.checkExpect(v3.hashCode() != v1.hashCode(), true);
  }

  // tests equals (override)
  void testEquals(Tester t) {
    t.checkExpect(v1.equals(v1), true);
    t.checkExpect(v1.equals(v1Dupl), true);
    t.checkExpect(v1Dupl.equals(v1), true);
    t.checkExpect(v1.equals(v2), false);
    t.checkExpect(v3.equals(v1), false);
  }

  // testing edgeSorter class
  void testEdgeSorter(Tester t) {
    t.checkExpect(edges, new ArrayList<Edge>(Arrays.asList(e2, e4, e3, e1)));
    edges.sort(sortEdgesByWeight);
    t.checkExpect(edges, new ArrayList<Edge>(Arrays.asList(e1, e2, e3, e4)));
  }

  // testing initVerticies
  void testInitVerts(Tester t) {
    initData();
    // allVertices is empty before initVertices is called
    t.checkExpect(m1.board, new ArrayList<Vertex>(new ArrayList<Vertex>()));
    // mutation
    m1.initVerts();
    // side effects
    t.checkExpect(m1.board, 
        new ArrayList<ArrayList<Vertex>>(Arrays.asList(
            new ArrayList<Vertex>(Arrays.asList(ver1, ver2, ver3)),
            new ArrayList<Vertex>(Arrays.asList(ver4, ver5, ver6)),
            new ArrayList<Vertex>(Arrays.asList(ver7, ver8, ver9)))));
  }

  // testing initEdge
  void testInitEdge(Tester t) {
    initData();
    m1.initVerts();
    // edges is empty before initEdge is called
    t.checkExpect(m1.edges, new ArrayList<Edge>());
    // mutation
    m1.initEdge();
    // side effects
    t.checkExpect(m1.edges, 
        new ArrayList<Edge>(Arrays.asList(
            new Edge(ver4, ver7, 214),
            new Edge(ver3, ver6, 981),
            new Edge(ver1, ver2, 1113),
            new Edge(ver7, ver8, 1246),
            new Edge(ver5, ver6, 1290),
            new Edge(ver4, ver5, 1293),
            new Edge(ver8, ver9, 1456),
            new Edge(ver2, ver5, 1888),
            new Edge(ver2, ver3, 4380),
            new Edge(ver6, ver9, 5099),
            new Edge(ver5, ver8, 5323),
            new Edge(ver1, ver4, 5797))));
  }

  // testing initHash
  void testInitHash(Tester t) {
    initData();
    m1.initVerts();
    // checking that hash before initHash is empty
    t.checkExpect(m1.hash, new HashMap<Vertex, Vertex>());

    // mutation of hash to link all vertices to itself
    m1.initHash();

    // what hash should be after initHash is called
    HashMap<Vertex, Vertex> initialHash = new HashMap<Vertex, Vertex>();
    initialHash.put(m1.board.get(0).get(0), m1.board.get(0).get(0));
    initialHash.put(m1.board.get(0).get(1), m1.board.get(0).get(1));
    initialHash.put(m1.board.get(0).get(2), m1.board.get(0).get(2));
    initialHash.put(m1.board.get(1).get(0), m1.board.get(1).get(0));
    initialHash.put(m1.board.get(1).get(1), m1.board.get(1).get(1));
    initialHash.put(m1.board.get(1).get(2), m1.board.get(1).get(2));
    initialHash.put(m1.board.get(2).get(0), m1.board.get(2).get(0));
    initialHash.put(m1.board.get(2).get(1), m1.board.get(2).get(1));
    initialHash.put(m1.board.get(2).get(2), m1.board.get(2).get(2));

    // checking side effects
    t.checkExpect(m1.hash, initialHash);
  }

  // testing find
  void testFind(Tester t) {
    initData();
    m1.initVerts();
    m1.initEdge();
    // making the HashMap:
    /*  ____________
     * |1|2|3|4|5|6|
     *  ------------
     * |5|1|5|5|5|6|
     *  ------------
     */
    HashMap<Vertex, Vertex> testHash = new HashMap<Vertex, Vertex>();
    testHash.put(ver1, ver5);
    testHash.put(ver2, ver1);
    testHash.put(ver3, ver5);
    testHash.put(ver4, ver5);
    testHash.put(ver5, ver5);
    testHash.put(ver6, ver6);

    // setting m1's hash to the test hash
    m1.hash = testHash;

    // testing the find method
    t.checkExpect(m1.find(ver1), ver5);
    t.checkExpect(m1.find(ver2), ver5);
    t.checkExpect(m1.find(ver3), ver5);
    t.checkExpect(m1.find(ver4), ver5);
    t.checkExpect(m1.find(ver5), ver5);
    t.checkExpect(m1.find(ver6), ver6);
  }

  // testing Kruskal
  void testKruskal(Tester t) {
    initData();
    m1.initVerts();
    m1.initEdge();
    m1.initHash();
    // checking edges before kruskal is called
    t.checkExpect(m1.edges, 
        new ArrayList<Edge>(Arrays.asList(
            new Edge(ver4, ver7, 214),
            new Edge(ver3, ver6, 981),
            new Edge(ver1, ver2, 1113),
            new Edge(ver7, ver8, 1246),
            new Edge(ver5, ver6, 1290),
            new Edge(ver4, ver5, 1293),
            new Edge(ver8, ver9, 1456),
            new Edge(ver2, ver5, 1888),
            new Edge(ver2, ver3, 4380),
            new Edge(ver6, ver9, 5099),
            new Edge(ver5, ver8, 5323),
            new Edge(ver1, ver4, 5797))));
    // mutation
    m1.kruskal();
    t.checkExpect(m1.edges, 
        new ArrayList<Edge>(Arrays.asList(
            new Edge(ver4, ver7, 214),
            new Edge(ver3, ver6, 981),
            new Edge(ver1, ver2, 1113),
            new Edge(ver7, ver8, 1246),
            new Edge(ver5, ver6, 1290),
            new Edge(ver4, ver5, 1293),
            new Edge(ver8, ver9, 1456),
            new Edge(ver2, ver5, 1888))));
  }

  // testing drawCell
  void testDrawCell(Tester t) {
    this.initData();
    // cell with all black borders
    t.checkExpect(m2.drawCell(m2v1), 
        new AboveImage(
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
            new AboveImage(    
                new BesideImage(
                    // | [center] |
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.white),
                        new RectangleImage(1, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.black))),
                new BesideImage(
                    // .--.
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                        new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))));
    // resetting
    this.initData();
    // cell with bottom edge
    m2.edges.add(new Edge(m2v1, m2v2, 1));
    t.checkExpect(m2.drawCell(m2v1), 
        new AboveImage(
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
            new AboveImage(    
                new BesideImage(
                    // | [center] |
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.white),
                        new RectangleImage(1, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.black))),
                new BesideImage(
                    // .--.
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.white),
                        new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))));
    // resetting
    this.initData();
    // cell with top edge
    m2.edges.add(new Edge(m2v1, m2v2, 1));
    t.checkExpect(m2.drawCell(m2v2), 
        new AboveImage(
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.white),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
            new AboveImage(    
                new BesideImage(
                    // | [center] |
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.white),
                        new RectangleImage(1, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.black))),
                new BesideImage(
                    // .--.
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                        new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))));   
    // resetting
    this.initData();
    // cell with right edge
    m2.edges.add(new Edge(m2v1, m2v3, 1));
    t.checkExpect(m2.drawCell(m2v1), 
        new AboveImage(
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
            new AboveImage(    
                new BesideImage(
                    // | [center] |
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.white),
                        new RectangleImage(1, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.white))),
                new BesideImage(
                    // .--.
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                        new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))));
    // resetting
    this.initData();
    // cell with left edge
    m2.edges.add(new Edge(m2v3, m2v1, 1));
    t.checkExpect(m2.drawCell(m2v3), 
        new AboveImage(
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
            new AboveImage(    
                new BesideImage(
                    // | [center] |
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.white),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.white),
                        new RectangleImage(1, m2.vertexHeight - 2, 
                            OutlineMode.SOLID, Color.black))),
                new BesideImage(
                    // .--.
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                    new BesideImage(
                        new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                        new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))));
  }

  // tests the drawMaze function
  void testDrawMaze(Tester t) {
    this.initData();
    this.initBoard();
    // before mutation
    t.checkExpect(m2.scene, null);
    // mutation
    m2.drawMaze();
    // base with cells drawn on it (how drawMaze *should* mutate the scene)
    WorldScene base = new WorldScene(1000, 600);
    base.placeImageXY(m2.drawCell(m2v1), m2v1.x + (1000 / 4), m2v1.y + (600 / 4));
    base.placeImageXY(m2.drawCell(m2v2), m2v3.x + (1000 / 4), m2v3.y + (600 / 4));
    base.placeImageXY(m2.drawCell(m2v3), m2v2.x + (1000 / 4), m2v2.y + (600 / 4));
    base.placeImageXY(m2.drawCell(m2v4), m2v4.x + (1000 / 4), m2v4.y + (600 / 4));
    base.placeImageXY(new RectangleImage(500, 300, OutlineMode.SOLID, Color.red), 
        450, 350);
    base.placeImageXY(new RectangleImage(500, 300, OutlineMode.SOLID, Color.green), 
        250, 150);
    // after mutation
    t.checkExpect(m2.scene, base);
  }

  // tests the makeScene function
  void testMakeScene(Tester t) {
    this.initData();
    // scene before maze is created
    t.checkExpect(m2.scene, null);
    // mutation
    m2.makeScene();
    // base with cells & their given edges drawn on it (how makeScene *should* mutate the scene)
    WorldScene base = new WorldScene(1000, 600);
    base.placeImageXY(new AboveImage(
        new BesideImage(
            // .--.
            new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
            new BesideImage(
                new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
        new AboveImage(    
            new BesideImage(
                // | [center] |
                new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                        OutlineMode.SOLID, Color.white),
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.white))),
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.white),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))), 
        1000 / 4, 600 / 4);
    base.placeImageXY(new AboveImage(
        new BesideImage(
            // .--.
            new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
            new BesideImage(
                new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.white),
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
        new AboveImage(    
            new BesideImage(
                // | [center] |
                new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                        OutlineMode.SOLID, Color.white),
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.white))),
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))), 
        1000 / 4, 600 / 4 + 600 / 2);
    base.placeImageXY(new AboveImage(
        new BesideImage(
            // .--.
            new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
            new BesideImage(
                new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
        new AboveImage(    
            new BesideImage(
                // | [center] |
                new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.white),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                        OutlineMode.SOLID, Color.white),
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.black))),
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))), 
        1000 / 4 + 1000 / 2, 600 / 4);
    base.placeImageXY(new AboveImage(
        new BesideImage(
            // .--.
            new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
            new BesideImage(
                new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))),
        new AboveImage(    
            new BesideImage(
                // | [center] |
                new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.white),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, m2.vertexHeight - 2, 
                        OutlineMode.SOLID, Color.white),
                    new RectangleImage(1, m2.vertexHeight - 2, OutlineMode.SOLID, Color.black))),
            new BesideImage(
                // .--.
                new RectangleImage(1, 1, OutlineMode.SOLID, Color.black),
                new BesideImage(
                    new RectangleImage(m2.vertexWidth - 2, 1, OutlineMode.SOLID, Color.black),
                    new RectangleImage(1, 1, OutlineMode.SOLID, Color.black))))), 
        1000 / 4 + 1000 / 2, 600 / 4 + 600 / 2);

    base.placeImageXY(new RectangleImage(500, 300, OutlineMode.SOLID, Color.red), 
        750, 450);
    base.placeImageXY(new RectangleImage(500, 300, OutlineMode.SOLID, Color.green), 
        250, 150);

    // after mutation
    t.checkExpect(m2.scene, base);
  }

  // tests initOutEdge
  void testInitOutEdge(Tester t) {
    this.initData();
    m1.initVerts();
    m1.initEdge();
    m1.initHash();
    m1.kruskal();

    // testing that the outEdges before initOutEdge are empty arraylists
    t.checkExpect(m1.board.get(0).get(0).outEdges, new ArrayList<Edge>());
    t.checkExpect(m1.board.get(1).get(1).outEdges, new ArrayList<Edge>());

    // mutation
    m1.initOutedge();

    // testing side effects
    t.checkExpect(m1.board.get(0).get(0).outEdges, 
        new ArrayList<Edge>(Arrays.asList(
            new Edge(m1.board.get(0).get(0), m1.board.get(0).get(1), 1113))));

    t.checkExpect(m1.board.get(1).get(1).outEdges.size(), 3);
    t.checkExpect(m1.board.get(1).get(1).outEdges.get(0).from, 
        m1.board.get(1).get(1));
    t.checkExpect(m1.board.get(1).get(1).outEdges.get(0).to, 
        m1.board.get(1).get(2));
    t.checkExpect(m1.board.get(1).get(1).outEdges.get(1).from, 
        m1.board.get(1).get(1));
    t.checkExpect(m1.board.get(1).get(1).outEdges.get(1).to, 
        m1.board.get(1).get(0));
    t.checkExpect(m1.board.get(1).get(1).outEdges.get(2).from, 
        m1.board.get(1).get(1));
    t.checkExpect(m1.board.get(1).get(1).outEdges.get(2).to, 
        m1.board.get(0).get(1));
  }

  // testing bfs
  void testBfs(Tester t) {
    // initializing everything except initOutedge
    this.initData();
    m1.initVerts();
    m1.initEdge();
    m1.initHash();
    m1.kruskal();

    // checking if bfs returns false
    t.checkExpect(m1.bfs(m1.board.get(0).get(0), m1.board.get(2).get(2)), false);
    t.checkExpect(m1.bfs(m1.board.get(2).get(2), m1.board.get(0).get(0)), false);

    // initializing outedges
    m1.initOutedge();

    // checking if bfs returns true
    t.checkExpect(m1.bfs(m1.board.get(0).get(0), m1.board.get(2).get(2)), true);
    t.checkExpect(m1.bfs(m1.board.get(2).get(2), m1.board.get(0).get(0)), true);
  }

  // testing dfs
  void testDfs(Tester t) {
    // initializing everything except initOutedge
    this.initData();
    m1.initVerts();
    m1.initEdge();
    m1.initHash();
    m1.kruskal();

    // checking if dfs returns false
    t.checkExpect(m1.dfs(m1.board.get(0).get(0), m1.board.get(2).get(2)), false);
    t.checkExpect(m1.dfs(m1.board.get(2).get(2), m1.board.get(0).get(0)), false);

    // initializing outedges
    m1.initOutedge();

    t.checkExpect(m1.dfs(m1.board.get(0).get(0), m1.board.get(2).get(2)), true);
    t.checkExpect(m1.dfs(m1.board.get(2).get(2), m1.board.get(0).get(0)), true);
  }

  // tests the reset method
  void testReset(Tester t) {
    // starting with un-reset version of board
    this.initData();
    this.initBoard();
    this.m2.makeScene();
    this.m2.drawMaze();
    this.m2.onKeyEvent("b");
    while (this.m2.searched.size() > 0) {
      this.m2.onTick();
    }
    // before mutation
    t.checkExpect(this.m2.gameState, "searching");
    t.checkExpect(this.m2.board.size(), 4);
    t.checkExpect(this.m2.hash.size(), 4);
    t.checkExpect(this.m2.edges.size(), 3);
    t.checkExpect(this.m2.mazeMade, true);
    t.checkExpect(this.m2.searched.size(), 0);
    t.checkExpect(this.m2.cheapestPath.size(), 2);
    t.checkExpect(this.m2.timer, 0.0);   
    // mutation
    this.m2.reset();
    // after mutation
    t.checkExpect(this.m2.gameState, "still");
    t.checkExpect(this.m2.board.size(), 0);
    t.checkExpect(this.m2.hash.size(), 0);
    t.checkExpect(this.m2.edges.size(), 0);
    t.checkExpect(this.m2.mazeMade, false);
    t.checkExpect(this.m2.searched.size(), 0);
    t.checkExpect(this.m2.cheapestPath.size(), 0);
    t.checkExpect(this.m2.timer, 0.0);      
  }

  // tests the onTick method
  void testOnTick(Tester t) {
    this.initData();
    this.initBoard();
    // test when nothing is happening
    // mutation
    this.m2.onTick();
    // after mutation
    t.checkExpect(this.m2.gameState, "still");
    // test when searching is occurring
    this.m2.onKeyEvent("b");
    // mutation
    this.m2.onTick();
    // after mutation
    t.checkExpect(this.m2.gameState, "searching");
    // when all vertices colored & game has been updated to new state
    int onTickIterations = this.m2.searched.size() + 2;
    // test when searching has completed
    // mutation
    for (int i = 0; i < onTickIterations; i++) {
      this.m2.onTick();
    }
    // after mutation
    t.checkExpect(this.m2.gameState, "solution");
  }

  // tests the onKeyEvent method
  void testOnKeyEvent(Tester t) {
    this.initData();
    this.initBoard();
    // before mutation
    t.checkExpect(this.m2.gameState, "still");
    t.checkExpect(this.m2.searched.size(), 0);
    // mutation -- when 'b' is pressed
    this.m2.onKeyEvent("b");
    // after mutation
    t.checkExpect(this.m2.gameState, "searching");
    t.checkExpect(this.m2.searched.size(), 1);

    // resetting
    this.initData();
    this.initBoard();
    // mutation -- when 'd' is pressed
    this.m2.onKeyEvent("d");
    // after mutation
    t.checkExpect(this.m2.gameState, "searching");
    t.checkExpect(this.m2.searched.size(), 1);

    // mutation -- when 'r' is pressed
    this.m2.onKeyEvent("r");
    // after mutation
    t.checkExpect(this.m2.gameState, "still");
    t.checkExpect(this.m2.searched.size(), 0);
  }

  // tests the searchHelp method
  void testSearchHelp(Tester t) {
    this.initData();
    this.initBoard();
    this.m2.makeScene();
    // Testing with queue
    // before mutation
    t.checkExpect(this.m2.searched, new ArrayList<Vertex>());
    // mutation
    this.m2.searchHelp(this.m2.board.get(0).get(0), 
        this.m2.board.get(this.m2.width - 1).get(this.m2.height - 1), new Queue<Vertex>());
    // after mutation
    t.checkExpect(this.m2.searched, Arrays.asList(this.m2v1, this.m2v3, this.m2v2));

    // reset
    this.initData();
    this.initBoard();
    this.m2.makeScene();

    // Testing with stack
    // before mutation
    t.checkExpect(this.m2.searched, new ArrayList<Vertex>());
    // mutation
    this.m2.searchHelp(this.m2.board.get(0).get(0), 
        this.m2.board.get(this.m2.width - 1).get(this.m2.height - 1), new Stack<Vertex>());
    // after mutation
    t.checkExpect(this.m2.searched, Arrays.asList(this.m2v1, this.m2v2, this.m2v3));
  }

  // testing the makePath method
  void testMakePath(Tester t) {
    this.initData();
    this.initBoard();
    this.m2.makeScene();
    // creating a sample edgeList for makePath to work with
    HashMap<Vertex, Vertex> pathTest = new HashMap<Vertex, Vertex>();
    pathTest.put(m2v3, m2v1);
    pathTest.put(m2v2, m2v1);
    pathTest.put(m2v4, m2v3);
    // before mutation
    t.checkExpect(this.m2.cheapestPath, new ArrayList<Vertex>());
    // mutation
    this.m2.makePath(pathTest, this.m2.board.get(this.m2.width - 1).get(this.m2.height - 1));
    // after mutation
    t.checkExpect(this.m2.cheapestPath, Arrays.asList(m2v3, m2v1));  
  }

  // big bang
  void testMazeWorld(Tester t) {
    MazeWorld m = new MazeWorld(20, 12);
    this.initData();
    m.bigBang(1000, 600, 0.0001);
  }
}