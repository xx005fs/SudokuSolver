package com.company;

public class EmptyBlockInfo {
    private int value = 0;
    private int row;
    private int col;
//    private int countPossibilities = 0;

    public EmptyBlockInfo(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

//    public void setCountPossibilities(int countPossibilities) {
//        this.countPossibilities = countPossibilities;
//    }
}
