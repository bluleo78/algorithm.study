import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int MAX_Y = 1000000;
	static Tank[] tanks;
	static int h;
	static long[] nodes;

	static class Tank {
		int x;
		int y;
		long score;

		public Tank(int x2, int y2, long s) {
			x = x2;
			y = y2;
			score = s;
		}

	}

	public static void main(String[] args) throws IOException {
		int T;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		T = Integer.parseInt(br.readLine());
		long[] result = new long[T];

		// Process
		long elapsedTime = 0;
		for (int t = 0; t < T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());

			tanks = new Tank[N];
			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				int s = Integer.parseInt(st.nextToken());
				tanks[i] = new Tank(x, y, s);
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
		// sort
		Arrays.sort(tanks, new Comparator<Tank>() {

			@Override
			public int compare(Tank o1, Tank o2) {
				// TODO Auto-generated method stub
				return o1.x - o2.x;
			}
		});

		// init segment tree
		initTree();

		//
		long result = 0;
		for (int i = N - 1; i >= 0; i--) {
			result += searchTree(0, tanks[i].y, MAX_Y, 0, h - 1);

			updateTree(tanks[i].y, tanks[i].score);
		}

		return result;
	}

	private static void initTree() {
		h = 1;
		while (h < MAX_Y)
			h <<= 1;

		nodes = new long[2 * h];
	}

	private static long searchTree(int n, int y0, int y1, int ry0, int ry1) {
		if (y0 <= ry0 && y1 >= ry1) {
			return nodes[n];
		}

		if (y0 > ry1 || y1 < ry0) {
			return 0;
		}

		int mid = (ry0 + ry1) / 2;

		return searchTree(n * 2 + 1, y0, y1, ry0, mid) + searchTree(n * 2 + 2, y0, y1, mid + 1, ry1);
	}

	private static void updateTree(int y, long score) {
		int n = h - 1 + y;
		nodes[n] = score;

		while (n != 0) {
			n = (n - 1) / 2;
			nodes[n] = nodes[n * 2 + 1] + nodes[n * 2 + 2];
		}
	}
}
