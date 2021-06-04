package com.aliencat.datastructure.tree;

import java.util.LinkedList;
import java.util.List;

public class TreeNodeWithChild<T> {

    private T data;
    private List<TreeNodeWithChild> children;

    public TreeNodeWithChild(T data) {
        this.data = data;
        this.children = new LinkedList<>();
    }

    public TreeNodeWithChild(T data, TreeNodeWithChild child) {
        this.data = data;
        this.children = new LinkedList<>();
        this.children.add(child);
    }
}
