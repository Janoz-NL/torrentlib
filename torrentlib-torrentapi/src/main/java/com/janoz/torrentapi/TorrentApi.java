package com.janoz.torrentapi;

import java.io.IOException;
import java.util.List;

import com.janoz.torrentlib.model.Magnet;

/**
 * Created by vriesgij on 23-9-2015.
 */
public interface TorrentApi {

    /**
     *
     * @param params
     * @return
     * @throws IOException
     */
    public List<Magnet> queryApi(SearchParamsBuilder params) throws IOException;


}
