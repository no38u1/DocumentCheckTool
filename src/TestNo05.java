import java.util.List;

import entity.CheckResult;
import entity.EvidenceListRow;
import entity.EvidenceListSheetEntity;
import utility.Result;
import utility.Util;

public class TestNo05 {
	private static final int TEST_NO = 5;
	private static StringBuilder sb = new StringBuilder();

	private static final EvidenceListSheetEntity evidenceList = Main.evidenceList;

	public static CheckResult doTest() {
		checkRow();
		checkColum();
		Result checkResult = Result.NG;
		String comment = sb.toString().trim();
		if (comment.isEmpty()) {
			checkResult = Result.OK;
		}
		return new CheckResult(TEST_NO, checkResult, comment);
	}

	private static void checkColum() {
		List<Integer> sums = evidenceList.getSums();
		evidenceList.getRows().forEach(r -> {
			for (int i = 0; i < sums.size(); i++) {
				sums.set(i, sums.get(i) - r.getNumbers().get(i));
			}
		});

		for (int i = 0; i < sums.size(); i++) {
			if (sums.get(i) != 0) {
				addColumnErrorComment(i);
			}
		}
	}

	private static void addColumnErrorComment(int column_No) {
		//ここの「+4」を消す
		String column = evidenceList.getColumnHeaders().get(column_No + 4).getAllAsString();
		sb.append("「").append(column).append("」列で合計値が間違っています").append(Util.sep);
	}

	private static void checkRow() {
		evidenceList.getRows().stream()
				.filter(r -> r.getNumbers().get(0) != r.getNumbers().get(1) + r.getNumbers().get(2))
				.forEach(r -> addRowErrorComment(r));
	}

	private static void addRowErrorComment(EvidenceListRow row) {
		sb.append("「S.NO.").append(row.getS_No()).append(" ").append(row.getTestContent())
				.append("」行でAllの値が間違っています").append(Util.sep);
	}
}