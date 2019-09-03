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

public class Main4 {

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
		recyclePipes();

		// Print
		System.out.println(maxProfit);
	}

	static class Node {
		Node parent;
		int nIdx;
		int mIdx;
		int value = 0;
		int bound = 0;
		int[] MA1 = new int[50];

		public Node(int nIdx, int mIdx, Node parent) {
			this.parent = parent;
			this.nIdx = nIdx;
			this.mIdx = mIdx;
			if (parent != null) {
				for (int i = 0; i < M; i++) {
					this.MA1[i] = parent.MA1[i];
				}
				this.value = parent.value + 1;
			} else {
				for (int i = 0; i < M; i++) {
					this.MA1[i] = MA[i];
				}
			}
			if (this.mIdx >= 0) {
				this.MA1[this.mIdx] -= NA[this.nIdx];
				if (this.MA1[this.mIdx] < 0 ) {
					this.value = -1;
				}
			}
		}
	}

	private static void recyclePipes() {
		PriorityQueue<Node> queue = new PriorityQueue<Node>(new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				return o2.bound - o1.bound;
			}
		});
		Node rootNode = new Node(-1, -1, null);
		int bestValue = 0;
		rootNode.bound = calculateBound(rootNode);
		queue.add(rootNode);

		while (!queue.isEmpty()) {
			Node currNode = queue.poll();

			if (currNode.nIdx < N - 1 && currNode.bound > bestValue) {
				for (int mIdx = 0; mIdx < M; mIdx++) {
					Node childNode = new Node(currNode.nIdx + 1, mIdx, currNode);
					if (childNode.value > bestValue) {
						bestValue = childNode.value;
						System.out.println(bestValue);
					}
					childNode.bound = calculateBound(childNode);
					if (childNode.bound > bestValue) {
						queue.add(childNode);
					}
				}
			}
		}

		maxProfit = bestValue;
	}

	private static int calculateBound(Node node) {
		if (node.value == -1) {
			return -1;
		}
		int remainWeight = 0;
		for (int mIdx = 0; mIdx < M; mIdx++) {
			if (node.nIdx < 0 || node.MA1[mIdx] >= NA[node.nIdx]) {
				remainWeight += node.MA1[mIdx];
			}
		}
		int futureProfit = 0;
		int futureWeight = 0;
		int nIdx2 = node.nIdx + 1;
		while (nIdx2 < N && remainWeight - (futureWeight + NA[nIdx2]) >= 0) {
			futureProfit++;
			futureWeight += NA[nIdx2];
			nIdx2++;
		}
		int bound = node.value + futureProfit + (remainWeight - futureWeight) / NA[nIdx2 - 1];

		return bound;
	}
}
