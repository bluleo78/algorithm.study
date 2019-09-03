import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	static int N;
	static int S;
	static long[][] TIME;
	static long[][] cache;

	public static void main(String[] args) throws IOException {
		int T;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		T = Integer.parseInt(br.readLine());
		long[] result = new long[T];

		// Process
		for (int t = 0; t < T; t++) {
			N = Integer.parseInt(br.readLine());
			S = Integer.parseInt(br.readLine());

			TIME = new long[N][N];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (i == j) {
						TIME[i][j] = 0;
					} else {
						TIME[i][j] = Integer.MAX_VALUE;
					}
				}
			}
			for (int i = 0; i < N - 1; i++) {
				TIME[i][i + 1] = Long.parseLong(br.readLine());
				TIME[i + 1][i] = TIME[i][i + 1];
			}

			result[t] = solve();
		}

		for (int t = 0; t < T; t++) {
			System.out.format("%d\n", result[t]);
		}

		// System.out.format("TIME => %2.2f\n", elapsedTime / 1000.0);
	}

	private static long solve() {
		//
		for (int i = 0; i < N - 2; i++) {
			for (int j = i + 2; j < N; j++) {
				TIME[i][j] = TIME[i][j - 1] + TIME[j - 1][j];
				TIME[j][i] = TIME[i][j];
			}
		}
		cache = new long[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				cache[i][j] = -1;
			}
		}

		// search
		return calc(S - 1, S - 1, N - 1);
	}

	private static long calc(int currPos, int otherPos, int i) {
		if (cache[currPos][otherPos] != -1)
			return cache[currPos][otherPos];
		if (i == 0)
			return 0;
		long result = Long.MAX_VALUE;
		if (currPos <= otherPos) {
			if (currPos - 1 >= 0) {
				result = Math.min(result, TIME[currPos][currPos - 1] * (i) + calc(currPos - 1, otherPos, i - 1));
			}
			if (otherPos + 1 <= N - 1) {
				result = Math.min(result, TIME[currPos][otherPos + 1] * (i) + calc(otherPos + 1, currPos, i - 1));
			}
		} else {
			if (currPos < N - 1) {
				result = Math.min(result, TIME[currPos][currPos + 1] * (i) + calc(currPos + 1, otherPos, i - 1));
			}
			if (otherPos - 1 >= 0) {
				result = Math.min(result, TIME[currPos][otherPos - 1] * (i) + calc(otherPos - 1, currPos, i - 1));
			}
		}

		cache[currPos][otherPos] = result;

		return result;
	}
}
