package utility;

public enum Result {
	OK("○"),
	NG("×"),
	NA("NA");

	private final String name;

	private Result(String name) {
		this.name = name;
	}

	public String getResultAsSymbol() {
		return this.name;
	}
}
