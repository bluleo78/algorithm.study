import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class MainKruskal {

	static class Edge {
		int source;
		int target;
		int weight;

		public Edge(int s, int t, int w) {
			source = s;
			target = t;
			weight = w;
		}
	}

	static int N;
	static int M;
	static int[][] MS;
	static List<Edge> edgeList;
	static int[] parent;

	public static void main(String[] args) throws IOException {
		int TC;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		TC = Integer.parseInt(br.readLine());

		// Process
		long startTime = System.currentTimeMillis();
		for (int tc = 0; tc < TC; tc++) {
			N = Integer.parseInt(br.readLine());
			M = Integer.parseInt(br.readLine());
			edgeList = new ArrayList<Edge>();
			for (int j = 0; j < M; j++) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				int source = Integer.parseInt(st.nextToken()) - 1;
				int target = Integer.parseInt(st.nextToken()) - 1;
				int weight = Integer.parseInt(st.nextToken());
				Edge edge = new Edge(source, target, weight);
				edgeList.add(edge);
			}
			System.out.printf("#%d %d\n", tc, calculate());
		}

		System.out.printf("TIME => %2.2f", (System.currentTimeMillis() - startTime) / 1000.0);
	}

	private static int calculate() {
		// Sort edges
		edgeList.sort(new Comparator<Edge>() {

			@Override
			public int compare(Edge o1, Edge o2) {
				return o1.weight - o2.weight;
			}
		});

		int min = Integer.MAX_VALUE;
		parent = new int[N];
		for (int i = 0; i < M;) {
			// prime
			int cost = runKruskal(edgeList.get(i).weight);

			// if fail to make MST, exit loop
			if (cost < 0)
				break;

			// get minimum cost
			min = Math.min(min, cost);

			// get next edge which its weight is larger than current weight
			int currWeight = edgeList.get(i).weight;
			do {
				i++;
			} while (i < M - 1 && edgeList.get(i).weight == currWeight);
		}

		return min;
	}

	private static int runKruskal(int initWeight) {
		int max = Integer.MIN_VALUE;

		boolean[] visited = new boolean[N];
		for (int i = 0; i < N; i++) {
			parent[i] = i;
			visited[i] = false;
		}
		for (int i = 0; i < M; i++) {
			Edge edge = edgeList.get(i);
			if (edge.weight < initWeight) {
				continue;
			}

			int rootX = findRoot(edge.source);
			int rootY = findRoot(edge.target);

			if (rootX == rootY) {
				// cycle
				continue;
			} else {
				parent[rootX] = rootY;
				visited[edge.source] = true;
				visited[edge.target] = true;
				max = edge.weight - initWeight;
			}
		}

		int visitedCnt = 0;
		for (int i = 0; i < N; i++) {
			if (visited[i])
				visitedCnt++;
		}

		return visitedCnt == N ? max : -1;
	}

	private static int findRoot(int x) {
		if (x == parent[x]) {
			return x;
		} else {
			parent[x] = findRoot(parent[x]);
			return parent[x];
		}
	}
}
