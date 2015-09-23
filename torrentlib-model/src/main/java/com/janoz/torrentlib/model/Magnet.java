package com.janoz.torrentlib.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Magnet {

	private static final String ENCODING = "UTF-8";
	private String exactTopic;
	private String displayName;
	private Set<String> trackers = new HashSet<String>();

	public static Magnet fromUrl(String url)
			throws UnsupportedEncodingException {
		
		Map<String, List<String>> params = getUrlParameters(url);
		Magnet result = new Magnet();
		result.exactTopic = params.get("xt").get(0);
		//FIXME dn blijkbaar niet verplicht...
		result.displayName = params.get("dn").get(0);
		result.trackers.addAll(params.get("tr"));
		return result;
	}

	
	
	public String getExactTopic() {
		return exactTopic;
	}



	public String getDisplayName() {
		return displayName;
	}



	public Set<String> getTrackers() {
		return trackers;
	}



	@Override
	public String toString() {
		return "Magnet [exactTopic=" + exactTopic + ", displayName="
				+ displayName + ", trackers=" + trackers + "]";
	}

	public String toUrl() throws UnsupportedEncodingException {
		StringBuffer result = new StringBuffer().append("magnet:?xt=")
				.append(exactTopic).append("&dn=").append(URLEncoder.encode(displayName,ENCODING));
		for (String tracker : trackers) {
			result.append("&tr=").append(tracker);
		}
		return result.toString();

	}

	public static Map<String, List<String>> getUrlParameters(String url)
			throws UnsupportedEncodingException {
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		String[] urlParts = url.split("\\?");
		if (urlParts.length > 1) {
			String query = urlParts[1];
			for (String param : query.split("&")) {
				String pair[] = param.split("=");
				String key = URLDecoder.decode(pair[0], ENCODING);
				String value = "";
				if (pair.length > 1) {
					value = URLDecoder.decode(pair[1], ENCODING);
				}
				List<String> values = params.get(key);
				if (values == null) {
					values = new ArrayList<String>();
					params.put(key, values);
				}
				values.add(value);
			}
		}
		return params;
	}
}
