package com.janoz.transmission.model;

import java.util.HashMap;
import java.util.Map;

public enum TorrentStatus {
	UNKNOWN(-1, "Unknown"),
	STOPPED(0, "Stopped"),
    CHECK_WAITING(1, "Check waiting"),
    CHECKING(2, "Checking"),
    DOWNLOAD_WAITING(3, "Download waiting"),
    DOWNLOADING(4, "Downloading"),
    SEED_WAITING(5, "Seed waiting"),
    SEEDING(6, "Seeding");
	
	private String description;
	private int status;
	
	private TorrentStatus(int status, String description) {
		this.status = status;
		this.description = description;
	}
	
	private static Map<Integer, TorrentStatus> map = new HashMap<Integer, TorrentStatus>();
	static {
		for (TorrentStatus status : values()) {
			map.put(status.status, status);
			
		}
	}
	
	public static TorrentStatus getByStatus(int status) {
		TorrentStatus result = map.get(status);
		return result == null?UNKNOWN:result;
	}
	
	public static TorrentStatus getByStatus(Long status) {
		if (status == null) return UNKNOWN;
		return getByStatus(status.intValue());
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}
