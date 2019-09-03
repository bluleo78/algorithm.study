import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main3 {
	static int N;
	static int M;
	static int D;
	static Node[] nodes;

	static class Node {
		int number = -1;
		int[] ancestors;
		int depth = -1;
		List<Integer> edges = new ArrayList<Integer>();
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		N = Integer.parseInt(br.readLine());
		D = (int) Math.floor(Math.log(N) / Math.log(2.0));
		nodes = new Node[N];
		for (int i = 0; i < N; i++) {
			nodes[i] = new Node();
			nodes[i].number = i + 1;
			nodes[i].ancestors = new int[D + 1];
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

			System.out.format("%d\n", solve(nodeIdx0, nodeIdx1));
		}
	}

	private static void setAncestors() {

		// 2^d ancestors
		for (int d = 1; d <= D; d++) {
			for (int i = 0; i < N; i++) {
				nodes[i].ancestors[d] = nodes[nodes[i].ancestors[d - 1]].ancestors[d - 1];
			}
		}
	}

	private static void makeTree(int nodeIdx, int depth) {
		Node currNode = nodes[nodeIdx];
		currNode.depth = depth;
		for (int subNodeIdx : currNode.edges) {
			if (nodes[subNodeIdx].depth != -1)
				continue;
			nodes[subNodeIdx].ancestors[0] = nodeIdx;
			makeTree(subNodeIdx, depth + 1);
		}
	}

	private static int solve(int nodeIdx0, int nodeIdx1) {
		Node node0 = nodes[nodeIdx0];
		Node node1 = nodes[nodeIdx1];

		if (node0.depth > node1.depth) {
			for (int d = D; d >= 0; d--) {
				if (node0.depth - node1.depth >= (1 << d)) {
					node0 = nodes[node0.ancestors[d]];
				}
			}
		} else if (node0.depth < node1.depth) {
			for (int d = D; d >= 0; d--) {
				if (node1.depth - node0.depth >= (1 << d)) {
					node1 = nodes[node1.ancestors[d]];
				}
			}
		}

		if (node0 != node1) {
			for (int d = D; d >= 0; d--) {
				if (node0.ancestors[d] != node1.ancestors[d]) {
					node0 = nodes[node0.ancestors[d]];
					node1 = nodes[node1.ancestors[d]];
				}
			}

			return nodes[node0.ancestors[0]].number;
		} else {
			return node0.number;
		}
	}
}
