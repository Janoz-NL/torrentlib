package com.janoz.transmission.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import com.janoz.transmission.TransmissionService;
import com.janoz.transmission.model.Magnet;
import com.janoz.transmission.model.Torrent;
import com.janoz.transmission.model.TorrentArguments;
import com.janoz.transmission.model.TorrentStatus;

/**
 * This is not a test. This is just something to run while developing and testing.
 */

@Ignore
public class TransmissionServiceIntegrationTest {

	private static URL transmissionUrl;
	static {
		try {
			transmissionUrl = new URL("http://localhost:9091/transmission/rpc/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private TransmissionService transmissionService = new TransmissionServiceImpl(transmissionUrl);

	
	@Test
	public void listTorrentsTest() throws IOException, ParseException {
		List<Torrent> torrents = transmissionService.listTorrents();
		for (Torrent torrent : torrents) {
			System.out.print(torrent.getId());
			System.out.print("\t");
			System.out.print(torrent.getStatus());
			System.out.print("\t");
			System.out.print(torrent.getHash());
			System.out.print("\t");
			System.out.print(torrent.getPrecentDone());
			System.out.print("\t");
			System.out.print(torrent.getName());
			System.out.print("\t");
			System.out.print(torrent.isFinished());
			System.out.println();
		}
	}
	
	@Test
	public void addTorrent() throws IOException, ParseException {
		Magnet magnet;
		Torrent torrent;
	
		TorrentArguments ta = new TorrentArguments();
		magnet = Magnet.fromUrl("magnet:");
		torrent = transmissionService.addTorrent(magnet, ta);
		System.out.println(torrent.getId() + "\t" + torrent.getHash() + "\t" + torrent.getName());
}


    @Test
    public void removeStoppedTorrents() throws Exception {
        transmissionService.removeTorrent(
                transmissionService.listTorrents().stream()
                        .filter(torrent -> TorrentStatus.STOPPED.equals(torrent.getStatus()))
                        .collect(Collectors.toList()));


    }
}
