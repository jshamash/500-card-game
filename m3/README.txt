FHGUI
The GUI works essentially by adding and removing different panels depending on the state of the game while
keeping everything within a single JFrame.  For example, a panel with all possible bid choices is added when
the game is at the bidding stage, and this panel is removed once bidding has been completed.  The GUI acts
as an observer to the game engine and updates the player's hand and information about the game such as team
scores when it receives notifications by the game engine.  When it is the human player's turn to make a move, 
the game engine waits on a synchronized lock until the GUI receives input from the player at which point it
will notify the game engine that it can continue executing.  This process is skipped, however, if the user
enables the autoplay mode which can be toggled on or off by pressing the space bar key which will cause all
decisions to be made by the advanced ai strategies.  For the robots themselves, we decided to have just a single
robot player class that is capable of switching between different strategy levels when selected between games.

Advanced AI
The bidding and exchange strategies are slight variations on the basic ones. The playing strategy observes the game
engine in order to keep track of what cards have been played so far as well as which players were not able to follow
which suits. It does not access any information that would not be available to a human player. It uses the information
it gathers to make smart decisions about which cards it should play.
The advanced AI, as is, is better than the basic AI, but not by a very large margin. After implementing and testing 
the strategies as is, we realized there would be more effective ways of designing them. However, time did not permit
us to be able to remake the strategies. One major advancement we would make to the advanced AI is in the playing strategy;
we would construct a tree of all possible outcomes for a given trick, then use a minimax algorithm to select the best
possible play.