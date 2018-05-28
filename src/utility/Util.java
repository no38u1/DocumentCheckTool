package utility;

public class Util {
	public static final String sep = System.lineSeparator();
	public static final String odsSep = "\n";

	public static String toLowerCaseAndRemoveSymbol(String s) {
		return s
				.toLowerCase()
				.replaceAll("_", "")
				.replaceAll("-", "")
				.replaceAll("/", "")
				.replaceAll("\\(", "")
				.replaceAll("\\)", "")
				.replaceAll(" ", "")
				.replaceAll("　", "")
				.replaceAll("behaviour", "behavior");
	}
}
