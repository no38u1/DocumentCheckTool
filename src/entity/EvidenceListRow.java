package entity;

import java.util.List;

public class EvidenceListRow {

	public EvidenceListRow(int s_No, String testContent, String testResult, String reasonOfNoTest,
			List<Integer> numbers) {
		super();
		this.s_No = s_No;
		this.testContent = testContent;
		this.testResult = testResult;
		this.reasonOfNoTest = reasonOfNoTest;
		this.isExecuted = numbers.get(0) != 0;
		this.numbers = numbers;
	}

	private int s_No;
	private String testContent;
	private String testResult;
	private String reasonOfNoTest;
	private boolean isExecuted;
	private List<Integer> numbers;

		public int getS_No() {
		return s_No;
	}
	public String getTestContent() {
		return testContent;
	}
	public String getTestResult() {
		return testResult;
	}
	public String getReasonOfNoTest() {
		return reasonOfNoTest;
	}
	public boolean getIsExecuted() {
		return isExecuted;
	}
	public List<Integer> getNumbers() {
		return numbers;
	}
}
