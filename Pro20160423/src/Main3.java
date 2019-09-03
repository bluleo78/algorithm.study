import java.util.*;
public class Main3 {
    public static void main(String[] args) {
        new Solver();
    }
}
class Solver {
    Scanner sc = new Scanner(System.in);
    int N, K;
    int[] numbers;
     
    public Solver() {
        int numTests = sc.nextInt();
        double start = System.currentTimeMillis();
        for (int i = 1; i <= numTests; i++) {
            N = sc.nextInt();
            K = sc.nextInt();
            // read in numbers
            readInNumbers();
            // find minimum cost making the cut count <= K
            int max_cost = maxCost();
            int minCost = findMinCost(0, max_cost);
            double end = System.currentTimeMillis();
            double seconds = (end-start)/1000;
            System.out.println("#" + i + " " + minCost + " (" + seconds + ")");
        }
    }
     
    int findMinCost(int lowCost, int highCost) {
        if (lowCost >= highCost) {
            // found the cost
            return lowCost;
        }
        int midCost = (lowCost + highCost) / 2;
         
        int cut_count = minCutCountWith(midCost);
        if (cut_count <= K) return findMinCost(lowCost, midCost);
        else return findMinCost(midCost + 1, highCost);
    }
     
    int maxCost() {
        int min = numbers[0];
        int max = numbers[0];
        for (int i = 1; i < N; i++) {
            min = Math.min(min, numbers[i]);
            max = Math.max(max, numbers[i]);
        }
        return max - min;
    }
     
    void readInNumbers() {
        numbers = new int[N];
        for (int i = 0; i < N; i++) {
            numbers[i] = sc.nextInt();
        }
    }
     
    int minCutCountWith(int cost) {
        int cut_count = 0;
        int chunk_min = numbers[0];
        int chunk_max = numbers[0];
        for (int i = 1; i < N; i++) {
            // if adding numbers[i] does not make the current chunk cost > cost,
            // numbers[i] is appended to current chunk.
            // Otherwise, we cut and start new chunk with numbers[i].
            int temp_chunk_min = Math.min(chunk_min, numbers[i]);
            int temp_chunk_max = Math.max(chunk_max, numbers[i]);
            if (temp_chunk_max - temp_chunk_min > cost) {
                // cut and start new chunk
                cut_count++;
                chunk_min = numbers[i];
                chunk_max = numbers[i];
            } else {
                chunk_min = temp_chunk_min;
                chunk_max = temp_chunk_max;
            }
        }
        return cut_count;
    }
}