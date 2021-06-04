package com.aliencat.datastructure.tree;

/**
 * 二叉树
 */
public class BinaryTree<T extends Comparable<T>> {

    //根节点
    private Entry<T> root;
    //节点数量
    private int size = 0;

    public BinaryTree(){}

    /**
     * 添加元素
     */
    public T put(T item){
        //每次添加数据的时候都是从根节点向下遍历
        Entry<T> t = root;
        size++;
        if (t == null){
            //当前的叉树树的为空，将新节点设置为根节点，父节点为null
            root = new Entry<>(item,null);
            return root.item;
        }
        //自然排序结果，如果传入的数据小于当前节点返回-1，大于当前节点返回1，否则返回0
        int ret = 0;
        //记录父节点
        Entry<T> p = t;
        while (t != null){
            //与当前节点比较
            ret = item.compareTo(t.item);
            p = t;
            if (ret < 0)  //插入节点比当前节点小，把当前节点设置为左子节点，然后与左子比较，以此类推找到合适的位置
                t = t.left;
            else if (ret > 0) //大于当前节点
                t = t.right;
            else {
                t.item = item; //相等就把旧值覆盖掉
                return t.item;
            }
        }
        Entry<T> e = new Entry<>(item,p); //创建新节点
        //根据比较结果将新节点放入合适的位置
        if (ret < 0)
            p.left = e;
        else
            p.right = e;
        return e.item;
    }

    public void print(){
        midIterator(root);
    }

    /**
     * 中序遍历
     */
    public void midIterator(Entry<T> e){
        if (e != null){
            midIterator(e.left);
            System.out.print(e.item + " ");
            midIterator(e.right);
        }
    }

    /**
     * 获取根节点
     */
    public Entry<T> getRoot(){return root;}

    /**
     * 前序遍历
     */
    public void prevIterator(Entry<T> e){
        if (e != null) {
            System.out.print(e.item + " ");
            prevIterator(e.left);
            prevIterator(e.right);
        }
    }

    /**
     * 后续遍历
     */
    public void subIterator(Entry<T> e){
        if (e != null) {
            subIterator(e.left);
            subIterator(e.right);
            System.out.print(e.item + " ");
        }
    }

    /**
     * 获取节点节点
     */
    private Entry<T> getEntry(T item){
        Entry<T> t = root;
        int ret;
        //从根节点开始遍历
        for (;t != null;){
            ret = item.compareTo(t.item);
            if (ret < 0)
                t = t.left;
            else if (ret > 0)
                t = t.right;
            else
                return t;
        }
        return null;
    }

    /**
     * 判断是否存在该元素
     */
    public boolean contains(T item){
        return getEntry(item) != null;
    }

    /**
     * 删除元素
     * 删除元素如果细分的话，可以分为4中：没有子节点，只有左节点，只有右节点，有两个子节点
     * 1）没有子节点这种情况比较简单，直接删除就可以了
     * 2）只有左节点或右节点，这两种情况处理方式是一致的，只是方向相反，所以在一起讲了，
     * 将删除节点的父节点的左节点（右节点）指向删除节点的子节点，将左节点（右节点）指向删除节点的父节点
     * 3）有两个子节点，这种情况相对来说比较复杂一点：
     * 找到删除节点的前驱节点或后继节点，然后将前驱或后继节点的值赋给删除节点，然后将前驱或后继节点删除。本质是删除前驱或后继节点
     * 前驱节点的特点：
     * 1）删除的左子节点没有右子节点，那么左子节点即为前驱节点
     * 2）删除节点的左子节点有右子节点，那么最右边的最后一个节点即为前驱节点
     * 后继节点的特点：
     * 与前驱节点刚好相反，总是右子节点，或则右子节点的最左子节点;例如：删除节点为c ，那么前驱节点为 m，后继节点为n
     *                                          a
     *                                       /     \
     *                                    b          c
     *                                  / \         /  \
     *                                d    e       f    g
     *                              /  \  / \     / \   / \
     *                             h   i j  k    l  m  n   o
     */
    public boolean remove(T item){
        //获取删除节点
        Entry<T> delEntry = getEntry(item);
        if (delEntry == null) return false;
        //删除节点的父节点
        Entry<T> p = delEntry.parent;
        size--;
        //情况1：没有子节点
        if (delEntry.left == null && delEntry.right == null){
            if (delEntry == root){ //删除节点为根节点
                root = null;
            }else {//非根节点
                //删除的是父节点的左节点
                if (delEntry == p.left){
                    p.left = null;
                }else {//删除右节点
                    p.right = null;
                }
            }
        }else if (delEntry.right == null){ //情况2：删除的节点只有左节点
            Entry<T> lc = delEntry.left;
            //删除的节点为根节点，将删除节点的左节点设置成根节点
            if (p == null) {
                lc.parent = null;
                root = lc;
            } else {//非根节点
                if (delEntry == p.left){//删除左节点
                    p.left = lc;
                }else {//删除右节点
                    p.right = lc;
                }
                lc.parent = p;
            }
        }else if (delEntry.left == null){ //情况3：删除节点只有右节点
            Entry<T> rc = delEntry.right;
            //删除根节点
            if (p == null) {
                rc.parent = null;
                root = rc;
            }else {//删除非根节点
                if (delEntry == p.left)
                    p.left = rc;
                else
                    p.right = rc;
                rc.parent = p;
            }
        }else {//情况4：删除节点有两个子节点 有两个节点,找到后继节点，将值赋给删除节点，然后将后继节点删除掉即可
            Entry<T> successor = successor(delEntry);//获取到后继节点
            delEntry.item = successor.item;
            //后继节点为右子节点
            if (delEntry.right == successor){
                //右子节点有右子节点
                if (successor.right != null) {
                    delEntry.right = successor.right;
                    successor.right.parent = delEntry;
                }else {//右子节点没有子节点
                    delEntry.right = null;
                }
            }else {//后继节点必定是左节点
                successor.parent.left = null;
            }
            return true;
        }
        //让gc回收
        delEntry.parent = null;
        delEntry.left = null;
        delEntry.right = null;
        return true;
    }

    /**
     * 查找后继节点
     */
    private Entry<T> successor(Entry<T> delEntry) {
        Entry<T> r = delEntry.right;//r节点必定不为空
        while (r.left != null){
            r = r.left;
        }
        return r;
    }

    public int size(){return size;}

    public boolean isEmpty(){return size == 0;}

    public void clear(){
        clear(getRoot());
        root = null;
    }

    private void clear(Entry<T> e){
        if (e != null){
            clear(e.left);
            e.left = null;
            clear(e.right);
            e.right = null;
        }
    }

    final class Entry<T extends Comparable<T>>{
        //保存的数据
        private T item;
        //左子树
        private Entry<T> left;
        //右子树
        private Entry<T> right;
        //父节点
        private Entry<T> parent;
        Entry(T item,Entry<T> parent){
            this.item = item;
            this.parent = parent;
        }
    }

}