package com.aliencat.datastructure.tree;

import java.util.List;

public class TreeNodeWithBro<T> {

    private T data;
    private List<TreeNodeWithChild> children;
    private TreeNodeWithBro<T> bro;

    public T getData() {
        return data;
    }

    public TreeNodeWithBro(T data) {
        this.data = data;
    }

    public List<TreeNodeWithChild> getChild() {
        return children;
    }

    public TreeNodeWithBro<T> getBro() {
        return bro;
    }

    public void addChild(TreeNodeWithChild child) {
        children.add(child);
    }

    public void setBro(TreeNodeWithBro bro) {
        this.bro = bro;
    }
}
