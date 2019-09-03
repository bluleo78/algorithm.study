import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[] CARS;
	static long[] D1;
	static long[] D2;
	static long[] cached1;
	static long[] cached2;

	static class Edge {

		int x;
		int y;
		long weight;

		List<Edge> exEdges = new ArrayList<Edge>();;
		boolean isExcluded;

		public Edge(int x2, int y2, long w) {
			x = x2;
			y = y2;
			weight = w;
		}
	}

	public static void main(String[] args) throws IOException {
		int T;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		T = Integer.parseInt(br.readLine());
		long[] result = new long[T];

		// Process
		long elapsedTime = 0;
		for (int t = 0; t < T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());

			st = new StringTokenizer(br.readLine());
			CARS = new int[N];
			for (int i = 0; i < N; i++) {
				int c = Integer.parseInt(st.nextToken()) - 1;
				CARS[c] = i;
			}

			long startTime = System.currentTimeMillis();
			result[t] = solve();
			elapsedTime += (System.currentTimeMillis() - startTime);
		}

		for (int t = 0; t < T; t++) {
			System.out.format("#%d %d\n", t + 1, result[t]);
		}

		System.out.format("TIME => %2.2f\n", elapsedTime / 1000.0);
	}

	private static long solve() {
		// D1
		D1 = new long[N];
		for (int i = 1; i < N; i++) {
			D1[i] = Math.abs(CARS[i - 1] - CARS[i]);
			D1[i] = Math.min(D1[i], N - D1[i]);
		}

		// D1
		D2 = new long[N];
		for (int i = 2; i < N; i++) {
			D2[i] = Math.abs(CARS[i - 2] - CARS[i]);
			D2[i] = Math.min(D2[i], N - D2[i]);
		}

		cached1 = new long[N];
		cached2 = new long[N];

		return count(N - 1);
	}

	private static long count(int c) {
		return Math.min(count1(N - 1), count2(N - 1));

	}

	private static long count1(int c) {
		if (c == 0) {
			return Math.min(CARS[0], N - CARS[0]);
		}

		if (cached1[c] > 0)
			return cached1[c];

		long result = Integer.MAX_VALUE;

		if (c >= 1) {
			result = Math.min(result, count1(c - 1) + D1[c]);
			if (c >= 2) {
				result = Math.min(result, count2(c - 1) + D2[c]);
			}
		}

		cached1[c] = result;

		return result;
	}

	private static long count2(int c) {
		if (c == 0) {
			return Integer.MAX_VALUE;
		}

		if (cached2[c] > 0)
			return cached2[c];

		long result = Integer.MAX_VALUE;
		if (c == 1) {
			result = Math.min(CARS[1], N - CARS[1]) + D1[1];
		}
		if (c >= 2) {
			result = Math.min(result, count1(c - 2) + D2[c] + D1[c]);
		}
		cached2[c] = result;
		return result;
	}

}
