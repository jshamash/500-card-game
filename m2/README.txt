-Design Decisions

In order to support a human player as well as robots later on, we decided to create an abstract
player class that the different types of players extend. It contains abstract methods for bidding,
exchanging cards and playing. We also decided to have players keep track of data that pertains to
them such as their their name, their hand, the number of tricks they have won and more.  The game
engine keeps track of more global data such as the current trick, the index representing which player's
turn it is and the players themselves.

With regards to observers, we have implemented it using a pull model. The game engine includes methods
that allow observers to get any information they might need, however we were careful to ensure that only
string representations of mutable fields could be obtained to protect the information kept by the game
engine and the players. Because there are different events that take place during a game, an int representing
a specific event is also passed to the observers that allows them to know what state the game is currently in. 

-TestGameEngine

Because much of what happens during a game is random, the tests for the GameEngine were designed
to check basic values like player hand sizes as opposed to the actual contents.  For the Bid,
each player's bid was matched with a regex to see if it was valid.  The tests for computeScore and
isGameOver iterate over 1000 games to ensure that cases such as isContractMade and slams occur
which can be rare, as well as to check that score values are always in the proper range.

-TestBasicBiddingStrategy

To test for this strategy, various hands are used to test different cases that could arise, mainly
looking at the extreme cases (i.e. best and worst possible hands).  These were then combined with
different bid combinations to check for proper point computations and resulting bids.  The tests
achieve full coverage of the BasicBiddingStrategy class.

-TestBasicCardExchangeStrategy

The test here involves simply passing a hand containing 16 various cards covering all suits and
most ranks to selectCardsToDiscard. It then checks to ensure that the 6 lowest valued cards according
to the rules of the game are returned using all 5 trump cases.

-TestBasicPlayingStrategy

The goal here was to cover cases using different hands and tricks.  In the leading case, since a card
is chosen at random, this is run over 1000 iterations to ensure that all playable cards are capable
of being selected.  This is done for both trump and no trump situations.  The remaining tests look
at the non-leading cases, trying different lead and trump suits and hand combinations.  The tests
achieve full coverage of the BasicPlayingStrategy class.