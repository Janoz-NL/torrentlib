package com.janoz.transmission.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.janoz.transmission.TransmissionService;
import com.janoz.torrentlib.model.Magnet;
import com.janoz.transmission.model.Torrent;
import com.janoz.transmission.model.TorrentArguments;
import com.janoz.transmission.model.TorrentFile;
import com.janoz.transmission.model.TorrentStatus;

public class TransmissionServiceImpl implements TransmissionService {
	private static final String ENCODING = "UTF-8";

	private static final String SESSION_HEADER = "X-Transmission-Session-Id";

	private String sessionId;
	private URL transmissionUrl;
	
	public TransmissionServiceImpl(URL transmissionUrl) {
		this.transmissionUrl = transmissionUrl;
	}
	


	@Override
	public Torrent addTorrent(Magnet magnet) throws IOException, ParseException {
		return addTorrent(magnet, null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Torrent addTorrent(Magnet magnet, TorrentArguments arguments) throws IOException, ParseException {
		JSONObject args = new JSONObject();
		args.put("filename", magnet.toUrl());
		if (arguments != null) 
			arguments.putIn(args);
		JSONObject response = doCommand("torrent-add",args);
		checkResult(response);
		JSONObject responseArgs = (JSONObject)response.get("arguments");
		JSONObject jsonTorrent = (JSONObject)responseArgs.get("torrent-added");
		Torrent torrent = toTorrent(jsonTorrent);
		//transmissionBug
		torrent.setName(URLDecoder.decode(torrent.getName(),ENCODING));

		return torrent;
	}
	
	@Override
	public void removeTorrent(Torrent... torrents) throws IOException,
	ParseException {
		removeTorrent(Arrays.asList(torrents));
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void removeTorrent(Collection<Torrent> torrents) throws IOException,
			ParseException {
		JSONArray ids = new JSONArray();
		for (Torrent torrent : torrents) {
			ids.add(torrent.getId());
		}
		JSONObject args = new JSONObject();
		args.put("ids", ids);
		JSONObject response = doCommand("torrent-remove",args);
		checkResult(response);
	}



	/* (non-Javadoc)
	 * @see com.janoz.transmission.impl.TransmissionService#listTorrents()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Torrent> listTorrents() throws IOException, ParseException {
		JSONArray fields = constructJSONArray("id", "name",
			      "hashString","status","percentDone","isFinished","downloadDir","files");
		JSONObject args = new JSONObject();
		args.put("fields", fields);
		JSONObject response = doCommand("torrent-get", args);
		JSONObject arguments = (JSONObject)response.get("arguments");
		JSONArray torrents = (JSONArray)arguments.get("torrents");
		List<Torrent> result = new ArrayList<Torrent>();
		for (JSONObject jsonTorrent : (List<JSONObject>)torrents) {
			Torrent torrent = toTorrent(jsonTorrent);
			result.add(torrent);
		}
		return result;
	}



	@SuppressWarnings("unchecked")
	private JSONArray constructJSONArray(String... strings) {
		JSONArray array = new JSONArray();
		for (String string : strings) {
			array.add(string);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	private JSONObject doCommand(String command, JSONObject args)
			throws IOException, ParseException {
		JSONObject request = new JSONObject();
		request.put("method", command);
		request.put("arguments", args);

		JSONObject response =doCommand(request);
		return response;
	}

	private void checkResult(JSONObject response) {
		String result = (String)response.get("result");
		if (!"success".equals(result)) {
			throw new RuntimeException(result);
		}
	}
	
	private Torrent toTorrent(JSONObject jsonTorrent) {
		Torrent torrent = new Torrent();
		torrent.setId((Long)jsonTorrent.get("id"));
		torrent.setName((String)jsonTorrent.get("name"));
		torrent.setHash((String)jsonTorrent.get("hashString"));
		torrent.setStatus(TorrentStatus.getByStatus((Long) jsonTorrent.get("status")));
		torrent.setPrecentDone(((Number)jsonTorrent.get("percentDone")).floatValue());
		torrent.setDownloadDir((String)jsonTorrent.get("downloadDir"));
		for (Object jsonObj : ((JSONArray)jsonTorrent.get("files"))) {
			JSONObject jsonFile = (JSONObject) jsonObj;
			TorrentFile file = new TorrentFile();
			file.setName((String)jsonFile.get("name"));
			file.setLength((Long)jsonFile.get("length"));
			file.setBytesCompleted((Long)jsonFile.get("bytesCompleted"));
			torrent.addFile(file);
		}
		return torrent;
	}
	
	
	private JSONObject doCommand(JSONObject request) throws IOException, ParseException{

		HttpURLConnection huc = (HttpURLConnection) transmissionUrl.openConnection();
		huc.setRequestMethod("POST");
		if (sessionId!= null)
			huc.setRequestProperty(SESSION_HEADER, sessionId);

		huc.setDoOutput(true);
		OutputStream output = huc.getOutputStream();
		output.write(request.toString().getBytes(Charset.forName("UTF8")));
		output.flush();
		output.close();

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
		} catch (IOException e) {
			if (huc.getResponseCode() == 409) {
				sessionId = huc.getHeaderField(SESSION_HEADER);
				return doCommand(request);
			}
			throw e;
		}
		JSONParser parser = new JSONParser();
		JSONObject response = (JSONObject) parser.parse(in);
	    in.close();
	    return response;
	}



}
