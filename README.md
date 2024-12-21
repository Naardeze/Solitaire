# Solitaire
Simple Java Swing Solitaire Card Game

Turn_1 or Turn_3 -> (auto) completed -> display time.

Solitaire: (main) window

Game: game'panel' -> Layout piles, shuffle and deal cards.
-MouseListener -> player moves.
-Runnable -> autocomplete (tableau cards faceUp & deck + waste empty)

Card: (enum) suit, rank -> faceUp.
    (icon) back -> !faceUp.

Pile: with (deck, foundation) or without (waste, tableau) symbol.
    containerListener -> cardXY