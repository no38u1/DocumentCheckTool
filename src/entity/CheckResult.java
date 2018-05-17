package entity;

import utility.Result;

public class CheckResult {

	public CheckResult(int testNo, Result checkResult, String comment) {
		super();
		this.testNo = testNo;
		this.checkResult = checkResult;
		this.comment = comment;
	}
	private int testNo;
	private Result checkResult;
	private String comment;

	public int getTestNo() {
		return testNo;
	}
	public Result getCheckResult() {
		return checkResult;
	}
	public String getComment() {
		return comment;
	}
}
