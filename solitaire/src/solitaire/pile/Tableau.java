package solitaire.pile;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import solitaire.card.Card;

final public class Tableau extends Pile {
    final public static int DOWN = 8;
    final public static int UP = 20;
    
    public Tableau() {
        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                if (getComponentCount() == 1) {
                    e.getChild().setLocation(0, 0);
                } else if (((Card) getComponent(1)).isFaceUp()) {
                    e.getChild().setLocation(0, getComponent(1).getY() + UP);
                } else {
                    e.getChild().setLocation(0, getComponent(1).getY() + DOWN);
                }
            }
        });
    }
    
    public boolean isLegal(Card card) {
        if (card.getRank() == Card.Rank.KING) {
            return isEmpty();
        } else {
            return card.getRank() != Card.Rank.ACE && !isEmpty() && getCard().getSuit().getSuitColor() != card.getSuit().getSuitColor() && getCard().getRank() == Card.Rank.values()[card.getRank().ordinal() + 1];
        }
    }
    
}
