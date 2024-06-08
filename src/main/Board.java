package main;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {
    public int tileSize = 85;
    public int rows = 8;
    public int columns = 8;


    ArrayList<Piece> pieceList = new ArrayList<>();


    public Piece selectedPiece;

    Input input = new Input(this);

    public CheckScanner scanner = new CheckScanner(this);

    public int enPassantTile = -1;

    public Board(){
        this.setPreferredSize(new Dimension(columns*tileSize,rows*tileSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    public Piece getPiece(int columns,int rows){
        for(Piece piece : pieceList){
            if(columns==piece.col && rows==piece.row){
                return piece;
            }
        }
        return null;
    }


    public void MakeMove(Move move){

        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }else if(move.piece.name.equals("King")){
            moveKing(move);
        }
            move.piece.col = move.newColumn;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newColumn*tileSize;
            move.piece.yPos = move.newRow*tileSize;

            move.piece.isFirstMove = false;

            capture(move.capture);
        }


    private void moveKing(Move move){
        if(Math.abs(move.piece.col-move.piece.row)==2){
            Piece rock;
            if(move.piece.col<move.newColumn){
                rock = getPiece(7,move.piece.row);
                rock.col = 5;
            }else{
                rock = getPiece(0,move.piece.row);
                rock.col = 3;
            }
            rock.xPos = rock.col * tileSize;
        }
    }

    private void movePawn(Move move) {

        //enPassant
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if(getTileNum(move.newColumn,move.newRow) == enPassantTile){
            move.capture = getPiece(move.newColumn,move.newRow+colorIndex);
        }
        if(Math.abs(move.piece.row - move.newRow) == 2){
            enPassantTile = getTileNum(move.newColumn,move.newRow+colorIndex);
        }else {
            enPassantTile = -1;
        }


        //Promotions
        int colorIndexProm = move.piece.isWhite ? 0 : 7;
        if(move.newRow == colorIndexProm ){
            promotePawn(move);
        }
    }

    private void promotePawn(Move move) {
        pieceList.add(new Queen(this,move.newColumn,move.newRow,move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece){
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move){
        if(sameTeam(move.piece,move.capture)){
           return false;
        }

        if(!move.piece.isValidMovement(move.newColumn,move.newRow)){
            return false;
        }
        if(move.piece.moveCollidesWithPiece(move.newColumn,move.newRow)){
            return false;
        }

        if(scanner.isKingChecked(move)){
            return false;
        }

        return true;
    }

    public boolean sameTeam(Piece p1,Piece p2){
        if(p1==null || p2==null){
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public int getTileNum(int col,int row){
        return row*rows+col;
    }

    Piece findKing(boolean isWhite){
        for(Piece piece : pieceList){
            if(isWhite == piece.isWhite && piece.name.equals("King")){
                return piece;
            }
        }
        return null;
    }

    public void addPieces(){
        //black knights
        pieceList.add(new Knight(this,1,0,false));
        pieceList.add(new Knight(this,6,0,false));
        //white knights
        pieceList.add(new Knight(this,1,7,true));
        pieceList.add(new Knight(this,6,7,true));
        //black pawns
        for(int i=0;i<columns;i++){
            pieceList.add(new Pawn(this, i ,1,false));
        }
        //white pawns
        for(int i=0;i<columns;i++){
            pieceList.add(new Pawn(this, i ,6,true));
        }
        //black bishops
        pieceList.add(new Bishop(this,2,0,false));
        pieceList.add(new Bishop(this,5,0,false));
        //white bishops
        pieceList.add(new Bishop(this,2,7,true));
        pieceList.add(new Bishop(this,5,7,true));
        //black rocks
        pieceList.add(new Rock(this,0,0,false));
        pieceList.add(new Rock(this,7,0,false));
        //white rocks
        pieceList.add(new Rock(this,0,7,true));
        pieceList.add(new Rock(this,7,7,true));
        //black queen
        pieceList.add(new Queen(this,3,0,false));
        //white queen
        pieceList.add(new Queen(this,3,7,true));
        //black king
        pieceList.add(new King(this,4,0,false));
        //white king
        pieceList.add(new King(this,4,7,true));

    }

    public void paintComponent(Graphics g){
        Graphics2D graphics2D = (Graphics2D) g;

        for(int r=0;r<rows;r++){
            for(int c=0;c<columns;c++){
                graphics2D.setColor((c+r)%2==0 ? new Color(255,198,181) : new Color(170, 111, 53));
                graphics2D.fillRect(c*tileSize,r*tileSize,tileSize,tileSize);
            }
        }
        if(selectedPiece != null){
            for(int r=0;r<rows;r++){
                for(int c=0;c<columns;c++){

                    if(isValidMove(new Move(this,selectedPiece,c,r))){

                        graphics2D.setColor(new Color(68, 180, 57,190));
                        graphics2D.fillRect(c*tileSize,r*tileSize,tileSize,tileSize);
                    }
                }
            }
        }



        for(Piece piece : pieceList){
            piece.paint(graphics2D);
        }

    }
}