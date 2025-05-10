package Model;

public class CalculLevenshtein {
    public static int calcularDistanciaLevenshtein(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n+1][m+1];

        // casos base
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        // omplir matriu
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int eliminacio = dp[i-1][j] + 1;
                int insercio  = dp[i][j-1] + 1;

                int costSubstitucio = (s1.charAt(i-1) == s2.charAt(j-1)) ? 0 : 1;
                int substitucio  = dp[i-1][j-1] + costSubstitucio;

                dp[i][j] = Math.min(Math.min(eliminacio, insercio), substitucio);
            }
        }

        return dp[n][m];
    }
}
