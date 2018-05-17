package entity;

import java.time.LocalDate;
import java.util.NavigableMap;

import utility.Util;

public class PropatiesEntity {

	public PropatiesEntity(NavigableMap<LocalDate, String> versionInfo) {
		super();
		this.versionInfo = versionInfo;
	}

	private NavigableMap<LocalDate, String> versionInfo;

	public NavigableMap<LocalDate, String> getVersionInfo() {
		return versionInfo;
	}

	public String getAllAsString() {
		StringBuilder sb = new StringBuilder("──────").append(Util.sep);
		versionInfo.forEach((k, v) -> sb.append(k).append(", ").append(v).append(Util.sep));
		return sb.toString();
	}
}
