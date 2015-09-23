package com.janoz.torrentapi.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import com.janoz.torrentapi.SearchParamsBuilder;
import com.janoz.transmission.model.Magnet;

/**
 * Created by vriesgij on 23-9-2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class TorrentApiImplTest {

    TorrentApiImpl cut = new TorrentApiImpl();

    @Mock
    TorrentApiImpl.StreamSupplier streamSupplierMock;

    @Before
    public void setupMocks() {
        cut.streamSupplier = streamSupplierMock;
    }

    @Test
    public void testNoToken() throws Exception {
        Mockito.when(streamSupplierMock.openApiStream(Mockito.anyString())).thenReturn(
                stringAsReader("{\"token\":\"THETOKEN\"}"),
                stringAsReader(ERRORRESPONSE_20));

        Assert.assertTrue(cut.queryApi(new SearchParamsBuilder().query("something")).isEmpty());

    }


    @Test
    public void testSimpleSearch() throws Exception {
        Whitebox.setInternalState(cut, "token", "TOKEN");
        Mockito.when(streamSupplierMock.openApiStream(Mockito.anyString())).thenReturn(
                stringAsReader(SOME_SEARCH_RESULTS));

        List<Magnet> result = cut.queryApi(new SearchParamsBuilder().query("something"));

    }

    @Test
    public void testEmptySearch() throws Exception {
        Whitebox.setInternalState(cut,"token","TOKEN");
        Mockito.when(streamSupplierMock.openApiStream(Mockito.anyString())).thenReturn(
                stringAsReader(ERRORRESPONSE_20));

        Assert.assertTrue(cut.queryApi(new SearchParamsBuilder().query("something")).isEmpty());

    }

    @Test
    public void testInvalidToken2() throws Exception {
        Whitebox.setInternalState(cut,"token","TOKEN");
        Mockito.when(streamSupplierMock.openApiStream(Mockito.anyString())).thenReturn(
                stringAsReader(ERRORRESPONSE_2),
                stringAsReader("{\"token\":\"THETOKEN\"}"),
                stringAsReader(ERRORRESPONSE_20));

        Assert.assertTrue(cut.queryApi(new SearchParamsBuilder().query("something")).isEmpty());

    }
    @Test
    public void testInvalidToken4() throws Exception {
        Whitebox.setInternalState(cut,"token","TOKEN");
        Mockito.when(streamSupplierMock.openApiStream(Mockito.anyString())).thenReturn(
                stringAsReader(ERRORRESPONSE_4),
                stringAsReader("{\"token\":\"THETOKEN\"}"),
                stringAsReader(ERRORRESPONSE_20));

        Assert.assertTrue(cut.queryApi(new SearchParamsBuilder().query("something")).isEmpty());
    }

    @Test(expected = IOException.class)
    public void testGarbage() throws Exception{
        Mockito.when(streamSupplierMock.openApiStream(Mockito.anyString())).thenReturn(
                stringAsReader("garbage"));

        cut.queryApi(new SearchParamsBuilder().query("something"));
    }

    private static final String SOME_SEARCH_RESULTS = "{\"torrent_results\":["
            + "{\"filename\":\"Fear.the.Walking.Dead.S01E02.So.Close.Yet.So.Far.1080p.WEB-DL.DD5.1.H.264-NTb[rartv]\","
            + "\"category\":\"TV HD Episodes\","
            + "\"download\":\"magnet:?xt=urn:btih:b8faedbc72f6ebdd26ed5fd257a1bcc203f845b2&dn=Fear.the.Walking.Dead.S01E02.So.Close.Yet.So.Far.1080p.WEB-DL.DD5.1.H.264-NTb%5Brartv%5D&tr=http%3A%2F%2Ftracker.trackerfix.com%3A80%2Fannounce&tr=udp%3A%2F%2F9.rarbg.me%3A2710&tr=udp%3A%2F%2F9.rarbg.to%3A2710&tr=udp%3A%2F%2Fopen.demonii.com%3A1337%2Fannounce\"},"
            + "{\"filename\":\"Fear.the.Walking.Dead.S01E02.So.Close.Yet.So.Far.720p.WEB-DL.DD5.1.H.264-NTb[rartv]\","
            + "\"category\":\"TV HD Episodes\","
            + "\"download\":\"magnet:?xt=urn:btih:b14089b187b1f9fbacca79aeb28013e8129c6411&dn=Fear.the.Walking.Dead.S01E02.So.Close.Yet.So.Far.720p.WEB-DL.DD5.1.H.264-NTb%5Brartv%5D&tr=http%3A%2F%2Ftracker.trackerfix.com%3A80%2Fannounce&tr=udp%3A%2F%2F9.rarbg.me%3A2710&tr=udp%3A%2F%2F9.rarbg.to%3A2710&tr=udp%3A%2F%2Fopen.demonii.com%3A1337%2Fannounce\"}"
            + "]}";

    private static final String ERRORRESPONSE_2 = "{\"error\":\"Invalid token set!\",\"error_code\":2}";
    private static final String ERRORRESPONSE_4 = "{\"error\":\"Invalid token. Use get_token for a new one!\",\"error_code\":4}";
    private static final String ERRORRESPONSE_20 = "{\"error\":\"No results found\",\"error_code\":20}";


            private BufferedReader stringAsReader(String result) {
        return new BufferedReader(new StringReader(result));
    }


}
