package entity;

public class ColumnHeader {

	public ColumnHeader(String category, String detail) {
		super();
		this.category = category;
		this.detail = detail;
	}

	private String category;
	private String detail;

	public String getCategory() {
		return category;
	}

	public String getDetail() {
		return detail;
	}

	public String getAllAsString() {

		return new StringBuilder().append(category)
				.append(" / ").append(detail).toString();
	}
}
