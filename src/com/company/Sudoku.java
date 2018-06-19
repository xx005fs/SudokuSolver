package com.company;

import java.util.ArrayList;
import java.util.Comparator;

public class Sudoku {
    private int[][] board = new int[9][9];
    private int[][] usrBoard = new int[9][9];
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
        String replacedString = string.replaceAll("\\W", "");
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                counter++;
                this.board[i][j] = replacedString.charAt(counter) - '0';
            }
        }
        return this.board;
    }

    protected int[][] getBoard() {
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

    public String output() {
        String LETTERSPACE = repeat(" ", 4);
        String MYTAB = repeat(" ",6);
        String NUMSPACE = repeat(" ",2);
        StringBuilder result = new StringBuilder();
        int blockValue;
        result.append(MYTAB + LETTERSPACE + "A B C   D E F   G H I\n");
        for (int i = 0; i < board.length; i++) {
            if (i == 0 || i == 3 || i == 6) {
                result.append(NUMSPACE + MYTAB + repeat("-", 25) + "\n");
            }
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] > 0) {
                    blockValue = board[i][j];
                }
                else {
                    if (usrBoard[i][j] > 0) {
                        blockValue = usrBoard[i][j];
                    }
                    else {
                        blockValue = 0;
                    }
                }
                if (j == 3 || j == 6) {
                    result.append("| " + blockValue + " ");
                }
                else if (j == 0) {
                    result.append(MYTAB + (i + 1) + " | " + blockValue + " ");
                }
                else if (j == board[i].length - 1) {
                    result.append(blockValue + " |\n");
                }
                else {
                    result.append(blockValue + " ");
                }
            }
        }
        result.append(NUMSPACE + MYTAB + repeat("-", 25));
        String finalResult = result.toString().replaceAll("0", "-");
        return finalResult;
    }

    private String repeat(String s, int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(s);
        }
        return builder.toString();
    }

    public boolean presentInRow(int value, int row) {
        int[] rowArray = this.board[row];
        for (int i = 0; i < rowArray.length; i++) {
            if (value == rowArray[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean presentInCol(int value, int col) {
        int[] columnArray = new int[9];
        for (int i = 0; i < 9; i++) {
            columnArray[i] = this.board[i][col];
        }
        for (int i = 0; i < 9; i++) {
            if (value == columnArray[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean presentInCell(int value, int col, int row) {
        int[] cellArray = new int[9];
        int celCol = col / 3;
        int celRow = row / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cellArray[i * 3 + j] = this.board[celRow * 3 + i][celCol * 3 + j];
            }
        }
        for (int i = 0; i < cellArray.length; i++) {
            if (value == cellArray[i]) {
                return true;
            }
        }
        return false;
    }

    public String getBoardAsString() {
        int counter = 0;
        String result = "";
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                counter++;
                result += board[i][j];
                if (counter % 3 == 0 && counter != 81) {
                    result += " ";
                }
            }
        }
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    //Solver for sudoku...
    private void analyzeRow() {
        int value;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                value = this.board[row][col];
                if (value > 0 && value <= 9) {
                    this.analyzedRow[row][value - 1] = true;
                }
            }
        }
    }

    private void analyzeCol() {
        int value;
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                value = this.board[row][col];
                if (value > 0 && value <= 9) {
                    this.analyzedCol[col][value - 1] = true;
                }
            }
        }
    }

    private void analyzeCell() {
        int value;
        for (int cellRow = 0; cellRow < 3; cellRow++) {
            for (int cellCol = 0; cellCol < 3; cellCol++) {
                for (int i = 0; i < 9; i++) {
                    value = this.board[cellRow * 3 + i / 3][cellCol * 3 + i % 3];
                    if (value > 0 && value <= 9) {
                        this.analyzedCell[cellRow][cellCol][value - 1] = true;
                    }
                }
            }
        }
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
        analyzeRow();
        analyzeCol();
        analyzeCell();
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

    private ArrayList<EmptyBlockInfo> findBlockInfo() {
        ArrayList<EmptyBlockInfo> blockList = new ArrayList<>();
        EmptyBlockInfo block;
        boolean tmp;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (this.board[row][col] == 0) {
                    block = new EmptyBlockInfo(row, col);
                    for (int value = 1; value <= 9; value++) {
                        tmp = analyzedRow[row][value - 1] || analyzedCol[col][value - 1] || analyzedCell[row / 3][col / 3][value - 1];
                        if (!tmp) {
                            block.addValue(value);
                        }
                    }
                    blockList.add(block);
                }
            }
        }
        blockList.sort(Comparator.comparingInt(EmptyBlockInfo::countValues));
        return blockList;
    }

    public boolean bruteForce() {
        ArrayList<EmptyBlockInfo> blocks = findBlockInfo();
        boolean tmp;
        int row;
        int col;
        int value;
        for (int i = 0; i < blocks.size(); i++) {
            row = blocks.get(i).getRow();
            col = blocks.get(i).getCol();
            for (int j = 0; j < blocks.get(i).countValues(); j++) {
                value = blocks.get(i).getPossibilities().get(j);
                tmp = analyzedRow[row][value - 1] || analyzedCol[col][value - 1] || analyzedCell[row / 3][col / 3][value - 1];
                if (!tmp) {
                    this.board[row][col] = value;
                    updateAnalyzed(value, row, col);
                    if (bruteForce()) {
                        return true;
                    }
                    else {
                        this.board[row][col] = 0;
                        resetAnalyzed(row, col, value);
                    }
                }
            }
            return false;
        }
        return true;
    }

    //Brute Force Solver Ends...
    //------------------------------------------------------------------------------------------------------------------

    //User Interface Methods...

    public boolean add(int col, int row, int value) {
        if (this.board[row][col] == 0 && value != 0) {
            if (!presentInCol(value, col) && !presentInRow(value, row) && !presentInCell(value, col, row)) {
                this.usrBoard[row][col] = value;
                return true;
            }
        }
        return false;
    }

    public int remove(int col, int row) {
        int result;
        if (this.board[row][col] != 0) {
            return 0;
        }
        result = this.usrBoard[row][col];
        this.usrBoard[row][col] = 0;
        return result;
    }

    public boolean isSolved() {
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.board[i][j] != 0) {
                    counter++;
                    this.usrBoard[i][j] = this.board[i][j];
                }
                else {
                    if (this.usrBoard[i][j] != 0) {
                        counter++;
                    }
                }
            }
        }
        if (counter != 81) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            int[] array = new int[9];
            int value;
            for (int j = 0; j < 9; j++) {
                value = this.usrBoard[i][j];
                array[value - 1] = 1;
            }
            for (int j = 0; j < 9; j++) {
                if (array[j] == 0) {
                    return false;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            int[] array = new int[9];
            int value;
            for (int j = 0; j < 9; j++) {
                value = this.usrBoard[j][i];
                array[value - 1] = 1;
            }
            for (int j = 0; j < 9; j++) {
                if (array[j] == 0) {
                    return false;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] array = new int[9];
                int value;
                for (int k = 0; k < 9; k++) {
                    value = this.usrBoard[i * 3 + k / 3][j * 3 + k % 3];
                    array[value - 1] = 1;
                }
                for (int k = 0; k < 9; k++) {
                    if (array[k] == 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
