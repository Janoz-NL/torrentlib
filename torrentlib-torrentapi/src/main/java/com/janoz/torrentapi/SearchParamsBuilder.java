package com.janoz.torrentapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by vriesgij on 23-9-2015.
 */
public class SearchParamsBuilder {
    private Long theTvDb;
    private String imdb;
    private Long theMovieDb;
    private String query;
    private String category;
    private Long limit;
    private Long minSeeders;
    private Long minLeechers;
    private Sort sort;

    public SearchParamsBuilder theTvDb(Long theTvDb) {
        this.theTvDb = theTvDb;
        return this;
    }

    public SearchParamsBuilder imdb(String imdb) {
        this.imdb = encode(imdb);
        return this;
    }

    public SearchParamsBuilder theMovieDb(Long theMovieDb) {
        this.theMovieDb = theMovieDb;
        return this;
    }

    public SearchParamsBuilder query(String query) {
        String encQuery = encode(query);
        this.query = this.query == null ? encQuery : this.query + "+" + encQuery;
        return this;
    }

    public SearchParamsBuilder episode(int season, int episode) {
        return query(String.format("S%02dE%02d", season, episode));
    }

    public SearchParamsBuilder category(Category category) {
        this.category = this.category == null ? "" + category.getCategory() : this.category + ';' + category.getCategory();
        return this;
    }

    public SearchParamsBuilder limit(int limit) {
        this.limit = Long.valueOf(limit);
        return this;
    }

    public SearchParamsBuilder minSeeders(Long minSeeders) {
        this.minSeeders = minSeeders;
        return this;
    }
    public SearchParamsBuilder minLeechers(Long minLeechers) {
        this.minLeechers = minLeechers;
        return this;
    }

    public SearchParamsBuilder sort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public String toQuery(String token) {
        StringBuilder sb = new StringBuilder("mode=search&token=");
        sb.append(token);
        if (theTvDb != null) {
            sb.append("&search_tvdb=");
            sb.append(theTvDb);
        }
        if (imdb != null) {
            sb.append("&search_imdb=");
            sb.append(imdb);
        }
        if (theMovieDb != null) {
            sb.append("&search_themoviedb=");
            sb.append(theMovieDb);
        }
        if (query != null) {
            sb.append("&search_string=");
            sb.append(query);
        }
        if (minLeechers != null) {
            sb.append("&min_leechers=");
            sb.append(minLeechers);
        }
        if (minSeeders != null) {
            sb.append("&min_seeders=");
            sb.append(minSeeders);
        }
        if (category != null) {
            sb.append("&category=");
            sb.append(category);
        }
        if (limit != null) {
            sb.append("&limit=");
            sb.append(limit);
        }
        if (sort != null) {
            sb.append("&sort=");
            sb.append(sort.name().toLowerCase());
        }

        return sb.toString();
    }
    public enum Sort {
        LAST(),
        SEEDERS(),
        LEECHERS();
    }

    public enum Category {
        XXX(4),
        MOVIES_XVID(14),
        MOVIES_XVID_720(48),
        MOVIES_X264(17),
        MOVIES_X264_1080(44),
        MOVIES_X264_720(45),
        MOVIES_X264_3D(47),
        MOVIES_BD(42),
        MOVIES_BD_REMUX(46),
        TV(18),
        TV_HD(41),
        MUSIC_MP3(23),
        MUSIC_FLAC(25),
        GAMES_PC_ISO(27),
        GAMES_PC_RIP(28),
        GAMES_PS3(40),
        GAMES_XBOX360(32),
        SOFTWARE_PC(33),
        EBOOKS(35);


        private int category;
        Category(int category) {
            this.category = category;
        }

        public int getCategory() {
            return category;
        }
    }

    private String encode(String src) {
        try {
            return URLEncoder.encode(src, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
