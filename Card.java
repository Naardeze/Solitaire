package solitaire;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

final public class Card extends JLabel {
    final public static int CARD_WIDTH = 45;
    final public static int CARD_HEIGHT = 60;
    
    final private static ImageIcon BACK = new ImageIcon("back.png");
    public static BufferedImage face;
    
    public static enum Suit {
        SPADES, HEARTS, CLUBS, DIAMONDS;
        
        public static enum SuitColor {
            BLACK, RED
        }
    
        public SuitColor getSuitColor() {
            return SuitColor.values()[ordinal() % SuitColor.values().length];
        }
    }
    
    public static enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }
    
    final private Suit suit;
    final private Rank rank;
    
    private Card(Suit suit, Rank rank) {
        super(BACK);
        
        this.suit = suit;
        this.rank = rank;

        setSize(CARD_WIDTH, CARD_HEIGHT);
    }
    
    public Suit getSuit() {
        return suit;
    }
    
    public Rank getRank() {
        return rank;
    }
    
    public boolean isFaceUp() {
        return getIcon() != BACK;
    }
    
    public void setFaceUp(boolean faceUp) {
        if (faceUp) {
            setIcon(new ImageIcon(face.getSubimage(rank.ordinal() * getWidth(), suit.ordinal() * getHeight(), getWidth(), getHeight())));
        } else {
            setIcon(BACK);
        }
    }
    
    public static ArrayList<Card> getCards() {
        ArrayList<Card> cards = new ArrayList();
        
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        
        return cards;
    }

}
