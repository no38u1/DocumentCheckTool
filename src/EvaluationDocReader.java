import java.util.ArrayList;
import java.util.List;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;

import entity.ColumnHeader;
import entity.EvidenceListRow;
import entity.EvidenceListSheetEntity;
import utility.Util;

public class EvaluationDocReader {

	private static SpreadsheetDocument spreadsheetDoc = null;
	private static List<Table> tableList = null;
	private static List<String> sheetNames = new ArrayList<>();
	private static Table evidenceList = null;

	//TODO これらのフィールドは「チェンナイ評価テスト」ではなく「チェンナイ評価テストのEvidenceLisitのメインシート」の為の変数。クラスとメソッドの構成から見直しが必要。
	private static final int firstRowIndex = 7;
	private static final int firstColumnIndex = 4;
	private static int rowCount;
	private static final int colCount = 15;

	public static EvidenceListSheetEntity readEvidenceList(String filePass) {
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

		//TODO ここで何かしらのWarningを追加（シート名やシート数が違う等）

		//TODO 本来は、List<SheetEntity> EvidenceListの1項目としてEvidenceListSheetEntityがあるべき

		//TODO Indexシートを読み込む。EvidenceList以外でもIndexシートを読み込む必要があるので汎用的なメソッド作成

		//末尾のシートを読み込む
		evidenceList = spreadsheetDoc.getSheetByIndex(tableList.size() - 1);
		rowCount = evidenceList.getRowCount() - 1; //getRowCountは0ではなく1からカウントが始まる。"-1"は、最後にある合計行を除く為

		String sheetName = evidenceList.getTableName();
		String story = getValue("B2");
		String storedPlace = getValue("B3");
		String version = getValue("C4");
		List<ColumnHeader> columnHeaders = getColumnHeaders();
		List<EvidenceListRow> rows = getRows();
		List<Integer> sums = getSums();

		EvidenceListSheetEntity mainSheet = new EvidenceListSheetEntity(sheetName,
				story, storedPlace, version, columnHeaders, rows, sums);

		return mainSheet;
	}

	private static List<Integer> getSums() {
		List<Integer> sumList = new ArrayList<>();

		int sumRow = rowCount;
		for (int j = firstColumnIndex; j < colCount; j++) {
			sumList.add(Integer.parseInt(getValue(j, sumRow)));
		}
		return sumList;
	}

	private static List<ColumnHeader> getColumnHeaders() {
		//TODO 動的に取れるようにする(①まず縦結合チェック→あればcategoryとdetailを同じにする ②次に横結合チェック
		List<ColumnHeader> list = new ArrayList<ColumnHeader>();
		list.add(new ColumnHeader(getValue("A6"), getValue("A6")));
		list.add(new ColumnHeader(getValue("B6"), getValue("B7")));
		list.add(new ColumnHeader(getValue("B6"), getValue("C7")));
		list.add(new ColumnHeader(getValue("B6"), getValue("D7")));
		list.add(new ColumnHeader(getValue("E6"), getValue("E7")));
		list.add(new ColumnHeader(getValue("E6"), getValue("F7")));
		list.add(new ColumnHeader(getValue("E6"), getValue("G7")));
		list.add(new ColumnHeader(getValue("H6").replace(Util.odsSep, ""), getValue("H7")));
		list.add(new ColumnHeader(getValue("H6").replace(Util.odsSep, ""), getValue("I7")));
		list.add(new ColumnHeader(getValue("J6"), getValue("J7")));
		list.add(new ColumnHeader(getValue("J6"), getValue("K7")));
		list.add(new ColumnHeader(getValue("L6"), getValue("L7")));
		list.add(new ColumnHeader(getValue("L6"), getValue("M7")));
		list.add(new ColumnHeader(getValue("N6"), getValue("N7")));
		list.add(new ColumnHeader(getValue("N6"), getValue("O7")));
		return list;
	}

	private static List<EvidenceListRow> getRows() {

		List<EvidenceListRow> rowList = new ArrayList<>();

		for (int i = firstRowIndex; i < rowCount; i++) {
			int s_No = Integer.parseInt(getValue(0, i));
			String testContent = getValue(1, i);
			String testResult = getValue(2, i);
			String reasonOfNoTest = getValue(3, i);

			List<Integer> numbers = new ArrayList<>();

			for (int j = firstColumnIndex; j < colCount; j++) {
				try {
					numbers.add(Integer.parseInt(getValue(j, i)));
				} catch (NumberFormatException e) {
					numbers.add(Integer.MIN_VALUE);	//Error
				}
			}

			rowList.add(new EvidenceListRow(s_No, testContent, testResult, reasonOfNoTest, numbers));
		}
		return rowList;
	}


	public static String getFileInfoAsString() {
		StringBuilder sb = new StringBuilder();
		sb.append("──────").append(Util.sep)
		.append("【Evidence_list.ods】File Information").append(Util.sep)
		.append("Number of sheets: ").append(tableList.size()).append(Util.sep)
		.append("Sheet Name: ").append(Util.sep);
		tableList.forEach(t -> sb.append(" ").append(t.getTableName()).append(Util.sep));
		return sb.toString();
	}

	/**
	 * Gets the single cell value that is positioned at the specified cell address as a string.
	 * @param address - the cell address of the cell.
	 * For example: "$Sheet1.A1", "Sheet1.A1" and "A1" are all valid cell address.
	 * @return String - the string value of this cell.
	 * If the cell type is not string, the display text will be returned.
	 */
	private static String getValue(String address) {
		return evidenceList.getCellByPosition(address).getStringValue().trim();
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
		return evidenceList.getCellByPosition(colIndex, rowIndex).getStringValue().trim();
	}
}