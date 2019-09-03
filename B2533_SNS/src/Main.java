import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	static class Node {
		List<Integer> links = new ArrayList<Integer>();
		List<Integer> children = new ArrayList<Integer>();
	}

	static int N;
	static Node[] V;
	static boolean[] visited;
	static List<List<Integer>> nodeListByDepth = new ArrayList<List<Integer>>();

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		BufferedReader br = new BufferedReader(new InputStreamReader(
//				new FileInputStream(new File("/home/bluleo78/workspace/algorithm/B2533_SNS/src/sample.txt"))));

		// Parse
		N = Integer.parseInt(br.readLine());

		V = new Node[N];
		visited = new boolean[N];
		for (int i = 0; i < N; i++) {
			V[i] = new Node();
		}

		for (int i = 0; i < N - 1; i++) {
			StringTokenizer token = new StringTokenizer(br.readLine());
			int nIdx0 = Integer.parseInt(token.nextToken()) - 1;
			int nIdx1 = Integer.parseInt(token.nextToken()) - 1;

			V[nIdx0].links.add(nIdx1);
			V[nIdx1].links.add(nIdx0);
		}

		System.out.println(solve());
	}

	private static int solve() {

		// make tree
		List<Integer> nodeList = new ArrayList<Integer>();
		nodeList.add(0);
		while (true) {
			List<Integer> nextNodeList = new ArrayList<Integer>();
			nodeListByDepth.add(nodeList);
			for (int nIdx : nodeList) {
				visited[nIdx] = true;
				for (int subIdx : V[nIdx].links) {
					if (!visited[subIdx]) {
						V[nIdx].children.add(subIdx);
						nextNodeList.add(subIdx);
					}
				}
			}
			if (nextNodeList.isEmpty())
				break;
			nodeList = nextNodeList;
		}

		int[] earlyCnt = new int[N];
		int[] notEarlyCnt = new int[N];
		for (int i = nodeListByDepth.size() - 1; i >= 0; i--) {
			for (int j : nodeListByDepth.get(i)) {
				earlyCnt[j] = 1;
				notEarlyCnt[j] = 0;
				for (int subIdx : V[j].children) {
					earlyCnt[j] += Math.min(earlyCnt[subIdx], notEarlyCnt[subIdx]);
					notEarlyCnt[j] += earlyCnt[subIdx];
				}
			}
		}
		return Math.min(earlyCnt[0], notEarlyCnt[0]);
	}
}
