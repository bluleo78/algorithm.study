import java.io.FileInputStream;
 
import java.util.Scanner;
 
//종이띠 자르기_DP
class Main2{
  
    static int iPaperCnt = 0;
    static int iCutCnt = 0;
    static int[] aPaper = new int[1001];
    static int iAnswer = 0;
    static int[][] aCost = new int[1001][1001];
    public static void main(String args[]) throws Exception{
   
        //System.setIn(System.in);
   
        Scanner sc = new Scanner(System.in);
   
        int T = sc.nextInt();
        double Start = System.currentTimeMillis();
 
        for(int i=0; i<T;i++)
        {
            iPaperCnt = sc.nextInt();
            iCutCnt = sc.nextInt();
            iAnswer = Integer.MAX_VALUE;
            for(int j=1; j<1001; j++)
            {
                if(j<= iPaperCnt)
                    aPaper[j] = sc.nextInt();
                else
                    aPaper[j] = -1;
     
                for(int k=0; k<1001; k++)
                    aCost[j][k] = -1;
            }
            iAnswer = GetMinCost(iPaperCnt, iCutCnt);
            double End = System.currentTimeMillis();
            double seconds = (End-Start)/1000;
            System.out.println("#" + (i+1) + " " + iAnswer + " (" + seconds + ")" );
        }
    }
  
  
    public static int GetMinCost(int iIndex, int iLeftCutCnt)
    {
        if(aCost[iIndex][iLeftCutCnt] != -1)
        {
            return aCost[iIndex][iLeftCutCnt];
        }
        else if(iIndex <= iLeftCutCnt-1)
        {
            aCost[iIndex][iLeftCutCnt] = 0;
        }
        else if(iIndex == 1)
        {
            aCost[iIndex][iLeftCutCnt] = 0;
        }
        else
        {
            if(iLeftCutCnt > 0)
            {
                int iMaxPartCost = Integer.MAX_VALUE;
     
                for(int i=iIndex; i>0; i--)
                {
                    int iLeftCost = 0;
                    int iRightCost = 0;
                    iLeftCost = GetMinCost((i - 1), iLeftCutCnt -1);
                    iRightCost = GetPartCost(i, iIndex); 
 
                    int iCurrentCost= iLeftCost;
                    if(iLeftCost < iRightCost)
                    {
                        iCurrentCost = iRightCost;
                    }
                    if(iMaxPartCost > iCurrentCost)
                        iMaxPartCost = iCurrentCost;
                    else if(iMaxPartCost < iCurrentCost)
                        break;
                }
                aCost[iIndex][iLeftCutCnt] = iMaxPartCost;
 
            }
            else
            {
                aCost[iIndex][iLeftCutCnt] = GetPartCost(1, iIndex);
 
            }
        }
        return aCost[iIndex][iLeftCutCnt];
    }
  
    public static int GetPartCost(int iStart, int iEnd)
    {
        int iMin = aPaper[iStart];
        int iMax = aPaper[iStart];
        for(int i= iStart; i<= iEnd; i++)
        {
            if(iMin > aPaper[i])
            {
                iMin = aPaper[i];
            }
            if(iMax < aPaper[i])
            {
                iMax = aPaper[i];
            }
        }
        return iMax - iMin;
    }
 
}