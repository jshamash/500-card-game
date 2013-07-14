package comp303.fivehundred.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import comp303.fivehundred.util.Card.Suit;

/**
 * A mutable list of cards. Does not support duplicates.
 * The cards are maintained in the order added.
 */

/**
 * 
 * @author Ashley Kyung Min Kim
 *
 */
public class CardList implements Iterable<Card>, Cloneable
{
	private ArrayList<Card> aCards;
	/**
	 * Creates a new, empty card list.
	 */
	public CardList()
	{
		aCards = new ArrayList<Card>();
	}
	
	/**
	 * Adds a card if it is not
	 * already in the list. Has no effect if the card
	 * is already in the list.
	 * @param pCard The card to add.
	 * @pre pCard != null
	 */
	public void add(Card pCard)
	{
		assert pCard != null;
		

		if (!aCards.contains(pCard))
		{
			aCards.add(pCard);
		}
	}
	
	/**
	 * @return The number of cards in the list.
	 */
	public int size()
	{
		return aCards.size();
	}
	
	/**
	 * @return The first card in the list, according to whatever
	 * order is currently being used. 
	 * @throws GameUtilException if the list is empty.
	 */
	public Card getFirst()
	{
		if (aCards.isEmpty()) 
		{
			throw new GameUtilException("Card list is empty!");
		}
		return aCards.get(0);
	}
	
	/**
	 * @return The last card in the list, according to whatever
	 * order is currently being used. 
	 * @throws GameUtilException If the list is empty.
	 */
	public Card getLast()
	{
		if (aCards.isEmpty())
		{
			throw new GameUtilException("Card list is empty!");
		}
		return aCards.get(aCards.size()-1);
	}
	
	/**
	 * Removes a card from the list. Has no effect if the card is
	 * not in the list.
	 * @param pCard The card to remove. 
	 * @pre pCard != null;
	 */
	public void remove(Card pCard)
	{
		assert pCard != null;
		
		if (aCards.contains(pCard))
		{
			aCards.remove(pCard);
		}
	}
	
	/**
	 * Checks if pCard is in the CardList.
	 * 
	 * @param pCard The card to be checked.
	 * @return True if the CardList contains pCard, false otherwise.
	 */
	public boolean contains(Card pCard)
	{
		for (Card c : aCards)
		{
			if (c.equals(pCard))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @see java.lang.Object#clone()
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public CardList clone()
	{
		try
		{
			// Creates a deep copy.
			CardList iClone = (CardList)super.clone();
			iClone.aCards = (ArrayList<Card>) aCards.clone();
			return iClone;
		}
		catch(CloneNotSupportedException e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * @see java.lang.Iterable#iterator()
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Card> iterator()
	{
		Iterator<Card> iCard = aCards.iterator();
		return iCard;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 * {@inheritDoc}
	 */
	public String toString()
	{
		String iString = "";
		for (Iterator<Card> i = this.iterator(); i.hasNext(); )
		{
			Card lCard = (Card)i.next();
			iString += lCard.toString() + " ";
		}
		return iString;
	}
	
	/**
	 * @pre aCards.size() > 0
	 * @return A randomly-chosen card from the set. 
	 */
	public Card random()
	{
		Random rng = new Random();
		return aCards.get(Math.abs(rng.nextInt())%this.size());
	}
	
	/**
	 * Returns another CardList, sorted as desired. This
	 * method has no side effects.
	 * @param pComparator Defines the sorting order.
	 * @return the sorted CardList
	 * @pre pComparator != null
	 */
	public CardList sort(Comparator<Card> pComparator)
	{
		assert pComparator != null;
		
		// Creates a copy that is sorted.
		CardList lCards = this.clone();
		Collections.sort(lCards.aCards, pComparator);
		return lCards;
	}
	
	/**
	 * Get the card at a specific index.
	 * @param pIndex Index of the card
	 * @return A string representation of the card
	 */
	public String getCardAtIndex(int pIndex)
	{
		return aCards.get(pIndex).toString();
	}
	
	/**
	 * Whether or not this cardlist contains a given suit.
	 * @param pSuit the suit in question
	 * @return true if it contains pSuit.
	 */
	public boolean hasSuit(Suit pSuit)
	{
		for (Card c : this)
		{
			if (!c.isJoker() && c.getSuit() == pSuit)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get the cards in this list with a given suit.
	 * @param pSuit The suit
	 * @param pTrump The trump suit. Can be null for no trump.
	 * @return A new CardList containing all the cards of this list with suit pSuit.
	 */
	public CardList cardsOf(Suit pSuit, Suit pTrump)
	{
		CardList lReturn = new CardList();
		for (Card c : this)
		{
			if (c.getEffectiveSuit(pTrump) == pSuit)
			{
				lReturn.add(c);
			}
		}
		
		return lReturn;
	}
	
	/**
	 * Merges this CardList with another.
	 * @param pList The list to merge with.
	 * @return A new CardList which is the concatenation of this list with pList.
	 */
	public CardList merge(CardList pList)
	{
		CardList lReturn = this.clone();
		for (Card c : pList)
		{
			lReturn.add(c);
		}
		return lReturn;
	}
	
	/**
	 * Gets the jokers from this cardlist.
	 * @return A new CardList containing the jokers of this cardlist.
	 */
	public CardList getJokers()
	{
		CardList lReturn = new CardList();
		for (Card c : this)
		{
			if (c.isJoker())
			{
				lReturn.add(c);
			}
		}
		return lReturn;
	}
	
	/**
	 * Whether or not this contains a joker.
	 * @return True if it contains a joker.
	 */
	public boolean hasJoker()
	{
		for (Card c : this)
		{
			if (c.isJoker())
			{
				return true;
			}
		}
		return false;
	}
}
