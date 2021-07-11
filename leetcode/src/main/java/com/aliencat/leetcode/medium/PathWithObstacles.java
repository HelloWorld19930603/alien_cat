package com.aliencat.leetcode.medium;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 面试题 08.02. 迷路的机器人
 * 设想有个机器人坐在一个网格的左上角，网格 r 行 c 列。
 * 机器人只能向下或向右移动，但不能走到一些被禁止的网格（有障碍物）。
 * 设计一种算法，寻找机器人从左上角移动到右下角的路径。
 * 网格中的障碍物和空位置分别用 1 和 0 来表示。
 * <p>
 * 返回一条可行的路径，路径由经过的网格的行号和列号组成。左上角为 0 行 0 列。如果没有可行的路径，返回空数组。
 * 示例 1:
 * 输入:
 * [[0,0,0],[0,1,0],[0,0,0]]
 * 输出: [[0,0],[0,1],[0,2],[1,2],[2,2]]
 * 解释:
 * 输入中标粗的位置即为输出表示的路径，即
 * 0行0列（左上角） -> 0行1列 -> 0行2列 -> 1行2列 -> 2行2列（右下角）
 * 说明：r 和 c 的值均不超过 100。
 */
public class PathWithObstacles {

    List<List<Integer>> paths;

    /**
     * 动态规划 + 剪枝法
     * 假设f(x,y)位置能够达到，则必有f(x-1,y) || f(x,y-1) 亦可以到达
     * 反之，则不可达。
     */
    public List<List<Integer>> pathWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid.length < 1 || obstacleGrid[0][0] == 1) {
            return new ArrayList<>();
        }
        int r = obstacleGrid.length, c = obstacleGrid[0].length;
        /**
         *  visit二维数组 用来记录对应位置的访问状态
         *  默认 false - 未访问过或者可以到达目的,
         *  true - 从该点不可达目的,对其剪枝
         */
        boolean[][] visit = new boolean[r][c];
        paths = new LinkedList();  //链表添加元素效率更高
        findPath(r - 1, c - 1, visit, obstacleGrid);
        return paths;
    }

    /**
     * 判断当前位置是否可以通过
     * 1 不可过和 0 可以过
     */
    private boolean toPass(int r, int c, int[][] obstacleGrid) {
        if (r < 0 || r >= obstacleGrid.length || c < 0 || c >= obstacleGrid[0].length) {
            return false;
        }
        return obstacleGrid[r][c] != 1;
    }

    public boolean findPath(int r, int c, boolean[][] visit, int[][] obstacleGrid) {
        if (!toPass(r, c, obstacleGrid)) {
            return false;
        }
        if (visit[r][c]) {
            return false;
        }
        boolean success = false;
        if (r == 0 && c == 0) {
            success = true;
        }
        if (!success && r > 0 && toPass(r - 1, c, obstacleGrid)) {
            success = findPath(r - 1, c, visit, obstacleGrid);
        }
        if (!success && c > 0 && toPass(r, c - 1, obstacleGrid)) {
            success = findPath(r, c - 1, visit, obstacleGrid);
        }
        if (success) {
            addPath(r, c, paths);
        }
        visit[r][c] = !success;
        return success;
    }

    /**
     * 添加可达路径位置
     */
    public void addPath(int r, int c, List paths) {
        List list = new ArrayList(2);  //默认情况下ArrayList大小为10，这里设置为2减小内存
        list.add(r);
        list.add(c);
        paths.add(list);
    }
}
