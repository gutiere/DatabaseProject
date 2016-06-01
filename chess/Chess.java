package chess;

public class Chess {

    private Board myBoard;
    private boolean myWhiteTurn;

    public Chess() {
        myBoard = new Board();
        myWhiteTurn = Math.random() < 0.5;
    }

    public boolean isWhiteTurn() {
        return myWhiteTurn;
    }

    public boolean move(char theOriginCol, int theOriginRow, char theDestCol, int theDestRow) {
        return myBoard.move(theOriginCol - 96, theOriginRow, theDestCol - 96, theDestRow);
    }


}
