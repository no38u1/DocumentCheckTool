import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Table;

import entity.CheckResult;
import entity.EvidenceListSheetEntity;
import entity.PropatiesEntity;
import utility.Util;

public class Main {
	//Related to Files and Foledrs
	static final String spa = FileSystems.getDefault().getSeparator();
//	static final String rootPass = "dataA"+ spa + "dataA";
	static final String rootPass = "dataD";
//	static final String rootPass = ".";
	static Set<Path> paths = new TreeSet<>();
	//Reading files
	static PropatiesEntity propaties;
	static EvidenceListSheetEntity evidenceList;
	//User Input Items
	static String confirmerName;
	static LocalDate startDate;
	//Results
	static List<CheckResult> results = new ArrayList<>();

	public static void main(String[] args) {

		final String PROPATIES = rootPass + spa + "CheckTool" + spa + "Propaties.ods";
		System.out.println(rootPass);
		getAllFileName(rootPass);


		final String FILE_PASS = getEvidenceListPass();

		getConfirmerInputInfo();

		long start = System.currentTimeMillis();
		//
		propaties = PropatiesFileReader.readPropaties(PROPATIES);
		evidenceList = EvaluationDocReader.readEvidenceList(FILE_PASS);
		//
		long lap1 = System.currentTimeMillis();
		//
		results.add(TestNo02.doTest());
		results.add(TestNo04.doTest());
		results.add(TestNo05.doTest());
		results.add(TestNo06.doTest());
		results.add(TestNo11.doTest());
		//
		long lap2 = System.currentTimeMillis();
		//odsファイルへ書き出し
		String targetName = evidenceList.getStoryName();
    	Pattern illegalFileNamePattern = Pattern.compile("[(\\\\/:*?\"<>|)]");
    	String fileName = illegalFileNamePattern.matcher(targetName).replaceAll("_");
	    String sourcePath = rootPass + spa + "CheckTool" + spa + "Format.ods";

	    String targetPath = rootPass + spa + fileName + "_CheckResult.ods";
		try {
			SpreadsheetDocument spreadsheetDoc = SpreadsheetDocument.loadDocument(sourcePath);
	        Table cover = spreadsheetDoc.getTableByName("Cover");
	        cover.getCellByPosition("C3").setStringValue(confirmerName);
	        Calendar c = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMMM yyyy 'at' h:mm a z", Locale.US);
	        String confirmationDate = sdf.format(c.getTime());
	        cover.getCellByPosition("C4").setStringValue(confirmationDate);
	        //
	        Table sheet = spreadsheetDoc.getTableByName("CheckResult");
	        sheet.getCellByPosition("E4").setStringValue(results.get(0).getCheckResult().getResultAsSymbol());
	        sheet.getCellByPosition("F4").setStringValue(results.get(0).getComment());
	        sheet.getCellByPosition("E6").setStringValue(results.get(1).getCheckResult().getResultAsSymbol());
	        sheet.getCellByPosition("F6").setStringValue(results.get(1).getComment());
	        sheet.getCellByPosition("E7").setStringValue(results.get(2).getCheckResult().getResultAsSymbol());
	        sheet.getCellByPosition("F7").setStringValue(results.get(2).getComment());
	        sheet.getCellByPosition("E8").setStringValue(results.get(3).getCheckResult().getResultAsSymbol());
	        sheet.getCellByPosition("F8").setStringValue(results.get(3).getComment());
			sheet.getCellByPosition("E12").setStringValue(results.get(4).getCheckResult().getResultAsSymbol());
			sheet.getCellByPosition("F12").setStringValue(results.get(4).getComment());
			//
			spreadsheetDoc.save(targetPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		//
		//【検証用】取得した「ファイル置き場にある全ファイル名」をコンソール画面へ表示
		System.out.println("──────" + Util.sep + "【File name list】");
		paths.forEach(System.out::println);
		//【検証用】読み込む前のodsファイル情報をコンソール画面へ表示
		System.out.println(PropatiesFileReader.getFileInfoAsString());
		System.out.println(EvaluationDocReader.getFileInfoAsString());
		//【検証用】読み込んだodsファイルの中身をコンソール画面へ表示
//		System.out.println(propaties.getAllAsString());
//		System.out.println(evidenceList.getAllAsString());
		//【検証用】チェック結果をコンソール画面へ表示
		System.out.println(getAllResultsAsString());
		//【検証用】実行にかかった時間
		System.out.println("───");
		System.out.println("Read file   : " + (lap1 - start) + "ms");
		System.out.println("Check error : " + (lap2 - lap1) + "ms");
		System.out.println("Data writing: " + (end - lap2) + "ms");
		System.out.println("");
		//
		System.out.println("──────");
        System.out.println("検品とファイルの作成が完了しました。");
        System.out.println("新たに作成された " + fileName + "_CheckResult.ods ファイルを開き、念のため中身を確認してください。");

	}


	private static String getEvidenceListPass() {
		List<Path> list = paths.stream()
				.filter(p -> Util.toLowerCaseAndRemoveSymbol(p.getFileName().toString()).contains("evidencelist"))
				.collect(Collectors.toList());
		if (list.size() == 0) {
			System.out.println("Evidence listが存在しません");
		}else if (list.size() > 1) {
			System.out.println("Evidence listと思われるファイルが複数存在します");
		}else {
			return list.get(0).toString();
		};
		return null;	//TODO エラーだった際はチェックを中断する
	}

	private static void getAllFileName(String FOLDER) {
		Path path = Paths.get(FOLDER);
		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (!file.toString().contains(".~lock.") && !file.toString().contains(".bat")
							&& !file.toString().contains(".classpath") && !file.toString().contains(".gitignore") && !file.toString().contains(".project")) {
						paths.add(file);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (!dir.getFileName().toString().toLowerCase().contains("test") && !dir.toString().equals(rootPass)) {
						return FileVisitResult.SKIP_SUBTREE;
					}
					if (!dir.equals(path)) {//探索の起点となるフォルダ自身は除外
						paths.add(dir);
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TODO 取得したPathにTastCaseフォルダが存在していなかった場合/複数存在していた場合の処理（S-No.4等が実行できないので処理を終了させる）
	}

	private static void getConfirmerInputInfo() {
		System.out.println();
		System.out.println("検品作業者の名前を入力してください（例：nozawa_y）");
		Scanner sc = new Scanner(System.in);
		String s = sc.next();
			confirmerName = s;

		System.out.println();
		System.out.println("チケット記載のActual Start Dateを数字8桁（yyyymmdd）で入力してください。");
		System.out.println("もし空欄の場合は\"0\"を入力してください。");
		while (true) {
			s = sc.next();
			try {
				if (s.equals("0")) {
					startDate = null;
					break;
				}
				startDate = LocalDate.parse(s,
						DateTimeFormatter.ofPattern("uuuuMMdd").withResolverStyle(ResolverStyle.STRICT));
				break;
			} catch (Exception e) { //TODO 適切なExceptionをcatchするよう修正
				System.out.println("入力値が間違っています。正しいActual Start Dateを再入力してください");
			}
		}
		sc.close();
	}


	private static String getAllResultsAsString() {
		StringBuilder sb = new StringBuilder();
		results.forEach(l -> {
			sb.append("───").append(Util.sep).append("【Test").append(String.format("%02d", l.getTestNo())).append("】")
					.append(l.getCheckResult().getResultAsSymbol()).append(Util.sep)
					.append(l.getComment()).append(Util.sep);
		});
		return sb.toString();
	}
}
