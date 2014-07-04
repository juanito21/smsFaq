package functions;

import java.util.List;


public class CommonFunctions {
	
	public static String SEP = ";";
	
	/**
	 * Compute the max between two integers
	 * @param a is the first integer
	 * @param b is the second integer
	 * @return the max's one
	 */
	public static int max(int a, int b) {
		if(a > b) return a;
		else return b;
	}
	
	/**
	 * Compute the min between three integers
	 * @param a is the first integer
	 * @param b is the second integer
	 * @param c is the third integer
	 * @return the min's one
	 */
	public static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}
	
	/**
	 * Generate a string randomly
	 * @param n is the length
	 * @return the random string
	 */
	public static String generateString(int n) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String pass = "";
		for(int i=0;i<n;i++) {
			int j = (int) Math.floor(Math.random() * chars.length());
			pass += chars.charAt(j);
		}
		return pass;
	}
	
	/**
	 * Log function
	 * @param tag : the log tag
	 * @param msg : the log message
	 */
	public static void log(String tag, String msg) {
		System.out.println(tag + " : " + msg);
	}
	
	/**
	 * Use to separate words in a sentence (question)
	 * @param line : the sentence
	 * @param sep : the separator to use
	 * @return For example : "az ber erz, zeaz ?" -> "az:ber:erz:zeaz"
	 */
	public static String toFormatString(String line, String sep) {
		line = line.toLowerCase();
		char in[] = line.toCharArray();
		String res = "";
		boolean prevCharIsSpecial = false;
		for(int i=0;i<in.length;i++) {
			if(Character.isLetter(in[i])) {
				res += in[i];
				if(prevCharIsSpecial) prevCharIsSpecial = false;
			}
			else if(!prevCharIsSpecial) {
				res += sep;
				prevCharIsSpecial = true;
			}
		}
		return res;
	}
	/**
	 * Get a tab of terms from a String
	 * @param s : the String
	 * @return a tab of terms
	 */
	public static String[] toTabTerm(String s) {
		return toFormatString(s, SEP).split(SEP);
	}
	
	/**
	 * Transform digit in a String in a String form
	 * @param s : our string
	 * @return for example : "w8 1 sec" -> "weight one sec"
	 */
	public static String digitToString(String s) {
		s = s.replaceAll("0", "zero");
		s = s.replaceAll("1", "one");
		s = s.replaceAll("2", "two");
		s = s.replaceAll("3", "three");
		s = s.replaceAll("4", "four");
		s = s.replaceAll("5", "five");
		s = s.replaceAll("6", "six");
		s = s.replaceAll("7", "seven");
		s = s.replaceAll("8", "eight");
		s = s.replaceAll("9", "nine");
		return s;
	}
	
	/**
	 * Method allowing to remove vowels from a string
	 * @param s is the string
	 * @return the new string without vowels
	 */
	public static String disemvoweled(String s) {
		char[] chars = s.toCharArray();
		String res = "";
		for(int i=0;i<chars.length;i++) {
			if(!Character.toString(chars[i]).matches("[aeiou]+")) res += chars[i];
		}
		return res;
	}
	
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
		} return L[m][n];
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
	 * Delete stop words from a String
	 * @param src : the SMS disemvoweled
	 * @param words : the list of stop words
	 * @return the SMS without stop words
	 */
	public static String deleteWords(String src, List<String> words) {
		for(String w : words) {
			w = disemvoweled(w);
			src = src.replaceAll("\\b" + w + "\\b", "");
		} return src;
	}
	
	/**
	 * Count the numbers of words matching in both string
	 * @param s : the first string
	 * @param t : the second string
	 * @return the numbers of words matching
	 */
	public static int nbOfWordsMatching(String s, String t) {
		String[] sTab = toTabTerm(s);
		String[] tTab = toTabTerm(t);
		int res = 0;
		for(String str1 : sTab) {
			for(String str2 : tTab) {
				if(str1.equals(str2)) res ++;
			}
		} return res;
	}
	
}
