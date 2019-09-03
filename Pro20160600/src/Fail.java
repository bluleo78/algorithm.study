import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class Fail {

	public static class Edge {
		int from;
		int to;
		int cost;

		public Edge(int i, int j, int c) {
			from = i;
			to = j;
			cost = c;
		}
	}

	static private List<Edge> edgeList;

	public static void main(String[] args) throws IOException {
		int TC;
		int N;
		int M;
		int[][] MS;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		TC = Integer.parseInt(br.readLine());

		// Process
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < TC; i++) {
			N = Integer.parseInt(br.readLine());
			M = Integer.parseInt(br.readLine());
			MS = new int[N][N];
			for (int x = 0; x < N; x++) {
				for (int y = 0; y < N; y++) {
					MS[x][y] = -1;
				}
			}
			edgeList = new ArrayList<Edge>();
			for (int j = 0; j < M; j++) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				int m0 = Integer.parseInt(st.nextToken());
				int m1 = Integer.parseInt(st.nextToken());
				int c = Integer.parseInt(st.nextToken());
				MS[m0 - 1][m1 - 1] = c;
				MS[m1 - 1][m0 - 1] = c;
				edgeList.add(new Edge(m0 - 1, m1 - 1, c));
			}
			boolean[] visited = new boolean[N];
			for (int j = 0; j < N; j++) {
				visited[j] = false;
			}
			edgeList.sort(new Comparator<Edge>() {

				@Override
				public int compare(Edge o1, Edge o2) {
					return o1.cost - o2.cost;
				}
			});

			System.out.printf("#%d %d\n", i, calculate(N, MS, 0, M - 1));
		}

		System.out.printf("TIME => %2.2f", (System.currentTimeMillis() - startTime) / 1000.0);
	}

	private static int calculate(int N, int[][] MS, int startIdx, int endIdx) {
		if (startIdx >= endIdx) {
			return 0;
		}
		int cost1 = Integer.MAX_VALUE;
		// remove start
		if (isRemovable(N, MS, edgeList.get(startIdx).from, edgeList.get(startIdx).to)) {
			int m0 = edgeList.get(startIdx).from;
			int m1 = edgeList.get(startIdx).to;
			int cost = MS[m0][m1];
			MS[m0][m1] = -1;
			MS[m1][m0] = -1;
			cost1 = calculate(N, MS, startIdx + 1, endIdx);
			MS[m0][m1] = cost;
			MS[m1][m0] = cost;
		}

		int cost2 = Integer.MAX_VALUE;
		if (isRemovable(N, MS,  edgeList.get(endIdx).from, edgeList.get(endIdx).to)) {
			int m0 = edgeList.get(endIdx).from;
			int m1 = edgeList.get(endIdx).to;
			int cost = MS[m0][m1];
			MS[m0][m1] = -1;
			MS[m1][m0] = -1;
			cost2 = calculate(N, MS, startIdx, endIdx - 1);
			MS[m0][m1] = cost;
			MS[m1][m0] = cost;
		}

		return Math.min(edgeList.get(endIdx).cost - edgeList.get(startIdx).cost, Math.min(cost1, cost2));
	}

	private static boolean isRemovable(int N, int[][] MS, int m0, int m1) {
		int cost = MS[m0][m1];
		MS[m0][m1] = -1;
		MS[m1][m0] = -1;
		boolean[] visited = new boolean[N];
		for (int j = 0; j < N; j++) {
			visited[j] = false;
		}
		boolean result = isConnected(N, MS, m0, m1, visited);
		MS[m0][m1] = cost;
		MS[m1][m0] = cost;

		return result;
	}

	private static boolean isConnected(int N, int[][] MS, int m0, int m1, boolean[] visited) {
		visited[m0] = true;
		if (MS[m0][m1] != -1)
			return true;

		for (int i = 0; i < N; i++) {
			if (MS[m0][i] == -1)
				continue;
			if (visited[i])
				continue;
			if (isConnected(N, MS, i, m1, visited)) {
				return true;
			}
		}

		visited[m0] = false;
		return false;
	}
}
