package com.aliencat.datastructure.tree;

/**
 * 红黑树
 * 是一种结点带有颜色属性的二叉查找树，但它除了满足二叉查找树的特点外，还有以下要求：
 * <p>
 * 1. 节点是红色或黑色。
 * 2. 根是黑色。
 * 3. 所有叶子都是黑色（叶子是NIL节点）。
 * 4. 每个红色节点必须有两个黑色的子节点。（从每个叶子到根的所有路径上不能有两个连续的红色节点。）
 * 5. 从任一节点到其每个叶子的所有简单路径都包含相同数目的黑色节点。
 */
public class RedBlackTree<T extends Comparable<T>> {

    private RBTNode<T> root;    // 根结点
    public RedBlackTree() {
        root = null;
    }

    private RBTNode<T> parentOf(RBTNode<T> node) {
        return node != null ? node.parent : null;
    }

    private boolean colorOf(RBTNode<T> node) {
        return node == null || node.color;
    }

    private boolean isRed(RBTNode<T> node) {
        return (node != null) && (node.color == false);
    }

    private boolean isBlack(RBTNode<T> node) {
        return !isRed(node);
    }

    //颜色转换
    private void reverseColor(RBTNode<T> node) {
        if (node != null)
            node.color = !node.color;
    }

    private void setColor(RBTNode<T> node, boolean color) {
        if (node != null)
            node.color = color;
    }

    private void setParent(RBTNode<T> node, RBTNode<T> parent) {
        if (node != null)
            node.parent = parent;
    }

    /*
     * 前序遍历"红黑树"
     */
    private void preOrder(RBTNode<T> tree) {
        if (tree != null) {
            System.out.print(tree.value + " ");
            preOrder(tree.left);
            preOrder(tree.right);
        }
    }

    public void preOrder() {
        preOrder(root);
    }

    /*
     * 中序遍历"红黑树"
     */
    private void inOrder(RBTNode<T> tree) {
        if (tree != null) {
            inOrder(tree.left);
            System.out.print(tree.value + " ");
            inOrder(tree.right);
        }
    }

    public void inOrder() {
        inOrder(root);
    }

    /*
     * 后序遍历"红黑树"
     */
    private void postOrder(RBTNode<T> tree) {
        if (tree != null) {
            postOrder(tree.left);
            postOrder(tree.right);
            System.out.print(tree.value + " ");
        }
    }

    public void postOrder() {
        postOrder(root);
    }


    public RBTNode<T> search(T value) {
        return search(root, value);
    }

    /*
     * (递归实现)查找"红黑树x"中键值为value的节点
     */
    private RBTNode<T> search(RBTNode<T> x, T value) {
        if (x == null)
            return null;

        int cmp = value.compareTo(x.value);
        if (cmp < 0)
            return search(x.left, value);
        else if (cmp > 0)
            return search(x.right, value);
        else
            return x;
    }

