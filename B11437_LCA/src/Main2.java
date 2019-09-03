import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main2 {
	static int N;
	static int M;
	static int D;
	static int[][] ANC;
	static int[] DEP;
	static Node[] nodes;

	static class Node {
		List<Integer> edges = new ArrayList<Integer>();
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		N = Integer.parseInt(br.readLine());
		D = (int) Math.floor(Math.log(N) / Math.log(2.0));
		nodes = new Node[N];
		ANC = new int[N][D + 1];
		DEP = new int[N];
		for (int i = 0; i < N; i++) {
			nodes[i] = new Node();
			for (int j = 0; j < D + 1; j++) {
				ANC[i][j] = 0;
			}
			DEP[i] = -1;
		}

		for (int i = 0; i < N - 1; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int nodeIdx0 = Integer.parseInt(st.nextToken()) - 1;
			int nodeIdx1 = Integer.parseInt(st.nextToken()) - 1;
			nodes[nodeIdx0].edges.add(nodeIdx1);
			nodes[nodeIdx1].edges.add(nodeIdx0);
		}

		makeTree(0, 0);

		setAncestors();

		M = Integer.parseInt(br.readLine());

		for (int i = 0; i < M; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int nodeIdx0 = Integer.parseInt(st.nextToken()) - 1;
			int nodeIdx1 = Integer.parseInt(st.nextToken()) - 1;

			System.out.format("%d\n", solve(nodeIdx0, nodeIdx1) + 1);
		}
	}

	private static void setAncestors() {
		// 2^d ancestors
		for (int d = 1; d <= D; d++) {
			for (int i = 0; i < N; i++) {
				ANC[i][d] = ANC[ANC[i][d - 1]][d - 1];
			}
		}
	}

	private static void makeTree(int nodeIdx, int depth) {
		Node currNode = nodes[nodeIdx];
		DEP[nodeIdx] = depth;
		for (int subNodeIdx : currNode.edges) {
			if (DEP[subNodeIdx] != -1)
				continue;
			ANC[subNodeIdx][0] = nodeIdx;
			makeTree(subNodeIdx, depth + 1);
		}
	}

	private static int solve(int nodeIdx0, int nodeIdx1) {
		if (DEP[nodeIdx0] > DEP[nodeIdx1]) {
			for (int d = D; d >= 0; d--) {
				if (DEP[nodeIdx0] - DEP[nodeIdx1] >= (1 << d)) {
					nodeIdx0 = ANC[nodeIdx0][d];
				}
			}
		} else if (DEP[nodeIdx0] < DEP[nodeIdx1]) {
			for (int d = D; d >= 0; d--) {
				if (DEP[nodeIdx1] - DEP[nodeIdx0] >= (1 << d)) {
					nodeIdx1 = ANC[nodeIdx1][d];
				}
			}
		}

		if (nodeIdx0 == nodeIdx1) {
			return nodeIdx0;
		} else {
			for (int d = D; d >= 0; d--) {
				if (ANC[nodeIdx0][d] != ANC[nodeIdx1][d]) {
					nodeIdx0 = ANC[nodeIdx0][d];
					nodeIdx1 = ANC[nodeIdx1][d];
				}
			}

			return ANC[nodeIdx0][0];
		}
	}
}
