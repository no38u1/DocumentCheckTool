import java.util.List;

import entity.CheckResult;
import entity.EvidenceListRow;
import utility.Result;
import utility.Util;

public class TestNo05 {
	private static final int TEST_NO = 5;
	public static boolean isNotNumError = false;
	public static StringBuilder sb = new StringBuilder();

	public static CheckResult doTest() {
		if (isNotNumError) {
			sb.append("数字以外の不正な値（空欄や文字列等）のセルがあります").append(Util.sep);
		}
		checkRow();
		checkColum();
		String comment = sb.toString().trim();
		Result checkResult = comment.isEmpty() ? Result.OK : Result.NG;
		return new CheckResult(TEST_NO, checkResult, comment);
	}

	private static void checkColum() {
		List<Integer> sums = Main.evidenceList.getSums();
		Main.evidenceList.getRows().forEach(r -> {
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
		String column = Main.evidenceList.getColumnHeaders().get(column_No + 4).getAllAsString();
		sb.append("「").append(column).append("」列で合計値が間違っています").append(Util.sep);
	}

	private static void checkRow() {
		Main.evidenceList.getRows().stream()
				.filter(r -> r.getNumbers().get(0) != r.getNumbers().get(1) + r.getNumbers().get(2))
				.forEach(r -> addRowErrorComment(r));
	}

	private static void addRowErrorComment(EvidenceListRow row) {
		sb.append("「S.NO.").append(row.getS_No()).append(" ").append(row.getTestContent())
				.append("」行でAllの値が間違っています").append(Util.sep);
	}
}