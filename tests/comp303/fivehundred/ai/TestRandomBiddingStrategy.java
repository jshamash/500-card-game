package comp303.fivehundred.ai;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Random;
import comp303.fivehundred.model.Bid;

/**
 * Test method for RandomBiddingStrategy.
 * @author Jake Shamash
 */
public class TestRandomBiddingStrategy
{
	@Test
	public void testInit()
	{
		new RandomBiddingStrategy();
		new RandomBiddingStrategy(50);
		
		// Should throw exception on illegal arguments
		try
		{
			new RandomBiddingStrategy(101);
			fail();
		}
		catch (AIException e)
		{
		}
		try
		{
			new RandomBiddingStrategy(-1);
			fail();
		}
		catch (AIException e)
		{
		}
	}

	@Test
	public void testSelectBid()
	{
		final int iterations = 1000;
		Random r = new Random();
		Bid[] bids = new Bid[3];
		Bid returned;
		Bid maxBid;
		RandomBiddingStrategy bidder;
		
		for (int i = 0; i < iterations; i++)
		{
			// Random bids
			bids[0] = new Bid(r.nextInt(25));
			bids[1] = new Bid(r.nextInt(25));
			bids[2] = new Bid(r.nextInt(25));
			bidder = new RandomBiddingStrategy(r.nextInt(101));
			returned = bidder.selectBid(bids, null);
			maxBid = Bid.max(bids);
			assertTrue(isValid(returned, maxBid));
			
			// All bids are passes
			bids[0] = new Bid();
			bids[1] = new Bid();
			bids[2] = new Bid();
			maxBid = Bid.max(bids);
			bidder = new RandomBiddingStrategy(r.nextInt(101));
			returned = bidder.selectBid(bids, null);
			assertTrue(isValid(returned, maxBid));
			
			// Max bid has already been placed
			bids[0] = new Bid(10, null);
			bidder = new RandomBiddingStrategy(r.nextInt(101));
			returned = bidder.selectBid(bids, null);
			assertTrue(returned.isPass());
			
			// Setting percentage to 0 means that if 9 no trump is the highest bid, bidder
			// should always choose 10 no trump and never pass.
			bids[0] = new Bid(12);
			bids[2] = new Bid(23);
			bidder = new RandomBiddingStrategy(0);
			returned = bidder.selectBid(bids, null);
			assertTrue(returned.toIndex() == 24);
			
			// Setting percentage to 100 means that bidder should always pass.
			bidder = new RandomBiddingStrategy(100);
			returned = bidder.selectBid(bids, null);
			assertTrue(returned.isPass());
		}
	}
	
	// Returns true if b is a pass or b is between maxBid and 24.
	public boolean isValid(Bid b, Bid maxBid)
	{
		return b.isPass() || (b.compareTo(maxBid) > 0 && b.toIndex() <= 24);
	}
}