import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int M;
	static double[][] B;
	static double[][] W;
	static double[][] P;

	public static void main(String[] args) throws IOException {
		int T;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		T = Integer.parseInt(br.readLine());
		List<double[]> result = new ArrayList<double[]>();

		// Process
		for (int t = 0; t < T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());

			result.add(solve());
		}

		for (int t = 0; t < T; t++) {
			System.out.format("%d %1.7f %1.7f %1.7f\n", t + 1, result.get(t)[0], result.get(t)[1], result.get(t)[2]);
		}

		// System.out.format("TIME => %2.2f\n", elapsedTime / 1000.0);
	}

	private static double[] solve() {
		//
		B = new double[N + 1][M + 1];
		W = new double[N + 1][M + 1];
		P = new double[N + 1][M + 1];
		for (int i = 0; i < N + 1; i++) {
			for (int j = 0; j < M + 1; j++) {
				B[i][j] = -1.0;
				W[i][j] = -1.0;
				P[i][j] = -1.0;
			}
		}

		double[] result = new double[3];

		calc(N, M);

		result[0] = W[N][M];
		result[1] = B[N][M];
		result[2] = P[N][M];

		return result;
	}

	private static void calc(int w, int b) {
		if (W[w][b] >= 0.0) {
			return;
		}
		if (w > 0 && b == 0) {
			W[w][b] = 1.0;
			B[w][b] = 0.0;
			P[w][b] = 0.0;
		} else if (w == 0 && b > 0) {
			W[w][b] = 0.0;
			B[w][b] = 1.0;
			P[w][b] = 0.0;
		} else if (w == 1 && b == 1) {
			W[w][b] = 0.0;
			B[w][b] = 0.0;
			P[w][b] = 1.0;
		} else {
			W[w][b] = 0;
			B[w][b] = 0;
			P[w][b] = 0;
			if (w > 0 && w >= 2) {
				calc(w - 1, b);
				W[w][b] += W[w - 1][b] * w / (w + b) * (w - 1) / (w + b - 1);
				B[w][b] += B[w - 1][b] * w / (w + b) * (w - 1) / (w + b - 1);
				P[w][b] += P[w - 1][b] * w / (w + b) * (w - 1) / (w + b - 1);
			}
			if (b > 0 && b >= 2) {
				calc(w, b - 1);
				W[w][b] += W[w][b - 1] * b / (w + b) * (b - 1) / (w + b - 1);
				B[w][b] += B[w][b - 1] * b / (w + b) * (b - 1) / (w + b - 1);
				P[w][b] += P[w][b - 1] * b / (w + b) * (b - 1) / (w + b - 1);
			}
			if (w > 0 && b > 0) {
				calc(w - 1, b - 1);
				W[w][b] += W[w - 1][b - 1]
						* (1.0 * w / (w + b) * b / (w + b - 1) + 1.0 * b / (w + b) * w / (w + b - 1));
				B[w][b] += B[w - 1][b - 1]
						* (1.0 * w / (w + b) * b / (w + b - 1) + 1.0 * b / (w + b) * w / (w + b - 1));
				P[w][b] += P[w - 1][b - 1]
						* (1.0 * w / (w + b) * b / (w + b - 1) + 1.0 * b / (w + b) * w / (w + b - 1));
			}
		}
	}
}
