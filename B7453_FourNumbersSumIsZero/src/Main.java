import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[] A;
	static int[] B;
	static int[] C;
	static int[] D;
	static int[] S1;
	static int[] S2;
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

		System.currentTimeMillis();
		solve();
		System.out.format("%d\n", R);
		// System.out.format("%2.2f\n", (System.currentTimeMillis() - startTime) /
		// 1000.0);
	}

	private static void solve() {
		System.currentTimeMillis();
		S1 = new int[N * N];
		S2 = new int[N * N];
		M = new HashMap<Integer, Integer>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				S1[i * N + j] = A[i] + B[j];
				S2[i * N + j] = C[i] + D[j];
			}
		}

		// sort
		Arrays.sort(S1);
		Arrays.sort(S2);
		// System.out.format("%2.2f\n", (System.currentTimeMillis() - startTime) /
		// 1000.0);

		R = 0;
		int idx1 = 0;
		int idx2 = N * N - 1;
		while (idx1 < N * N) {
			if (S1[idx1] + S2[idx2] > 0) {
				if (idx2 > 0) {
					idx2--;
				} else {
					break;
				}
			} else if (S1[idx1] + S2[idx2] < 0) {
				idx1++;
			} else {
				// S1[idx1] + S2[idx2] == 0
				int V2 = S2[idx2];
				int valCnt2 = 0;
				while (idx2 >= 0 && S2[idx2] == V2) {
					valCnt2++;
					idx2--;
				}
				int V1 = S1[idx1];
				int valCnt1 = 0;
				while (idx1 < N * N && S1[idx1] == V1) {
					valCnt1++;
					idx1++;
				}

				R += ((long)valCnt1 * (long)valCnt2);

				if (idx2 < 0) {
					break;
				}
			}
		}
	}
}
