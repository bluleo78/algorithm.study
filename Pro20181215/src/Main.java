import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Main {
	static int TX;
	static int TY;
	static int N;
	static Rectangle[] R;

	static class Rectangle {
		int x0;
		int y0;
		int x1;
		int y1;

		public Rectangle(int x0, int y0, int x1, int y1) {
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
		}

		public int getArea() {
			return (x1 - x0 + 1) * (y1 - y0 + 1);
		}
	}

	public static void main(String[] args) throws IOException {
		int T;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		T = Integer.parseInt(br.readLine());
		int[] result = new int[T];

		// Process
		long startTime = System.currentTimeMillis();
		for (int t = 0; t < T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			TX = Integer.parseInt(st.nextToken());
			TY = Integer.parseInt(st.nextToken());
			N = Integer.parseInt(st.nextToken());

			R = new Rectangle[N];
			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				int y0 = Integer.parseInt(st.nextToken());
				int y1 = Integer.parseInt(st.nextToken());
				int x0 = Integer.parseInt(st.nextToken());
				int x1 = Integer.parseInt(st.nextToken());

				R[i] = new Rectangle(x0, y0, x1, y1);
			}
			result[t] = calculate();
		}

		for (int t = 0; t < T; t++) {
			System.out.format("#%d %d\n", t + 1, result[t]);
		}

		System.out.format("TIME => %2.2f\n", (System.currentTimeMillis() - startTime) / 1000.0);
	}

	private static int calculate() {
		int max = Integer.MIN_VALUE;

		Arrays.sort(R, new Comparator<Rectangle>() {
			@Override
			public int compare(Rectangle o1, Rectangle o2) {
				return o1.x0 - o2.x0;
			}
		});

		Rectangle[] B = new Rectangle[N];
		for (int i = 0; i < N; i++) {
			B[i] = R[i];

			// is continuous area?
			if (i > 0) {
				if (hasSharedEdge(R[i - 1], R[i])) {
					Rectangle M = calcMergedArea(R[i - 1], R[i]);
					if (M.getArea() >= B[i].getArea()) {
						B[i] = M;
					}
				}
				if (hasSharedEdge(B[i - 1], R[i])) {
					Rectangle M = calcMergedArea(B[i - 1], R[i]);
					if (M.getArea() >= B[i].getArea()) {
						B[i] = M;
					}
				}
			}

			max = Math.max(max, B[i].getArea());
		}

		return max;
	}

	private static boolean hasSharedEdge(Rectangle r0, Rectangle r1) {
		if (r0.x1 + 1 != r1.x0) {
			return false;
		}

		return r0.y1 >= r1.y0 && r0.y0 <= r1.y1;
	}

	private static Rectangle calcMergedArea(Rectangle r0, Rectangle r1) {
		return new Rectangle(r0.x0, Math.max(r0.y0, r1.y0), r1.x1, Math.min(r0.y1, r1.y1));
	}
}
