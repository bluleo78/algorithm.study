import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static Node[] nodes;
	static List<Integer>[] edges;
	static boolean[] visited;

	static class Node {
		boolean hasCat = false;
		int catCnt = 0;
		int depth = 0;
		List<Node> children = new ArrayList<Node>();
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

			nodes = new Node[N];
			edges = new List[N];
			visited = new boolean[N];
			st = new StringTokenizer(br.readLine());
			for (int i = 0; i < N; i++) {
				int cat = Integer.parseInt(st.nextToken());
				nodes[i] = new Node();
				nodes[i].hasCat = cat == 1 ? true : false;
				edges[i] = new ArrayList<Integer>();
				visited[i] = false;
			}

			for (int i = 0; i < N - 1; i++) {
				st = new StringTokenizer(br.readLine());
				int n0 = Integer.parseInt(st.nextToken()) - 1;
				int n1 = Integer.parseInt(st.nextToken()) - 1;
				edges[n0].add(n1);
				edges[n1].add(n0);
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
		// make tree from 1st node
		makeTree(0, 0);

		// count
		return count(nodes[0], null);
	}

	private static int makeTree(int nodeIdx, int depth) {

		visited[nodeIdx] = true;

		int catCnt = 0;
		for (Integer subNodeIdx : edges[nodeIdx]) {
			if (visited[subNodeIdx])
				continue;

			nodes[nodeIdx].children.add(nodes[subNodeIdx]);

			catCnt += makeTree(subNodeIdx, depth + 1);
		}

		if (nodes[nodeIdx].hasCat) {
			catCnt++;
		}
		nodes[nodeIdx].catCnt = catCnt;
		nodes[nodeIdx].depth = depth;

		return catCnt;
	}

	private static long count(Node node, Node catSplitNode) {
		long caseCnt = 1;
		if (node.hasCat) {
			for (Node subNode : node.children) {
				long subCaseCnt = count(subNode, node);
				if (subCaseCnt > 0) {
					caseCnt = caseCnt * subCaseCnt % 1000007;
				}
			}
			if (catSplitNode != null) {
				caseCnt = caseCnt * (long) (Math.pow(2, node.depth - catSplitNode.depth) - 1) % 1000007;
			} else {
				caseCnt = caseCnt * (long) (Math.pow(2, node.depth)) % 1000007;
			}

			return caseCnt;
		} else {
			if (node.children.size() == 0) {
				if (catSplitNode != null) {
					return caseCnt * (long) (Math.pow(2, node.depth - catSplitNode.depth)) % 1000007;
				} else {
					return caseCnt * (long) (Math.pow(2, node.depth)) % 1000007;
				}
			}

			// visit all sub nodes with catCnt > 0
			List<Long> subCaseCntList = new ArrayList<Long>();
			for (Node subNode : node.children) {
				if (subNode.catCnt > 0) {
					long subCaseCnt = count(subNode, subNode.catCnt == node.catCnt ? catSplitNode : node);
					subCaseCntList.add(subCaseCnt);
				}
			}

			// only one path
			if (subCaseCntList.size() == 1) { // not split node
				caseCnt = subCaseCntList.get(0);
			} else { // split node
				if (catSplitNode != null) {
					subCaseCntList.add((long) (Math.pow(2, node.depth - catSplitNode.depth) - 1));
				}
				caseCnt = countCombi(0, subCaseCntList.size() - 1, subCaseCntList);
				caseCnt += countCombi(0, subCaseCntList.size(), subCaseCntList);
			}

			// multiply no cat nodes
			for (Node subNode : node.children) {
				if (subNode.catCnt == 0) {
					caseCnt = caseCnt * count(subNode, node) % 1000007;
				}
			}
		}

		return caseCnt;
	}

	private static long countCombi(int idx, int selSize, List<Long> subCaseCntList) {
		if (selSize == 0)
			return 1;
		if (subCaseCntList.size() - idx == selSize) {
			long caseCnt = 1;
			for (int i = idx; i < subCaseCntList.size(); i++) {
				caseCnt = caseCnt * subCaseCntList.get(i) % 1000007;
			}
			return caseCnt;
		}

		long caseCnt = subCaseCntList.get(idx) * countCombi(idx + 1, selSize - 1, subCaseCntList) % 1000007;
		caseCnt = (caseCnt + countCombi(idx + 1, selSize, subCaseCntList)) % 1000007;
		return caseCnt;
	}
}
