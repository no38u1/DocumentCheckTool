import java.time.LocalDate;
import java.util.NavigableMap;

import entity.CheckResult;
import utility.Result;
import utility.Util;

public class TestNo02 {
	private static final int TEST_NO = 2;
	private static StringBuilder sb = new StringBuilder();

	private static final double actualVer = Double.parseDouble(Main.evidenceList.getVersion());
	private static final NavigableMap<LocalDate, String> allVerInfo = Main.propaties.getVersionInfo();
	private static final LocalDate date = Main.startDate;

	public static CheckResult doTest() {
		check();
		Result checkResult = Result.NG;
		String comment = sb.toString().trim();
		if (comment.isEmpty()) {
			checkResult = Result.OK;
		}
		return new CheckResult(TEST_NO, checkResult, comment);
	}

	private static void check() {
		if (date == null) {
			sb.append("Actual Start Dateが未記載の為、正しいVersionかどうかの判断ができません");
//		} else if (false){
			//TODO 未来日付だった場合のエラー処理
		} else{
			try {
				double expectedVer = Double.parseDouble(allVerInfo.floorEntry(date).getValue());
				if (actualVer < expectedVer) {
					sb.append("Evidence list記載のVersion: ").append(actualVer).append(Util.sep)
					.append("使用すべきVersion: ").append(expectedVer).append("以降");
				};
			} catch (NullPointerException e) {
				sb.append("Actual Start Dateが古すぎる為、正しいVersionかどうかの判断ができません");
			}
		}
	}
}