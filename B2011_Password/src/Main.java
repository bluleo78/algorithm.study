import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	static String PASSWORD;
	static int L;
	static long[] cached;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// Parse
		PASSWORD = br.readLine();
		L = PASSWORD.length();

		//
		System.out.format("%d\n", solve());
	}

	private static long solve() {
		cached = new long[L];
		for (int i = 0; i < L; i++) {
			cached[i] = -1;
		}
		return count(0);
	}

	private static long count(int i) {
		long result = 0;

		if (cached[i] >= 0) {
			return cached[i];
		}

		char c0 = PASSWORD.charAt(i);
		if (c0 >= '1' && c0 <= '9') {
			if (i + 1 < L) {
				result += count(i + 1);
			} else {
				result += 1;
			}
		}

		if (i + 1 < L) {
			char c1 = PASSWORD.charAt(i + 1);
			if ((c0 == '1' && c1 >= '0' && c1 <= '9') || (c0 == '2' && c1 >= '0' && c1 <= '6')) {
				if (i + 2 < L) {
					result += count(i + 2);
				} else {
					result += 1;
				}
			}
		}

		result = result % 1000000;
		cached[i] = result;

		return result;
	}
}
