import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import entity.CheckResult;
import utility.Result;
import utility.Util;

public class TestNo04 {
	private static final int TEST_NO = 4;
	private static Result checkResult = Result.NG;
	private static StringBuilder sb = new StringBuilder();

	private static final Map<String, Path> actualFileNames = Main.files.entrySet().stream()
			.filter(f -> !Files.isDirectory(f.getValue()))
			.filter(f -> f.getKey().contains("testcase"))
			.collect(Collectors.toMap(
					f -> f.getKey(),
					f -> f.getValue(),
					(u, v) -> v, TreeMap::new));

	private static Set<String> expectedFineNames = Main.evidenceList.getRows().stream()
			.filter(r -> r.getNumbers().get(0) != 0) 						//テストが実行されている(Allに値がある)セルのみを抽出
			.map(r -> r.getTestResult().trim().split(Util.odsSep))			//各セルに行が複数ある場合はそれぞれを1行として配列へ
			.flatMap(Arrays::stream)										//配列を1つにまとめる
			.map(s -> s.trim().replaceAll("　", "").replaceFirst("-", ""))
			.filter(s -> s.matches(".+\\.[^.]{3,4}$")) 						//拡張子がある行のみを抽出
			.collect(Collectors.toCollection(TreeSet::new));

	public static CheckResult doTest() {
		check();
		String comment = sb.toString().trim();
		return new CheckResult(TEST_NO, checkResult, comment);
	}

	private static void check() {
		actualFileNames.forEach((k,v)-> System.out.println(v.toString().replaceFirst("dataA\\\\278431_Testcase\\\\", "")));
		System.out.println("★★★★");
		expectedFineNames.forEach(s -> System.out.println(s));
		System.out.println("★★★★");






//		if (date == null) {
//			sb.append("Actual Start Dateが未記載の為、正しいVersionかどうかの判断ができません");
////		} else if (false){
//			//TODO 未来日付だった場合のエラー処理
//		} else{
//			try {
//				//
//				double expectedVer = Double.parseDouble(allVerInfo.floorEntry(date).getValue());
//				//
//				if (actualVer < expectedVer) {
//					sb.append("Versionが古すぎます").append(Util.sep)
//					.append("- 使用すべきVersion: ").append(expectedVer).append("以降").append(Util.sep)
//					.append("- Evidence list記載のVersion: ").append(actualVer);
//				} else {
//					sb.append("- 使用すべきVersion: ").append(expectedVer).append("以降").append(Util.sep)
//					.append("- Evidence list記載のVersion: ").append(actualVer);
//					checkResult = Result.OK;
//
//				};
//			} catch (NullPointerException e) {
//				sb.append("Actual Start Dateが古すぎる為、正しいVersionかどうかの判断ができません");
//			}
//		}
	}
}