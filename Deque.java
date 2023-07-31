/*
 * THIS IS THE DEQUE FILE.
 * It contains the deque class, and all the methods that deque works with
 * This file was taken straight from our Deque homework submission, and is complete
 * with tests.
 */

import java.util.function.Predicate;
import tester.Tester;

//Double ended queue (deque) class that only has a header (sentinel)
class Deque<T> {
  Sentinel<T> header;

  // Deque constructor with no parameters and header (sentinel) as a field
  Deque() {
    this.header = new Sentinel<T>();
  }

  //Deque constructor with header (sentinel) as a parameter and field
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // counts the number of nodes in a list Deque, not including the header node
  int size() {
    return header.next.sizeHelp();
  }

  // consumes a value of type T and inserts it at the front of the list
  void addAtHead(T val) {
    this.header.addAtHeadHelp(val);
  }

  // consumes a value of type T and inserts it at the tail of the list
  void addAtTail(T val) {
    this.header.addAtTailHelp(val);
  }

  // removes the first node from this Deque, throws a runtime exception if an 
  // attempt is made to remove from an empty list
  T removeFromHead() {
    return this.header.next.removeFromHeadHelp();
  }

  // removes the last node from this Deque, throws a runtime exception if an 
  // attempt is made to remove from an empty list
  T removeFromTail() {
    return this.header.prev.removeFromTailHelp();
  }

  // takes a Predicate<T> and produces the first node in this Deque for which
  // the given predicate returns true.
  ANode<T> find(Predicate<T> val) {
    return this.header.next.findHelp(val);
  }
}

// abstract node that can be a Sentinel<T> node or Node<T> containing data, 
// has next/prev fields
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // ANode constructor that takes in an ANode<T> next and an ANode<T> prev.
  ANode(ANode<T> next, ANode<T> prev) {
    this.next = next;
    this.prev = prev;
  }

  // helper; counts the number of nodes in an ANode, not including the sentinel node
  abstract int sizeHelp();

  // helper; consumes a value of type T and inserts it at the front of the list
  void addAtHeadHelp(T val) {
    new Node<T>(val, this.next, this);
  }

  // helper; consumes a value of type T and inserts it at the tail of the list
  void addAtTailHelp(T val) {
    new Node<T>(val, this, this.prev);
  }

  // helper; removes the first node from list of nodes, throws a runtime exception 
  // if an attempt is made to remove from a sentinel
  abstract T removeFromHeadHelp();

  // helper; removes the tail node from list of nodes, throws a runtime exception 
  // if an attempt is made to remove from a sentinel
  abstract T removeFromTailHelp();

  // helper; takes a Predicate<T> and produces the first node in the list of nodes
  // for which the given predicate returns true. If the predicate never returns true,
  // returns the sentinel node.
  abstract ANode<T> findHelp(Predicate<T> val);
}

// node that contains data, extends ANode
class Node<T> extends ANode<T> {
  T data;

  // Node constructor with only data as a parameter
  Node(T data) {
    super(null, null);
    this.data = data;
  }

  // Node constructor that takes in data, next, and prev as parameters
  Node(T data, ANode<T> next, ANode<T> prev) {
    super(next, prev);
    this.data = data;
    this.next.prev = this;
    this.prev.next = this;
    if (next == null || prev == null) {
      throw new IllegalArgumentException("Node cannot be null.");
    }
  }

  T removeFromHeadHelp() {
    T temp = this.data;
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return temp;
  }


  T removeFromTailHelp() {
    T temp = this.data;
    this.next.prev = this.prev;
    this.prev.next = this.next;
    return temp;
  }


  int sizeHelp() {
    return this.next.sizeHelp() + 1;
  }

  ANode<T> findHelp(Predicate<T> val) {
    if (val.test(this.data)) {
      return this;
    }
    return this.next.findHelp(val);
  }
}

// Sentinel node that has only next and prev as fields
class Sentinel<T> extends ANode<T> {
  // Sentinel constructor that takes in zero arguments, initializes 
  Sentinel() {
    super(null, null);
    this.next = this;
    this.prev = this;
  }

  int sizeHelp() {
    return 0;
  }

  T removeFromHeadHelp() {
    throw new RuntimeException("Cannot remove a node from an empty list.");
  }

  T removeFromTailHelp() {
    throw new RuntimeException("Cannot remove a node from an empty list.");
  } 

  ANode<T> findHelp(Predicate<T> val) {
    return this;
  }
}


// PREDICATES for find
// returns true if string has length of 3, false otherwise.
class HasLength3 implements Predicate<String> {
  public boolean test(String s) {
    return s.length() == 3;
  }
}

// returns true if string contains the letter "e", false otherwise.
class ContainsE implements Predicate<String> {
  public boolean test(String s) {
    return s.contains("e");
  }
}

