import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int M;
	static int[][] R;
	static int[][] H;
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
			M = Integer.parseInt(st.nextToken());

			R = new int[N][M];
			for (int i = 0; i < N; i++) {
				String nLine = br.readLine();
				for (int j = 0; j < M; j++) {
					R[i][j] = nLine.charAt(j) == '0' ? 0 : 1;
				}
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

		cache = new int[N][M][2];
		for (int x = 0; x < N; x++) {
			for (int y = 0; y < M; y++) {
				cache[x][y][0] = -1;
				cache[x][y][1] = -1;
			}
		}
		H = new int[N][M];
		for (int x = 0; x < N; x++) {
			for (int y = 0; y < M; y++) {
				if (R[x][y] == 1) {
					// y length
					if (y - 1 >= 0 && R[x][y - 1] == 1) {
						H[x][y] = H[x][y - 1] + 1;
					} else {
						H[x][y] = 1;
					}

					result += countRectangles(x, y, H[x][y]);
				} else {
					H[x][y] = 0;
				}
			}
		}

		return result;
	}

	private static int countRectangles(int x, int y, int h) {
		int result = 0;
		int d = H[x][y] == h ? 0 : 1;
		if (cache[x][y][d] != -1) {
			return cache[x][y][d];
		}
		if (x - 1 >= 0 && R[x - 1][y] == 1) {
			result = countRectangles(x - 1, y, Math.min(h, H[x - 1][y])) + h;
		} else {
			result = h;
		}

		cache[x][y][d] = result;

		return result;
	}

}
