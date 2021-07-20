package com.pattern.iterator;

public class IteratorTest {
  public static void main(String[] args) {
    //

      ConcreteAggregate<Integer> aggregate = new ConcreteAggregate<Integer>();

      aggregate.set(0);
      aggregate.set(1);
      aggregate.set(2);
      aggregate.set(3);
      aggregate.set(4);
      aggregate.set(5);
      aggregate.set(6);
      aggregate.set(7);
      aggregate.set(8);

      Iterator<Integer> iterator = aggregate.createIterator();

      while (iterator.hasNext()){
          Integer next = iterator.next();
          System.out.println(next);
      }

  }
}
