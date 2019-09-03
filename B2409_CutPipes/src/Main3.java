import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Main3 {

	static int M;
	static int N;
	static int[] MA;
	static int[] NA;
	static int[] TA;
	static int MAX = 0;
	static Map<Integer, Integer> cache = new HashMap<Integer, Integer>();
	static int cases = 0;
	static int hit = 0;
	static int maxProfit = 0;

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
		Arrays.sort(MA);
		Arrays.sort(NA);

		// Process
		for (int mIdx = 0; mIdx < M; mIdx++) {
			recyclePipes(0, mIdx, 0);
		}

		// Print
		System.out.println(maxProfit);
	}

	private static void recyclePipes(int nIdx0, int mIdx0, int prevProfit) {
		// check boundary condition
		if (nIdx0 >= N) {
			return;
		}
		if (MA[mIdx0] - NA[nIdx0] < 0) {
			return;
		}

		// weight
		MA[mIdx0] -= NA[nIdx0];

		// profit
		int currProfit = prevProfit + 1;
		if (currProfit > maxProfit) {
			maxProfit = currProfit;
			System.out.println(maxProfit);
		}

		// bound
		int bound = calculateBound(nIdx0, currProfit);

		if (bound > maxProfit) {
			//
			List<Integer> indexList= new ArrayList<Integer>();
			for (int mIdx = 0; mIdx < M; mIdx++) {
				indexList.add(mIdx);
			}
			indexList.sort(new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					return MA[o1] - MA[o2];
				}
			});
			// Traverse tree
			for (int mIdx: indexList) {
				// Cut & Produce 'nIdx' pipe for proceeding 'nIdx + 1'
				recyclePipes(nIdx0 + 1, mIdx, currProfit);
			}
		}

		// Recover pipe for next iteration
		MA[mIdx0] += NA[nIdx0];
	}

	private static int calculateBound(int nIdx0, int currProfit) {
		int remainWeight = 0;
		for (int mIdx = 0; mIdx < M; mIdx++) {
			if (MA[mIdx] >= NA[nIdx0]) {
				remainWeight += MA[mIdx];
			}
		}
//		if (cache.get(remainWeight) != null) {
//			return cache.get(remainWeight);
//		}
		int futureProfit = 0;
		int futureWeight = 0;
		int nIdx2 = nIdx0 + 1;
		while (nIdx2 < N && remainWeight - (futureWeight + NA[nIdx2]) >= 0) {
			futureProfit++;
			futureWeight += NA[nIdx2];
			nIdx2++;
		}
		int bound = currProfit + futureProfit + (remainWeight - futureWeight) / NA[nIdx2 - 1];
		
//		cache.put(remainWeight, bound);

		return bound;
	}
}
