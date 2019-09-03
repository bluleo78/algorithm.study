import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int M;
	static int[][] ADJ_MATRIX;
	static int[] DIST;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		ADJ_MATRIX = new int[N][N];
		DIST = new int[N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i == j) {
					ADJ_MATRIX[i][j] = 0;
				} else {
					ADJ_MATRIX[i][j] = Integer.MAX_VALUE;
				}
			}
		}

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int nodeIdx0 = Integer.parseInt(st.nextToken()) - 1;
			int nodeIdx1 = Integer.parseInt(st.nextToken()) - 1;
			int nodeVal = Integer.parseInt(st.nextToken());
			if (nodeVal < ADJ_MATRIX[nodeIdx0][nodeIdx1]) {
				ADJ_MATRIX[nodeIdx0][nodeIdx1] = nodeVal;
			}
		}

		// Solve
		DIST[0] = 0;
		for (int i = 1; i < N; i++) {
			DIST[i] = ADJ_MATRIX[0][i];
		}

		boolean cycle = false;
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (DIST[j] < Integer.MAX_VALUE && ADJ_MATRIX[j][i] < Integer.MAX_VALUE
							&& DIST[j] + ADJ_MATRIX[j][i] < DIST[i]) {
						DIST[i] = DIST[j] + ADJ_MATRIX[j][i];
						if (k == N - 1) {
							cycle = true;
						}
					}
				}
			}
		}

		if (cycle) {
			System.out.println(-1);
		} else {
			for (int i = 1; i < N; i++) {
				if (DIST[i] < Integer.MAX_VALUE) {
					System.out.format("%d\n", DIST[i]);
				} else {
					System.out.println(-1);
				}
			}
		}
	}
}
