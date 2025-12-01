import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Frame extends JFrame implements ActionListener {
    BoardGame game;
    Square[][] squares = new Square[8][8];
    private JTextField message = new JTextField("Let's play chess");
    private JTextField playerTurnMessage = new JTextField("White");

    public JPanel containerPanel;


    private void updateBoard(){
        this.setVisible(true);
    }

    public Frame(BoardGame game, JPanel containerPanel) {

        this.containerPanel = containerPanel;
        JPanel buttonPanel = new JPanel();
        JPanel messagePanel = new JPanel();
        //this.game  = new ChessBoard(containerPanel);
        this.game = game;
        JPanel messagePanelPlayerTurn = new JPanel();
        buttonPanel.setLayout(new GridLayout(8,8));
        int size = 8;
        Color color;

        for ( int x = 0; x < size; x ++) {
            if (x % 2 != 0){
                color = java.awt.Color.white;
            }else color = java.awt.Color.gray;
            for (int y = 0; y < size; y++) {
                if (color == java.awt.Color.gray){
                    color = java.awt.Color.white;
                }else color = java.awt.Color.gray;

                Square button = new Square(x, y, color);
                button.setBackground(color);
                button.addActionListener(this);
                this.squares[x][y] = button;
                buttonPanel.add(button);
            }
        }

        buttonPanel.setPreferredSize(new Dimension(600, 600));
        messagePanel.add(message);
        messagePanelPlayerTurn.add(playerTurnMessage);
        this.containerPanel.add(messagePanelPlayerTurn);
        this.containerPanel.add(messagePanel);
        this.containerPanel.add(buttonPanel);
        this.containerPanel.setLayout(new BoxLayout(this.containerPanel, BoxLayout.Y_AXIS));
        this.getContentPane().add(this.containerPanel);
        this.setVisible(true);
        this.setSize (700,700);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        game.buildBoard(squares);
        setVisible(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
       Square command = (Square) e.getSource();
       int x = command.returnXPosition();
       int y = command.returnYPosition();
       game.executeInput(x,y);
       message.setText(game.getMessage());
       playerTurnMessage.setText(game.getStatus());
       updateBoard();
    }
}