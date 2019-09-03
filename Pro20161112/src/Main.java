import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int K;
	static int[] NA;

	static class TreeNode {
		int value = -1;
		int valueCnt = 0;
		int maxValue;

		TreeNode left;
		TreeNode right;

		public TreeNode(int v) {
			value = v;
			maxValue = v;
			valueCnt = 1;
		}

		public TreeNode(TreeNode l, TreeNode r) {
			left = l;
			right = r;
			valueCnt = l.valueCnt + r.valueCnt;
			maxValue = r.maxValue;
		}
	}

	public static void main(String[] args) throws IOException {
		int T;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		T = Integer.parseInt(br.readLine());
		int[] result = new int[T];

		// Process
		long startTime = System.currentTimeMillis();
		for (int t = 0; t < T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());

			st = new StringTokenizer(br.readLine());
			NA = new int[N];
			for (int i = 0; i < N; i++) {
				NA[i] = Integer.parseInt(st.nextToken());
			}
			result[t] = calculate();
		}

		for (int t = 0; t < T; t++) {
			System.out.format("#%d %d\n", t + 1, result[t]);
		}

		System.out.format("TIME => %2.2f\n", (System.currentTimeMillis() - startTime) / 1000.0);
	}

	private static int calculate() {
		int midValue = Integer.MIN_VALUE;

		TreeNode rootNode = null;

		for (int i = 0; i < N; i++) {
			if (i >= K) {
				rootNode = removeNodeFromTree(rootNode, NA[i - K]);
			}
			rootNode = addNodeToTree(rootNode, NA[i]);

			if (rootNode.valueCnt == K) {
				midValue = Math.max(midValue, getMidValueFromTree(rootNode));
			}
		}
		return midValue;
	}

	private static TreeNode removeNodeFromTree(TreeNode rootNode, int value) {

		rootNode.valueCnt--;

		if (rootNode.value == value) {
			return rootNode.valueCnt == 0 ? null : rootNode;
		}

		if (value <= rootNode.left.maxValue) {
			TreeNode leftNode = removeNodeFromTree(rootNode.left, value);
			if (leftNode == null) {
				return rootNode.right;
			} else {
				rootNode.left = leftNode;
			}
		} else {
			TreeNode rightNode = removeNodeFromTree(rootNode.right, value);
			if (rightNode == null) {
				return rootNode.left;
			} else {
				rootNode.right = rightNode;
			}
		}

		rootNode.maxValue = rootNode.right.maxValue;

		return rootNode;
	}

	private static TreeNode addNodeToTree(TreeNode rootNode, int value) {
		if (rootNode == null) {
			return new TreeNode(value);
		}

		if (rootNode.value != -1) {
			if (value == rootNode.value) {
				rootNode.valueCnt++;
				return rootNode;
			} else if (value < rootNode.value) {
				return new TreeNode(new TreeNode(value), rootNode);
			} else {
				return new TreeNode(rootNode, new TreeNode(value));
			}
		}

		if (value <= rootNode.left.maxValue) {
			TreeNode leftNode = addNodeToTree(rootNode.left, value);
			rootNode.left = leftNode;
		} else {
			TreeNode rightNode = addNodeToTree(rootNode.right, value);
			rootNode.right = rightNode;
		}

		rootNode.valueCnt = rootNode.left.valueCnt + rootNode.right.valueCnt;
		rootNode.maxValue = rootNode.right.maxValue;

		return rootNode;
	}

	private static int getMidValueFromTree(TreeNode rootNode) {

		return getMidValueFromTree(rootNode, rootNode.valueCnt / 2);
	}

	private static int getMidValueFromTree(TreeNode rootNode, int index) {
		if (rootNode.value != -1) {
			return rootNode.value;
		}

		if (index <= rootNode.left.valueCnt - 1) {
			return getMidValueFromTree(rootNode.left, index);
		} else {
			return getMidValueFromTree(rootNode.right, index - rootNode.left.valueCnt);
		}
	}
}
