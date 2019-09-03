import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Main2 {

	static int M;
	static int N;
	static int[] MA;
	static int[] NA;
	static int[] TA;
	static int MAX = 0;
	static Map<String, Integer> cache = new HashMap<String, Integer>();
	static int cases = 0;
	static int hit = 0;

	public static void main(String[] args) throws IOException {
		// Input
		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Scanner scanner = new Scanner(System.in);

		// Parsing input
		// M = Integer.parseInt(br.readLine());
		M = scanner.nextInt();
		MA = new int[M];
		for (int i = 0; i < M; i++) {
			MA[i] = scanner.nextInt();
		}
		N = scanner.nextInt();
		NA = new int[N];
		TA = new int[N];
		for (int i = 0; i < N; i++) {
			NA[i] = scanner.nextInt();
			TA[i] = Integer.MAX_VALUE;
		}

		// Initialize
		Arrays.sort(NA);

		// Process
		int result = recyclePipes(0);

		// Print
		System.out.println(result);
		System.out.println(1.0 * hit / cases);
	}

	private static int recyclePipes(int nIdx) {
		// check boundary condition
		if (nIdx >= N) {
			return 0;
		}

		// Sort input array for generating hash key of cache
		// Arrays.sort(MA);
		ArrayList<Integer> q = new ArrayList<Integer>();
		for (int mIdx = 0; mIdx < M; mIdx++) {
			if (MA[mIdx] >= NA[nIdx]) {
				q.add(MA[mIdx]);
			}
		}

		String key = q.stream().sorted().map(i -> String.valueOf(i)).collect(Collectors.joining(":"));

		// Check cache
		if (cache.get(key) != null) {
			return cache.get(key);
		}

		// Traverse tree
		int max = -1;
		for (int mIdx = 0; mIdx < M; mIdx++) {
			// Check cutting is possible
			if (MA[mIdx] - NA[nIdx] >= 0) {
				// Cut & Produce 'nIdx' pipe for proceeding 'nIdx + 1'
				MA[mIdx] = MA[mIdx] - NA[nIdx];
				int cost = recyclePipes(nIdx + 1);
				if (cost > max) {
					max = cost;
				}

				// Recover pipe for next iteration
				MA[mIdx] = MA[mIdx] + NA[nIdx];
			}
		}

		// add cache
		cache.put(key, max + 1);

		return max + 1;
	}
}
