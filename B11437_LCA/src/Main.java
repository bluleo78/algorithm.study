import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class Node {
	int nodeIdx = -1;
	int depth = 10000;
}

public class Main {
	static int N;
	static int M;
	static int D;
	static int H;
	static List<Integer>[] EDGES;
	static int S_LEAF_CNT;
	static int[] S_LEAF_IDX;
	static Node S_ROOT_NODE;
	static List<Node> S_LEAF_NODES;
	static Node[] S_NODES;
	static int S_NODE_CNT;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		N = Integer.parseInt(br.readLine());
		D = (int) Math.floor(Math.log(N) / Math.log(2.0));
		EDGES = new List[N];
		S_LEAF_IDX = new int[N];
		S_LEAF_CNT = 0;
		for (int i = 0; i < N; i++) {
			EDGES[i] = new ArrayList<Integer>();
			S_LEAF_IDX[i] = -1;
		}

		for (int i = 0; i < N - 1; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int nodeIdx0 = Integer.parseInt(st.nextToken()) - 1;
			int nodeIdx1 = Integer.parseInt(st.nextToken()) - 1;
			EDGES[nodeIdx0].add(nodeIdx1);
			EDGES[nodeIdx1].add(nodeIdx0);
		}

		H = 1;
		while (H < 2 * N)
			H = H << 1;
		S_NODES = new Node[H * 2 + 1];
		S_NODE_CNT = 0;
		for (int i = 0; i < H * 2 + 1; i++) {
			S_NODES[i] = new Node();
		}

		makeSegmentTree(0, 0);
		M = Integer.parseInt(br.readLine());

		for (int i = 0; i < M; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int nodeIdx0 = Integer.parseInt(st.nextToken()) - 1;
			int nodeIdx1 = Integer.parseInt(st.nextToken()) - 1;

			System.out.format("%d\n", solve(nodeIdx0, nodeIdx1));
		}
	}

	private static void makeSegmentTree(int nodeIdx, int depth) {
		S_LEAF_IDX[nodeIdx] = S_NODE_CNT;
		updateSegmentTree(S_NODE_CNT++, nodeIdx, depth);
		for (int subNodeIdx : EDGES[nodeIdx]) {
			if (S_LEAF_IDX[subNodeIdx] >= 0)
				continue;
			makeSegmentTree(subNodeIdx, depth + 1);
			updateSegmentTree(S_NODE_CNT++, nodeIdx, depth);
		}
	}

	private static void updateSegmentTree(int sLeafIdx, int nodeIdx, int depth) {
		int sNodeIdx = (H - 1) + sLeafIdx;
		S_NODES[sNodeIdx].depth = depth;
		S_NODES[sNodeIdx].nodeIdx = nodeIdx;
		while (sNodeIdx > 0) {
			sNodeIdx = (sNodeIdx - 1) / 2;
			Node leftNode = S_NODES[sNodeIdx * 2 + 1];
			Node rightNode = S_NODES[sNodeIdx * 2 + 2];
			if (leftNode.depth < rightNode.depth) {
				S_NODES[sNodeIdx].depth = leftNode.depth;
				S_NODES[sNodeIdx].nodeIdx = leftNode.nodeIdx;
			} else {
				S_NODES[sNodeIdx].depth = rightNode.depth;
				S_NODES[sNodeIdx].nodeIdx = rightNode.nodeIdx;
			}
		}
	}

	private static Node searchSegmentTree(int sNodeIdx, int sMinLeafIdx, int sMaxLeafIdx, int sLeftNodeIdx,
			int sRightNodeIdx) {

		if (sMinLeafIdx <= sLeftNodeIdx && sMaxLeafIdx >= sRightNodeIdx)
			return S_NODES[sNodeIdx];

		if (sMaxLeafIdx < sLeftNodeIdx || sMinLeafIdx > sRightNodeIdx) {
			return S_NODES[2 * H];
		}

		int sMidNodeIdx = (sLeftNodeIdx + sRightNodeIdx) / 2;
		Node leftResult = searchSegmentTree(sNodeIdx * 2 + 1, sMinLeafIdx, sMaxLeafIdx, sLeftNodeIdx, sMidNodeIdx);
		Node rightResult = searchSegmentTree(sNodeIdx * 2 + 2, sMinLeafIdx, sMaxLeafIdx, sMidNodeIdx + 1,
				sRightNodeIdx);

		if (leftResult.depth < rightResult.depth) {
			return leftResult;
		} else {
			return rightResult;
		}
	}

	private static int solve(int nodeIdx0, int nodeIdx1) {
		int sLeafIdx0 = S_LEAF_IDX[nodeIdx0];
		int sLeafIdx1 = S_LEAF_IDX[nodeIdx1];
		if (sLeafIdx0 > sLeafIdx1) {
			int temp = sLeafIdx1;
			sLeafIdx1 = sLeafIdx0;
			sLeafIdx0 = temp;
		}

		return searchSegmentTree(0, sLeafIdx0, sLeafIdx1, 0, H - 1).nodeIdx + 1;
	}
}
