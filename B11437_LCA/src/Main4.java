import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main4 {
	static int N;
	static int M;
	static int D;
	static List<Integer>[] EDGES;
	static int S_LEAF_CNT;
	static int[] S_LEAF_IDX;
	static Node S_ROOT_NODE;
	static List<Node> S_LEAF_NODES;

	static class Node {
		int number = -1;
		int depth = -1;
		int leftRange;
		int rightRange;
		Node left;
		Node right;
		Node min;

		public Node(int n, int d, int idx) {
			number = n;
			depth = d;
			min = this;
			leftRange = idx;
			rightRange = idx;
		}

		public Node(Node l, Node r) {
			left = l;
			right = r;
			min = left.min.depth < right.min.depth ? left.min : right.min;
			leftRange = left.leftRange;
			rightRange = right.rightRange;
		}
	}

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

		S_LEAF_NODES = new ArrayList<Node>();
		makeSegmentTreeLeaves(0, 0);
		S_ROOT_NODE = makeSegmentTree(S_LEAF_NODES);
		M = Integer.parseInt(br.readLine());

		for (int i = 0; i < M; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int nodeIdx0 = Integer.parseInt(st.nextToken()) - 1;
			int nodeIdx1 = Integer.parseInt(st.nextToken()) - 1;

			System.out.format("%d\n", solve(nodeIdx0, nodeIdx1));
		}
	}

	private static Node makeSegmentTree(List<Node> nodes) {
		List<Node> parentNodeList = new ArrayList<Node>();

		for (int i = 0; i < nodes.size(); i++) {
			if (i + 1 < nodes.size()) {
				Node leftNode = nodes.get(i);
				Node rightNode = nodes.get(i + 1);
				parentNodeList.add(new Node(leftNode, rightNode));
				i++;
			} else {
				parentNodeList.add(nodes.get(i));
			}
		}

		if (parentNodeList.size() > 1) {
			return makeSegmentTree(parentNodeList);
		} else {
			return parentNodeList.get(0);
		}
	}

	private static void makeSegmentTreeLeaves(int nodeIdx, int depth) {
		S_LEAF_IDX[nodeIdx] = S_LEAF_NODES.size();
		S_LEAF_NODES.add(new Node(nodeIdx + 1, depth, S_LEAF_NODES.size()));
		for (int subNodeIdx : EDGES[nodeIdx]) {
			if (S_LEAF_IDX[subNodeIdx] >= 0)
				continue;
			makeSegmentTreeLeaves(subNodeIdx, depth + 1);
			S_LEAF_NODES.add(new Node(nodeIdx + 1, depth, S_LEAF_NODES.size()));
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

		return searchSegmentTree(S_ROOT_NODE, sLeafIdx0, sLeafIdx1).number;
	}

	private static Node searchSegmentTree(Node node, int leftRange, int rightRange) {
		if (node.left == null || node.right == null) {
			return node;
		}

		if (node.leftRange == leftRange && node.rightRange == rightRange) {
			return node.min;
		}

		Node minLeftNode = null;
		Node minRightNode = null;
		if (leftRange <= node.left.rightRange) {
			if (rightRange <= node.left.rightRange) {
				minLeftNode = searchSegmentTree(node.left, leftRange, rightRange);
			} else {
				minLeftNode = searchSegmentTree(node.left, leftRange, node.left.rightRange);
				minRightNode = searchSegmentTree(node.right, node.right.leftRange, rightRange);
			}
		} else {
			minRightNode = searchSegmentTree(node.right, leftRange, rightRange);
		}

		if (minLeftNode == null) {
			return minRightNode;
		} else if (minRightNode == null) {
			return minLeftNode;
		} else {
			return minLeftNode.depth < minRightNode.depth ? minLeftNode : minRightNode;
		}
	}
}
