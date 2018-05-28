import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import entity.CheckResult;
import entity.EvidenceListSheetEntity;
import utility.Result;

public class TestNo11 {
	private static final int TEST_NO = 11;
	private static StringBuilder sb = new StringBuilder();

	private static final EvidenceListSheetEntity evidenceList = Main.evidenceList;

	public static CheckResult doTest() {
		checkBugSeverityType();
		String comment = sb.toString().trim();
		Result checkResult = comment.isEmpty() ? Result.OK : Result.NG;
		return new CheckResult(TEST_NO, checkResult, comment);
	}

	private static void checkBugSeverityType() {
		Set<String> expected = new LinkedHashSet<>();
		//TODO 半角スペースや大文字小文字等の微妙なブレは吸収
		Collections.addAll(expected, "Critical(Blocker + Critical)", "Major", "Normal", "Minor");
		Set<String> actual = new LinkedHashSet<>();
		//TODO ここの7を消す
		for (int i = 7; i < evidenceList.getColumnHeaders().size(); i++) {
			actual.add(evidenceList.getColumnHeaders().get(i).getCategory());
		}

		if(!actual.equals(expected)) {
			addErrorComment();
		}
	}

	private static void addErrorComment() {
		//TODO できれば、どこのセルが間違っているのかのMessage追加
		sb.append("不具合の重要度表記が間違っています");
	}
}