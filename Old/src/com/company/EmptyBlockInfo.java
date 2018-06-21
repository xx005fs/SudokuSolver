package com.company;

import java.util.ArrayList;

public class EmptyBlockInfo {
    private ArrayList<Integer> possibilities = new ArrayList<>();
    private int row;
    private int col;

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

    public void addValue(int value) {
        possibilities.add(value);
    }

    public ArrayList<Integer> getPossibilities() {
        return possibilities;
    }

    public int countValues() {
        return possibilities.size();
    }
}
