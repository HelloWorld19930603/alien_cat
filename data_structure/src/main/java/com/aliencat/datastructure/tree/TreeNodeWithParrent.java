package com.aliencat.datastructure.tree;

import javax.swing.tree.TreeNode;

public class TreeNodeWithParrent<T> {

    private T data;
    private TreeNodeWithParrent parent;

    public T getData(){
        return data;
    }

    public void setData(T data){
        this.data = data;
    }

    public TreeNodeWithParrent getParent(){
        return parent;
    }

    public void TreeNodeWithParrent(TreeNodeWithParrent parent){
        this.parent = parent;
    }

}
