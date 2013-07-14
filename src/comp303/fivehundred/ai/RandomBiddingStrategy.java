package comp303.fivehundred.ai;

import comp303.fivehundred.model.Bid;
import comp303.fivehundred.model.Hand;
import java.util.Random;

/**
 * Enters a valid but random bid. Passes a configurable number of times.
 * If the robot does not pass, it uses a universal probability
 * distribution across all bids permitted.
 * @author Jake Shamash
 */
public class RandomBiddingStrategy implements IBiddingStrategy
{
	
	private double aPassFrequency;
	
	/**
	 * Builds a robot that passes 50% of the time and bids randomly otherwise.
	 */
	public RandomBiddingStrategy()
	{
		final double lFiftyPercent = 0.5;
		aPassFrequency = lFiftyPercent;
	}
	
	/** 
	 * Builds a robot that passes the specified percentage number of the time.
	 * @param pPassFrequency A percentage point (e.g., 50 for 50%) of the time the robot 
	 * will pass. Must be between 0 and 100 inclusive. 
	 * Whether the robot passes is determined at random.
	 */
	public RandomBiddingStrategy(int pPassFrequency)
	{
		final int lOneHundred = 100;
		if (pPassFrequency < 0 || pPassFrequency > lOneHundred)
		{
			throw new AIException("Percentage point must be between 0 and 100 inclusive.");
		}
		this.aPassFrequency = pPassFrequency / lOneHundred;
	}
	
	@Override
	public Bid selectBid(Bid[] pPreviousBids, Hand pHand)
	{		
		final int lHighestBid = 24;
		Random r = new Random();
		
		// Returns passing bid with probability aPassFrequency
		if (r.nextDouble() <= this.aPassFrequency)
		{
			return new Bid();
		}
		
		Bid maxBid = Bid.max(pPreviousBids);
		
		// If all bids are passes, return any bid.
		if (maxBid.isPass())
		{
			return new Bid(r.nextInt(lHighestBid + 1));
		}
		
		int maxIndex = maxBid.toIndex();
		
		// Pass if highest bid has already been made
		if (maxIndex == lHighestBid)
		{
			return new Bid();
		}
		
		// Otherwise, generate a random bid higher than maxBid.
		// Generates a random number between maxIndex and lHighestBid:
		int randomBid = r.nextInt(lHighestBid - maxIndex) + maxIndex + 1;
		
		return new Bid(randomBid);
	}
}