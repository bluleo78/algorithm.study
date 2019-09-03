import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int M;
	static int K;
	static Edge[] edges;
	static int[] groups;
	static int groupCnt;
	static List<Edge> exEdges;

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
			M = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());

			edges = new Edge[M];
			for (int i = 0; i < M; i++) {
				st = new StringTokenizer(br.readLine());
				int x = Integer.parseInt(st.nextToken()) - 1;
				int y = Integer.parseInt(st.nextToken()) - 1;
				int w = Integer.parseInt(st.nextToken());

				edges[i] = new Edge(x, y, w);
			}

			exEdges = new ArrayList<Edge>();
			for (int i = 0; i < K; i++) {
				st = new StringTokenizer(br.readLine());
				int e0 = Integer.parseInt(st.nextToken()) - 1;
				int e1 = Integer.parseInt(st.nextToken()) - 1;
				edges[e0].exEdges.add(edges[e1]);
				edges[e1].exEdges.add(edges[e0]);
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
		// sort edges
		Arrays.sort(edges, new Comparator<Edge>() {

			@Override
			public int compare(Edge o1, Edge o2) {
				return o1.weight > o2.weight ? 1 : (o1.weight == o2.weight ? 0 : -1);
			}
		});

		groups = new int[N];
		for (int i = 0; i < N; i++) {
			groups[i] = i;
		}
		groupCnt = N;

		return processKruskal(0, 0);
	}

	private static long processKruskal(int eIdx, long minCost) {
		if (eIdx >= M) {
			if (groupCnt == 1) {
				return minCost;
			} else {
				return -1;
			}
		}

		Edge edge = edges[eIdx];

		// check edge is excluded
		if (edge.isExcluded) {
			return processKruskal(eIdx + 1, minCost);
		}

		// check two nodes are already linked
		if (getGroup(edge.x) == getGroup(edge.y)) {
			return processKruskal(eIdx + 1, minCost);
		}

		long result = -1;

		// check edge is exclusive
		if (edge.exEdges.size() > 0) {
			// check if not used
			result = processKruskal(eIdx + 1, minCost);
		}

		// calculate
		List<Edge> exEdges = new ArrayList<Edge>();
		for (Edge e : edge.exEdges) {
			if (!e.isExcluded) {
				e.isExcluded = true;
				exEdges.add(e);
			}
		}

		int bkGroupX = groups[edge.x];
		int bkGroupY = groups[edge.y];
		int maxGroup = Math.max(getGroup(edge.x), getGroup(edge.y));
		setGroup(edge.x, maxGroup);
		setGroup(edge.y, maxGroup);
		groupCnt--;

		// call
		if (result >= 0) {
			long result1 = processKruskal(eIdx + 1, minCost + edge.weight);
			if (result1 >= 0) {
				result = Math.min(result1, result);
			}
		} else {
			result = processKruskal(eIdx + 1, minCost + edge.weight);
		}

		// restore
		for (Edge e : exEdges) {
			e.isExcluded = false;
		}
		groups[edge.x] = bkGroupX;
		groups[edge.y] = bkGroupY;
		groupCnt++;

		return result;
	}

	private static int getGroup(int x) {
		if (groups[x] == x) {
			return x;
		} else {
			groups[x] = getGroup(groups[x]);
			return groups[x];
		}
	}

	private static void setGroup(int x, int maxGroup) {
		groups[x] = maxGroup;
	}
}
