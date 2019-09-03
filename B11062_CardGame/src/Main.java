import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static class Score {
		int score1;
		int score2;

		public Score(int i, int j) {
			score1 = i;
			score2 = j;
		}
	}

	static int T;
	static int N;
	static int[] CARDS;
	static Score[][] cache1;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		T = Integer.parseInt(br.readLine());
		for (int i = 0; i < T; i++) {
			N = Integer.parseInt(br.readLine());
			CARDS = new int[N];
			StringTokenizer token = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				CARDS[j] = Integer.parseInt(token.nextToken());
			}
			cache1 = new Score[N][N];

			;
			System.out.println(solve(0, N - 1).score1);
		}
	}

	private static Score solve(int startIdx, int endIdx) {
		if (startIdx == endIdx) {
			return new Score(CARDS[startIdx], 0);
		} else if (startIdx > endIdx) {
			return new Score(0, 0);
		}

		if (cache1[startIdx][endIdx] != null) {
			return cache1[startIdx][endIdx];
		}

		Score case1 = solve(startIdx + 1, endIdx);
		Score case2 = solve(startIdx, endIdx - 1);

		int s1 = CARDS[startIdx] + case1.score2;
		int s2 = CARDS[endIdx] + case2.score2;

		Score ret = null;
		if (s1 > s2) {
			ret = new Score(s1, case1.score1);
		} else {
			ret = new Score(s2, case2.score1);
		}

		cache1[startIdx][endIdx] = ret;

		return ret;
	}
}
