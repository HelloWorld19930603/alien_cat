package com.pattern.iterator;

/**
 * ������ģʽ
 * @param <T>
 */
public interface Iterator<T> {

     boolean hasNext();

     T next() ;

     T first();

     T currentItem();
}
