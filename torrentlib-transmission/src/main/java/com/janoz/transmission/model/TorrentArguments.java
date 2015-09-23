package com.janoz.transmission.model;

import org.json.simple.JSONObject;

/**
 * Class containing arguments for adding a torrent.
 * 
 * 
 * @author Gijs de Vries aka Janoz
 *
 */
public class TorrentArguments {

	private String downloadLocation;

	public void setDownloadLocation(String downloadLocation) {
		this.downloadLocation = downloadLocation;
	}

	@SuppressWarnings("unchecked")
	public void putIn(JSONObject args) {
		if (downloadLocation != null) {
			args.put("download-dir", downloadLocation);
		}		
	}
	
	
	
}
