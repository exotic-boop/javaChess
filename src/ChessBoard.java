/*Author: Oscar Åström
* Date: 2023 - 01 15
* Course: DD 1385
* */


import javax.swing.*;
import java.util.Objects;

public class ChessBoard implements BoardGame {
    int size = 8;
    private Square[][] squares;
    Piece selectedPiece;
    String currentPlayer = "White";
    String message;
    String statusMessage;
    Piece blackKing;
    Piece whiteKing;
    boolean gameOver = false;
    private JPanel containerPanel;
    private JTextField doYouWantToMove = new JTextField("Do you want this move?");
    private Square targetPredictedSquare;
    private Square originalPredictedSquare;
    private Piece predictedTargetPiece;

    public ChessBoard(JPanel containerPanel){
        this.containerPanel = containerPanel;
    }
    @Override
    public void buildBoard(Square[][] squares){
        this.squares = squares;
        this.blackKing = new King("King","Black","\u265A");
        this.whiteKing = new King("King","White","\u2654");


        for (int i = 0; i < size; i++){
            squares[1][i].addPiece(new Pawn("Pawn","Black","\u265F"));
            squares[6][i].addPiece(new Pawn("Pawn","White","\u2659"));
        }
            squares[0][3].addPiece(new Queen("Queen","Black","\u265B"));
            squares[0][0].addPiece(new Rook("Rook","Black","\u265C"));
            squares[0][7].addPiece(new Rook("Rook","Black","\u265C"));
            squares[0][2].addPiece(new Bishop("Bishop","Black","\u265D"));
            squares[0][5].addPiece(new Bishop("Bishop","Black","\u265D"));
            squares[0][1].addPiece(new Knight("Horse","Black","\u265E"));
            squares[0][6].addPiece(new Knight("Horse","Black","\u265E"));
            squares[0][4].addPiece(blackKing);
            squares[7][0].addPiece(new Rook("Rook","White","\u2656"));
            squares[7][7].addPiece(new Rook("Rook","White","\u2656"));
            squares[7][3].addPiece(new Queen("Queen","White","\u2655"));
            squares[7][5].addPiece(new Bishop("Bishop","White","\u2657"));
            squares[7][2].addPiece(new Bishop("Bishop","White","\u2657"));
            squares[7][6].addPiece(new Knight("Horse","White","\u2658"));
            squares[7][1].addPiece(new Knight("Horse","White","\u2658"));
            squares[7][4].addPiece(whiteKing);
    }


    @Override
    public void executeInput(int x, int y) {
        if(gameOver){
            return;
        }
        if (this.selectedPiece == null){
            selectPiece(x,y);
        }else moveSelectedPiece(x,y);
    }

    private void selectPiece(int x,int y){

        Square targetSquare =squares[x][y];
        if (targetSquare.checkIfEmpty()){
            message = "selected empty square";
            return;
        }

        var temp  = targetSquare.returnPiece();
        if (!Objects.equals(temp.color, currentPlayer)){
            message = "Wrong color its : " + currentPlayer +" turn!";
            return;
        }
        this.selectedPiece = temp;
        targetSquare.select();

        generateMoves();
    }

    private void generateMoves() {
        int numValidMoves = 0;
        int autoMoveX = 0;
        int autoMoveY = 0;
        for (int x = 0; x < 8; x++ ) {
            for (int y =0; y< 8; y++){
                if(selectedPiece.xPosition == x && selectedPiece.yPosition== y){
                    continue;
                }

                if (selectedPiece.isMovementValid(x, y, squares)){
                    if(predictMoveWillResultInCheck(x,y, selectedPiece)){
                       continue;
                    }
                    numValidMoves++;
                    autoMoveX = x;
                    autoMoveY = y;
                    squares[x][y].select();
                }
            }
        }

        if(numValidMoves == 1){
            predictMove(autoMoveX, autoMoveY, selectedPiece);
            int response = JOptionPane.showConfirmDialog(containerPanel, doYouWantToMove);
            undoPrediction(selectedPiece);
            if(response == 0){
                moveSelectedPiece(autoMoveX,autoMoveY);
            }
            else {
                clearSelections();
            }
        }
        else {
            message = "Selected piece :" + selectedPiece.name;
        }
    }

    private boolean predictMoveWillResultInCheck(int x, int y, Piece piece){
        if(x == piece.xPosition && y == piece.yPosition){
            return false;
        }
        predictMove(x,y,piece);
        boolean isCheck = lookForCheck(piece.color);
        undoPrediction(piece);
        return isCheck;
    }

