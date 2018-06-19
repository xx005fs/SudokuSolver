package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Sudoku easy = new Sudoku("800400910003000000000003004000001040058000700070006800000002000000000160910060500");
    private static Sudoku medium = new Sudoku("980 254 000 640 073 200 020 000 900 030 000 006 060 000 090 700 642 803 400 026 010 390 008 402 172 000 000");
    private static Sudoku hard = new Sudoku("600 500 073 500 002 004 001 003 580 903 080 705 850 037 020 076 050 000 087 300 250 010 470 006 360 821 000");
    private static Sudoku expert = new Sudoku("046 005 700 000 900 000 090 001 006 000 000 900 030 000 000 400 520 008 080 000 070 570 300 082 200 000 300");
    private static Sudoku usrSudoku;

    public static void main(String[] args) {
        play();
    }

    private static void displayMenu() {
        System.out.println("\n1. Fill in a value...\n2. Undo last move...\n3. I give up. Show me the solution!\n4. Quit");
    }

    private static int difficulty() {
        Scanner scanner = new Scanner(System.in);
        int result = 1;
        System.out.println("\n1. Easy...\n2. Medium... \n3. Hard... \n4. Expert... \n5. Exit game...");
        String usrChoice = scanner.next().toLowerCase();
        if (usrChoice.equals("1")) {
            System.out.println(easy.toString());
            usrSudoku = new Sudoku(easy.getBoardAsString());
        }
        else if (usrChoice.equals("2")) {
            System.out.println(medium.toString());
            usrSudoku = new Sudoku(medium.getBoardAsString());
        }
        else if (usrChoice.equals("3")) {
            System.out.println(hard.toString());
            usrSudoku = new Sudoku(hard.getBoardAsString());
        }
        else if (usrChoice.equals("4")) {
            System.out.println(expert.toString());
            usrSudoku = new Sudoku(expert.getBoardAsString());
        }
        else if (usrChoice.equals("5")) {
            System.out.println("Exiting the game......");
            result = 0;
        }
        else {
            System.out.println("Invalid difficulty selection, try again...");
            result = -1;
        }
        return result;
    }

    private static boolean parseAdd(String str) {
        Scanner scanner = new Scanner(System.in);
        if (str.matches("[A-I][1-9]")) {
            int colASCII = (int)str.charAt(0);
            int rowASCII = (int)str.charAt(1);
            System.out.println("\nNow type in the value...");
            int usrValue = scanner.nextInt();
            if (usrValue <= 9 && usrValue >= 1) {
                if (usrSudoku.add(colASCII - 65, rowASCII - 49, usrValue)) {
                    System.out.println("Successfully added " + usrValue + " to " + str + "\n");
                    System.out.println(usrSudoku.output());
                    return true;
                }
            }
        }
        else {
            return false;
        }
        return false;
    }

    private static void play() {
        Scanner scanner = new Scanner(System.in);
        boolean endGame;
        int diffReturn;
        String location;
        boolean parseAddReturn;
        ArrayList<String> undoList = new ArrayList<>();
        while ((diffReturn = difficulty()) != 0) {
            endGame = false;
            if (diffReturn == 1) {
                while (!endGame) {
                    displayMenu();
                    String usrChoice = scanner.next();
                    if (usrChoice.equals("1")) {
                        System.out.println("\nType in the location to fill in...");
                        location = scanner.next().toUpperCase();
                        parseAddReturn = parseAdd(location);
                        boolean solvedStatus = usrSudoku.isSolved();
                        if (solvedStatus) {
                            System.out.println("\nYou Won!");
                            endGame = true;
                        }
                        if (!parseAddReturn) {
                            System.out.println("Can't fill this value in this place, try again...");
                            System.out.println(usrSudoku.output());
                        }
                        else {
                            undoList.add(location);
                        }
                    }
                    else if (usrChoice.equals("2")) {
                        int index = undoList.size() - 1;
                        if (index >= 0) {
                            String obj = undoList.get(index);
                            if (obj.matches("[A-I][1-9]")) {
                                usrSudoku.remove((int)obj.charAt(0) - 65, (int)obj.charAt(1) - 49);
                                System.out.println(usrSudoku.output());
                                undoList.remove(index);
                            }
                        }
                        else {
                            System.out.println("Can't undo...");
                        }
                    }
                    else if (usrChoice.equals("3")) {
                        if (!usrSudoku.solve()) {
                            usrSudoku.bruteForce();
                        }
                        System.out.println(usrSudoku.toString());
                        endGame = true;
                    }
                    else if (usrChoice.equals("4")) {
                        System.out.println("Quitting to difficulty selection...");
                        endGame = true;
                    }
                    else {
                        System.out.println("Invalid input, try again...");
                        endGame = false;
                    }
                }
            }
        }
    }
}
