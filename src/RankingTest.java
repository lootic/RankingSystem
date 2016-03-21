import static org.junit.Assert.*;

import org.junit.Test;

public class RankingTest {
	
	@Test
	public void twoNewPlayersSingleResultAvailable() {
		Ranking ranking = new Ranking();
		ranking.addResult("1\tplayer1\n2\tplayer2");
		
		assertEquals("player1 1\nplayer2 -1", ranking.standings());
	}
	
	@Test
	public void threePlayersSIngleResultAvailable() {

		Ranking ranking = new Ranking();
		ranking.addResult("1\tplayer1\n2\tplayer2\n3\tplayer3");
		
		assertEquals("player1 2\nplayer2 0\nplayer3 -2", ranking.standings());
	}
	
	@Test
	public void twoNewPlayersYoloAndSwagSingleResultAvailable() {
		Ranking ranking = new Ranking();
		ranking.addResult("1\tyolo\n2\tswag");
		
		assertEquals("yolo 1\nswag -1", ranking.standings());
	}
	
	@Test
	public void fiveNewPlayersSingleResultAvailable() {
		Ranking ranking = new Ranking();
		ranking.addResult("1\tplayer1\n2\tplayer2\n3\tplayer3\n4\tplayer4\n5\tplayer5");
		
		assertEquals("player1 4\nplayer2 2\nplayer3 0\nplayer4 -2\nplayer5 -4", ranking.standings());
	}
	
	@Test
	public void twoPlayersTwoResultsAvailable() {
		Ranking ranking = new Ranking();
		ranking.addResult("1\tyolo\n2\tswag");
		ranking.addResult("1\tswag\n2\tyolo");
		
		assertEquals("swag 0\nyolo 0", ranking.standings());
	}
	
	@Test
	public void twoPlayersTwoResultsAvailableSamePlayerWinningTwiceShouldOnlyStealPointOnce() {
		Ranking ranking = new Ranking();
		ranking.addResult("1\tyolo\n2\tswag");
		ranking.addResult("1\tyolo\n2\tswag");
		
		assertEquals("yolo 1\nswag -1", ranking.standings());
	}
	
	@Test
	public void twoPlayersTwoResultsAvailableSamePlacementTwiceShouldNotStealPoints() {
		Ranking ranking = new Ranking();
		ranking.addResult("1\tyolo\n1\tswag");
		ranking.addResult("1\tyolo\n1\tswag");
		
		assertEquals("swag 0\nyolo 0", ranking.standings());
	}
	
	@Test
	public void twoPlayersSingleWithFourPlayersTwoPlayersWithSamePlacement() {
		Ranking ranking = new Ranking();
		ranking.addResult("1\tyolo\n2\tswag\n2\tyoloswag\n3\tswaggidy");
		
		assertEquals("yolo 3\nswag 0\nyoloswag 0\nswaggidy -3", ranking.standings());
	}
}
