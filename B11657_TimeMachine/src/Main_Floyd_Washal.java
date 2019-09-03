import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main_Floyd_Washal {
	static int N;
	static int M;
	static int[][] ADJ_MATRIX;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		ADJ_MATRIX = new int[N][N];

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
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (i != k && j != k && ADJ_MATRIX[i][k] < Integer.MAX_VALUE && ADJ_MATRIX[k][j] < Integer.MAX_VALUE
							&& ADJ_MATRIX[i][j] > ADJ_MATRIX[i][k] + ADJ_MATRIX[k][j]) {
						ADJ_MATRIX[i][j] = ADJ_MATRIX[i][k] + ADJ_MATRIX[k][j];
					}
				}
			}
		}

		boolean negativeCycle = false;
		for (int i = 0; i < N; i++) {
			if (ADJ_MATRIX[i][i] < 0) {
				negativeCycle = true;
			}
		}

		if (negativeCycle) {
			System.out.println(-1);
		} else {
			for (int i = 1; i < N; i++) {
				if (ADJ_MATRIX[0][i] < Integer.MAX_VALUE) {
					System.out.format("%d\n", ADJ_MATRIX[0][i]);
				} else {
					System.out.println(-1);
				}
			}
		}
	}
}
