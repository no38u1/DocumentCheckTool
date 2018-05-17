import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;

import entity.PropatiesEntity;
import utility.Util;

public class PropatiesFileReader {

	private static SpreadsheetDocument spreadsheetDoc = null;
	private static List<Table> tableList = null;
	private static List<String> sheetNames = new ArrayList<>();
	private static Table sheet = null;

	public static PropatiesEntity readPropaties(String filePass) {
		//odsファイルを読み込む
		try {
			spreadsheetDoc = SpreadsheetDocument.loadDocument(filePass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		tableList = spreadsheetDoc.getTableList();
		tableList.forEach(t -> {
			sheetNames.add(t.getTableName());
		});

		sheet = spreadsheetDoc.getSheetByIndex(0);
		NavigableMap<LocalDate, String> versionInfo = getVersionOfTestCase();
		return new PropatiesEntity(versionInfo);
	}

	private static NavigableMap<LocalDate, String> getVersionOfTestCase() {
		NavigableMap<LocalDate, String> m = new TreeMap<LocalDate, String>();
		for (int i = 1; i < sheet.getRowCount(); i++) {
			m.put(getDate(1, i), getValue(0, i));
		}
		return m;
	}

	public static String getFileInfoAsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("──────").append(Util.sep)
		.append("【propaties.ods】File Information").append(Util.sep)
		.append("Number of sheets: ").append(tableList.size()).append(Util.sep)
		.append("Sheet Name: ").append(Util.sep);
		tableList.forEach(t -> sb.append(" ").append(t.getTableName()).append(Util.sep));
		return sb.toString();
	}

	/**
	 *  Gets the single cell value that is specified at the specified column and row as a string.
	 *  Index starts form 0.
	 *  If you want to get the value of A2 cell, write it as "getValue(0,1)"
	 * @param colIndex - the column index of the cell.
	 * @param rowIndex - the row index of the cell.
	 * @return the string value of this cell.
	 * If the cell type is not string, the display text will be returned.
	 */
	private static String getValue(int colIndex, int rowIndex) {
		return sheet.getCellByPosition(colIndex, rowIndex).getStringValue().trim();
	}

	private static LocalDate getDate(int colIndex, int rowIndex) {
		String DATE_FORMAT = "uuuu/MM/dd";
		return LocalDate.parse(sheet.getCellByPosition(colIndex, rowIndex).getStringValue(),
				DateTimeFormatter.ofPattern(DATE_FORMAT));
	}
}