    private void predictMove(int x, int y, Piece piece){
        targetPredictedSquare = squares[x][y];
        originalPredictedSquare = squares[piece.xPosition][piece.yPosition];
        predictedTargetPiece = targetPredictedSquare.returnPiece();

        movePieceToSquare(targetPredictedSquare, originalPredictedSquare, true);
    }

    private void undoPrediction(Piece piece){
        targetPredictedSquare.removePiece();
        if(predictedTargetPiece != null){
            targetPredictedSquare.addPiece(predictedTargetPiece);
        }

        originalPredictedSquare.addPiece(piece);
    }

    private Piece getKing(String color){
        return color == whiteKing.color ? whiteKing : blackKing;
    }
    private boolean lookForCheck(String color){
        Piece king = getKing(color);
        for(int x = 0; x < 8; x++){
            for ( int y = 0; y < 8; y++){
                if (squares[x][y].checkIfEmpty() == false){
                    if(squares[x][y].piece.color != color && squares[x][y].piece.isMovementValid(king.xPosition, king.yPosition, squares)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkForCheckMate(String color){
        for (int x = 0; x < 8; x++ ) {
            for (int y =0; y< 8; y++){
                Square square = squares[x][y];
                if(square.checkIfEmpty()){
                    continue;
                }
                Piece piece = square.returnPiece();
                if(!Objects.equals(piece.color, color)){
                    continue;
                }
                if (pieceHasValidMove(piece)){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean pieceHasValidMove(Piece piece){
        for (int x = 0; x < 8; x++ ) {
            for (int y =0; y< 8; y++){
                if(piece.xPosition == x && piece.yPosition == y){
                    continue;
                }

                if (piece.isMovementValid(x, y, squares)) {
                    if(!predictMoveWillResultInCheck(x,y, piece)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void unSelectAllSquare(){
        for (int x = 0; x < 8;x++){
            for (int y = 0; y < 8; y++){
                squares[x][y].unSelect();
            }
        }
    }
    private void promotion(Square square){
        if (this.selectedPiece.color == "White" && selectedPiece.name == "Pawn"){
            if (square.x == 0 ){
                square.removePiece();
                square.addPiece(new Queen("Queen", "White","\u2655"));
            }
        } else if (this.selectedPiece.color == "Black"&& selectedPiece.name == "Pawn") {
            if(square.x == 7) {
                square.removePiece();
                square.addPiece(new Queen("Queen", "Black", "\u265B"));
            }
        }
    }
    private void swapPlayer(){
        if(Objects.equals(currentPlayer, "White")){
            currentPlayer = "Black";
        }else currentPlayer = "White";
    }

    private void moveSelectedPiece(int x, int y){
        Square targetSquare = squares[x][y];
        if(!targetSquare.isSelected()){
            message = "Invalid move";
            return;
        }

        if(x == selectedPiece.xPosition && y == selectedPiece.yPosition ){
            clearSelections();
            return;
        }

        Square selectedSquare = squares[selectedPiece.xPosition][selectedPiece.yPosition];
        movePieceToSquare(targetSquare, selectedSquare, false);

        String opponent = getOpponentColor(currentPlayer);
        if(lookForCheck(opponent)){
            this.message = "CHECK";
            if(checkForCheckMate(opponent)){
                this.message = "CHECKMate";
                gameOver = true;
            }
        }
        clearSelections();
        if(!gameOver){
            swapPlayer();
            statusMessage = currentPlayer;
        }
    }

    private void clearSelections(){
        this.selectedPiece= null;
        unSelectAllSquare();
    }

    private String getOpponentColor(String color){
        return color == "White" ? "Black" : "White";
    }
    private void movePieceToSquare(Square targetSquare, Square selectedSquare, boolean predict) {
        if(selectedSquare.checkIfEmpty()){
            //System.err.println("Trying to move piece from empty square");
            return;
        }

        Piece pieceToMove = selectedSquare.returnPiece();
        if (!targetSquare.checkIfEmpty()){
            if (targetSquare.piece.color != selectedPiece.color){
                targetSquare.removePiece();
                message = "target Down";
            }
        }
        selectedSquare.removePiece();
        if(predict){
            targetSquare.addPiece(pieceToMove);
        }
        else {
            targetSquare.movePiece(pieceToMove);
        }

        promotion(targetSquare);
    }

    @Override
    public String getMessage() {
        return message;
    }


    @Override
    public String getStatus() {
        return statusMessage;
    }
}

