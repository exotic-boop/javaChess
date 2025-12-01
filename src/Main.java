import javax.swing.*;

public class Main {
    public static void main(String[] args){
        JPanel containerPanel = new JPanel();
        BoardGame game = new ChessBoard(containerPanel);
        JFrame Frame = new Frame(game, containerPanel);
    }
}
