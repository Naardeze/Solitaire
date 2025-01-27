package solitaire;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import static solitaire.Solitaire.AUTO;
import solitaire.Solitaire.GameType;
import solitaire.card.Card;
import static solitaire.card.Card.CARD_HEIGHT;
import static solitaire.card.Card.CARD_WIDTH;
import solitaire.pile.Foundation;
import solitaire.pile.Pile;
import solitaire.pile.Tableau;

final public class Game extends JLabel implements MouseListener, Runnable {
    final private static int GAP = 1;
    final private static int INSET = 5;
    final private static int STEP = 15;
    
    final private Pile deck = new Pile("\ud83d\udd04");
    final private Pile waste = new Pile();
    final private Foundation[] foundation = {new Foundation("\u2660"), new Foundation("\u2665"), new Foundation("\u2663"), new Foundation("\u2666")};
    final private Tableau[] tableau = {new Tableau(), new Tableau(), new Tableau(), new Tableau(), new Tableau(), new Tableau(), new Tableau()};
    
    final private GameType gameType;
    
    final private long startTime = System.currentTimeMillis();
    
    public Game(GameType gameType) {
        this.gameType = gameType;
        
        AUTO.setVisible(false);
        
        ArrayList<Card> cards = Card.getCards();
        Collections.shuffle(cards);
        cards.forEach(card -> deck.setCard(card));
        
        deck.setFont(deck.getFont().deriveFont((float) CARD_HEIGHT / 2));
        deck.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                e.getChild().setLocation(0, 0);
            }
        });
        deck.setBounds(INSET + 6 * (CARD_WIDTH + GAP), INSET, CARD_WIDTH, CARD_HEIGHT);
        add(deck);
        
        waste.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                e.getChild().setLocation(waste.getWidth() - e.getChild().getWidth(), 0);
                
                for (int i = 1; i < Math.min(1 + e.getChild().getX() / STEP, waste.getComponentCount()); i++) {
                    waste.getComponent(i).setLocation(waste.getComponent(i).getX() - STEP, 0);
                }
            }
            @Override
            public void componentRemoved(ContainerEvent e) {
                for (int i = 0; i < Math.min(e.getChild().getX() / STEP, waste.getComponentCount()); i++) {
                    waste.getComponent(i).setLocation(waste.getComponent(i).getX() + STEP, 0);
                }
            }
        });
        waste.setBounds(INSET + 5 * (CARD_WIDTH + GAP) - 2 * STEP, INSET, 2 * STEP + CARD_WIDTH, CARD_HEIGHT);
        add(waste);
        
        for (int i = 0; i < foundation.length; i++) {
            foundation[i].setBounds(INSET + i * (CARD_WIDTH + GAP), INSET, CARD_WIDTH, CARD_HEIGHT);
            add(foundation[i]);
        }
        
        Tableau.faceDown = new ArrayList();
        
        for (int i = 0; i < tableau.length; i++) {
            tableau[i].setBounds(INSET + i * (CARD_WIDTH + GAP), INSET + CARD_HEIGHT + INSET, CARD_WIDTH, i * Tableau.DOWN + (Card.Rank.values().length - 2) * Tableau.UP + CARD_HEIGHT);
            add(tableau[i]);
            
            for (int j = 0; j < i; j++) {
                tableau[i].setCard(deck.getCard());
                Tableau.faceDown.add(tableau[i].getCard());
            }
            
            tableau[i].setCard(deck.getCard());
            tableau[i].getCard().flip();
        }
        
        setOpaque(true);
        setBackground(new Color(0, 200, 0));
        setHorizontalAlignment(JLabel.CENTER);
        setFont(getFont().deriveFont((float) CARD_HEIGHT / 3));
        setPreferredSize(new Dimension(tableau[tableau.length - 1].getX() + tableau[tableau.length - 1].getWidth() + INSET, tableau[tableau.length - 1].getY() + tableau[tableau.length - 1].getHeight() + INSET));

        addMouseListener(this);
    }
    
    private boolean isCompleted() {
        for (Pile foundation : foundation) {
            if (foundation.getComponentCount() < Card.Rank.values().length) {
                return false;
            }
        }
        
        long time = (System.currentTimeMillis() - startTime) / 1000;
        setText(time / 60 + ":" + new DecimalFormat("00").format(time % 60));
        
        return true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && getComponentAt(e.getPoint()) instanceof Pile) {
            Pile pile = (Pile) getComponentAt(e.getPoint());
            
            if (pile == deck) {
                if (!deck.isEmpty()) {
                    for (int i = Math.min(gameType.getValue(), deck.getComponentCount()); i > 0; i--) {
                        waste.setCard(deck.getCard());
                        waste.getCard().flip();
                    }
                } else {
                    do {
                        deck.setCard(waste.getCard());
                        deck.getCard().flip();
                    } while (!waste.isEmpty());
                }
                
                repaint();
            } else if (findComponentAt(e.getPoint()) instanceof Card) {
                Card card = (Card) findComponentAt(e.getPoint());
                
                if (pile instanceof Foundation || (pile == waste && card == waste.getCard()) || (pile instanceof Tableau && !Tableau.faceDown.contains(card))) {
                    Pile next = null;
                    
                    test : {                        
                        for (Tableau tableau : Arrays.copyOfRange(tableau, Arrays.asList(tableau).indexOf(pile) + 1, tableau.length)) {
                            if (tableau.isLegal(card)) {
                                next = tableau;
                                
                                break test;
                            }
                        }
                        
                        if (pile == waste || (pile instanceof Tableau && card == pile.getCard())) {
                            Foundation foundation = this.foundation[card.getSuit().ordinal()];
                            
                            if (foundation.nextRank() == card.getRank()) {
                                next = foundation;
                                
                                break test;
                            }
                        }
                        
                        if (pile instanceof Tableau) {
                            for (Tableau tableau : Arrays.copyOfRange(tableau, 0, Arrays.asList(tableau).indexOf(pile))) {
                                if (tableau.isLegal(card)) {
                                    next = tableau;
                                
                                    break;
                                }
                            }
                        }
                    }
                    
                    if (next != null) {
                        for (int i = pile.getComponentZOrder(card); i >= 0; i--) {
                            next.setCard((Card) pile.getComponent(i));
                        }
                        
                        if (pile instanceof Tableau) {
                            if (isCompleted()) {
                                removeMouseListener(this);
                                
                                AUTO.setVisible(false);
                            } else if (!pile.isEmpty() && Tableau.faceDown.remove(pile.getCard())) {
                                pile.getCard().flip();
                                
                                if (Tableau.faceDown.isEmpty()) {
                                    AUTO.setVisible(deck.isEmpty() && waste.isEmpty());
                                }
                            }
                        } else if (pile == waste && waste.isEmpty() && deck.isEmpty()) {
                            remove(deck);
                            
                            AUTO.setVisible(Tableau.faceDown.isEmpty());
                        }
                        
                        repaint();
                    }
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void run() {
        do {
            for (Pile tableau : tableau) {
                if (!tableau.isEmpty()) {
                    Foundation foundation = this.foundation[tableau.getCard().getSuit().ordinal()];
                    
                    if (tableau.getCard().getRank() == foundation.nextRank()) {
                        foundation.setCard(tableau.getCard());
                        
                        repaint();
                        
                        try {
                            Thread.sleep(140);
                        } catch (Exception ex) {}
                    }
                }
            }
        } while (!isCompleted());
    }
    
}
