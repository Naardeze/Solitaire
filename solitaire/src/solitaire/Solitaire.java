package solitaire;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import solitaire.card.Card;

final public class Solitaire extends JFrame {
    final public static JButton AUTO = new JButton("AUTO");
    
    public enum GameType {
        Turn_1(1), Turn_3(3);
        
        final private int value;
        
        GameType(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }
    
    private Game game = new Game(GameType.Turn_1);
    
    private Solitaire() {
        super("Solitaire");
        
        setIconImage(Toolkit.getDefaultToolkit().createImage("spades.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        
        for (GameType gameType : GameType.values()) {
            menu.add(gameType.name()).addActionListener(e -> {
                game = new Game(gameType);

                setContentPane(game);
                validate();
            });
        }
        
        AUTO.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        AUTO.addActionListener(e -> {
            AUTO.setVisible(false);
            
            game.removeMouseListener(game);
            new Thread(game).start();
        });
        
        menuBar.add(menu);
        menuBar.add(AUTO);
        
        setJMenuBar(menuBar);
        setContentPane(game);
        
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws Exception {
        Card.face = ImageIO.read(new File("face.png"));
        
        new Solitaire();
    }
    
}
