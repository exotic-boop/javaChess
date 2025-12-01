import javax.swing.*;
import java.awt.*;

public class Square extends JButton {
    int x;
    int y;
    Piece piece;
    Color color;
    boolean selected = false;

    public Square(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    public void addPiece (Piece piece){
        this.piece = piece;
        this.piece.setPosition(x,y);
        var com = add(piece.getLabel());
        com.setVisible(true);
    }

    public boolean checkIfEmpty() {
        if (this.piece == null){
            return true;
        } return false;

    }
    public void unSelect(){
        selected =false;
        setBackground(this.color);
    }
    public void select(){
        selected = true;
        setBackground(Color.green);
    }
    public Piece returnPiece() {
        return this.piece;

    }
    public int returnXPosition(){
        return this.x;
    }
    public int returnYPosition(){
        return this.y;
    }
    public void removePiece(){
        remove(this.piece.getLabel());
        updateUI();
        this.piece = null;
    }

    public boolean isSelected() {
        return selected;
    }

    public void movePiece(Piece selectedPiece) {
        this.piece = selectedPiece;
        this.piece.movePiece(x,y);
        var com = add(piece.getLabel());
        com.setVisible(true);
    }
}