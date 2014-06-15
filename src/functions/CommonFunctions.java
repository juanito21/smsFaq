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
	
	/**
	 * Replaces characters that may be confused by an SQL
	 * parser with their equivalent escape characters.
	 * @param s : the SQL query
	 * @return escaped String
	 */
	public static String escapeSQL(String s){
	    int length = s.length();
	    int newLength = length;
	    // first check for characters that might
	    // be dangerous and calculate a length
	    // of the string that has escapes.
	    for (int i=0; i<length; i++){
	    	char c = s.charAt(i);
	    	switch(c){
	        	case '\\':
	        	case '\"':
	        	case '\'':
	        	case '\0':{
	        		newLength += 1;
	        	} break;
	    	}
	    }
	    if (length == newLength){
	    	// nothing to escape in the string
	    	return s;
	    }
	    StringBuffer sb = new StringBuffer(newLength);
	    for (int i=0; i<length; i++){
	    	char c = s.charAt(i);
	    	switch(c){
	        	case '\\':{
	        		sb.append("\\\\");
	        	} break;
	        	case '\"':{
	        		sb.append("\\\"");
	        	} break;
	        	case '\'':{
	        		sb.append("\\\'");
	        	} break;
	        	case '\0':{
	        		sb.append("\\0");
	        	} break;
	        	default: {
	        		sb.append(c);
	        	}
	    	}
	    }
	    return sb.toString();
	}
	
	public static void log(String tag, String msg) {
		System.out.println(tag + " : " + msg);
	}
	
}
