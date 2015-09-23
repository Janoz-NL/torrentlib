package com.janoz.transmission.model;

public class TorrentFile {

	private String name;
	private long bytesCompleted;
	private long length;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getBytesCompleted() {
		return bytesCompleted;
	}
	public void setBytesCompleted(long bytesCompleted) {
		this.bytesCompleted = bytesCompleted;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
}
