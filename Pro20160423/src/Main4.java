import java.io.FileInputStream;
import java.util.Scanner;
 
// [원칙] 종이띠의 최대값과 최소값을 무조건 분리한다.
// 1. 종이띠의 최대값 과 최소값을 찾아 두 값 中 index가 빠른 것을 종이띠를 분리하는 지점으로 선택한다
// 2. 분리된 종이띠들 中 최대비용을 갖는 종이띠를 찾는다. 이 종이띠에 대해서만 1번을 수행한다.
// 3. 이 부분을 제공된 자르기 횟수(K)만큼 수행한다.
 
 
 
public class Main4 {
    static int T, N, K; // T: Testcase, N: 대상 갯수(0 <= K < N <= 1,000),
                        // K : "최대" 가위질 수   (※ 종이띠의 적힌 자연수는 1,000,000 이하)  
    static int iData[];
     
    static int Answer; 
    static int max, min;
 
    static int maxCostV[][];    // 분리되는 종이띠들의 최대비용(max-min) 값 저장
    static int posIndex_S[][];  // 분리되는 종이띠들의 최대값 index 또는 최소값 index 중 작은 index 저장 
    static int posIndex_F[][];  // 분리되는 종이띠들의 최대값 index 또는 최소값 index 중 작은 index 저장
    static int maxCostindex[];          // 분리된 종이띠 중 최대비용이 있는 종이띠 index를 값으로 저장 
    static int maxCostValue[];          // 자르는 횟수별 분리된 종이띠 중 최대비용 값만 저장
     
    static int cnt;
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
         
        T = sc.nextInt();      
         
        int tmp_maxindex, tmp_minindex;
        int t_maxV=0, t_maxI=0;
         
        for (int TC=1; TC <= T; TC++){
            N = sc.nextInt();
            K = sc.nextInt();
             
            maxCostV = new int [N][N]; 
            posIndex_S = new int[N][N];        
            posIndex_F = new int[N][N];    
            maxCostindex = new int [N];      
            maxCostValue = new int [N];
            iData = new int [N];   
            Answer = Integer.MAX_VALUE;
             
            for(int i=0; i<N; i++){
                iData[i]=sc.nextInt();             
            }  
             
            cnt=0; 
                     
            while(cnt<=K){
                //System.out.println();
                 
                tmp_maxindex=0; tmp_minindex=0;
                max=0; min=1000000;
                t_maxV=0; t_maxI=0;
                 
                if (cnt==0){
                    for (int i=0; i<N; i++){
                        if (max < iData[i]) max = iData[i];
                        if (min > iData[i]) min = iData[i];
                    }
                    posIndex_S[cnt][0]=0;           // 다음 자르기에서 분할될 종이띠의 맨 마지막 index 저장(최대값 과 최소값 중 작은 index를 저장)
                    posIndex_F[cnt][0]=N-1;
 
                    maxCostV[cnt][0] = max-min;     // max-min이 최소 비용              
                    maxCostindex[cnt]=0;            // 최대비용이 있는 종이띠 index
                    maxCostValue[cnt]=max-min;      // 자르는 횟수(K)별 분리된 종이띠 중 최대비용 값만 저장
                     
                    //System.out.println(">> #"+cnt+"(0)"+" => maxCostV: "+maxCostV[cnt][0]);
                    //System.out.println("========>>> #"+cnt+" => maxCostValue:"+maxCostValue[cnt]+" ("+maxCostindex[cnt]+")");
                }else{ 
                    for(int i=0; i<cnt; i++){                        // 앞서 자른 종이띠의 정보를 그대로 다음 자르기 정보로 넘겨준다.
                        posIndex_S[cnt][i] = posIndex_S[cnt-1][i];
                        posIndex_F[cnt][i] = posIndex_F[cnt-1][i];
                    }
                                                                    // 앞서 자른 종이띠 중 가장 최대비용이 높은 종이띠에서 최대값과 최소값, 그리고 index를 구한다.
                    for(int j=posIndex_S[cnt][maxCostindex[cnt-1]]; j<=posIndex_F[cnt][maxCostindex[cnt-1]];j++){
                        if (max < iData[j]){
                            max = iData[j]; tmp_maxindex=j;
                        }
                        if (min > iData[j]){
                            min = iData[j]; tmp_minindex=j;
                        }
                    }
                         
                    if (tmp_maxindex > tmp_minindex){        // 최대값과 최소값의 index 중 낮은 값을 취하여 기존 종이띠는 끝의 index를, 새로운 종이띠는 시작 index를 갱신한다
                        posIndex_F[cnt][cnt]=posIndex_F[cnt][maxCostindex[cnt-1]];
                        posIndex_F[cnt][maxCostindex[cnt-1]]=tmp_minindex;
                        posIndex_S[cnt][cnt]=tmp_minindex+1;
                    }
                    else {
                        posIndex_F[cnt][cnt]=posIndex_F[cnt][maxCostindex[cnt-1]];
                        posIndex_F[cnt][maxCostindex[cnt-1]]=tmp_maxindex;
                        posIndex_S[cnt][cnt]=tmp_maxindex+1;
                    }
                     
                    for (int i=0; i<=cnt; i++){
                        max=0; min=1000000;
 
                        for (int j=posIndex_S[cnt][i]; j<=posIndex_F[cnt][i]; j++){
                            if (max < iData[j]) max = iData[j];
                            if (min > iData[j]) min = iData[j];
                        }
                         
                        if (t_maxV < (max-min)){
                            t_maxV = max-min; t_maxI=i;
                        }              
                        maxCostV[cnt][i] = max-min;
                        //System.out.println(">> #"+cnt+"("+i+")"+" => maxCostV: "+maxCostV[cnt][i]);
 
                    }
 
                    maxCostValue[cnt] = t_maxV;    
                    maxCostindex[cnt] = t_maxI;
                    //System.out.println("========>>> #"+cnt+" => maxCostValue:"+maxCostValue[cnt]+" ("+maxCostindex[cnt]+")");
                }
                cnt++;
            }
             
            for(int i=0; i<=K; i++){
                if (maxCostValue[i] < Answer) Answer = maxCostValue[i];
            }
             
            //System.out.println();
            System.out.println("#"+TC+" "+ Answer);        
        }
    }
}