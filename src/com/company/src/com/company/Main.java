package com.company;

import java.util.Scanner;

public class Main {
    private static String MYSPACE = "  ";
    private static Sudoku usrSudoku;
    //046 005 700 000 900 000 090 001 006 000 000 900 030 000 000 400 520 008 080 000 070 570 300 082 200 000 300
    //046005700000900000090001006000000900030000000400520008080000070570300082200000300
    //000000000000000000000000000000000000000000000000000000000000000000000000000000000

    public static void main(String[] args) {
        run();
    }

    private static void displayMenu() {
        System.out.println("\n" + MYSPACE + "Type in the string of the Sudoku you want to solve, a blank space in a board is a zero...\n");
    }

    private static void run() {
        Scanner scanner = new Scanner(System.in);
        boolean endGame = false;
        String usrChoice;
        int usrChoiceSolve;
        boolean solvedResult;
        boolean endContinue = false;
        while (!endGame) {
            displayMenu();
            usrChoice = scanner.nextLine().replaceAll("\\D", "");
            if (!usrChoice.matches("[0-9]{81}")) {
                System.out.println("\n" + MYSPACE + "Invalid input, try again...");
                continue;
            }
            usrSudoku = new Sudoku(usrChoice);
            System.out.println();
            System.out.println(usrSudoku);
            if (!usrSudoku.checkValidBoard()) {
                System.out.println("This Sudoku board is invalid...");
                break;
            }
            while (!endContinue) {
                System.out.println("\n" + MYSPACE + "Confirm if the board is correct...\n");
                System.out.println(MYSPACE + "1. YES...\n" + MYSPACE + "2. NO...");
                usrChoiceSolve = scanner.nextInt();
                if (usrChoiceSolve != 1 && usrChoiceSolve != 2) {
                    System.out.println("\n" + MYSPACE + "Invalid input, try again...");
                    continue;
                }
                if (usrChoiceSolve == 2) {
                    System.out.println(MYSPACE + "Exiting...");
                    endGame = true;
                    break;
                }
                System.out.println("\n" + MYSPACE + "Solving...");
                solvedResult = usrSudoku.solve();
                if (solvedResult) {
                    System.out.println();
                    System.out.println(usrSudoku);
                    System.out.println("\n" + MYSPACE + "The Sudoku is successfully solved...");
                }
                else {
                    solvedResult = usrSudoku.bruteForce();
                    if (solvedResult) {
                        System.out.println();
                        System.out.println(usrSudoku);
                        System.out.println("\n" + MYSPACE + "The Sudoku is successfully solved...");
                    }
                    else {
                        System.out.println("\n" + MYSPACE + "This Sudoku is invalid and it can't be solved...");
                    }
                }
                endContinue = true;
                endGame = true;
            }
        }
    }
}
