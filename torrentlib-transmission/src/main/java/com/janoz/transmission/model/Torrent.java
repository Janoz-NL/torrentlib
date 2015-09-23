package com.janoz.transmission.model;

import java.util.ArrayList;
import java.util.List;

public class Torrent {

	private String hash;
	private String name;
	private long id;
	private TorrentStatus status;
	private float precentDone;
	private String downloadDir;
	private List<TorrentFile> files = new ArrayList<TorrentFile>();

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TorrentStatus getStatus() {
		return status;
	}

	public void setStatus(TorrentStatus status) {
		this.status = status;
	}

	public float getPrecentDone() {
		return precentDone;
	}

	public void setPrecentDone(float precentDone) {
		this.precentDone = precentDone;
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	public void addFile(TorrentFile file){
		files.add(file);
	}
	
	public List<TorrentFile> getFiles() {
		return files;
	}
	
	public boolean isFinished() {
		//Finished seems to be not working. Trying to determine it otherwise
		return this.precentDone == 1.0f && (TorrentStatus.SEEDING.equals(status) || TorrentStatus.STOPPED.equals(status));
	}

}
