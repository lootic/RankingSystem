import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Ranking {
	private Map<String, Integer> standings;

	private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(
			Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = e2.getValue().compareTo(e1.getValue());
				return res != 0 ? res : 1;
			}
		});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}

	private static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

	public Ranking() {
		standings = new TreeMap<String, Integer>();
	}

	public void addResult(String result) {
		String[] rows = result.split("\n");
		iterateThroughResults(rows);
	}

	private void iterateThroughResults(String[] rows) {
		Map<String, Integer> pointsToRemove = new TreeMap<String, Integer>();
		Map<String, Integer> pointsToAdd = new TreeMap<String, Integer>();

		for (int i = 0; i < rows.length; ++i) {
			String[] placementAndPlayer = rows[i].split("\t");

			for (int j = i + 1; j < rows.length; ++j) {
				String[] otherPlacementAndPlayer = rows[j].split("\t");

				if (shouldPlayerStealPoint(placementAndPlayer, otherPlacementAndPlayer)) {
					String player = placementAndPlayer[1].trim().toLowerCase();
					String otherPlayer = otherPlacementAndPlayer[1].trim().toLowerCase();
					int points = 1;

					Integer playerResult = pointsToAdd.get(player);
					playerResult = playerResult == null ? 0 : playerResult;
					pointsToAdd.put(player, playerResult + points);

					Integer otherPlayerResult = pointsToRemove.get(otherPlayer);
					otherPlayerResult = otherPlayerResult == null ? 0 : otherPlayerResult;
					pointsToRemove.put(otherPlayer, otherPlayerResult - points);
				}
			}
		}

		for (String key : pointsToRemove.keySet()) {
			addToResult(key, pointsToRemove.get(key));
		}
		for (String key : pointsToAdd.keySet()) {
			addToResult(key, pointsToAdd.get(key));
		}
	}

	private void addToResult(String player, int addedResult) {
		Integer oldResult = getRankingPoints(player);
		oldResult = oldResult == null ? 0 : oldResult;
		standings.put(player.trim().toLowerCase(), oldResult + addedResult);
	}

	private boolean shouldPlayerStealPoint(String[] placementAndPlayer, String[] otherPlacementAndPlayer) {
		Integer oldResult = getRankingPoints(placementAndPlayer[1]);
		Integer otherPlayerResult = getRankingPoints(otherPlacementAndPlayer[1]);
		if (!(oldResult == null && otherPlayerResult == null) && otherPlayerResult == null) {
			return false;
		}
		return (!otherPlacementAndPlayer[0].trim().toLowerCase().equals(placementAndPlayer[0].trim().toLowerCase())
				&& (oldResult == null || otherPlayerResult == null || otherPlayerResult >= oldResult));
	}

	private Integer getRankingPoints(String player) {
		Integer oldResult = standings.get(player.trim().toLowerCase());
		return oldResult;
	}

	private int halfDifference(String player, String otherPlayer) {
		Integer oldResult = getRankingPoints(player);
		Integer otherPlayerResult = getRankingPoints(otherPlayer);

		if (oldResult == null || otherPlayerResult == null) {
			return 1;
		}
		int halfDifference = (otherPlayerResult - oldResult) / 4;
		System.out.println("(" + otherPlayer + ": " + otherPlayerResult + " - " + player + ": " + oldResult
				+ ") / 3 == " + halfDifference);
		return halfDifference != 0 ? halfDifference : 1;
	}

	public void addResultFromPath(String pathToResult) throws IOException {
		addResult(readFile(pathToResult));
	}

	public String standings() {
		StringBuilder result = new StringBuilder();
		for (Entry<String, Integer> entry : entriesSortedByValues(standings)) {
			result.append(entry.getKey() + " " + entry.getValue() + "\n");
		}
		result.delete(result.length() - 1, result.length());
		return result.toString();
	}

	public static void main(String[] args) throws IOException {
		Ranking ranking = new Ranking();

		ranking.addResultFromPath("res/smashologyIV.result");
		System.out.println();
		System.out.println(ranking.standings());
		ranking.addResultFromPath("res/smashologyIII.result");
		System.out.println();
		System.out.println(ranking.standings());
		ranking.addResultFromPath("res/smashologyII.result");
		System.out.println();
		System.out.println(ranking.standings());
		ranking.addResultFromPath("res/smashologyI.result");
		System.out.println();
		System.out.println(ranking.standings());
		ranking.addResultFromPath("res/preassociation.result");
		System.out.println();
		System.out.println(ranking.standings());

	}/**/

	/**
	 * Bobi Lootic Puzzle Sebson Parmsib Myrox Lumia Nacon Milom Laser thunder
	 */
}
