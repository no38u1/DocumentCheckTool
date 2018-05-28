import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import entity.CheckResult;
import utility.Result;
import utility.Util;

public class TestNo04 {
	private static final int TEST_NO = 4;
	private static StringBuilder sb = new StringBuilder();

	private static final Path testCaseFolder = Main.paths.stream()
			.filter(Files::isDirectory)
			.findFirst().get();	//TODO フォルダが2個以上あったときor0個のときの例外処理

	private static final Map<String, String> filesInFolder = Main.paths.stream()
			.filter(p -> !Files.isDirectory(p))
			.filter(p -> p.getParent().equals(testCaseFolder)) //TestCaseフォルダ内にあるファイルのみ抽出
			.map(p -> p.getFileName().toString())
			.collect(Collectors.toMap(
					Util::toLowerCaseAndRemoveSymbol,
					f -> f,
					(s, a) -> a // キー重複時は上書き
	));

	private static final Map<String, String> filesInEvi = Main.evidenceList.getRows().stream()
			.filter(r -> r.getIsExecuted())
			.map(r -> r.getTestResult().trim().split(Util.odsSep)) //各セルに行が複数ある場合、それぞれを1行として配列へ
			.flatMap(Arrays::stream) //配列を1つにまとめる
			.map(f -> f.trim().replaceAll("　", "").replaceFirst("-", ""))
			.filter(s -> s.matches(".+\\.[^.]{3,4}$")) //拡張子がある行のみを抽出
			.collect(Collectors.toMap(
					Util::toLowerCaseAndRemoveSymbol,
					f -> f,
					(s, a) -> a // キー重複時は上書き
	));

	public static CheckResult doTest() {
		//検証用
//		System.out.println("──────");
//		System.out.println("【TestNo.4】");
//		System.out.println("TestCaseFoldr: " + testCaseFolder);
//		System.out.println("filesInFolder: ");
//		filesInFolder.forEach((k,v) -> System.out.println("  - " + k));
//		System.out.println("filesInEvidenceList: ");
//		filesInEvi.forEach((k,v) -> System.out.println("  - " + k));
		//
		check();
		String comment = sb.toString().trim();
		Result checkResult = comment.isEmpty() ? Result.OK : Result.NG;
		return new CheckResult(TEST_NO, checkResult, comment);
	}

	private static void check() {

		Set<String> invalidName = filesInEvi.entrySet().stream()
				.filter(f -> f.getKey().contains("xxxxx"))
				.map(f -> f.getValue())
				.collect(Collectors.toCollection(TreeSet::new));

		Set<String> notExistFilesInFolder = filesInEvi.entrySet().stream()
				.filter(f -> !f.getKey().contains("xxxxx"))
				.filter(f -> filesInFolder.get(f.getKey()) == null)
				.map(f -> f.getValue())
				.collect(Collectors.toCollection(TreeSet::new));

		Set<String> notExistFilesInEvi = filesInFolder.entrySet().stream()
				.filter(f -> filesInEvi.get(f.getKey()) == null)
				.map(f -> f.getValue())
				.collect(Collectors.toCollection(TreeSet::new));

		if (!invalidName.isEmpty()) {
			sb.append("Evidence listに記載のファイル名が不正：").append(Util.sep);
			invalidName.forEach(f -> sb.append("  - ").append(f).append(Util.sep));
		}

		if (!notExistFilesInFolder.isEmpty()) {
			sb.append("Evidence listに記載はあるがTestcase.zip内には存在しないファイル：").append(Util.sep);
			notExistFilesInFolder.forEach(f -> sb.append("  - ").append(f).append(Util.sep));
		}

		if (!notExistFilesInEvi.isEmpty()) {
			sb.append("Testcase.zip内に存在するがEvidence listには記載がないファイル：").append(Util.sep);
			notExistFilesInEvi.forEach(f -> sb.append("  - ").append(f).append(Util.sep));
		}
	}
}