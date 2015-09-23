package com.janoz.torrentapi;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by vriesgij on 23-9-2015.
 */
public class SearchParamsBuilderTest {

    private static String PREFIX = "mode=search&token=TOKEN";
    @Test
    public void testTheTvDb() {
        doAssert("&search_tvdb=1234", builder -> builder.theTvDb(1234L));
    }

    @Test
    public void testImdb() {
        doAssert("&search_imdb=tt1234", builder -> builder.imdb("tt1234"));
    }

    @Test
    public void testTheMovieDb() {
        doAssert("&search_themoviedb=12345", builder -> builder.theMovieDb(12345L));
    }

    @Test
    public void testEpisode() {
        doAssert("&search_string=S02E03", builder -> builder.episode(2,3));
        doAssert("&search_string=S02E123", builder -> builder.episode(2,123));
    }

    @Test
    public void testEpisodeAndQuery() {
        doAssert("&search_string=S01E02+DD5.1", builder -> builder.episode(1,2).query("DD5.1"));
    }

    @Test
    public void testQuery() {
        doAssert("&search_string=some+string", builder -> builder.query("some string"));
    }

    @Test
    public void testQueries() {
        doAssert("&search_string=some+string+other+string", builder -> builder.query("some string").query("other string"));
    }

    @Test
    public void testCategory() {
        doAssert("&category=17", builder -> builder.category(SearchParamsBuilder.Category.MOVIES_X264));
    }
    @Test
    public void testCategories() {
        doAssert("&category=41;18", builder -> builder.category(SearchParamsBuilder.Category.TV_HD).category(SearchParamsBuilder.Category.TV));
    }

    @Test
    public void testLimit() {
        doAssert("&limit=100", builder -> builder.limit(100));
    }

    @Test
    public void testMinSeeders() {
        doAssert("&min_seeders=123", builder -> builder.minSeeders(123L));
    }

    @Test
    public void testMinLeechers() {
        doAssert("&min_leechers=321", builder -> builder.minLeechers(321L));
    }

    @Test
    public void testSort() {
        doAssert("&sort=last", builder -> builder.sort(SearchParamsBuilder.Sort.LAST));
    }


    private void doAssert(String expectedQuery, ParamInserter inserter) {
        SearchParamsBuilder cut = new SearchParamsBuilder();
        inserter.doParams(cut);
        Assert.assertEquals(PREFIX + expectedQuery, cut.toQuery("TOKEN"));
    }

    private interface ParamInserter {
        void doParams(SearchParamsBuilder builder);
    }
}