//returns true if string contains the letter "d", false otherwise.
class ContainsD implements Predicate<String> {
  public boolean test(String s) {
    return s.contains("d");
  }
}

//Deque Examples class for testing
class DequeExamples {
  Sentinel<String> s1;
  Deque<String> deque1;

  Sentinel<String> s2;
  Node<String> abc;
  Node<String> bcd;
  Node<String> cde;
  Node<String> def;
  Deque<String> deque2;

  Sentinel<String> s3;
  Node<String> two;
  Node<String> three;
  Node<String> four;
  Node<String> five;
  Node<String> six;
  Deque<String> deque3;

  // initializes the 3 example deques
  void initData() {

    // deque1
    s1 = new Sentinel<String>();

    deque1 = new Deque<String>(s1);

    // deque2
    s2 = new Sentinel<String>();

    abc = new Node<String>("abc");
    bcd = new Node<String>("bcd");
    cde = new Node<String>("cde");
    def = new Node<String>("def");

    abc = new Node<String>("abc", bcd, s2);
    bcd = new Node<String>("bcd", cde, abc);
    cde = new Node<String>("cde",def, bcd);
    def = new Node<String>("def", s2, cde);

    deque2 = new Deque<String>(s2);

    // deque 3
    s3 = new Sentinel<String>();

    two = new Node<String>("two");
    three = new Node<String>("three");
    four = new Node<String>("four");
    five = new Node<String>("five");
    six = new Node<String>("six");

    two = new Node<String>("two", three, s3);
    three = new Node<String>("three", four, two);
    four = new Node<String>("four",five, three);
    five = new Node<String>("five", six, four);
    six = new Node<String>("six", s3, five);

    deque3 = new Deque<String>(s3);
  }

