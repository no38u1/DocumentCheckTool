import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entity.CheckResult;
import entity.EvidenceListSheetEntity;
import entity.PropatiesEntity;
import utility.Util;

public class Main {
	static PropatiesEntity propaties;
	static LocalDate startDate;
	static EvidenceListSheetEntity evidenceList = null;
	static List<CheckResult> lists = new ArrayList<>();

	public static void main(String[] args) {

		final String PROPATIES = "data\\propaties.ods";
		final String FILE_PASS = "data\\targetFile01.ods";

		getActualStartDate();

		long start = System.currentTimeMillis();
		//
		propaties = PropatiesFileReader.readPropaties(PROPATIES);
		evidenceList = EvaluationDocReader.readEvidenceList(FILE_PASS);
		//
		long lap = System.currentTimeMillis();
		//
		lists.add(TestNo02.doTest());
		lists.add(TestNo05.doTest());
		lists.add(TestNo06.doTest());
		lists.add(TestNo11.doTest());
		//
		long end = System.currentTimeMillis();
		//
		//TODO Subject名_CheckResult.ods でチェック結果ファイルを作成して書き出す
		//
		//【検証用】読み込む前のodsファイル内容をコンソール画面へ表示
		System.out.println(PropatiesFileReader.getFileInfoAsString());
		System.out.println(EvaluationDocReader.getFileInfoAsString());
		//【検証用】読み込んだ結果データをコンソール画面へ表示
		System.out.println(propaties.getAllAsString());
		System.out.println(evidenceList.getAllAsString());
		//【検証用】チェック結果をコンソール画面へ表示
		System.out.println(getAllResultsAsString());
		//【検証用】実行にかかった時間
		System.out.println("──────");
		System.out.println("Read file: " + (lap - start) + "ms");
		System.out.println("Check error: " + (end - lap) + "ms");

	}

	private static void getActualStartDate() {
		System.out.println(
				"Actual Start Dateを数字8桁（yyyymmdd）で入力してください。");
		System.out.println("もし空欄の場合は\"0\"を入力してください。");

		Scanner sc = new Scanner(System.in);
		while (true) {
			String s = sc.next();
			try {
				if (s.equals("0")) {
					startDate = null;
					break;
				}
				startDate = LocalDate.parse(s,
						DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT));
				break;
			} catch (Exception e) {
				System.out.println("入力値が間違っています。正しいActual Start Dateを再入力してください");
			}
		}
		sc.close();
	}

	private static String getAllResultsAsString() {
		StringBuilder sb = new StringBuilder();
		lists.forEach(l -> {
			sb.append("───").append(Util.sep).append("【Test")
					.append(String.format("%02d", l.getTestNo()))
					.append("】").append(l.getCheckResult().getResultAsSymbol()).append(Util.sep)
					.append(l.getComment()).append(Util.sep);
		});
		return sb.toString();
	}
}