    public RBTNode<T> iterativeSearch(T value) {
        return iterativeSearch(root, value);
    }
    /*
     * (非递归实现)查找"红黑树x"中键值为value的节点
     */
    private RBTNode<T> iterativeSearch(RBTNode<T> x, T value) {
        while (x != null) {
            int cmp = value.compareTo(x.value);
            if (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else
                return x;
        }

        return x;
    }



    /*
     * 查找最小结点：返回tree为根结点的红黑树的最小结点。
     */
    private RBTNode<T> minimum(RBTNode<T> tree) {
        if (tree == null)
            return null;

        while (tree.left != null)
            tree = tree.left;
        return tree;
    }

    public T minimum() {
        RBTNode<T> p = minimum(root);
        if (p != null)
            return p.value;

        return null;
    }

    /*
     * 查找最大结点：返回tree为根结点的红黑树的最大结点。
     */
    private RBTNode<T> maximum(RBTNode<T> tree) {
        if (tree == null)
            return null;

        while (tree.right != null)
            tree = tree.right;
        return tree;
    }

    public T maximum() {
        RBTNode<T> p = maximum(root);
        if (p != null)
            return p.value;

        return null;
    }

    /*
     * 找结点(x)的后继结点。即，查找"红黑树中数据值大于该结点"的"最小结点"。
     */
    public RBTNode<T> successor(RBTNode<T> x) {
        // 如果x存在右孩子，则"x的后继结点"为 "以其右孩子为根的子树的最小结点"。
        if (x.right != null)
            return minimum(x.right);

        // 如果x没有右孩子。则x有以下两种可能：
        // (01) x是"一个左孩子"，则"x的后继结点"为 "它的父结点"。
        // (02) x是"一个右孩子"，则查找"x的最低的父结点，并且该父结点要具有左孩子"，找到的这个"最低的父结点"就是"x的后继结点"。
        RBTNode<T> y = x.parent;
        while ((y != null) && (x == y.right)) {
            x = y;
            y = y.parent;
        }

        return y;
    }

    /*
     * 找结点(x)的前驱结点。即，查找"红黑树中数据值小于该结点"的"最大结点"。
     */
    public RBTNode<T> predecessor(RBTNode<T> x) {
        // 如果x存在左孩子，则"x的前驱结点"为 "以其左孩子为根的子树的最大结点"。
        if (x.left != null)
            return maximum(x.left);

        // 如果x没有左孩子。则x有以下两种可能：
        // (01) x是"一个右孩子"，则"x的前驱结点"为 "它的父结点"。
        // (01) x是"一个左孩子"，则查找"x的最低的父结点，并且该父结点要具有右孩子"，找到的这个"最低的父结点"就是"x的前驱结点"。
        RBTNode<T> y = x.parent;
        while ((y != null) && (x == y.left)) {
            x = y;
            y = y.parent;
        }

        return y;
    }

    /*
     * 对红黑树的节点(x)进行左旋转
     *
     *      px                              px
     *     /                               /
     *    x                               y
     *   /  \          ---->             / \
     *  lx   y                          x  ry
     *     /   \                       /  \
     *    ly   ry                     lx  ly
     *
     *
     */
    private void leftRotate(RBTNode<T> x) {
        // 设置x的右孩子为y
        RBTNode<T> y = x.right;

        // 将 “y的左孩子” 设为 “x的右孩子”；
        // 如果y的左孩子非空，将 “x” 设为 “y的左孩子的父亲”
        x.right = y.left;
        if (y.left != null)
            y.left.parent = x;

        // 将 “x的父亲” 设为 “y的父亲”
        y.parent = x.parent;

        if (x.parent == null) {
            this.root = y;            // 如果 “x的父亲” 是空节点，则将y设为根节点
        } else {
            if (x.parent.left == x)
                x.parent.left = y;    // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
            else
                x.parent.right = y;    // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
        }

        // 将 “x” 设为 “y的左孩子”
        y.left = x;
        // 将 “x的父节点” 设为 “y”
        x.parent = y;
    }

    /*
     * 对红黑树的节点(y)进行右旋转
     *
     *            py                               py
     *           /                                /
     *          y                                x
     *         /  \       ---->                 /  \
     *        x   ry                           lx   y
     *       / \                                   / \
     *      lx  rx                                rx  ry
     *
     */
    private void rightRotate(RBTNode<T> y) {
        // 设置x是当前节点的左孩子。
        RBTNode<T> x = y.left;

        // 将 “x的右孩子” 设为 “y的左孩子”；
        // 如果"x的右孩子"不为空的话，将 “y” 设为 “x的右孩子的父亲”
        y.left = x.right;
        if (x.right != null)
            x.right.parent = y;

        // 将 “y的父亲” 设为 “x的父亲”
        x.parent = y.parent;

        if (y.parent == null) {
            this.root = x;            // 如果 “y的父亲” 是空节点，则将x设为根节点
        } else {
            if (y == y.parent.right)
                y.parent.right = x;    // 如果 y是它父节点的右孩子，则将x设为“y的父节点的右孩子”
            else
                y.parent.left = x;    // (y是它父节点的左孩子) 将x设为“x的父节点的左孩子”
        }

        // 将 “y” 设为 “x的右孩子”
        x.right = y;

        // 将 “y的父节点” 设为 “x”
        y.parent = x;
    }

    /*
     * 红黑树插入重新平衡
     * 若向红黑树中插入节点之后破坏了红黑树的约束，则通过对几种特定情况的判断进行再平衡操作
     */
    private void rebalance(RBTNode<T> node) {
        RBTNode<T> parent, gParent;

        // 若“父节点存在，并且父节点的颜色是红色”
        while (((parent = parentOf(node)) != null) && isRed(parent)) {
            gParent = parentOf(parent);

            //若“父节点”是“祖父节点的左孩子”
            if (parent == gParent.left) {
                // 情况1：叔叔节点是红色
                RBTNode<T> uncle = gParent.right;
                if ((uncle != null) && isRed(uncle)) {
                    reverseColor(uncle);
                    reverseColor(parent);
                    reverseColor(gParent);
                    node = gParent;
                    continue;
                }

                // 情况2：叔叔是黑色，且当前节点是右孩子，则把它左旋转化为情况3的场景
                if (parent.right == node) {
                    RBTNode<T> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // 情况3：叔叔是黑色，且当前节点是左孩子。
                reverseColor(parent);
                reverseColor(gParent);
                rightRotate(gParent);
            } else {    //若“父节点”是“祖父节点的右孩子”
                // 情况1：叔叔节点是红色
                RBTNode<T> uncle = gParent.left;
                if ((uncle != null) && isRed(uncle)) {
                    reverseColor(uncle);
                    reverseColor(parent);
                    reverseColor(gParent);
                    node = gParent;
                    continue;
                }

                // 情况2：叔叔是黑色，且当前节点是左孩子，则把它左旋转化为情况3的场景
                if (parent.left == node) {
                    RBTNode<T> tmp;
                    rightRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // 情况3：叔叔是黑色，且当前节点是右孩子。
                reverseColor(parent);
                reverseColor(gParent);
                leftRotate(gParent);
            }
        }

        // 若当前节点父节点不存在则此节点必为根节点，且为红色，则置为黑
        if(parent == null && !node.color)
            reverseColor(node);
    }


    /*
     * 新建结点(其值为value)，默认颜色为红，并将其插入到红黑树中
     */
    public void insert(T value) {
        RBTNode<T> node = new RBTNode<T>(value, false, null, null, null);
        insert(node);
    }

    /*
     * 将结点插入到红黑树中
     */
    private void insert(RBTNode<T> node) {
        int cmp;
        RBTNode<T> y = null;
        RBTNode<T> x = this.root;

        // 将节点添加到叶子节点上。
        while (x != null) {
            y = x;
            cmp = node.value.compareTo(x.value);
            if (cmp < 0)
                x = x.left;
            else
                x = x.right;
        }

        node.parent = y;
        if (y != null) {
            cmp = node.value.compareTo(y.value);
            if (cmp < 0)
                y.left = node;
            else
                y.right = node;
        } else {
            this.root = node;
        }

        // 检查是否破坏红黑树的五个特性，并进行平衡操作
        rebalance(node);
    }


    /*
     * 删除结点
     */
    public boolean remove(T value) {
        RBTNode<T> node;
        if ((node = search(root, value)) != null) { //找到键值对应的节点
            remove(node);
            return true;
        }else {
            return false;  //表示没有找到该值对应的节点
        }
    }

    /*
     * 删除结点(node)
     */
    private void remove(RBTNode<T> node) {
        RBTNode<T>  parent;
        //情况1： 被删除节点的"左右孩子都不为空"的情况。
        if ((node.left != null) && (node.right != null)) {
            // 被删节点的后继节点。(称为"取代节点")
            // 用它来取代"被删节点"的位置
            RBTNode<T> replace =  node.right;
            while (replace.left != null) {   //这里我找的是比node大的数中最小的那个
                replace = replace.left;
            }
            if(!replace.color){ //取代节点为红色，直接覆盖被取代节点值即可
                node.value = replace.value;
                replace.parent.left = null;  //删除取代节点
            }else{  //取代节点为黑色
                node.value = replace.value;
                parent = replace.parent;
                parent.left = null;  //删除取代节点
                removeRebalance(parent);    //从取代节点的父节点开始重新平衡，其实是转换成了情况4的处理逻辑
            }
        }else if (node.left != null) { //情况2：左子节点不为空，右子节点为空,此节点必为黑色，子节点必为红
            node.value = node.left.value; //左子节点直接覆盖被当前节点值即可
            node.left = null;  //删除左子节点
        } else if(node.right !=null){ //情况3：右子节点不为空，左子节点为空,此节点必为黑色，子节点必为红
            node.value = node.right.value; //右子节点直接覆盖被当前节点值即可
            node.right = null;  //删除右子节点
        }else{  //情况4：左右子节点都为空
            if(node.parent == null){ //根节点情况
                root = null;
            }else if(!node.color){ //节点为红色情况，删除不会破坏平衡
                if(node.parent.left == node){ //不是父节点左子节点必为右子节点
                    node.parent.left = null;
                }else{
                    node.parent.right = null;
                }
            }else{  //节点为黑色情况
                parent = node.parent;
                removeRebalance(parent);
                if(parent.left == node){
                    parent.left = null;
                }else{
                    parent.right = null;
                }
            }
        }
    }

    /*
     * 在从红黑树中删除黑色的非空叶子节点之后(红黑树失去平衡)，调用该方法；
     * 目的是将它重新塑造成一颗红黑树。
     */
    private void removeRebalance(RBTNode<T> node) {
        RBTNode<T> brother;
        RBTNode<T> parent;
        while (node != this.root) {
            parent = node.parent;
            if(parent.left == node){
                brother = parent.right;
                if(!brother.color){ //兄弟为红色，则侄子必全为黑
                    parent.left = null;
                    reverseColor(brother); //变为黑色
                    leftRotate(parent);    //旋转parent使之平衡
                    break;
                }else{   //兄弟为黑色，则侄子要么为空，要么为红
                    if(brother.right != null){
                        reverseColor(brother.right); //右侄子变为黑色
                        leftRotate(parent);
                        if(!parent.color){  //父节点为红
                            reverseColor(parent);
                            reverseColor(brother);
                        }
                        break;
                    }else if(brother.left != null){
                        reverseColor(brother.left);
                        rightRotate(brother);
                        leftRotate(parent);
                        if(!parent.color){  //父节点为红
                            reverseColor(parent);
                            reverseColor(brother);
                        }
                    }else{
                        reverseColor(parent.right);
                        node = parent;
                    }
                }
            }else{
                brother = parent.left;
                if(!brother.color){ //兄弟为红色，则侄子必全为黑
                    parent.right = null;
                    reverseColor(brother); //变为黑色
                    leftRotate(parent);    //旋转parent使之平衡
                    break;
                }else{   //兄弟为黑色，则侄子要么为空，要么为红
                    if(brother.left != null){
                        reverseColor(brother.left); //右侄子变为黑色
                        leftRotate(parent);
                        if(!parent.color){  //父节点为红
                            reverseColor(parent);
                            reverseColor(brother);
                        }
                        break;
                    }else if(brother.right != null){
                        reverseColor(brother.right);
                        rightRotate(brother);
                        leftRotate(parent);
                        if(!parent.color){  //父节点为红
                            reverseColor(parent);
                            reverseColor(brother);
                        }
                    }else{
                        reverseColor(parent.left);
                        node = parent;
                    }
                }

            }
        }
    }

    /*
     * 销毁红黑树
     */
    public void clear() {
        root = null;
    }


    /*
     * 打印"红黑树"
     *
     * value        -- 节点的键值
     * direction  --  0，表示该节点是根节点;
     *               -1，表示该节点是它的父结点的左孩子;
     *                1，表示该节点是它的父结点的右孩子。
     */
    private void print(RBTNode<T> tree, T value, int direction) {

        if (tree != null) {
            if (direction == 0)    // tree是根节点
                System.out.printf("%2d(Black) is root\n", tree.value);
            else                // tree是分支节点
                System.out.printf("%2d(%s) is %2d's %6s child\n", tree.value, isRed(tree) ? "Red" : "Black", value, direction == 1 ? "right" : "left");
            print(tree.left, tree.value, -1);
            print(tree.right, tree.value, 1);
        }
    }

    public void print() {
        if (root != null)
            print(root, root.value, 0);
    }

    public class RBTNode<T extends Comparable<T>> {
        boolean color;        // 颜色,false为红色，true为黑色；默认为false
        T value;              // 键值
        RBTNode<T> left;      // 左子结点
        RBTNode<T> right;     // 右子结点
        RBTNode<T> parent;    // 父结点

        public RBTNode(T value, boolean color, RBTNode<T> parent, RBTNode<T> left, RBTNode<T> right) {
            this.value = value;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public T getValue() {
            return value;
        }

        public String toString() {
            return value + "(" + (this.color ? "Black" : "Red") + ")";
        }
    }
}