package solitaire;

import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import static solitaire.Card.CARD_WIDTH;

final public class Foundation extends Pile {
    
    public Foundation(String suit) {
        super(suit);
        
        setFont(getFont().deriveFont((float) CARD_WIDTH));
        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                e.getChild().setLocation(0, 0);
            }
        });
    }
    
    public Object nextRank() {
        return Card.Rank.values()[getComponentCount()];
    }
    
}
