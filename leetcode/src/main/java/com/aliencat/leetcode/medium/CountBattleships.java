package com.aliencat.leetcode.medium;


/**
 * 419. 甲板上的战舰
 * 给你一个大小为 m x n 的矩阵 board 表示甲板，其中，每个单元格可以是一艘战舰 'X' 或者是一个空位 '.' ，
 * 返回在甲板 board 上放置的 战舰 的数量。
 * 战舰 只能水平或者垂直放置在 board 上。
 * 换句话说，战舰只能按 1 x k（1 行，k 列）或 k x 1（k 行，1 列）的形状建造，
 * 其中 k 可以是任意大小。两艘战舰之间至少有一个水平或垂直的空位分隔 （即没有相邻的战舰）。
 *
 * 输入：board = [["X",".",".","X"],[".",".",".","X"],[".",".",".","X"]]
 * 输出：2
 * 示例 2：
 *
 * 输入：board = [["."]]
 * 输出：0
 * 进阶：你可以实现一次扫描算法，并只使用 O(1) 额外空间，并且不修改 board 的值来解决这个问题吗？
 */
public class CountBattleships {

    public int countBattleships(char[][] board) {
        int num = 0;
        for(int i = 0;i<board.length-1;i++){
            for(int j = 0;j<board[0].length-1;j++){
                if(board[i][j] == 'X' && board[i+1][j] == '.' && board[i][j+1] == '.')
                    num++;
            }
        }
        for(int i=0,j = board[0].length - 1;i<board.length-1;i++){
            if(board[i][j] == 'X' && board[i+1][j] == '.')
                num++;
        }
        for(int i=board.length-1,j = 0;j<board[0].length-1;j++){
            if(board[i][j] == 'X' &&board[i][j+1] == '.')
                num++;
        }
        if(board[board.length -1][board[0].length-1] == 'X')
            num++;
        return num;
    }
}
