package com.aliencat.datastructure.table;

import org.junit.Assert;

import java.util.LinkedList;

/**
 * 跳表
 * 增加了向前指针的链表叫作跳表。
 * 跳表全称叫做跳跃表，简称跳表。
 * 跳表是一个随机化的数据结构，实质就是一种可以进行二分查找的有序链表。
 * 跳表在原有的有序链表上面增加了多级索引，通过索引来实现快速查找。
 * 跳表不仅能提高搜索性能，同时也可以提高插入和删除操作的性能。
 */
public class SkipList {

    private static final int MAX_LEVEL = 1 << 4;

    private static final float SKIPLIST_P = 0.5f;
    private final Node head = new Node();
    private int levelCount = 1;

    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        int max = 10000;
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < max; i++) {
            skipList.add(i);
            linkedList.add(i);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            Assert.assertEquals(i, skipList.get(i).data);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("SkipList耗时：" + (endTime - startTime));


        startTime = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            Assert.assertEquals(i, linkedList.get(i));
        }
        endTime = System.currentTimeMillis();
        System.out.println("LinkedList耗时：" + (endTime - startTime));
        //skipList.printAll();
    }

    /**
     * 获取元素
     *
     * @param value
     * @return
     */
    public Node get(int value) {
        Node p = head, forward;
        for (int i = levelCount - 1; i >= 0; --i) {
            while ((forward = p.forwards[i]) != null && forward.data < value) {
                p = forward;
            }
        }

        if ((forward = p.forwards[0]) != null && forward.data == value) {
            return forward;
        } else {
            return null;
        }
    }

    /**
     * 添加元素
     *
     * @param value
     */
    public void add(int value) {
        int level = randomLevel();
        Node newNode = new Node();
        newNode.data = value;
        newNode.maxLevel = level;
        Node[] update = new Node[level];
        for (int i = 0; i < level; ++i) {
            update[i] = head;
        }

        // 记录小于插入值的每一级最大值
        Node p = head, forward;
        for (int i = level - 1; i >= 0; --i) {
            while ((forward = p.forwards[i]) != null && forward.data < value) {
                p = forward;
            }
            update[i] = p;// 在搜索路径中使用更新保存节点
        }

        // 搜索路径节点下一个节点成为新节点
        for (int i = 0; i < level; ++i) {
            newNode.forwards[i] = update[i].forwards[i];
            update[i].forwards[i] = newNode;
        }

        // 更新节点高度
        if (levelCount < level) levelCount = level;
    }

    /**
     * 删除元素
     *
     * @param value
     */
    public void remove(int value) {
        Node[] update = new Node[levelCount];
        Node p = head, forward;
        for (int i = levelCount - 1; i >= 0; --i) {
            while ((forward = p.forwards[i]) != null && forward.data < value) {
                p = forward;
            }
            update[i] = p;
        }

        if (p.forwards[0] != null && p.forwards[0].data == value) {
            for (int i = levelCount - 1; i >= 0; --i) {
                if (update[i].forwards[i] != null && update[i].forwards[i].data == value) {
                    update[i].forwards[i] = update[i].forwards[i].forwards[i];
                }
            }
        }
    }

    // 理论来讲，一级索引中元素个数应该占原始数据的 50%，二级索引中元素个数占 25%，三级索引12.5% ，一直到最顶层。
    // 因为这里每一层的晋升概率是 50%。对于每一个新插入的节点，都需要调用 randomLevel 生成一个合理的层数。
    // 该 randomLevel 方法会随机生成 1~MAX_LEVEL 之间的数，且 ：
    //        50%的概率返回 1
    //        25%的概率返回 2
    //      12.5%的概率返回 3 ...
    private int randomLevel() {
        int level = 1;

        while (Math.random() < SKIPLIST_P && level < MAX_LEVEL)
            level += 1;
        return level;
    }

    public void printAll() {
        Node p = head;
        while (p.forwards[0] != null) {
            System.out.print(p.forwards[0] + " ");
            p = p.forwards[0];
        }
        System.out.println();
    }

    public class Node {
        private final Node[] forwards = new Node[MAX_LEVEL];
        private int data = -1;
        private int maxLevel = 0;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ data: ");
            builder.append(data);
            builder.append("; levels: ");
            builder.append(maxLevel);
            builder.append(" }");

            return builder.toString();
        }
    }
}
