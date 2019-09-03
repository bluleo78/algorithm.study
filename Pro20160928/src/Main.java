import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[] K;
	static int[] V;
	static int[] L;
	static int[] R;
	static int[] L1;
	static int[] R1;
	static int[] T;

	public static void main(String[] args) throws IOException {
		int TC;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		TC = Integer.parseInt(br.readLine());
		T = new int[TC];

		// Process
		long startTime = System.currentTimeMillis();
		for (int tc = 0; tc < TC; tc++) {
			N = Integer.parseInt(br.readLine());
			K = new int[N];
			V = new int[N];
			L = new int[N];
			L1 = new int[N];
			R = new int[N];
			R1 = new int[N];
			StringTokenizer st = new StringTokenizer(br.readLine());
			for (int i = 0; i < N; i++) {
				K[i] = Integer.parseInt(st.nextToken());
			}
			T[tc] = calculate();
		}

		for (int tc = 0; tc < TC; tc++) {
			System.out.printf("#%d %d\n", tc + 1, T[tc]);
		}

		System.out.printf("TIME => %2.2f", (System.currentTimeMillis() - startTime) / 1000.0);
	}

	private static int calculate() {
		// L
		L[0] = Math.max(0, K[0]);
		for (int i = 1; i < N; i++) {
			L[i] = Math.max(0, K[i] + L[i - 1]);
		}

		// L1
		L1[0] = Math.max(0, K[0]);
		for (int i = 1; i < N; i++) {
			L1[i] = Math.max(0, K[i] + L1[i - 1]);
			L1[i] = Math.max(L1[i], L[i - 1]);
		}

		// R
		R[N - 1] = Math.max(0, K[N - 1]);
		for (int i = N - 2; i >= 0; i--) {
			R[i] = Math.max(0, K[i] + R[i + 1]);
		}

		// R1
		R1[N - 1] = Math.max(0, K[N - 1]);
		for (int i = N - 2; i >= 0; i--) {
			R1[i] = Math.max(0, K[i] + R1[i + 1]);
			R1[i] = Math.max(R1[i], R[i + 1]);
		}

		// V
		for (int i = 0; i < N; i++) {
			int l = i > 0 ? L[i - 1] : 0;
			int l1 = i > 0 ? L1[i - 1] : 0;
			int r = i < N - 1 ? R[i + 1] : 0;
			int r1 = i < N - 1 ? R1[i + 1] : 0;
			int max = l + K[i] + r;
			max = Math.max(max, l + K[i] + r1);
			V[i] = Math.max(max, l1 + K[i] + r);
		}

		int sum = 0;
		for (int i = 0; i < N; i++) {
			sum = (sum + Math.abs(V[i])) % 1000000007;
		}

		return sum ;
	}
}
