package main;

import pieces.Piece;

public class Move {
    int oldRow;
    int oldColumn;
    int newRow;
    int newColumn;

    Piece piece;
    Piece capture;

    public Move(Board board,Piece piece,int newColumn,int newRow) {
        this.oldRow = piece.row;
        this.oldColumn = piece.col;
        this.newColumn = newColumn;
        this.newRow = newRow;

        this.piece = piece;
        this.capture = board.getPiece(newColumn, newRow);
    }
}
