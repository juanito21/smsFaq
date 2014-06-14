package functions;

import static functions.CommonFunctions.*;

public class WeightFunction {
	
	/**
	 * Compute the length of the longest common subsequence between two strings
	 * @param s is the first string
	 * @param t is the second string
	 * @return the length of the longest common subsequence
	 */
	public static int lcs(String s, String t) {
		int m = s.length();
		int n = t.length();
		int[][] L = new int[m+1][n+1];
		
		for(int i=0;i<=m;i++) {
			for(int j=0;j<=n;j++) {
				if(i==0 || j==0) L[i][j] = 0;
				else if(s.charAt(i-1) == t.charAt(j-1)) {
					L[i][j] = L[i-1][j-1] + 1;
				} else {
					L[i][j] = max(L[i-1][j], L[i][j-1]);
				}
			}
		}
		
		return L[m][n];
	}
	
	/**
	 * Compute the ratio between the LCS of two strings and the length of the term
	 * @param t is the first string
	 * @param s is the second string
	 * @return the ratio (between 0 and 1)
	 */
	public static double lcsRatio(String t, String s) {
		return (double)lcs(t,s)/t.length();
	}
	
	/**
	 * Compute the edit distance between two strings with the levenshtein distance
	 * @param t is the first string
	 * @param s is the second string
	 * @return the edit distance
	 */
	public static int editDistance(String t, String s) {
		return levenshteinDistance(cs(t), cs(s)) + 1;
	}
	
	/**
	 * Consonant skeleton allows to remove consecutive chars and vowels from a string
	 * @param s is the string
	 * @return the new string without consecutive chars and vowels
	 */
	public static String cs(String s) {
		s = s.replaceAll("([a-zA-Z])(\\1{2,})", "$1"); // Remove consecutive char
		s = s.replaceAll("[aeiou]\\B", ""); // Remove vowels
		return s;
	}
	
	/**
	 * Compute the Levenshtein distance
	 * @param s is the first string
	 * @param t is the second string
	 * @return the Levenshtein distance
	 */
	public static int levenshteinDistance(String s, String t) {
		int m = s.length();
		int n = t.length();
		int[][] distance = new int[m+1][n+1];
		 
		for (int i = 0; i <= m; i++)
			distance[i][0] = i;
		for (int j = 1; j <= n; j++)
			distance[0][j] = j;
 
		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++)
				distance[i][j] = minimum(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1]+ ((s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1));
 
		return distance[s.length()][t.length()];    
	}
	
	/**
	 * Compute the similarity measure between two string, that is how much two string are close
	 * @param t is the first string
	 * @param s is the second string
	 * @return a double meaning how these two strings are close
	 */
	public static double similarityMeasure(String t, String s) {
		if(t.length() != 0 && s.length() != 0) {
			if(t.charAt(0) == s.charAt(0)) {
				return lcsRatio(t,s)/(double)editDistance(t,s);
			} else return 0.0;
		} else return 0.0;
	}
	
	/**
	 * Compute the inverse document frequency for a term
	 * @param t is the term
	 * @return the idf of the term
	 */
	public static double idf(String t) {
		// Here we will have to use an API that will find N and f with t.
		int N = 100;
		int f = 5;
		return Math.log(N/f);
	}
	
	/**
	 * Compute the weight of a term compare to a noisy word from a SMS
	 * @param t is the term
	 * @param s is the noisy word from a SMS
	 * @return the weight
	 */
	public static double weight(String t, String s) {
		return similarityMeasure(t, s) * idf(t);
	}
}
