package com.pattern.iterator;

public interface Aggregate<T> {
    Iterator createIterator();
}
