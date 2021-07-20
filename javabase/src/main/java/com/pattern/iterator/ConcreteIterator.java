package com.pattern.iterator;

public class ConcreteIterator<T> implements Iterator<T> {

  private ConcreteAggregate<T> concreteAggregate;

  private int current = 0;

  public ConcreteIterator(ConcreteAggregate concreteAggregate) {
    this.concreteAggregate = concreteAggregate;
  }

  public boolean hasNext() {
    return current < concreteAggregate.count();
  }

  public T next() {
    T t = concreteAggregate.get(current);
    current++;
    return t;

  }

  public T first() {
    return concreteAggregate.get(0);
  }

  public T currentItem() {
    return concreteAggregate.get(current);
  }
}
