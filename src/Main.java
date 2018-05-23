import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import entity.CheckResult;
import entity.EvidenceListSheetEntity;
import entity.PropatiesEntity;
import utility.Util;

public class Main {
	static PropatiesEntity propaties;
	static LocalDate startDate;
	static Map<String, Path> files = new TreeMap<>();
	static EvidenceListSheetEntity evidenceList = null;
	static List<CheckResult> lists = new ArrayList<>();

	public static void main(String[] args) {

		final String FOLDER = "dataA";
		final String PROPATIES = "dataA\\propaties.ods";
		getAllFileName(FOLDER);

		final String FILE_PASS = getEvidenceListPass();

//		getActualStartDate();

		long start = System.currentTimeMillis();
		//
		propaties = PropatiesFileReader.readPropaties(PROPATIES);
		evidenceList = EvaluationDocReader.readEvidenceList(FILE_PASS);
		//
		long lap = System.currentTimeMillis();
		//
		lists.add(TestNo02.doTest());
		lists.add(TestNo04.doTest());
		lists.add(TestNo05.doTest());
		lists.add(TestNo06.doTest());
		lists.add(TestNo11.doTest());
		//
		long end = System.currentTimeMillis();
		//
		//TODO Subject名_CheckResult.ods でチェック結果ファイルを作成して書き出す
		//
		//【検証用】取得した「ファイル置き場にある全ファイル名」をコンソール画面へ表示
		System.out.println("──────" + Util.sep + "【File name list】");
		files.forEach((k,v) -> System.out.println(v));
		//【検証用】読み込む前のodsファイル情報をコンソール画面へ表示
		System.out.println(PropatiesFileReader.getFileInfoAsString());
		System.out.println(EvaluationDocReader.getFileInfoAsString());
		//【検証用】読み込んだodsファイルの中身をコンソール画面へ表示
		System.out.println(propaties.getAllAsString());
		System.out.println(evidenceList.getAllAsString());
		//【検証用】チェック結果をコンソール画面へ表示
		System.out.println(getAllResultsAsString());
		//【検証用】実行にかかった時間
		System.out.println("──────");
		System.out.println("Read file: " + (lap - start) + "ms");
		System.out.println("Check error: " + (end - lap) + "ms");

	}

	private static String getEvidenceListPass() {
		List<String> list = new ArrayList<>();
		for (Map.Entry<String, Path> e : files.entrySet()) {
			if (e.getKey().contains("evidencelist"))
				list.add(e.getValue().toString());
		}
		//TODO エラーだった際はチェックを中断する
		if (list.size() == 0) {
			System.out.println("Evidence listが存在しません");
		}else if (list.size() > 1) {
			System.out.println("Evidence listが複数存在します");
		}else {
			return list.get(0).toString();
		};
		return null;
	}

	private static void getAllFileName(String FOLDER) {
		Path path = Paths.get(FOLDER);
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (!file.toString().contains(".~lock."))	//いま編集中のodsの一時ファイルは除外
						files.put(Util.toLowerCaseAndRemoveSymbol(file.toString()), file);
					return FileVisitResult.CONTINUE;
				}
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (!dir.equals(path))	//探索の起点となるフォルダ自身は除外
						files.put(Util.toLowerCaseAndRemoveSymbol(dir.toString()), dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO 取得したPathにTastCaseフォルダが存在していなかった場合の処理（S-No.4等が実行できないので処理を終了させる）
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
			} catch (Exception e) {	//TODO 適切なExceptionをcatchするよう修正した方がいいか？
				System.out.println("入力値が間違っています。正しいActual Start Dateを再入力してください");
			}
		}
		sc.close();
	}

	private static String getAllResultsAsString() {
		StringBuilder sb = new StringBuilder();
		lists.forEach(l -> {
			sb.append("───").append(Util.sep).append("【Test").append(String.format("%02d", l.getTestNo())).append("】")
					.append(l.getCheckResult().getResultAsSymbol()).append(Util.sep)
					.append(l.getComment()).append(Util.sep);
		});
		return sb.toString();
	}
}
