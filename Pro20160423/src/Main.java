import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {

	private static int M;
	private static int[] NA;
	private static int[] KA;
	private static ArrayList<int[]> NSA = new ArrayList<int[]>();
	private static int[][] cache = new int[1001][1000];
	private static int[][] cache2 = new int[1001][1000];

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		M = Integer.parseInt(br.readLine());

		NA = new int[M];
		KA = new int[M];
		for (int i = 0; i < M; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			NA[i] = Integer.parseInt(st.nextToken());
			KA[i] = Integer.parseInt(st.nextToken());

			int[] arr = new int[NA[i]];
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < NA[i]; j++) {
				arr[j] = Integer.parseInt(st.nextToken());
			}
			NSA.add(arr);
		}

		// Process
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < M; i++) {
			System.out.printf("#%d %d\n", i, calculate(i));
		}
		System.out.printf("TIME => %2.2f", (System.currentTimeMillis() - startTime) / 1000.0);
	}

	private static int calculate(int m) {
		int N = NA[m];
		int K = KA[m];
		int[] NS = NSA.get(m);
		for (int i = 0; i < 1001; i++) {
			for (int j = 0; j < 1000; j++) {
				cache[i][j] = -1;
				cache2[i][j] = -1;
			}
		}
		return calcMin(NS, N, N, K);
	}

	private static int calcMin(int[] NS, int N, int n, int k) {
		if (cache2[n][k] != -1) {
			return cache2[n][k];
		}
		int min = Integer.MAX_VALUE;
		if (k <= 0) {
			min = calcDiff(NS, 0, n - 1);
		} else {
			int maxDiff = Integer.MIN_VALUE;
			for (int i = n - 1; i >= k; i--) {
				int diff = calcDiff(NS, i, n - 1);
				int subMax = Math.max(diff, calcMin(NS, N, i, k - 1));
				if (subMax <= maxDiff) {
					break;
				}
				if (subMax < min) {
					min = subMax;
				}
				if (diff > maxDiff) {
					maxDiff = diff;
				}
			}
		}

		cache2[n][k] = min;

		return min;
	}

	private static int calcDiff(int[] NS, int start, int end) {
		if (start == end)
			return 0;

		if (cache[start][end] != -1) {
			return cache[start][end];
		}
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		for (int i = start; i <= end; i++) {
			if (NS[i] < min) {
				min = NS[i];
			}
			if (NS[i] > max) {
				max = NS[i];
			}
			cache[start][i] = max - min;
		}

		return max - min;
	}
}
