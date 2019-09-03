import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main2 {
	static class Node {
		List<Integer> links = new ArrayList<Integer>();
		List<Integer> children = new ArrayList<Integer>();
	}

	static int N;
	static Node[] V;
	static boolean[] visited;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File("/home/bluleo78/workspace/algorithm/B2533_SNS/src/sample.txt"))));
		// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		N = Integer.parseInt(br.readLine());

		V = new Node[N];
		visited = new boolean[N];
		for (int i = 0; i < N; i++) {
			V[i] = new Node();
			visited[i] = false;
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
		makeTree(0);

		return Math.min(countEarlyAdaptor1(0), countEarlyAdaptor2(0));
	}

	private static void makeTree(int nIdx) {
		visited[nIdx] = true;
		for (int subIdx : V[nIdx].links) {
			if (!visited[subIdx]) {
				V[nIdx].children.add(subIdx);
				makeTree(subIdx);
			}
		}
	}

	private static int countEarlyAdaptor1(int nIdx) {
		int sum = 0;
		for (int subIdx : V[nIdx].children) {
			sum += Math.min(countEarlyAdaptor1(subIdx), countEarlyAdaptor2(subIdx));
		}
		return sum + 1;
	}

	private static int countEarlyAdaptor2(int nIdx) {
		int sum = 0;
		for (int subIdx : V[nIdx].children) {
			sum += countEarlyAdaptor1(subIdx);
		}
		return sum;
	}
}
