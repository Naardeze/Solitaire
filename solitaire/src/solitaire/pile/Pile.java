package solitaire.pile;

import java.awt.Color;
import javax.swing.JLabel;
import solitaire.card.Card;

public class Pile extends JLabel {
    
    public Pile() {}
    
    public Pile(String symbol) {
        super(symbol, JLabel.CENTER);
        
        setForeground(new Color(0, 150, 0));
    }
    
    public Card getCard() {
        return (Card) getComponent(0);
    }
    
    public void setCard(Card card) {
        add(card, 0);
    }
    
    public boolean isEmpty() {
        return getComponentCount() == 0;
    }
    
}
