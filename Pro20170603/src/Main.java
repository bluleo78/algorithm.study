import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class Node {
	int value = 0;
	int parentNode = -1;
	List<Integer> subNodes = new ArrayList<Integer>();
}

public class Main {
	static int N;
	static int K;
	static Node[] nodes;
	static int[][][] cache;

	public static void main(String[] args) throws IOException {
		int T;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		T = Integer.parseInt(br.readLine());
		long[] result = new long[T];

		// Process
		long elapsedTime = 0;
		for (int t = 0; t < T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());

			nodes = new Node[N];
			for (int i = 0; i < N; i++) {
				nodes[i] = new Node();
			}
			for (int i = 0; i < N - 1; i++) {
				st = new StringTokenizer(br.readLine());
				int source = Integer.parseInt(st.nextToken());
				int target = Integer.parseInt(st.nextToken());
				if (nodes[source - 1].subNodes.size() > 1) {
					nodes[source - 1].subNodes.add(target - 1);
					nodes[target - 1].parentNode = source - 1;
				} else if (nodes[target - 1].subNodes.size() > 1) {
					nodes[target - 1].subNodes.add(source - 1);
					nodes[source - 1].parentNode = target - 1;
				} else {
					nodes[source - 1].subNodes.add(target - 1);
					nodes[target - 1].parentNode = source - 1;
				}
			}
			String nLine = br.readLine();
			for (int i = 0; i < N; i++) {
				nodes[i].value = nLine.charAt(i) == '0' ? 0 : 1;
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
		long result = 0;
		int[][] R = new int[N][K + 1];

		for (int k = 0; k <= K; k++) {
			for (int i = 0; i < N; i++) {
				if (k == 0) {
					R[i][k] = nodes[i].value;
					continue;
				}
				// self
				int r = R[i][0];

				// sub nodes
				for (int subNode : nodes[i].subNodes) {
					r += R[subNode][k - 1];
				}
				R[i][k] = r;
			}
		}

		int[] s = new int[N];
		for (int i = 0; i < N; i++) {
			int r = R[i][K];

			// parent exits
			if (K > 0) {
				int k = K;
				int parentNode = nodes[i].parentNode;
				int currNode = i;
				while (parentNode != -1 && k > 0) {
					r += R[parentNode][0];
					if (k > 1) {
						for (int pSubNode : nodes[parentNode].subNodes) {
							if (pSubNode != currNode) {
								r += R[pSubNode][k - 2];
							}
						}
					}
					currNode = parentNode;
					parentNode = nodes[currNode].parentNode;
					k--;
				}
			}
			s[i] = r;
			result += r;
		}

		return result;
	}
}
