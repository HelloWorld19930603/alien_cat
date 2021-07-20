package com.pattern.iterator;

import java.util.ArrayList;
import java.util.List;

public class ConcreteAggregate<T> implements Aggregate<T> {

  private List<T> list = new ArrayList<T>();

  public Iterator<T> createIterator() {
    return new ConcreteIterator<T>(this);
  }

  public int count() {
    return list.size();
  }

  public T get(int index) {
    return list.get(index);
  }

  public void set(int index, T value) {
    list.add(index, value);
  }

  public boolean set(T value) {
    return list.add(value);
  }
}
