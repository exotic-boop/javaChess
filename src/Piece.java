import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Piece {
    String name;
    String icon;
    int xPosition;
    int yPosition;
    String color;
    //ImageIcon iconImage;
    JLabel label;

    public Piece (String name, String color, String icon){
        String pad = " ";
        this.color = color;
        this.name = name;
        this.icon = icon;
        //this.iconImage = new ImageIcon(this.name + this.color + ".png");
        label = new JLabel(pad + icon);
        label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN,32));
        label.setHorizontalTextPosition(0);
        label.setVerticalAlignment(0);
    }

    public void setPosition(int x, int y){
        xPosition = x;
        yPosition = y;
    }

    public void movePiece(int x, int y){
        xPosition = x;
        yPosition = y;
    }

    public JLabel getLabel(){
        return this.label;
    }

     public boolean isMovementValid(int x,int y, Square[][] squares){
        return canPieceMove(x,y,squares);
     }

     protected boolean canPieceMove(int x,int y, Square[][] squares){
        Square square = squares[x][y];
        if (square.checkIfEmpty()){
            return true;
        }

        Piece piece = square.returnPiece();

        return piece.color != color;
     }

}
abstract class DirectionalPiece extends Piece{

    public DirectionalPiece(String name, String color, String icon) {
        super(name, color, icon);
    }
    protected boolean canMoveToTarget(int x,int y, Square[][] squares){
        int diffX = x - xPosition;
        int diffY = y - yPosition;
        int distanceY = Math.abs(diffY);
        int distanceX = Math.abs(diffX);
        int directionX = distanceX == 0 ? 0 : diffX > 0 ? 1 : -1;
        int directionY = distanceY == 0 ? 0 : diffY > 0 ? 1 : -1;
        int maxDistance = Math.max(distanceX, distanceY);

        for(int i = 1; i < maxDistance; i ++){
            var nextXPos = xPosition + i * directionX;
            var nextYPos = yPosition + i * directionY;
            Square square = squares[nextXPos][nextYPos];
            if(square.checkIfEmpty()){
                continue;
            }
            return false;
        }
        return canPieceMove(x,y,squares);
    }

}

class Pawn extends Piece {
    boolean moved = false;
    int allowedDirection;
    public Pawn( String name, String color,String icon) {
        super(name = "Pawn",color,icon);
        allowedDirection = "White" == color ? 1 : -1;
    }

    private boolean allowedToMoveInDirection(int dir){
        if (Objects.equals(color, "Black")){
            if(dir > 0){
                return true;
            }
        }
        else {
            if(dir < 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public void movePiece(int x, int y){
        super.movePiece(x,y);
        moved = true;
    }
    @Override
    public boolean isMovementValid(int x, int y, Square[][] squares) {
        Square targetSquare = squares[x][y];
        int diffX = x - xPosition;
        int diffY = y - yPosition;
        int distanceY = Math.abs(diffY);
        int distanceX = Math.abs(diffX);

        if(!allowedToMoveInDirection(diffX)){
            return false;
        }
        if(!canPieceMove(x,y,squares)){
            return false;
        }

        if (targetSquare.checkIfEmpty()){
            if (!moved) {
                Square firstSquareBlack = squares[xPosition+1][y];
                Square firstSquareWhite = squares[xPosition-1][y];
                if (distanceX == 2 && distanceY == 0){
                    if (Objects.equals(color, "White") && firstSquareWhite.checkIfEmpty()){
                        return true;

                    } else return color == "Black" && firstSquareBlack.checkIfEmpty();
                }
            }
            return distanceX == 1 && distanceY == 0;
        }

        return distanceX == 1 && distanceY == 1;
    }
}
class Knight extends Piece {

    public Knight(String name, String color, String icon) {
        super(name, color, icon);
    }

    @Override
    public boolean isMovementValid(int x, int y, Square[][] squares) {
        int diffX = x - xPosition;
        int diffY = y - yPosition;
        int distanceY = Math.abs(diffY);
        int distanceX = Math.abs(diffX);

        if (distanceX == 2 && distanceY == 1) {
            return canPieceMove(x,y,squares);
        } else if (distanceY == 2 && distanceX == 1) {
            return canPieceMove(x,y,squares);
        } else return false;
    }
}
class Rook extends DirectionalPiece{

    public Rook(String name, String color, String icon) {
        super(name, color,icon);
    }
    @Override
    public boolean isMovementValid(int x,int y, Square[][] squares){
        if(xPosition == x || yPosition == y){
            return canMoveToTarget(x,y,squares);
        }
        return false;
    }
}
class Bishop extends DirectionalPiece{

    public Bishop(String name, String color, String icon) {

        super(name, color,icon);
    }
    @Override
    public boolean isMovementValid(int x,int y, Square[][] squares){
        int diffX = x - xPosition;
        int diffY = y - yPosition;
        int distanceY = Math.abs(diffY);
        int distanceX = Math.abs(diffX);

        if(distanceX == distanceY){
            return canMoveToTarget(x,y,squares);
        }
        return false;
    }
}
class King extends DirectionalPiece{

    public King(String name, String color, String icon) {

        super(name, color,icon);
    }
    @Override
    public boolean isMovementValid(int x, int y, Square[][] squares) {

        int diffX = x - xPosition;
        int diffY = y - yPosition;
        int distanceY = Math.abs(diffY);
        int distanceX = Math.abs(diffX);

        if (distanceX < 2 & distanceY < 2) {
            if (xPosition == x || yPosition == y) {
                return canMoveToTarget(x, y, squares);
            } else if (distanceX == distanceY) {
                return canMoveToTarget(x, y, squares);
            }
            return false;
        }
        return false;
    }

}
class Queen extends DirectionalPiece {

    public Queen(String name, String color, String icon) {
        super(name, color, icon);
    }

    @Override
    public boolean isMovementValid(int x, int y, Square[][] squares) {

        int diffX = x - xPosition;
        int diffY = y - yPosition;
        int distanceY = Math.abs(diffY);
        int distanceX = Math.abs(diffX);

        if (xPosition == x || yPosition == y) {
            return canMoveToTarget(x, y, squares);
        } else if (distanceX == distanceY) {
            return canMoveToTarget(x, y, squares);
        }
        return false;
    }
}
