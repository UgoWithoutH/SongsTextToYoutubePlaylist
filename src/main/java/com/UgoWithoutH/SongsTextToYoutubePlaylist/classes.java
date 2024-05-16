package com.UgoWithoutH.SongsTextToYoutubePlaylist;

import java.util.List;

public class classes {
    public class ItemResponse{
        classes.Item[] items;
    }

    public class Item {
        public String added_at;
        public AddedBy added_by;
        public boolean is_local;
        public String primary_color;
        public Track track;
        public VideoThumbnail video_thumbnail;
    }

    class AddedBy {
        public ExternalUrls external_urls;
        public String href;
        public String id;
        public String type;
        public String uri;
    }

    class ExternalUrls {
        public String spotify;
    }

    class Track {
        public Album album;
        public List<Artist> artists;
        public List<String> available_markets;
        public String href;
        public String id;
        public boolean is_local;
        public String name;
    }

    class Artist {
        public String name;
    }

    class VideoThumbnail {
        public String url;
        public int width;
        public int height;
    }

    class SearchSnippet{
        public String publishedAt;
        public String channelId;
        public String title;
        public String description;
        public VideoThumbnail thumbnails;
        public String channelTitle;
        public String liveBroadcastContent;
        public String publishTime;
    }

    public class VideoYT{
        public String kind;
        public String etag;
        public String id;
        public SearchSnippet snippet;
    }

    public class Video {
        public String kind;
        public String etag;
        public VideoId id;
        public SearchSnippet snippet;
    }

    public class VideoId{
        public String kind;
        public String videoId;
    }

    public class YoutubeJsonResult{
        classes.VideoYT[] items;
    }

    public class SearchResult {
        public String kind;
        public String etag;
        public String nextPageToken;
        public String regionCode;
        public PageInfo pageInfo;
        public List<Video> items;
    }

    public class PageInfo {
        public int totalResults;
        public int resultsPerPage;
    }
}
