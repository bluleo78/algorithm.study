import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class MainPrim2 {

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
	static ArrayList<ArrayList<Edge>> nodeList;

	public static void main(String[] args) throws IOException {
		int TC;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		TC = Integer.parseInt(br.readLine());

		// Process
		long startTime = System.currentTimeMillis();
		for (int tc = 0; tc < TC; tc++) {
			N = Integer.parseInt(br.readLine());
			nodeList = new ArrayList<ArrayList<Edge>>();

			for (int j = 0; j < N; j++) {
				nodeList.add(new ArrayList<Edge>());
			}
			M = Integer.parseInt(br.readLine());
			edgeList = new ArrayList<Edge>();
			for (int j = 0; j < M; j++) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				int source = Integer.parseInt(st.nextToken()) - 1;
				int target = Integer.parseInt(st.nextToken()) - 1;
				int weight = Integer.parseInt(st.nextToken());
				Edge edge = new Edge(source, target, weight);
				edgeList.add(edge);
				nodeList.get(source).add(new Edge(source, target, weight));
				nodeList.get(target).add(new Edge(target, source, weight));
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
		for (int i = 0; i < M;) {
			// prime

			int cost = runPrim(edgeList.get(i));

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

	private static int runPrim(Edge initEdge) {
		// Prepare
		PriorityQueue<Edge> candiateEdges = new PriorityQueue<Edge>(new Comparator<Edge>() {

			@Override
			public int compare(Edge o1, Edge o2) {
				return o1.weight - o2.weight;
			}
		});

		// Init
		boolean[] visited = new boolean[N];
		for (int i = 0; i < N; i++) {
			visited[i] = false;
		}
		visited[initEdge.source] = true;
		for (Edge edge : nodeList.get(initEdge.source)) {
			if (!visited[edge.target] && edge.weight >= initEdge.weight) {
				candiateEdges.add(edge);
			}
		}

		int max = Integer.MIN_VALUE;
		while (!candiateEdges.isEmpty()) {
			Edge candidateEdge = candiateEdges.poll();
			if (visited[candidateEdge.target]) {
				continue;
			}

			visited[candidateEdge.target] = true;
			max = Math.max(max, candidateEdge.weight - initEdge.weight);

			// Add candidates from current node
			for (Edge edge : nodeList.get(candidateEdge.target)) {
				if (!visited[edge.target] && edge.weight >= initEdge.weight) {
					candiateEdges.add(edge);
				}
			}
		}

		int visitedCnt = 0;
		for (int i = 0; i < N; i++) {
			if (visited[i])
				visitedCnt++;
		}

		return visitedCnt == N ? max : -1;
	}
}
