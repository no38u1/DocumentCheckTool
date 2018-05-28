import java.util.Arrays;

import entity.CheckResult;
import entity.EvidenceListRow;
import entity.EvidenceListSheetEntity;
import utility.Result;
import utility.Util;

public class TestNo06 {
	private static final int TEST_NO = 6;
	private static StringBuilder sb = new StringBuilder();

	private static final EvidenceListSheetEntity evidenceList = Main.evidenceList;

	public static CheckResult doTest() {
		checkExistenceOfComment();
		String comment = sb.toString().trim();
		Result checkResult = comment.isEmpty() ? Result.OK : Result.NG;
		return new CheckResult(TEST_NO, checkResult, comment);
	}

	private static void checkExistenceOfComment() {
		evidenceList.getRows().stream()
				.filter(r -> !r.getNumbers().get(2).equals(0)) //2は「NA」列
				.filter(r -> Arrays.asList("-", "_", "　", " ", "").contains(r.getReasonOfNoTest().trim()))
				.forEach(r -> addErrorComment(r));
	}

	private static void addErrorComment(EvidenceListRow row) {
		sb.append("「S-NO.").append(row.getS_No()).append(" ").append(row.getTestContent())
				.append("」行で、The reason of No test resultに理由が記載されていません").append(Util.sep);
	}
}