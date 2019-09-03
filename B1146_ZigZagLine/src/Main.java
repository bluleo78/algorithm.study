import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	static int N;
	static boolean[] visited;
	static int visitedCnt;
	static long[][][] cache1, cache2;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		N = Integer.parseInt(br.readLine());

		//
		System.out.format("%d\n", solve());
	}

	private static long solve() {
		visited = new boolean[N];
		for (int i = 0; i < N; i++) {
			visited[i] = false;
		}
		visitedCnt = 0;

		cache1 = new long[N][N][N];
		cache2 = new long[N][N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					cache1[i][j][k] = -1;
					cache2[i][j][k] = -1;
				}
			}
		}
		return count(0, -1, -1, -1, -1);
	}

	private static long count(int length, int prev, int pprev, int s, int l) {
		long result = 0;

		if (length == N)
			return 1;

		if (prev > pprev && cache1[length][s][l] >= 0) {
			return cache1[length][s][l];
		} else if (prev < pprev && cache2[length][s][l] >= 0) {
			return cache2[length][s][l];
		}

		if (length == 0) {
			for (int i = 0; i < N; i++) {
				if (!visited[i]) {
					visited[i] = true;
					result += count(length + 1, i, prev, i, N - 1 - i);
					visited[i] = false;
				}
			}
		} else if (length == 1) {
			int sc = 0;
			int lc = s + l - 1;
			for (int i = 0; i < N; i++) {
				if (!visited[i]) {
					visited[i] = true;
					result += count(length + 1, i, prev, sc++, lc--);
					visited[i] = false;

				}
			}
		} else if (prev > pprev) {
			int sc = s;
			int lc = l;
			for (int i = prev - 1; i >= 0; i--) {
				if (!visited[i]) {
					visited[i] = true;
					result += count(length + 1, i, prev, --sc, lc++);
					visited[i] = false;
				}
			}
		} else {
			int sc = s;
			int lc = l;
			for (int i = prev + 1; i < N; i++) {
				if (!visited[i]) {
					visited[i] = true;
					result += count(length + 1, i, prev, sc++, lc--);
					visited[i] = false;
				}
			}
		}

		result = result % 1000000;
		if (length >= 2) {
			if (prev > pprev) {
				cache1[length][s][l] = result;
			} else {
				cache2[length][s][l] = result;
			}
		}

		return result;
	}
}
