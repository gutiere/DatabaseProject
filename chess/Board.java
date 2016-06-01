/*
         black
    R N B K Q B N R
    P P P P P P P P
    - - - - - - - -
    - - - - - - - -
    - - - - - - - -
    - - - - - - - -
    p p p p p p p p
    r n b k q b n r
         white
*/

package chess;

import java.lang.String;

public class Board {

    private static final String NEW_BOARD = "rnbkqbnrpppppppp--------------------------------PPPPPPPPRNBKQBNR";
    private char[][] myBoard;

    public Board() {
        myBoard = new char[8][8];
        setBoard(NEW_BOARD);
    }

    public boolean move(char theOriginCol, int theOriginRow, char theDestCol, int theDestRow) {
        return move(theOriginCol - 96, theOriginRow, theDestCol - 96, theDestRow);
    }

    public boolean move(int theOriginCol, int theOriginRow, int theDestCol, int theDestRow) {
        boolean success = false;
        if (checkRules(theOriginCol, theOriginRow, theDestCol, theDestRow)) {
            char temp = myBoard[theOriginRow - 1][theOriginCol - 1];
            myBoard[theOriginRow - 1][theOriginCol - 1] = '-';
            myBoard[theDestRow - 1][theDestCol - 1] = temp;
            success = true;
        }
        return success;
    }

    private boolean checkRules(int theOriginCol, int theOriginRow, int theDestCol, int theDestRow) {
        boolean abide = true;
        abide = abide && checkVacancy(theDestCol, theDestRow);
        return abide;
    }

    private boolean checkVacancy(int theDestCol, int theDestRow) {
        return myBoard[theDestRow][theDestCol] == '-';
    }

    private void setBoard(String theBoard) {
        int counter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                myBoard[i][j] = theBoard.charAt(counter++);
            }
        }
    }

    private String getBoard() {
        String board = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board += myBoard[i][j];
            }
        }
        return board;
    }

    public String toString() {
        String board = "\n";
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                board += (char)myBoard[i][j];
                board += ' ';
            }
            board += '\n';
        }
        return board;
    }
}
