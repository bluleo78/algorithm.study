import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

	private static int N;
	private static int M;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		if (M == 0) {
			System.out.println(0);
			return;
		}

		st = new StringTokenizer(br.readLine());
		Boolean[] S = new Boolean[1000];
		for (int i = 0; i < 1000; i++) {
			S[i] = false;
		}
		for (int i = 0; i < M; i++) {
			S[Integer.parseInt(st.nextToken()) - 1] = true;
		}

		int min = Integer.MAX_VALUE;
		for (int x = 1; x <= 1001; x++) {
			if (x <= 1000 && S[x - 1])
				continue;
			for (int y = x; y <= 1001; y++) {
				if (y <= 1000 && S[y - 1])
					continue;
				for (int z = y; z <= 1001; z++) {
					if (z <= 1000 && S[z - 1])
						continue;
					int diff = Math.abs(N - x * y * z);
					if (diff <= min) {
						min = diff;
					}
				}
			}
		}

		System.out.println(min);
	}
}
