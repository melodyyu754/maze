/*
 * THIS IS THE ICOLLECTION FILE.
 * It contains the ICollection interface, as well as the Stack and Queue
 * classes that implement the ICollection interface. This interface
 * has 3 methods: isEmpty, add, and remove. These methods are then tested
 * for both the stack and the queue in our examples class.
 */

import tester.Tester;

// Represents a mutable collection of items
interface ICollection<T> {

  // Is this collection empty?
  boolean isEmpty();

  // EFFECT: adds the item to the collection
  void add(T item);

  // Returns the first item of the collection
  // EFFECT: removes that first item
  T remove();
}

// stacks are have a deque as a field, and when you add something
// to a stack, you add it to the head.
class Stack<T> implements ICollection<T> {
  Deque<T> contents;

  Stack() {
    this.contents = new Deque<T>();
  }

  public boolean isEmpty() {
    return this.contents.size() == 0;
  }

  public T remove() {
    return this.contents.removeFromHead();
  }

  public void add(T item) {
    this.contents.addAtHead(item);
  }
}

//queues are have a deque as a field, and when you add something
//to a queue, you add it to the tail.
class Queue<T> implements ICollection<T> {
  Deque<T> contents;

  Queue() {
    this.contents = new Deque<T>();
  }

  public boolean isEmpty() {
    return this.contents.size() == 0;
  }

  public T remove() {
    return this.contents.removeFromHead();
  }

  public void add(T item) {
    this.contents.addAtTail(item); // NOTE: Different from Stack!
  }
}

// tests all ICollection methods
class ExamplesICollection {
  Sentinel<String> s1;
  Deque<String> deque1;
  Stack<String> stack1;
  Queue<String> queue1;

  Sentinel<String> s2;
  Node<String> two;
  Node<String> three;
  Node<String> four;
  Node<String> five;
  Node<String> six;
  Deque<String> deque2;
  Stack<String> stack2;
  Queue<String> queue2;

  // initializes the 3 example deques
  void initData() {

    // deque1 - empty deque (just a sentinel
    s1 = new Sentinel<String>();
    deque1 = new Deque<String>(s1);

    // initializing stack1 and queue1
    stack1 = new Stack<String>();
    this.stack1.contents = deque1;
    queue1 = new Queue<String>();
    this.queue1.contents = deque1;

    // deque2 - deque with 5 nodes and a sentinel
    s2 = new Sentinel<String>();
    two = new Node<String>("two");
    three = new Node<String>("three");
    four = new Node<String>("four");
    five = new Node<String>("five");
    six = new Node<String>("six");

    two = new Node<String>("two", three, s2);
    three = new Node<String>("three", four, two);
    four = new Node<String>("four",five, three);
    five = new Node<String>("five", six, four);
    six = new Node<String>("six", s2, five);

    deque2 = new Deque<String>(s2);

    // initializing stack2 and queue2
    stack2 = new Stack<String>();
    this.stack2.contents = deque2;
    queue2 = new Queue<String>();
    this.queue2.contents = deque2;
  }

  // tests isEmpty for both stack and queue
  void testIsEmpty(Tester t) {
    initData();
    t.checkExpect(stack1.isEmpty(), true);
    t.checkExpect(queue1.isEmpty(), true);
    t.checkExpect(stack2.isEmpty(), false);
    t.checkExpect(queue2.isEmpty(), false);
  }

  // tests add for both stack and queue
  void testAdd(Tester t) {
    initData();
    // before mutation (adding), what node we remove if we remove from head?
    t.checkException(new RuntimeException("Cannot remove a node from an empty list."), 
        stack1, "remove"); 
    t.checkException(new RuntimeException("Cannot remove a node from an empty list."), 
        queue1, "remove"); 
    t.checkExpect(stack2.remove(), "two");
    initData();
    t.checkExpect(queue2.remove(), "two");

    initData();
    // mutation
    stack1.add("hi");
    queue1.add("hi");
    stack2.add("hi");
    queue2.add("hi");

    // check side effects
    t.checkExpect(stack1.remove(), "hi");
    t.checkExpect(queue1.remove(), "hi");
    t.checkExpect(stack2.remove(), "hi");
    t.checkExpect(queue2.remove(), "two");
  }

  // tests remove for both stack and queue
  void testRemove(Tester t) {
    initData();

    // removing from empty stack/queue
    t.checkException(new RuntimeException("Cannot remove a node from an empty list."), 
        stack1, "remove"); 
    t.checkException(new RuntimeException("Cannot remove a node from an empty list."), 
        queue1, "remove"); 

    //removing from stack2/queue2
    t.checkExpect(stack2.remove(), "two");
    initData();
    t.checkExpect(queue2.remove(), "two");
  }
}