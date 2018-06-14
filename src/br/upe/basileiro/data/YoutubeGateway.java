package br.upe.basileiro.data;


import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Print a list of videos matching a search term.
 *
 * @author Jeremy Walker
 */
public class YoutubeGateway {

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    private YouTube youtube;
    private String apiKey;
    
    public YoutubeGateway(YouTube youtube, String apiKey) {
    	this.youtube = youtube;
    	this.apiKey = apiKey;
    }
      
    public void search(String query) {
        YouTube.Search.List search = null;
        
		try {
			search = this.youtube.search().list("id,snippet");
			
	        search.setKey(this.apiKey);
	        search.setQ(query);
	        search.setType("video");
	        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
	        search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			
	        SearchListResponse searchResponse = search.execute();
	        List<SearchResult> searchResultList = searchResponse.getItems();
	        prettyPrint(searchResultList.iterator());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void getComments(String videoId) {
        CommentThreadListResponse videoCommentsListResponse;
		try {
			videoCommentsListResponse = youtube.commentThreads()
			        .list("snippet").setVideoId(videoId).setTextFormat("plainText").execute();
	        List<CommentThread> videoComments = videoCommentsListResponse.getItems();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults) {
    	
        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {
            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }
}