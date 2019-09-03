import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class MainUsingPriorityQueue {
	static int N;
	static int K;
	static int[] NA;

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
			N = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());

			st = new StringTokenizer(br.readLine());
			NA = new int[N];
			for (int i = 0; i < N; i++) {
				NA[i] = Integer.parseInt(st.nextToken());
			}
			result[t] = calculate();
		}

		for (int t = 0; t < T; t++) {
			System.out.format("#%d %d\n", t + 1, result[t]);
		}

		System.out.format("TIME => %2.2f\n", (System.currentTimeMillis() - startTime) / 1000.0);
	}

	private static int calculate() {
		PriorityQueue<Integer> leftQueue = new PriorityQueue<Integer>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		PriorityQueue<Integer> rightQueue = new PriorityQueue<Integer>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});

		ArrayList<Integer> initList = new ArrayList<Integer>();
		for (int i = 0; i < K; i++) {
			initList.add(NA[i]);
		}
		Collections.sort(initList);

		for (int i = 0; i < K / 2 + 1; i++) {
			leftQueue.add(initList.get(i));
		}
		for (int i = K / 2 + 1; i < K; i++) {
			rightQueue.add(initList.get(i));
		}

		int midValue = leftQueue.peek();
		int maxMidValue = midValue;
		for (int i = K; i < N; i++) {
			if (NA[i - K] <= midValue) {
				leftQueue.remove(NA[i - K]);
			} else {
				rightQueue.remove(NA[i - K]);
			}

			if (leftQueue.size() == 0) {
				leftQueue.add(NA[i]);
			} else if (NA[i] <= midValue) {
				leftQueue.add(NA[i]);
			} else {
				rightQueue.add(NA[i]);
			}

			if (leftQueue.size() - 1 >= rightQueue.size()) {
				for (int j = 0; j < leftQueue.size() - 1 - rightQueue.size(); j++) {
					rightQueue.add(leftQueue.poll());
				}
			} else {
				for (int j = 0; j < rightQueue.size() - 1 - leftQueue.size(); j++) {
					leftQueue.add(rightQueue.poll());
				}
			}
			midValue = leftQueue.peek();
			maxMidValue = Math.max(maxMidValue, midValue);
		}

		return maxMidValue;
	}
}
