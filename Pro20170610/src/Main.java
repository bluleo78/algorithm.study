import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeSet;

class Node {
	int idx;
	int distance;
	List<Edge> edges = new ArrayList<Edge>();

	public Node(int n, int d) {
		idx = n;
		distance = d;
	}
}

class Edge {
	int source;
	int target;
	int distance;

	public Edge(int s, int t, int d) {
		source = s;
		target = t;
		distance = d;
	}
}

public class Main {
	static int N;
	static int M;
	static int[][] D;
	static int[] D0;
	static Node[] nodes;

	public static void main(String[] args) throws IOException {
		int T;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		T = Integer.parseInt(br.readLine());
		long[] result = new long[T];

		// Process
		long elapsedTime = 0;
		for (int t = 0; t < T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());

			nodes = new Node[N];
			D = new int[N][N];
			for (int i = 0; i < N; i++) {
				nodes[i] = new Node(i, 0);
				for (int j = 0; j < N; j++) {
					if (i == j) {
						D[i][j] = 0;
					} else {
						D[i][j] = Integer.MAX_VALUE;
					}
				}
			}
			for (int i = 0; i < M; i++) {
				st = new StringTokenizer(br.readLine());
				int source = Integer.parseInt(st.nextToken()) - 1;
				int target = Integer.parseInt(st.nextToken()) - 1;
				int distance = Integer.parseInt(st.nextToken());
				nodes[source].edges.add(new Edge(source, target, distance));
				nodes[target].edges.add(new Edge(target, source, distance));
				D[source][target] = distance;
				D[target][source] = distance;
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
		long result = 0;
		
		processFloyd();

		LinkedHashSet<Integer> waitList = new LinkedHashSet<Integer>();
		waitList.add(0);
		for (int i = 0; i < N - 1; i++) {
			LinkedHashSet<Integer> nextWaitList = new LinkedHashSet<Integer>();
			for (int nIdx : waitList) {
				for (Edge e : nodes[nIdx].edges) {
					if (e.distance > D[nIdx][e.target]) {
						if (e.target == 1)
							result++;
						nextWaitList.add(e.target);
					}
				}
			}
			waitList = nextWaitList;
		}

		return result;
	}

	private static long processFloyd() {
		for (int i = 0; i < N; i++) {
			for (int x = 0; x < N; x++) {
				for (int y = 0; y < N; y++) {
					if (x == y)
						continue;
					if (D[x][i] < Integer.MAX_VALUE && D[i][y] < Integer.MAX_VALUE) {
						D[x][y] = Math.min(D[x][i] + D[i][y], D[x][y]);
					}
				}
			}
		}

		return 0;
	}

	private static long solveDijkstra() {
		long result = 0;

		D0 = new int[N];
		for (int i = 0; i < N; i++) {
			D0[i] = Integer.MAX_VALUE;
		}
		PriorityQueue<Node> waitQ = new PriorityQueue<Node>(new Comparator<Node>() {

			@Override
			public int compare(Node o1, Node o2) {
				return o1.distance - o2.distance;
			}
		});
		waitQ.add(new Node(0, 0));

		while (!waitQ.isEmpty()) {
			Node node = waitQ.poll();

			// NOTE. Same effects as updating node
			if (node.distance > D0[node.idx])
				continue;

			for (Edge e : nodes[node.idx].edges) {
				int nextNodeIdx = e.target;
				if (node.distance + e.distance < D0[nextNodeIdx]) {
					D0[nextNodeIdx] = node.distance + e.distance;
					waitQ.add(new Node(e.target, D0[nextNodeIdx]));
				}
			}

			if (node.idx == 4) {
				break;
			}
		}

		result = D0[4];

		return result;
	}
}
