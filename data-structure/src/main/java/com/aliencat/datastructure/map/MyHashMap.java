package com.aliencat.datastructure.map;

import java.io.Serializable;

public class MyHashMap<K, V> implements MyMap<K, V>, Serializable {

    /**
     * 默认容量
     */
    static final int DEFAULT_CAPACITY = 16;
    /**
     * 负载因子
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    int threshold;
    /**
     * 当前key索引位置
     */
    int keyIndex;
    /**
     * 保存Node<K,V>节点的数组
     */
    Node<K, V>[] table;
    /**
     * 存储当前Map容量的大小
     */
    int size;

    /**
     * 计算Hash值
     *
     * @param key
     * @return
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : Math.abs((h = key.hashCode()) ^ (h >>> 16));
    }

    @Override
    public void put(K key, V value) {
        Node<K, V> node;
        if (table == null) {
            table = resize();
            //table里面为空的情况
            node = new Node<K, V>(hash(key), key, value, null);
            table[keyIndex] = node;
            size++;
        } else {
            table = resize();
            //table不为空时
            Node<K, V> n;
            //是否hash冲突
            boolean hashConflict = false;
            for (int i = 0; i < table.length; i++) {
                n = table[i];
                if (n != null) {
                    if (n.hash == hash(key)) {
                        hashConflict = true;
                        //hash相等时
                        while (n != null) {
                            if (n.key.equals(key)) {
                                //hash相等并且key也相等，直接替换原来的值就行了
                                n.value = value;
                                table[i] = n;
                                size++;
                            } else {
                                node = new Node<K, V>(hash(key), key, value, null);
                                node.next = n;
                                table[i] = node;
                                size++;
                            }
                            n = n.next;
                        }
                    }
                }

            }
            if (!hashConflict) {
                //没有hash冲突，直接put
                node = new Node<K, V>(hash(key), key, value, null);
                table[++keyIndex] = node;
                size++;

            }
        }
    }

    @Override
    public V get(K key) {
        MyHashMap.Node<K, V> node;
        return (node = getNode(key)) == null ? null : node.value;
    }

    /**
     * 获取Node
     *
     * @param key
     * @return
     */
    final MyHashMap.Node<K, V> getNode(Object key) {
        if (table != null) {
            for (int i = 0; i < table.length; i++) {
                Node<K, V> node = table[i];
                if (node != null) {
                    //hash相等
                    if (node.hash == hash(key)) {
                        while (node != null) {
                            if (node.key.equals(key)) {
                                //hash和key都相等时`
                                return node;
                            }
                            node = node.next;
                        }
                    }
                }

            }
        }
        return null;
    }

    /**
     * 计算Hash值
     *
     * @param key
     * @return
     */

    /**
     * 扩容
     *
     * @return
     */
    final Node<K, V>[] resize() {
        Node<K, V>[] newTable;
        int newCapacity, oldCapacity;
        if (table == null) {
            keyIndex = 0;
            threshold = (int) (DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR);
            table = (MyHashMap.Node<K, V>[]) new MyHashMap.Node[DEFAULT_CAPACITY];
            newTable = table;
        } else {
            oldCapacity = table.length;
            if (table.length > threshold) {
                //扩容两倍
                newCapacity = threshold *= 2;
                newTable = (MyHashMap.Node<K, V>[]) new MyHashMap.Node[newCapacity];
                //把原来的table移动到newTable
                int newIndex = 0;
                for (int i = 0; i < oldCapacity; i++) {
                    Node<K, V> node = table[i];
                    //咱们这只使用最简单的方式、不考虑其他情况、不涉及红黑树
                    if (node != null) {
                        if (node.next == null) {
                            newTable[newIndex] = node;
                        } else {
                            MyHashMap.Node<K, V> loHead = null, loTail = null, hiHead = null, hiTail = null, next;
                            do {
                                next = node.next;
                                if (node.hash == 0) {
                                    if (loTail == null) {
                                        loHead = node;
                                    } else {
                                        loTail.next = node;
                                    }
                                    loTail = node;
                                } else {
                                    if (hiTail == null) {
                                        hiHead = node;
                                    } else {
                                        hiTail.next = node;
                                    }
                                    hiTail = node;
                                }
                            } while ((node = next) != null);
                            if (loTail != null) {
                                loTail.next = null;
                                newTable[newIndex] = loHead;
                            }
                            if (hiTail != null) {
                                hiTail.next = null;
                                newTable[newIndex + oldCapacity] = hiHead;
                            }
                        }
                    }
                    newIndex++;
                }
            } else {
                newTable = table;
            }
        }

        return newTable;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Node 实现MyMap Entry接口
     *
     * @param <K>
     * @param <V>
     */
    static class Node<K, V> implements MyMap.Entry<K, V> {
        //hash值
        final int hash;
        // key
        final K key;
        // value
        V value;
        // next节点
        MyHashMap.Node<K, V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return value;
        }

    }
}

