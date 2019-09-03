import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main2 {

	public static class NodeCost {
		public int pipeCount;
		public int remainder;
		public ArrayList<Integer> pipeIndexes = new ArrayList<Integer>();

		public NodeCost(int c, int r) {
			pipeCount = c;
			remainder = r;
		}

		public NodeCost(NodeCost src) {
			pipeCount = src.pipeCount;
			remainder = src.remainder;
			pipeIndexes.addAll(src.pipeIndexes);
		}
	}

	static int M;
	static int N;
	static int[] MA;
	static int[] NA;
	static boolean[] used;
	static Map<Integer, NodeCost> cache2;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parsing arguments
		M = Integer.parseInt(br.readLine());
		StringTokenizer st = new StringTokenizer(br.readLine());
		MA = new int[M];
		for (int i = 0; i < M; i++) {
			MA[i] = Integer.parseInt(st.nextToken());
		}
		N = Integer.parseInt(br.readLine());
		st = new StringTokenizer(br.readLine());
		NA = new int[N];
		for (int i = 0; i < N; i++) {
			NA[i] = Integer.parseInt(st.nextToken());
		}

		// sort
		Arrays.sort(MA);
		Arrays.sort(NA);

		// Initialize
		used = new boolean[N];
		for (int i = 0; i < N; i++) {
			used[i] = false;
		}

		// Calculate
		int result = 0;
		for (int i = 0; i < M; i++) {
			cache2 = new HashMap<Integer, NodeCost>();

			NodeCost cost = recyclePipe(MA[i], 0);
			result += cost.pipeCount;

			System.out.printf("D => %d, %d\n",  cost.pipeCount, cost.remainder);
			for (int nIdx : cost.pipeIndexes) {
				used[nIdx] = true;
			}
		}
		System.out.println(result);
	}

	private static NodeCost recyclePipe(int m, int nIdx) {
		int hash = m + nIdx * 1000000;

		// Check cached
		NodeCost maxCost = cache2.get(hash);
		if (maxCost != null) {
			return maxCost;
		}

		// Check boundary condition
		if (nIdx >= N || m - NA[nIdx] < 0) {
			NodeCost cost = new NodeCost(0, m);
			cache2.put(hash, cost);
			return cost;
		}

		// Check if already used
		if (!used[nIdx]) {
			// Calculate cost in case of producing 'nIdx' pipe from M
			NodeCost cost1 = new NodeCost(recyclePipe(m - NA[nIdx], nIdx + 1));
			cost1.pipeCount += 1;
			cost1.pipeIndexes.add(0, nIdx);

			// Calculate cost in case of not producing 'nIdx' pipe from M
			NodeCost cost2 = recyclePipe(m, nIdx + 1);

			// Compare two cases
			if (cost1.pipeCount > cost2.pipeCount) {
				maxCost = cost1;
			} else if (cost1.pipeCount == cost2.pipeCount) {
				if (cost1.remainder < cost2.remainder) {
					maxCost = cost1;
				} else {
					maxCost = cost2;
				}
			} else {
				maxCost = cost2;
			}
		} else {
			// Calculate cost in case of not producing 'nIdx' pipe from M
			maxCost = recyclePipe(m, nIdx + 1);
		}

		cache2.put(hash, maxCost);
		return maxCost;
	}
}
