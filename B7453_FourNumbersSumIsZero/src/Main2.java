import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main2 {
	static int N;
	static int[] A;
	static int[] B;
	static int[] C;
	static int[] D;
	static int[] S1;
	static List<Integer> S2;
	static Map<Integer, Integer> M;
	static long R;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		N = Integer.parseInt(br.readLine());

		// Process
		A = new int[N];
		B = new int[N];
		C = new int[N];
		D = new int[N];
		for (int i = 0; i < N; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			A[i] = a;
			B[i] = b;
			C[i] = c;
			D[i] = d;
		}

		long startTime = System.currentTimeMillis();
		solve();
		System.out.format("%d\n", R);
		System.out.format("%2.2f\n", (System.currentTimeMillis() - startTime) / 1000.0);
	}

	private static void solve() {
		long startTime = System.currentTimeMillis();
		S1 = new int[N * N];
		S2 = new ArrayList<Integer>();
		M = new HashMap<Integer, Integer>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				S1[i * N + j] = A[i] + B[j];
				Integer cached = M.get(C[i] + D[j]);
				if (cached == null) {
					S2.add(C[i] + D[j]);
					M.put(C[i] + D[j], 1);
				} else {
					M.put(C[i] + D[j], cached + 1);
				}
			}
		}

		// sort
		S2.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});
		System.out.format("%2.2f\n", (System.currentTimeMillis() - startTime) / 1000.0);

		R = 0;
		for (int n : S1) {
			int T = 0 - n;
			R += search(T, 0, S2.size() - 1);
		}
	}

	private static long search(int t, int start, int end) {
		if (end < start) {
			return 0;
		}

		int mid = (start + end) / 2;
		if (t < S2.get(mid)) {
			return search(t, start, mid - 1);
		} else if (t > S2.get(mid)) {
			return search(t, mid + 1, end);
		} else {
			return M.get(S2.get(mid));
		}
	}

}
