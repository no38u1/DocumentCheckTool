package utility;

public class Util {
	public static final String sep = System.lineSeparator();
	public static final String odsSep = "\n";

	public static String toLowerCaseAndRemoveSymbol(String s) {
		return s.replaceAll("_", "").replaceAll("-", "").toLowerCase();
	}
}
