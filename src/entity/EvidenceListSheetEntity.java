package entity;

import java.util.List;

import utility.Util;

public class EvidenceListSheetEntity {

	public EvidenceListSheetEntity(String sheetName, String storyName, String storedPlace, String version,
			List<ColumnHeader> columnHeaders,
			List<EvidenceListRow> rows, List<Integer> sums) {
		super();
		this.sheetName = sheetName;
		this.storyName = storyName;
		this.storedPlace = storedPlace;
		this.version = version;
		this.columnHeaders = columnHeaders;
		this.rows = rows;
		this.sums = sums;
	}

	private String sheetName;
	private String storyName;
	private String storedPlace;
	private String version;
	private List<ColumnHeader> columnHeaders;
	private List<EvidenceListRow> rows;
	private List<Integer> sums;

	public String getSheetName() {
		return sheetName;
	}

	public String getStoryName() {
		return storyName;
	}

	public String getStoredPlace() {
		return storedPlace;
	}

	public String getVersion() {
		return version;
	}

	public List<ColumnHeader> getColumnHeaders() {
		return columnHeaders;
	}

	public List<EvidenceListRow> getRows() {
		return rows;
	}

	public List<Integer> getSums() {
		return sums;
	}

	public String getAllAsString() {
		String sep = Util.sep;
		StringBuilder sb = new StringBuilder("──────").append(sep);
		sb.append("【Sheet Information】").append(sep)
				.append("sheetName:").append(sep).append(" ").append(sheetName).append(sep)
				.append("storyName:").append(sep).append(" ").append(storyName).append(sep)
				.append("storedPlace:").append(sep).append(" ").append(storedPlace).append(sep)
				.append("version:").append(sep).append(" ").append(version).append(sep)

				.append("columnHeaders:").append(sep);
		columnHeaders.forEach(c -> {
			sb.append(" ").append(c.getAllAsString()).append(sep);
		});

		sb.append("rows:").append(sep);
		rows.forEach(r -> {
			sb.append(" ").append(r.getS_No()).append(", ")
					.append(r.getTestContent()).append(", ")
					.append(r.getTestResult()).append(", ")
					.append(r.getReasonOfNoTest()).append(", ")
					.append(r.getIsExecuted()).append(", ");
			r.getNumbers().forEach(n -> {
				sb.append(n).append(", ");
			});
			sb.append(sep);
		});

		sb.append("sums:").append(sep);
		sums.forEach(s -> {
			sb.append(" ").append(s).append(", ");
		});

		return sb.toString();
	}
}
