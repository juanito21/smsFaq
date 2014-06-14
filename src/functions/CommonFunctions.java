package functions;

public class CommonFunctions {
	
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
}
