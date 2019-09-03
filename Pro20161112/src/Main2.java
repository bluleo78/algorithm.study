import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
  
class Main2 {
    static boolean debug = true;
    public static void pp (String f, Object...objects) {
        if (debug) { System.out.format(f, objects);}
    }
        
    static int MAX_LENGTH=100000;
    static int T, N, K;
    static int A[];
    static int Temp[];
     
    static void tc_gen() throws Exception {
        Formatter f = new Formatter(new File("output.txt"));
        Random r = new Random();
        int TC = 1;
        int N = 100000;
        int K = 21;
        for (int tc=1; tc<=TC; tc++) {
            f.format("%d %d\n", N, K);
            for (int i=1; i<=N; i++) {
                f.format("%d\n", r.nextInt(100)+1);
            }
        }
        f.close();
    }
     
    public static void main (String[] args) throws Exception {
        long s1 = System.nanoTime();
        //System.setIn(new FileInputStream("C:\\hgkang\\04.개인(172.21.111.22)\\SW검정시험\\workspace\\0_Pro_161112_MedianMax\\input.txt"));
        Scanner sc = new Scanner(System.in);
         
        T = sc.nextInt();
        A = new int[MAX_LENGTH+1];
//        tc_gen();
         
        for (int tc=1; tc<=T; tc++) {
            N = sc.nextInt();
            K = sc.nextInt();
            for (int i=1; i<=N; i++) {
                A[i] = sc.nextInt();
            }
             
            if (K==1) {
                int max_median=A[1];
                for (int i=2; i<=N; i++) {
                    if (A[i]>max_median) max_median=A[i];
                }
                System.out.printf("#%d: %d\n", tc, max_median);
                continue;
            }
            int algorithm = 2;
            if (algorithm==1) {
                long s2 = System.nanoTime();
                System.out.printf("#%d: %d\n", tc, naive_version());
                pp("%.2f ms\n",(System.nanoTime()-s2)*0.000001);
            }
            else {
                long s2 = System.nanoTime();
                System.out.printf("#%d: %d\n", tc, IDT_version());
                pp("%.2f ms\n",(System.nanoTime()-s2)*0.000001);
            }
        }
         
        if (debug) {
            pp("\nall: %.0f ms\n", (System.nanoTime()-s1)*0.000001);
            Runtime rt = Runtime.getRuntime();
            pp("memory: %d MB\n\n", (rt.totalMemory() - rt.freeMemory()) / (1024*1024));
        }
    }
     
    static int naive_version() {
        Temp = new int[K];
        int max_median = Integer.MIN_VALUE;
        for (int i=1; i<=N-K; i++) {
            for (int j=0; j<=K-1; j++) {
                Temp[j]=A[i+j];
            }
            Arrays.sort(Temp);
            int mid = Temp[(K+1)/2-1];
            if (max_median<mid) max_median = mid;
        }
        return max_median;
    }
     
    static int IDT_version() {
        Temp = new int[K];
        for (int i=1; i<=K; i++) {
            Temp[i-1]=A[i];
        }
        Arrays.sort(Temp);
        int center = (K+1)/2-1;
         
        int mid = Temp[center];
        int max_median = mid;
        Tree left = new Tree(false, Temp, 0, center-1); 
        Tree right = new Tree(true, Temp, center+1,K-1);
         
        for (int k=K+1; k<=N; k++) { 
            int a=A[k-K], b=A[k];
            if (a<mid && b<mid) { left.replace(a,b); }
            else if (a<mid && b==mid) { left.replace(a, mid); }
            else if (a<mid && b>mid) {
                left.replace(a, mid);
                int r = right.top();
                if (b<=r) { mid = b; }
                else { right.replace(r, b); mid = r; }
            }
            else if (a==mid && b<mid) {
                int l = left.top();
                if (b>=l) { mid = b; }
                else { mid = l; left.replace(l, b); }
            }
            else if (a==mid && b==mid) {}  // skip 
            else if (a==mid && b>mid) {
                int r = right.top();
                if (b<=r) { mid=b; }
                else { right.replace(r, b); mid = r; }
            }
            else if (a>mid && b<mid) {
                right.replace(a, mid);
                int l_max = left.top();
                if (l_max<=b) {  mid = b; }
                else { mid = l_max; left.replace(mid, b); }
            }
            else if (a>mid &&  b==mid) { right.replace(a,  mid); }
            else { right.replace(a,b); } // a>mid && b>mid
             
            if (mid > max_median) { max_median = mid; }
        }
         
        return max_median;
    }
     
}   
class IndexList extends ArrayList<Integer> {}
class Tree {
    HashMap<Integer,IndexList> map = new HashMap<Integer,IndexList>();
    int[] data;
    boolean is_min;
    Tree(boolean b, int[] arr, int i, int j) {
        is_min=b;
        int leaf_size = (int) Math.pow(2, Math.ceil(Math.log(j-i+1) / Math.log(2)));
        data = new int[leaf_size*2];  // internal node가 leafsize-1 만큼 있다 (0은 버린다)
        if (is_min) {
            Arrays.fill(data, Integer.MAX_VALUE);
        }
        else {
            Arrays.fill(data, Integer.MIN_VALUE);
        }
        for (int idx=leaf_size; i<=j; idx++, i++) {
            map_add(arr[i],idx);
            data[idx]=arr[i];  
            recon(idx);
        }
    }
    void map_add(int value, int index) {
        IndexList list = map.get(value);
        if (list == null) {
            list = new IndexList();
            map.put(value, list);
        }
        list.add(index);
    }
     
    void replace(int a, int b) {
        IndexList list = map.get(a); // 항상 null이 아니도록 코딩되어 있다
        int index = list.get(0);
        if (list.size()==1) {
            map.remove(a);
        }
        else {
            list.remove(0);
        }
        map_add(b,index);
        data[index]=b;
        recon(index);
    }
     
    void recon(int idx) {
        idx = idx/2;
        if (idx==1) {
            data[idx] = select(data[idx*2],data[idx*2+1]);
        }
        else {
            data[idx]=select(data[idx*2],data[idx*2+1]);
            recon(idx);
        }
    }
     
    int top() {
        return data[1]; 
    }
     
    int select(int a, int b) {
        if (is_min) {
            return a<b?a:b;
        }
        else {
            return a>b?a:b;
        }
    }
}