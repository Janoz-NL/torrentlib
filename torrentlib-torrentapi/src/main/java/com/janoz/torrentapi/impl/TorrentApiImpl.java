package com.janoz.torrentapi.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.janoz.torrentapi.SearchParamsBuilder;
import com.janoz.torrentapi.TorrentApi;
import com.janoz.torrentlib.model.Magnet;


/**
 * Created by vriesgij on 23-9-2015.
 */
public class TorrentApiImpl implements TorrentApi {

    private String token = null;

    /**
     *
     *
     * TODO: rate limit now lets request wait 2 secs. Could be done nicer
     *
     * @param params
     * @return list of matching magnet links
     * @throws Exception
     */
    public List<Magnet> queryApi(SearchParamsBuilder params) throws IOException {
        if (token == null) {
            refreshToken();
        }
        JSONObject response = doRequest(params.toQuery(token));
        if (response.containsKey("error_code")) {
            switch (((Long)response.get("error_code")).intValue()) {
                case 2: //invalid token
                case 4: //old token?
                    token = null;
                    return queryApi(params);
                case 5:
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new IOException(response.get("error") + " (code = " + response.get("error_code") + ")");
                    }
                    return queryApi(params);
                case 20:
                    return Collections.emptyList();
                default:
                    throw new IOException(response.get("error") + " (code = " + response.get("error_code") + ")");
            }
        }
        if (response.containsKey("torrent_results")) {
            JSONArray array = (JSONArray) response.get("torrent_results");
            return parseResult(array);
        } else {
            return Collections.emptyList();
        }

    }

    private List<Magnet> parseResult(JSONArray array) throws UnsupportedEncodingException {
        Iterator<JSONObject> it = array.iterator();
        List<Magnet> magnets = new ArrayList<Magnet>();
        while (it.hasNext()) {
            magnets.add(Magnet.fromUrl((String)it.next().get("download")));
        }
        return magnets;
    }

    private void refreshToken() throws IOException {
        JSONObject response = doRequest("get_token=get_token");
        token = (String)response.get("token");
    }

    private JSONObject doRequest(String query) throws IOException {
        BufferedReader in = streamSupplier.openApiStream(query);
        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(in);
        } catch (ParseException e) {
            throw new IOException(e.getMessage(),e);
        }
        in.close();
        return response;
    }

    StreamSupplier streamSupplier = new StreamSupplier();
    static class StreamSupplier {

        private static final String API_URL = "https://torrentapi.org/pubapi_v2.php?";

        BufferedReader openApiStream(String query) throws IOException {
            HttpURLConnection huc = (HttpURLConnection) new URL(API_URL + query).openConnection();

            return new BufferedReader(new InputStreamReader(huc.getInputStream()));
        }
    }
}
