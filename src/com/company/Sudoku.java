package com.company;

import java.util.ArrayList;

public class Sudoku {
    private int[][] board = new int[9][9];
    private boolean[][] analyzedRow = new boolean[9][9];
    private boolean[][] analyzedCol = new boolean[9][9];
    private boolean[][][] analyzedCell = new boolean[3][3][9];
    private boolean[][] solvedCell = new boolean[3][3];
    private boolean[] solvedRow = new boolean[9];
    private boolean[] solvedCol = new boolean[9];

    protected Sudoku(String values) {
        board = parseString(values);
    }

    private int[][] parseString(String string) {
        int counter = -1;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                counter++;
                this.board[i][j] = string.charAt(counter) - '0';
            }
        }
        return this.board;
    }

    @Override
    public String toString() {
        String LETTERSPACE = repeat(" ", 4);
        String MYTAB = repeat(" ",6);
        String NUMSPACE = repeat(" ",2);
        StringBuilder result = new StringBuilder();
        result.append(MYTAB + LETTERSPACE + "A B C   D E F   G H I\n");
        for (int i = 0; i < board.length; i++) {
            if (i == 0 || i == 3 || i == 6) {
                result.append(NUMSPACE + MYTAB + repeat("-", 25) + "\n");
            }
            for (int j = 0; j < board[i].length; j++) {
                if (j == 3 || j == 6) {
                    result.append("| " + board[i][j] + " ");
                }
                else if (j == 0) {
                    result.append(MYTAB + (i + 1) + " | " + board[i][j] + " ");
                }
                else if (j == board[i].length - 1) {
                    result.append(board[i][j] + " |\n");
                }
                else {
                    result.append(board[i][j] + " ");
                }
            }
        }
        result.append(NUMSPACE + MYTAB + repeat("-", 25));
        String finalResult = result.toString().replaceAll("0", "-");
        return finalResult;
    }

    private String repeat(String string, int repetition) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < repetition; i++) {
            builder.append(string);
        }
        return builder.toString();
    }

    //------------------------------------------------------------------------------------------------------------------
    //Solver for Sudoku...
    private boolean analyzeRow() {
        int value;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                value = this.board[row][col];
                if (value > 0 && value <= 9) {
                    if (!this.analyzedRow[row][value - 1]) {
                        this.analyzedRow[row][value - 1] = true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean analyzeCol() {
        int value;
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                value = this.board[row][col];
                if (value > 0 && value <= 9) {
                    if (!this.analyzedCol[col][value - 1]) {
                        this.analyzedCol[col][value - 1] = true;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean analyzeCell() {
        int value;
        for (int cellRow = 0; cellRow < 3; cellRow++) {
            for (int cellCol = 0; cellCol < 3; cellCol++) {
                for (int i = 0; i < 9; i++) {
                    value = this.board[cellRow * 3 + i / 3][cellCol * 3 + i % 3];
                    if (value > 0 && value <= 9) {
                        if (!this.analyzedCell[cellRow][cellCol][value - 1]) {
                            this.analyzedCell[cellRow][cellCol][value - 1] = true;
                        }
                        else {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void updateAnalyzed(int value, int row, int col) {
        if (value >= 1 && value <= 9) {
            this.analyzedRow[row][value - 1] = true;
            this.analyzedCol[col][value - 1] = true;
            this.analyzedCell[row / 3][col / 3][value - 1] = true;
        }
    }

    private void resetAnalyzed(int row, int col, int value) {
        if (value >= 1 && value <= 9) {
            this.analyzedRow[row][value - 1] = false;
            this.analyzedCol[col][value - 1] = false;
            this.analyzedCell[row / 3][col / 3][value - 1] = false;
        }
    }

    private int findValue(boolean[] arr) {
        int counter = 0;
        int setValue = 0;
        for (int value = 1; value <= 9; value++) {
            if (!arr[value - 1]) {
                counter++;
                setValue = value;
            }
        }
        if (counter == 1) {
            return setValue;
        }
        else {
            return -1;
        }
    }

    private int findBlock(boolean[][] matrix, boolean[] doneBlocks, int value) {
        int counter = 0;
        int setBlock = 0;
        for (int i = 0; i < 9; i++) {
            if (!doneBlocks[i] && !matrix[i][value - 1]) {
                counter++;
                setBlock = i;
            }
        }
        if (counter == 1) {
            return setBlock;
        }
        else {
            return -1;
        }
    }

    private int findOnlyInCell(int cellRow, int cellCol) {
        boolean isUpdate = false;
        int solvedCounter = 0;
        int row;
        int col;
        int i;
        int value;
        int setValue;
        int setBlock;
        boolean[][] tmp = new boolean[9][9];
        boolean[] doneBlocks = new boolean[9];
        // Find the value for a certain block...
        for (i = 0; i < 9; i++) {
            row = cellRow * 3 + i / 3;
            col = cellCol * 3 + i % 3;
            if (this.board[row][col] == 0) {
                for (value = 1; value <= 9; value++) {
                    tmp[i][value - 1] = analyzedRow[row][value - 1] || analyzedCol[col][value - 1] || analyzedCell[cellRow][cellCol][value - 1];
                }
                setValue = findValue(tmp[i]);
                if (setValue != -1) {
                    this.board[row][col] = setValue;
                    updateAnalyzed(setValue, row, col);
                    isUpdate = true;
                }
            }
            else {
                solvedCounter++;
                doneBlocks[i] = true;
            }
        }
        // Find the block for a certain value...
        if (solvedCounter < 9) {
            for (value = 1; value <= 9; value++) {
                setBlock = findBlock(tmp, doneBlocks, value);
                if (setBlock != -1) {
                    row = cellRow * 3 + setBlock / 3;
                    col = cellCol * 3 + setBlock % 3;
                    this.board[row][col] = value;
                    updateAnalyzed(value, row, col);
                    isUpdate = true;
                }
            }
        }
        if (solvedCounter == 9) {
            return 0;
        }
        else {
            if (isUpdate) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    private int findOnlyInRow(int row) {
        boolean isUpdate = false;
        int solvedCounter = 0;
        int col;
        int value;
        int setValue;
        int setBlock;
        boolean[][] tmp = new boolean[9][9];
        boolean[] doneBlocks = new boolean[9];
        // Find the value for a certain block...
        for (col = 0; col < 9; col++) {
            if (this.board[row][col] == 0) {
                for (value = 1; value <= 9; value++) {
                    tmp[col][value - 1] = analyzedRow[row][value - 1] || analyzedCol[col][value - 1] || analyzedCell[row / 3][col / 3][value - 1];
                }
                setValue = findValue(tmp[col]);
                if (setValue != -1) {
                    this.board[row][col] = setValue;
                    updateAnalyzed(setValue, row, col);
                    isUpdate = true;
                }
            }
            else {
                solvedCounter++;
                doneBlocks[col] = true;
            }
        }
        // Find the block for a certain value...
        if (solvedCounter < 9) {
            for (value = 1; value <= 9; value++) {
                setBlock = findBlock(tmp, doneBlocks, value);
                if (setBlock != -1) {
                    this.board[row][setBlock] = value;
                    updateAnalyzed(value, row, setBlock);
                    isUpdate = true;
                }
            }
        }
        if (solvedCounter == 9) {
            return 0;
        }
        else {
            if (isUpdate) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    private int findOnlyInCol(int col) {
        boolean isUpdate = false;
        int solvedCounter = 0;
        int row;
        int value;
        int setValue;
        int setBlock;
        boolean[][] tmp = new boolean[9][9];
        boolean[] doneBlocks = new boolean[9];
        // Find the value for a certain block...
        for (row = 0; row < 9; row++) {
            if (this.board[row][col] == 0) {
                for (value = 1; value <= 9; value++) {
                    tmp[row][value - 1] = analyzedRow[row][value - 1] || analyzedCol[col][value - 1] || analyzedCell[row / 3][col / 3][value - 1];
                }
                setValue = findValue(tmp[row]);
                if (setValue != -1) {
                    this.board[row][col] = setValue;
                    updateAnalyzed(setValue, row, col);
                    isUpdate = true;
                }
            }
            else {
                solvedCounter++;
                doneBlocks[row] = true;
            }
        }
        // Find the block for a certain value...
        if (solvedCounter < 9) {
            for (value = 1; value <= 9; value++) {
                setBlock = findBlock(tmp, doneBlocks, value);
                if (setBlock != -1) {
                    this.board[setBlock][col] = value;
                    updateAnalyzed(value, setBlock, col);
                    isUpdate = true;
                }
            }
        }
        if (solvedCounter == 9) {
            return 0;
        }
        else {
            if (isUpdate) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    protected boolean solve() {
        int result;
        int solvedCount = 0;
        int unProcCell;
        int unProcRow;
        int unProcCol;
        boolean finish = false;
        while (!finish) {
            solvedCount = 0;
            unProcCell = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!this.solvedCell[i][j]) {
                        result = findOnlyInCell(i, j);
                        if (result == 0) {
                            this.solvedCell[i][j] = true;
                            solvedCount++;
                        }
                        else {
                            if (result == -1) {
                                unProcCell++;
                            }
                        }
                    }
                    else {
                        solvedCount++;
                        unProcCell++;
                    }
                }
            }
            if (solvedCount == 9) {
                finish = true;
            }

            solvedCount = 0;
            unProcRow = 0;
            for (int i = 0; i < 9; i++) {
                if (!this.solvedRow[i]) {
                    result = findOnlyInRow(i);
                    if (result == 0) {
                        this.solvedRow[i] = true;
                        solvedCount++;
                    }
                    else {
                        if (result == -1) {
                            unProcRow++;
                        }
                    }
                }
                else {
                    solvedCount++;
                    unProcRow++;
                }
            }
            if (solvedCount == 9) {
                finish = true;
            }

            solvedCount = 0;
            unProcCol = 0;
            for (int i = 0; i < 9; i++) {
                if (!this.solvedCol[i]) {
                    result = findOnlyInCol(i);
                    if (result == 0) {
                        this.solvedCol[i] = true;
                        solvedCount++;
                    }
                    else {
                        if (result == -1) {
                            unProcCol++;
                        }
                    }
                }
                else {
                    solvedCount++;
                    unProcCol++;
                }
            }
            if (solvedCount == 9) {
                finish = true;
            }
            if (unProcCol == 9 && unProcCell == 9 && unProcRow == 9) {
                finish = true;
            }
        }
        if (solvedCount == 9) {
            return true;
        }
        else {
            return false;
        }
    }

    //Solver Ends...
    //------------------------------------------------------------------------------------------------------------------


    //------------------------------------------------------------------------------------------------------------------
    //Brute Force Solver for Sudoku...

    private EmptyBlockInfo[] findBlockInfo() {
        ArrayList<EmptyBlockInfo> blockList = new ArrayList<EmptyBlockInfo>();
        int counter;
        EmptyBlockInfo block;
        boolean tmp;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                counter = 0;
                if (this.board[row][col] == 0) {
                    block = new EmptyBlockInfo(row, col);
                    for (int value = 1; value <= 9; value++) {
                        tmp = analyzedRow[row][value - 1] || analyzedCol[col][value - 1] || analyzedCell[row / 3][col / 3][value - 1];
                        if (!tmp) {
                            counter++;
                        }
                    }
//                    block.setCountPossibilities(counter);
                    blockList.add(block);
                }
            }
        }
//        blockList.sort(Comparator.comparingInt(EmptyBlockInfo::getCountPossibilities));
        EmptyBlockInfo[] result = new EmptyBlockInfo[blockList.size()];
        return blockList.toArray(result);
    }

    protected boolean bruteForce() {
        EmptyBlockInfo[] blocks = findBlockInfo();
        boolean finished = false;
        boolean tmp;
        int row;
        int col;
        int i = 0;
        boolean flag;
        int preVal = 0;
        while (!finished) {
            row = blocks[i].getRow();
            col = blocks[i].getCol();
            preVal = blocks[i].getValue();
            if (preVal != 0) {
                this.board[row][col] = 0;
                resetAnalyzed(row, col, preVal);
                blocks[i].setValue(0);
            }
            flag = false;
            for (int value = preVal + 1; value <= 9; value++) {
                tmp = analyzedRow[row][value - 1] || analyzedCol[col][value - 1] || analyzedCell[row / 3][col / 3][value - 1];
                if (!tmp) {
                    this.board[row][col] = value;
                    blocks[i].setValue(value);
                    updateAnalyzed(value, row, col);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                //backtrace
                i--;
                if (i == -1) {
                    finished = true;
                }
            }
            else {
                i++;
                if (i == blocks.length) {
                    finished = true;
                }
            }
        }
        if (i == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    //Brute Force Solver Ends...
    //------------------------------------------------------------------------------------------------------------------

    //User Interface Methods...

    public boolean checkValidBoard() {
        if (!analyzeRow() || !analyzeCol() || !analyzeCell()) {
            return false;
        }
        return true;
    }
}