  // tests the size method
  void testSize(Tester t) {
    initData();
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque3.size(), 5);
  }

  // tests the sizeHelp method
  void testSizeHelp(Tester t) {
    initData();
    t.checkExpect(abc.sizeHelp(), 4);
    t.checkExpect(two.sizeHelp(), 5);
  }

  // tests the addAtHead method
  void testAddAtHead(Tester t) {
    initData();

    // before mutation 
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.next, s1);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque2.header.next, new Node<String>("abc", bcd, s2));
    t.checkExpect(deque3.size(), 5);
    t.checkExpect(deque3.header.next, new Node<String>("two", three, s3));

    // mutation
    deque1.addAtHead("aaa");
    deque2.addAtHead("xyz");
    deque3.addAtHead("one");

    // after mutation
    t.checkExpect(deque1.size(), 1);
    t.checkExpect(deque1.header.next, new Node<String>("aaa", s1, s1));
    t.checkExpect(deque2.size(), 5);
    t.checkExpect(deque2.header.next, new Node<String>("xyz", abc, s2));
    t.checkExpect(deque3.size(), 6);
    t.checkExpect(deque3.header.next, new Node<String>("one", two, s3));
  }

  // tests the addAtHeadHelp method
  void testAddAtHeadHelp(Tester t) {
    initData();

    // before mutation 
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.next, s1);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque2.header.next, new Node<String>("abc", bcd, s2));
    t.checkExpect(deque3.size(), 5);
    t.checkExpect(deque3.header.next, new Node<String>("two", three, s3));

    // mutation
    s1.addAtHeadHelp("aaa");
    s2.addAtHeadHelp("xyz");
    s3.addAtHeadHelp("one");

    // after mutation
    t.checkExpect(deque1.size(), 1);
    t.checkExpect(deque1.header.next, new Node<String>("aaa", s1, s1));
    t.checkExpect(deque2.size(), 5);
    t.checkExpect(deque2.header.next, new Node<String>("xyz", abc, s2));
    t.checkExpect(deque3.size(), 6);
    t.checkExpect(deque3.header.next, new Node<String>("one", two, s3));
  }

  // tests the addAtTail method
  void testAddAtTail(Tester t) {
    initData();

    // before mutation 
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.prev, s1);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque2.header.prev, new Node<String>("def", s2, cde));
    t.checkExpect(deque3.size(), 5);
    t.checkExpect(deque3.header.prev, new Node<String>("six", s3, five));

    // mutation
    deque1.addAtTail("bbb");
    deque2.addAtTail("efg");
    deque3.addAtTail("seven");

    // after mutation
    t.checkExpect(deque1.size(), 1);
    t.checkExpect(deque1.header.prev, new Node<String>("bbb", s1, s1));
    t.checkExpect(deque2.size(), 5);
    t.checkExpect(deque2.header.prev, new Node<String>("efg", s2, def));
    t.checkExpect(deque3.size(), 6);
    t.checkExpect(deque3.header.prev, new Node<String>("seven", s3, six));
  }

  // tests the addAtTailHelp method
  void testAddAtTailHelp(Tester t) {
    initData();

    // before mutation 
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.prev, s1);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque2.header.prev, new Node<String>("def", s2, cde));
    t.checkExpect(deque3.size(), 5);
    t.checkExpect(deque3.header.prev, new Node<String>("six", s3, five));

    // mutation
    s1.addAtTailHelp("bbb");
    s2.addAtTailHelp("efg");
    s3.addAtTailHelp("seven");

    // after mutation
    t.checkExpect(deque1.size(), 1);
    t.checkExpect(deque1.header.prev, new Node<String>("bbb", s1, s1));
    t.checkExpect(deque2.size(), 5);
    t.checkExpect(deque2.header.prev, new Node<String>("efg", s2, def));
    t.checkExpect(deque3.size(), 6);
    t.checkExpect(deque3.header.prev, new Node<String>("seven", s3, six));
  }

  // tests the removeFromHead method
  void testRemoveFromHead(Tester t) {
    initData();

    // before mutation 
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.next, s1);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque2.header.next, new Node<String>("abc", bcd, s2));
    t.checkExpect(deque3.size(), 5);
    t.checkExpect(deque3.header.next, new Node<String>("two", three, s3));

    // mutation
    t.checkException(new RuntimeException("Cannot remove a node from an empty list."), 
        deque1, "removeFromHead");  
    deque2.removeFromHead();
    deque3.removeFromHead();

    // after mutation
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.next, s1);
    t.checkExpect(deque2.size(), 3);
    t.checkExpect(deque2.header.next, new Node<String>("bcd", cde, s2));
    t.checkExpect(deque3.size(), 4);
    t.checkExpect(deque3.header.next, new Node<String>("three", four, s3));
  }

  // tests the removeFromHeadHelp method
  void testRemoveFromHeadHelp(Tester t) {
    initData();

    // before mutation 
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.next, s1);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque2.header.next, new Node<String>("abc", bcd, s2));
    t.checkExpect(deque3.size(), 5);
    t.checkExpect(deque3.header.next, new Node<String>("two", three, s3));

    // mutation
    t.checkException(new RuntimeException("Cannot remove a node from an empty list."), 
        s1, "removeFromHeadHelp");  
    abc.removeFromHeadHelp();
    two.removeFromHeadHelp();

    // after mutation
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.next, s1);
    t.checkExpect(deque2.size(), 3);
    t.checkExpect(deque2.header.next, new Node<String>("bcd", cde, s2));
    t.checkExpect(deque3.size(), 4);
    t.checkExpect(deque3.header.next, new Node<String>("three", four, s3));
  }

  // tests the removeFromTail method
  void testRemoveFromTail(Tester t) {
    initData();

    // before mutation 
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.prev, s1);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque2.header.prev, new Node<String>("def", s2, cde));
    t.checkExpect(deque3.size(), 5);
    t.checkExpect(deque3.header.prev, new Node<String>("six", s3, five));

    // mutation
    t.checkException(new RuntimeException("Cannot remove a node from an empty list."), 
        deque1, "removeFromTail");  
    deque2.removeFromTail();
    deque3.removeFromTail();

    // after mutation
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.prev, s1);
    t.checkExpect(deque2.size(), 3);
    t.checkExpect(deque2.header.prev, new Node<String>("cde", s2, bcd));
    t.checkExpect(deque3.size(), 4);
    t.checkExpect(deque3.header.prev, new Node<String>("five", s3, four));
  }

  // tests the removeFromTail method
  void testRemoveFromTailHelp(Tester t) {
    initData();

    // before mutation 
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.prev, s1);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque2.header.prev, new Node<String>("def", s2, cde));
    t.checkExpect(deque3.size(), 5);
    t.checkExpect(deque3.header.prev, new Node<String>("six", s3, five));

    // mutation
    t.checkException(new RuntimeException("Cannot remove a node from an empty list."), 
        s1, "removeFromTailHelp");  
    def.removeFromTailHelp();
    six.removeFromTailHelp();

    // after mutation
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque1.header.prev, s1);
    t.checkExpect(deque2.size(), 3);
    t.checkExpect(deque2.header.prev, new Node<String>("cde", s2, bcd));
    t.checkExpect(deque3.size(), 4);
    t.checkExpect(deque3.header.prev, new Node<String>("five", s3, four));
  }

  // tests the find method with 3 different predicates
  void testFind(Tester t) {
    initData();
    t.checkExpect(deque1.find(new HasLength3()), s1);
    t.checkExpect(deque1.find(new ContainsE()), s1);
    t.checkExpect(deque1.find(new ContainsD()), s1);
    t.checkExpect(deque2.find(new HasLength3()), abc);
    t.checkExpect(deque2.find(new ContainsE()), cde);
    t.checkExpect(deque2.find(new ContainsD()), bcd);
    t.checkExpect(deque3.find(new HasLength3()), two);
    t.checkExpect(deque3.find(new ContainsE()), three);
    t.checkExpect(deque3.find(new ContainsD()), s3);
  }
}
