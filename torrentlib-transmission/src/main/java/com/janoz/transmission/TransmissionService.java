package com.janoz.transmission;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.json.simple.parser.ParseException;

import com.janoz.torrentlib.model.Magnet;
import com.janoz.transmission.model.Torrent;
import com.janoz.transmission.model.TorrentArguments;

public interface TransmissionService {

	/**
	 * 
	 * @param magnet magnet link to be downloaded
	 * @return the resulting torrent
	 * @throws IOException
	 * @throws ParseException
	 */
	Torrent addTorrent(Magnet magnet) throws IOException,
			ParseException;

	/**
	 * 
	 * @param magnet magnet link to be downloaded
	 * @param arguments torrent arguments for downloading
	 * @return the resulting torrent
	 * @throws IOException
	 * @throws ParseException
	 */
	Torrent addTorrent(Magnet magnet, TorrentArguments arguments) throws IOException,
			ParseException;

	List<Torrent> listTorrents() throws IOException, ParseException;
	
	void removeTorrent(Torrent... torrents) throws IOException, ParseException;
	
	void removeTorrent(Collection<Torrent> torrents) throws IOException, ParseException;

}
